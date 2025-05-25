package com.waterboard.waterqualityprediction.services;

import com.ghgande.j2mod.modbus.io.ModbusTCPTransaction;
import com.ghgande.j2mod.modbus.msg.ReadMultipleRegistersRequest;
import com.ghgande.j2mod.modbus.msg.ReadMultipleRegistersResponse;
import com.ghgande.j2mod.modbus.net.TCPMasterConnection;
import com.waterboard.waterqualityprediction.models.RegisterData;
import com.waterboard.waterqualityprediction.repository.RegisterDataRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class DashboardService {

    private static final String MODBUS_IP = "34.218.209.76";
    private static final int MODBUS_PORT = 5020;

    private static final Map<Integer, Integer> TAG_ID_TO_REGISTER = Map.of(
            5001, 0,
            5002, 2,
            5003, 4,
            5004, 6,
            5005, 8,
            5006, 10
    );

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private RegisterDataRepository registerDataRepository;

    @Scheduled(fixedRate = 5000)
    public Map<Integer, Float> fetchModbusData() {
        try {
            Map<Integer, Float> registerValues = readHoldingRegisters();
            if (!registerValues.isEmpty()) {
                log.info("Fetched register values: {}", registerValues);
                messagingTemplate.convertAndSend("/topic/registerData", registerValues);

                // Save the fetched data to the database
                saveRegisterDataToDatabase(registerValues);
            } else {
                log.warn("No data received from Modbus server");
            }
            return registerValues;
        } catch (Exception e) {
            log.error("Error while fetching Modbus data", e);
            return new HashMap<>();
        }
    }

    public Map<Integer, Float> readHoldingRegisters() throws Exception {
        Map<Integer, Float> registerValues = new HashMap<>();
        TCPMasterConnection connection = null;

        try {
            InetAddress address = InetAddress.getByName(MODBUS_IP);
            connection = new TCPMasterConnection(address);
            connection.setPort(MODBUS_PORT);
            connection.connect();

            log.info("Connected to Modbus server at {}:{}", MODBUS_IP, MODBUS_PORT);

            for (Map.Entry<Integer, Integer> entry : TAG_ID_TO_REGISTER.entrySet()) {
                int tagId = entry.getKey();
                int registerIndex = entry.getValue();

                ReadMultipleRegistersRequest request = new ReadMultipleRegistersRequest(registerIndex, 2);
                ModbusTCPTransaction transaction = new ModbusTCPTransaction(connection);
                transaction.setRequest(request);
                transaction.execute();

                ReadMultipleRegistersResponse response = (ReadMultipleRegistersResponse) transaction.getResponse();
                if (response.getWordCount() == 2) {
                    int low = response.getRegister(0).getValue();
                    int high = response.getRegister(1).getValue();

                    // Convert registers to float
                    float value = convertRegistersToFloat(low, high);
                    registerValues.put(tagId, value);
                    log.info("TagID {} -> Value: {}", tagId, value);
                } else {
                    log.warn("Invalid response for TagID {}. Expected 2 registers, got {}", tagId, response.getWordCount());
                }
            }
        } finally {
            if (connection != null && connection.isConnected()) {
                connection.close();
            }
        }
        return registerValues;
    }

    private float convertRegistersToFloat(int low, int high) {
        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.putShort((short) low);
        buffer.putShort((short) high);
        buffer.rewind();
        return buffer.getFloat();
    }

    private void saveRegisterDataToDatabase(Map<Integer, Float> registerValues) {
        for (Map.Entry<Integer, Float> entry : registerValues.entrySet()) {
            Integer tagId = entry.getKey();
            Float value = entry.getValue();

            // Create new RegisterData entity
            RegisterData registerData = new RegisterData();
            registerData.setTagId(tagId);
            registerData.setValue(value);


            registerDataRepository.save(registerData);
            log.info("Saved TagID {} with value {}", tagId, value);
        }
    }
}
