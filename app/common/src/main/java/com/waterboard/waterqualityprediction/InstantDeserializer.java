package com.waterboard.waterqualityprediction;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.Instant;
import java.time.format.DateTimeFormatter;

public class InstantDeserializer extends JsonDeserializer<Instant> {
    @Override
    public Instant deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String dateString = p.getText().trim();
        // Manually append 'Z' to the string to assume UTC
        dateString = dateString + "Z";
        return Instant.from(DateTimeFormatter.ISO_DATE_TIME.parse(dateString));
    }
}
