package com.waterboard.waterqualityprediction.services;

import com.ghgande.j2mod.modbus.io.ModbusTCPTransaction;
import com.ghgande.j2mod.modbus.msg.ReadMultipleRegistersRequest;
import com.ghgande.j2mod.modbus.msg.ReadMultipleRegistersResponse;
import com.ghgande.j2mod.modbus.net.TCPMasterConnection;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class DashboardService {

    private static final String MODBUS_IP = "127.0.0.1";
    private static final int MODBUS_PORT = 5020;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Scheduled(fixedRate = 5000)
    public List<Integer> fetchModbusData() {
        try {
            List<Integer> registerValues = readHoldingRegisters(0, 10);
            if (!registerValues.isEmpty()) {
                log.info("Fetched register values: {}", registerValues); // Log the values
                messagingTemplate.convertAndSend("/topic/registerData", registerValues);
            } else {
                log.warn("No data received from Modbus server");
            }
        } catch (Exception e) {
            log.error("Error while fetching Modbus data", e);
        }
        return null;
    }

    public List<Integer> readHoldingRegisters(int startAddress, int quantity) throws Exception {
        List<Integer> registerValues = new ArrayList<>();
        TCPMasterConnection connection = null;

        try {
            InetAddress address = InetAddress.getByName(MODBUS_IP);
            connection = new TCPMasterConnection(address);
            connection.setPort(MODBUS_PORT);
            connection.connect();

            log.info("Connected to Modbus server at {}:{}", MODBUS_IP, MODBUS_PORT);

            ReadMultipleRegistersRequest request = new ReadMultipleRegistersRequest(startAddress, quantity);
            ModbusTCPTransaction transaction = new ModbusTCPTransaction(connection);
            transaction.setRequest(request);
            transaction.execute();

            ReadMultipleRegistersResponse response = (ReadMultipleRegistersResponse) transaction.getResponse();
            log.info("Received response with {} registers", response.getWordCount());

            for (int i = 0; i < response.getWordCount(); i++) {
                registerValues.add(response.getRegister(i).getValue());
            }
        } finally {
            if (connection != null && connection.isConnected()) {
                connection.close();
            }
        }

        return registerValues;
    }
}


