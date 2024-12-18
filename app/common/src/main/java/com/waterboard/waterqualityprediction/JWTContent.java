package com.waterboard.waterqualityprediction;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Builder
@Getter
@Setter
public class JWTContent {
    private String subject;
    private Map<String, String> payload;
    private float expiredIn;
}
