package com.waterboard.sensorManagement.controllers;

import com.amazonaws.services.iot.client.AWSIotException;
import com.waterboard.sensorManagement.dto.WQAttributes;
import com.waterboard.sensorManagement.services.MqttPubSubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MQTTController {

    @Autowired
    MqttPubSubService service;

    @PostMapping("/publish")
    public String publishMessage(@RequestBody WQAttributes payload) throws AWSIotException {

        service.publishMessage(payload);

        return "Published";
    }

}
