package com.waterboard.waterqualityprediction;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Setter
@Getter
public class GlobalAppConfig {
    @Value("${global.is_debug:false}")
    private boolean isDebugModeOn = false;

    @Value("${global.secret_key:103ur0wjf012u30ru130tr03tf}")
    private String secretKey = "103ur0wjf012u30ru130tr03tf";

    @Value("${global.home_dir}")
    private String homeDir;

    @Value("${global.app_url}")
    private String appUrl;

    @Value("${global.strings_file}")
    private String appStringsFile;
}
