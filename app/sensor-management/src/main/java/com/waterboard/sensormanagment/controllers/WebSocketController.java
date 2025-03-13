package com.waterboard.sensormanagment.controllers;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class WebSocketController {

    @MessageMapping("/message")
    @SendTo("/topic/messages")
    public String sendMessage(String message) {
        return "Received: " + message;
    }

    @MessageMapping("/registerData")
    @SendTo("/topic/registerData")
    public List<Integer> sendRegisterData(List<Integer> data) {
        return data;
    }
}

