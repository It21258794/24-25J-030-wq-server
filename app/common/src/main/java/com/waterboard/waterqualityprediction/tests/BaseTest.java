package com.waterboard.waterqualityprediction.tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import com.waterboard.waterqualityprediction.JsonUtils;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.ObjectMapperConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.path.json.mapper.factory.Jackson2ObjectMapperFactory;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.core.io.ClassPathResource;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;

import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;

import static io.restassured.RestAssured.given;

import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;

public class BaseTest {
    public static String SERVER_URL = "http://localhost:8080";
    public static final String BASE_PATH = "/api";
    public Faker faker = new Faker();
    public static final String DOCUMENTATION_DIR = "../main-application/src/main/resources/documentations";


    @RegisterExtension
    RestDocumentationExtension restDocumentation = new RestDocumentationExtension(DOCUMENTATION_DIR);

    private RequestSpecification documentationSpec;

    @BeforeAll
    public static void beforeAll() {
        RestAssured.baseURI = SERVER_URL;
        RestAssured.basePath = BASE_PATH;
    }

    @BeforeEach
    public void beforeTest(RestDocumentationContextProvider restDocumentation) throws MalformedURLException {
        TestUtils.ENV = new URL(SERVER_URL).getHost();
        TestUtils.fetch();
        this.documentationSpec = new RequestSpecBuilder()
                .addFilter(documentationConfiguration(restDocumentation))
                .build();
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
        RestAssured.config = RestAssuredConfig.config().objectMapperConfig(
                new ObjectMapperConfig().jackson2ObjectMapperFactory(new Jackson2ObjectMapperFactory() {
                    @Override
                    public ObjectMapper create(Type cls, String charset) {
                        return JsonUtils.getMapper();
                    }
                })
        );
    }

    @AfterEach
    public void afterTest() {
        TestUtils.store();
    }

    public RequestSpecification getGiven() {
        return given();
    }

    public RequestSpecification getDocumentedGiven(String documentName) {
        var gvn = given(this.documentationSpec)
                .filter(document(documentName, preprocessRequest(prettyPrint(),
                                removeMatchingHeaders("Host", "Content-Length")),
                        preprocessResponse(prettyPrint(),
                                removeMatchingHeaders(
                                        "Vary",
                                        "X-Content-Type-Options",
                                        "Cache-Control",
                                        "Pragma",
                                        "Expires",
                                        "X-Frame-Options",
                                        "Transfer-Encoding",
                                        "Date",
                                        "Keep-Alive",
                                        "Connection",
                                        "Content-Length",
                                        "X-XSS-Protection",
                                        "vary",
                                        "x-content-type-options",
                                        "cache-control",
                                        "pragma",
                                        "expires",
                                        "x-frame-options",
                                        "transfer-encoding",
                                        "date",
                                        "keep-alive",
                                        "connection",
                                        "content-length",
                                        "x-xss-protection",
                                        "CF-Cache-Status",
                                        "Expect-CT",
                                        "Server",
                                        "Content-Encoding",
                                        "alt-svc",
                                        "CF-RAY")))).and();
        try {
            return gvn.when();
        } catch (Exception e) {
            e.printStackTrace();
            return given();
        }
    }

    public static Response initAdminSession() {
        JSONObject object = new JSONObject();
        object.put("email", "kaveeshalankeshwara2001@gmail.com");
        object.put("password", "testuser1");
        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(object)
                .when().post("/user/login")
                .then()
                .statusCode(200)
                .extract().response();
        TestUtils.extractToken(response);
        return response;
    }

    public static Response initUserSession(String user) {
        JSONObject object = new JSONObject();
        object.put("email", TestUtils.getData(user + "_email"));
        object.put("password", TestUtils.getData(user + "_password"));
        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(object)
                .when().post("/user/login")
                .then()
                .statusCode(200)
                .extract().response();
        TestUtils.extractToken(response);
        return response;
    }
}
