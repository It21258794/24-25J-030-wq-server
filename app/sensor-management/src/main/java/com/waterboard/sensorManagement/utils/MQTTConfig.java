package com.waterboard.sensorManagement.utils;
import com.amazonaws.services.iot.client.AWSIotDevice;
import com.amazonaws.services.iot.client.AWSIotException;
import com.amazonaws.services.iot.client.AWSIotMqttClient;
import com.amazonaws.services.iot.client.AWSIotQos;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.waterboard.sensorManagement.dto.MyMessage;
import com.waterboard.sensorManagement.dto.WQAttributes;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MQTTConfig {
    String clientEndpoint = "alrxlukoeooxkg-ats.iot.ap-south-1.amazonaws.com";
    String clientId = "sensor_management";

    String awsAccessKeyId = System.getenv("AWS_ACCESS_KEY_ID");
    String awsSecretAccessKeyId = System.getenv("AWS_SECRET_ACCESS_KEY");

    String sessionToken = "";

    AWSIotMqttClient client = null;

    public void connect() {
        try {
            AWSIotMqttClient client = new AWSIotMqttClient(clientEndpoint, clientId, awsAccessKeyId, awsSecretAccessKeyId, sessionToken);
            client.connect();
            System.out.println("Connected to AWS IoT Core successfully.");
        } catch (AWSIotException e) {
            System.err.println("Failed to connect to AWS IoT Core: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void publish(WQAttributes payload) throws AWSIotException, JsonProcessingException {
        String topic = "sensor_management";
        AWSIotQos qos = AWSIotQos.QOS0;
        long timeout = 3000;
        ObjectMapper mapper = new ObjectMapper();
        AWSIotDevice device = new AWSIotDevice(topic);
        AWSIotMqttClient client = new AWSIotMqttClient(clientEndpoint, clientId, awsAccessKeyId, awsSecretAccessKeyId, sessionToken);
        client.attach(device);

        MyMessage message = new MyMessage(topic, qos, mapper.writeValueAsString(payload));
        client.publish(message, timeout);
    }



}

