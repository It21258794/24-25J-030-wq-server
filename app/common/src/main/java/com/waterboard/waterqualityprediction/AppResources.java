package com.waterboard.waterqualityprediction;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.Resources;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Slf4j
public class AppResources {
    public static Optional<String> asString(String resource) {
        String string = null;
        try {
            string = IOUtils.toString(Resources.getInputStream(resource), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(String.format("file retrieving error for resource = %s", resource));
        }

        return Optional.of(string);
    }
}
