package com.waterboard.waterqualityprediction;

import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@NoArgsConstructor
public class Result<T> {
    private T data;
    private Map<String, String> extras = new HashMap<>();

    public Result(T data) {
        this.data = data;
    }

    public Result(Map<String, String> extras) {
        this.extras = extras;
    }

    public Result(T data, Map<String, String> extras) {
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

    public static <T>Result<T> of(T value) {
        return new Result<>(value);
    }

    public static <T>Result<T> empty() {
        return new Result<>();
    }

    public static <T>Result<T> of(Optional<T> value) {
        if(value.isPresent()) {
            return Result.of(value.get());
        }
        return Result.empty();
    }

    public void setData(T data) {
        this.data = data;
    }
}