package com.waterboard.waterqualityprediction;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class ExternalApiStatus {
    private boolean isExternal;
}
