package com.waterboard.waterqualityprediction.dto.user;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PasswordResetTokenDto{
    @NotBlank(message = "passwordRestToken is missing")
    private String passwordResetToken;
}