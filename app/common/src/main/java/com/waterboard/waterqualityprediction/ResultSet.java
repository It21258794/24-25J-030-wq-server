package com.waterboard.waterqualityprediction;

import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@NoArgsConstructor
public class ResultSet<T> {
    private T data;
    private Map<String, String> extras = new HashMap<>();

    public ResultSet(T data) {
        this.data = data;
    }

    public ResultSet(Map<String, String> extras) {
        this.extras = extras;
    }

    public ResultSet(T data, Map<String, String> extras) {
        this.data = data;
        this.extras = extras;
    }

    public T get() {
        return data;
    }

    public T getOrDefault(T defaultValue) {
        if(isEmpty()) {
            return defaultValue;
        }
        return get();
    }

    public String getExtraOrDefault(String key, String defaultValue) {
        if(hasExtra(key)) {
            return getExtra(key);
        }

        return defaultValue;
    }

    public void addExtra(String key, String value) {
        extras.put(key, value);
    }

    public String getExtra(String key) {
        return extras.get(key);
    }

    public boolean hasExtra(String key) {
        return extras.containsKey(key) && extras.get(key) != null;
    }

    public boolean isEmpty() {
        return this.get() == null;
    }

    public boolean isPresent() {
        return !this.isEmpty();
    }

    public static <T> ResultSet<T> of(T value) {
        return new ResultSet<>(value);
    }

    public static <T> ResultSet<T> empty() {
        return new ResultSet<>();
    }

    public static <T> ResultSet<T> of(Optional<T> value) {
        if(value.isPresent()) {
            return ResultSet.of(value.get());
        }
        return ResultSet.empty();
    }

    public void setData(T data) {
        this.data = data;
    }
}