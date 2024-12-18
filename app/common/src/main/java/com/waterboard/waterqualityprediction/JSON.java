package com.waterboard.waterqualityprediction;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class JSON {

    public static String objectToString(Object object) {
        try {
            return getMapper().writeValueAsString(object);
        } catch (Exception exp) {
            log.error("object to string error = {}", exp.getMessage());
            exp.printStackTrace();
            return "{}";
        }
    }

    public static <T> T stringToObject(String json, Class<T> type) {
        try {
            return getMapper().readValue(json, type);
        } catch (IOException exp) {
            log.error("string to object error = {}, json = {}", exp.getMessage(), json);
            exp.printStackTrace();
            return null;
        }
    }

    public static <T> T stringToObject(String json, TypeReference<T> type) {
        try {
            return getMapper().readValue(json, type);
        } catch (IOException exp) {
            log.error("string to object error = {}, json = {}", exp.getMessage(), json);
            exp.printStackTrace();
            return null;
        }
    }

    public static ObjectMapper getMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper;
    }
}
