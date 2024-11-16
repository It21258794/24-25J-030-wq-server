package com.waterboard.waterqualityprediction.common;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class CheckExternalApi {
    private boolean isExternal;
}
