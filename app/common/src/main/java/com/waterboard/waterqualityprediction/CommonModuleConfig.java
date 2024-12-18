package com.waterboard.waterqualityprediction;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class CommonModuleConfig {

    // Data Encryption Service Configuration
    @Value("${encryption.provider:}")
    private String encryption_provider;

    @Value("${encryption.common_key:}")
    private String encryption_common_key;

    @Value("${encryption.enabled:}")
    private boolean encryption_enabled;


    @Value("${analytics.enable_rate_limits:true}")
    private boolean enableRateLimits = true;

}
