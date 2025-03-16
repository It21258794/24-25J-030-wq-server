package com.waterboard.waterqualityprediction.tests;

import com.waterboard.waterqualityprediction.dto.user.UserDto;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.response.Response;
public class UserModuleCommonTests {

    public static BaseTest baseTest;

    public static Response registerUser(UserDto user) {
        Response response = baseTest.getDocumentedGiven("user-registration")
                .contentType(ContentType.JSON)
                .headers(TestUtils.getHeaders())
                .body(user)
                .when().post("/user")
                .then()
                .statusCode(200)
                .extract().response();
        TestUtils.extractToken(response);
        return response;
    }

    public static Response changePassword(UserDto user) {
        Response response = baseTest.getDocumentedGiven("user-temp-password-change")
                .contentType(ContentType.JSON)
                .body(user)
                .when().post("/user/password-change")
                .then()
                .statusCode(200)
                .extract().response();
        return response;
    }

    public static Response login(UserDto user) {
        Response response = baseTest.getDocumentedGiven("user-login")
                .contentType(ContentType.JSON)
                .body(user)
                .when().post("/user/login")
                .then()
                .statusCode(200)
                .extract().response();
        TestUtils.extractToken(response);
        return response;
    }

    public static Response updatePassword(String emailToken, String password) {
        Response response2 = baseTest.getDocumentedGiven("user-create-password-reset-request")
                .contentType(ContentType.JSON)
                .queryParam("token", emailToken)
                .when().post("/user/password-reset/token")
                .then()
                .statusCode(200)
                .extract().response();
        String passwordResetToken = response2.jsonPath().getString("passwordResetToken");

        UserDto user = new UserDto();
        user.setPassword(password);

        Response response3 = baseTest.getDocumentedGiven("user-reset-password")
                .contentType(ContentType.JSON)
                .queryParam("token", passwordResetToken)
                .body(user)
                .when().post("/user/reset-password")
                .then()
                .statusCode(200)
                .extract().response();
        return response3;
    }
}
