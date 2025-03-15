package com.waterboard.waterqualityprediction.controllers;


import com.waterboard.waterqualityprediction.models.Message;
import com.waterboard.waterqualityprediction.models.ResponseMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.scheduling.annotation.Scheduled;

@Controller
public class MessageController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/message")
    @SendTo("/ws")
    public Message reciveMessage(@Payload Message message) {

        messagingTemplate.convertAndSend("/ws", message);
        return message;
    }

}