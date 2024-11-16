package com.waterboard.waterqualityprediction.models;

import com.waterboard.waterqualityprediction.dto.MessageDto;
import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SMSMessage {
    private String phoneNumber;
    private String message;

    public static SMSMessage init(MessageDto messageDto) {
        var dto = SMSMessage.builder()
                .message(messageDto.getMessage())
                .phoneNumber(messageDto.getPhoneNumber())
                .build();
        return dto;
    }
}
