package com.waterboard.waterqualityprediction;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Component
@Slf4j
public class AppStrings {

    private static final String defaultLanguage = "en";

    @Autowired
    private GlobalAppConfig globalAppConfig;
    private DocumentContext json;

    @Autowired
    private ResourceLoader resourceLoader;

    @PostConstruct
    public void postConstruct() throws IOException {
        log.info("Reading app string file from the classpath");
        if (globalAppConfig == null) {
            log.error("GlobalConfigs bean is not initialized!");
            throw new IllegalStateException("GlobalConfigs bean is not initialized!");
        }

        String appStringsFile = globalAppConfig.getAppStringsFile();
        if (appStringsFile == null || appStringsFile.isEmpty()) {
            log.error("App strings file path is not configured!");
            throw new IllegalStateException("App strings file path is not configured!");
        }

        Resource resource = resourceLoader.getResource(appStringsFile);
        if (!resource.exists()) {
            log.error("Resource file not found at path: {}", appStringsFile);
            throw new IllegalStateException("Resource file not found at path: " + appStringsFile);
        }

        String jsonString = IOUtils.toString(resource.getInputStream(), StandardCharsets.UTF_8);
        json = JsonPath.parse(jsonString);
    }

    public String getString(String path, String language) {
        return json.read(language + "." + path, String.class);
    }

    public String getString(String path) {
        return getString(path, defaultLanguage);
    }

    public ContactInformation getContactInformation() {
        return json.read("en.contact-information", ContactInformation.class);
    }

    public String getFormattedString(String path, Map<String, Object> vars) {
        return getFormattedString(path, vars, defaultLanguage);
    }

    public String getFormattedString(String path, Map<String, Object> vars, String language) {
        String str = getString(path, language);
        for (Map.Entry<String, Object> entry : vars.entrySet()) {
            str = str.replace("${" + entry.getKey() + "}", entry.getValue().toString());
        }
        return str;
    }
}
