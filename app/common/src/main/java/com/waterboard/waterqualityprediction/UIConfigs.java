package com.waterboard.waterqualityprediction;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Setter
@Getter
public class UIConfigs {

    @Value("${ui.url}")
    private String uiUrl;

    @Value("${ui.links.login}")
    private String loginUrl;

    public String getLoginUrl() {
        return uiUrl + loginUrl;
    }
}

