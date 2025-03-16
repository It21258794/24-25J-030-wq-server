package com.waterboard.waterqualityprediction;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Setter
@Getter
public class AnalyticsModuleConfigs {

    @Value("${analytics.enable_rate_limits:true}")
    private boolean enableRateLimits = true;

}
