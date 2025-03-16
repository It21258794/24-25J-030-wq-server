package com.waterboard.waterqualityprediction.tests;

import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import com.github.javafaker.Faker;
import org.apache.commons.lang3.RandomUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

public class TestUtils {
    public static final String HEADER = "Authorization";
    public static final String TOKEN_KEY = "auth_key";
    private static final String CONFIG_FILE_NAME = "vi-test-configs.properties";
    public static String ENV;
    public static Map<String, String> testData = new HashMap<String, String>();
    public static Faker faker = new Faker();

    public static void store() {
        Properties properties = new Properties();
        for (Map.Entry<String, String> entry : testData.entrySet()) {
            if (entry.getKey()==null || entry.getValue()==null) continue;
            properties.put(entry.getKey(), entry.getValue());
        }
        try {
            System.out.println("updating data");
            properties.store(new FileOutputStream(TestUtils.getTestDataPath()), "updating data");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void fetch() {
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(TestUtils.getTestDataPath()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (String key : properties.stringPropertyNames()) {
            testData.put(key, properties.get(key).toString());
        }
    }

    public static String getData(String key) {
        return testData.get(TestUtils.ENV + "_" +  key);
    }

    public static void setData(String key, String value) {
        System.out.print("key");
        testData.remove(TestUtils.ENV + "_" + key);
        testData.put(TestUtils.ENV + "_" + key, value);
    }

    public static String getEnv(String key){
        Properties properties = new Properties();
        try {
            properties.load(TestUtils.class.getClassLoader().getResourceAsStream(TestUtils.ENV + ".properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties.getProperty(key);
    }

    public static String getTestDataPath() {
        URL resourceUrl = TestUtils.class.getClassLoader().getResource(CONFIG_FILE_NAME);

        if (resourceUrl != null) {
            return Paths.get(resourceUrl.getPath()).toString();
        }

        String resourcePath = "../main-application/src/main/resources/" + CONFIG_FILE_NAME;
        File configFile = new File(resourcePath);

        if (!configFile.exists()) {
            try {
                Files.copy(TestUtils.class.getResourceAsStream("/" + CONFIG_FILE_NAME),
                        configFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return configFile.getAbsolutePath();
    }

    public static void extractToken(Response result) {
        TestUtils.setData(TOKEN_KEY, result.getHeader(HEADER));
    }

    public static Headers getHeaders() {
        Headers headers = new Headers(
                new Header(HEADER, getData(TOKEN_KEY)),
                new Header("V-App-Version", "TestCase-Version"),
                new Header("V-App-Platform", "TestCase-Platform"),
                new Header("V-Device", "TestCase-Device"),
                new Header("wt-device", "web")
        );

        return headers;
    }

    public static String randomStr() {
        return String.valueOf(RandomUtils.nextInt(1,99999));
    }

    public static String uuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }


}

