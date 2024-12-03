package com.waterboard.sensorManagement.services;

import com.amazonaws.services.iot.client.AWSIotException;
import com.amazonaws.services.iot.client.AWSIotQos;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.waterboard.sensorManagement.dto.MyMessage;
import com.waterboard.sensorManagement.dto.WQAttributes;
import org.springframework.beans.factory.annotation.Autowired;
import com.waterboard.sensorManagement.utils.MQTTConfig;
import org.springframework.stereotype.Service;

@Service
public class MqttPubSubService {

    @Autowired
    MQTTConfig mQTTConfig;

    public void publishMessage(WQAttributes payload) throws AWSIotException, JsonProcessingException {
        mQTTConfig.connect();
        mQTTConfig.publish(payload);

    }

}
