package com.waterboard.waterqualityprediction.dto.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class VerificationDataDto{

    @NotBlank(message = "serverRef field is missing")
    private String serverRef;
    @NotBlank(message = "otp field is missing")
    private String otp;
    @NotBlank(message = "type field is missing")
    private String type;

    public VerificationDataDto(String serverRef) {
        this.serverRef = serverRef;
    }
}
