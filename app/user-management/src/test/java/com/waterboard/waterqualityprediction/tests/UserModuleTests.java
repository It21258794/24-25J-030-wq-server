package com.waterboard.waterqualityprediction.tests;

import com.waterboard.waterqualityprediction.dto.user.UserDto;
import com.waterboard.waterqualityprediction.dto.user.VerificationDataDto;
import com.waterboard.waterqualityprediction.models.user.User;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;

@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserModuleTests extends BaseTest{
    @BeforeEach
    public void initCommonTest() {
        UserModuleCommonTests.baseTest = this;
    }

    @Test
    @Order(1)
    public void test_user_registration() {
        initAdminSession();
        UserDto user = new UserDto();
        user.setFirstName(faker.name().firstName());
        user.setLastName(faker.name().lastName());
        user.setEmail(faker.number().digits(6) + "@mail.com");
        user.setPhone(faker.phoneNumber().cellPhone());
        user.setPhoneCountryCode("94");
        user.setRole(User.UserRoles.USER.getRoleName());
        Response response = UserModuleCommonTests.registerUser(user);
        String serverRef = response.getBody().jsonPath().getString("serverRef");
        String emailToken = response.getHeader("vi-extra");
        if (emailToken != null) {
            TestUtils.setData("user_vi_extra", emailToken);
        } else {
            TestUtils.setData("user_vi_extra", "null");
        }
//        TestUtils.setData("user_server_ref", serverRef);
        TestUtils.setData("user_email", user.getEmail());
        TestUtils.setData("user_phone", user.getPhone());
        TestUtils.setData("user_password", "1234@User");
        TestUtils.setData("user_phone_country_code", user.getPhoneCountryCode());
        TestUtils.setData("user_temp_password", response.getBody().jsonPath().getString("currentPassword"));
    }

    @Test
    @Order(2)
    public void test_temp_password_change() {
        UserDto user = new UserDto();
        user.setEmail(TestUtils.getData("user_email"));
        user.setCurrentPassword(TestUtils.getData("user_temp_password"));
        user.setPassword("1234@User");
        System.out.println("test user email "+user.getEmail());
        System.out.println("test user password " +user.getPassword());
        Response response = UserModuleCommonTests.changePassword(user);
    }

    @Test
    @Order(3)
    public void test_user_login_email() {
        UserDto user = new UserDto();
        user.setEmail(TestUtils.getData("user_email"));
        user.setPassword(TestUtils.getData("user_password"));
        System.out.println("test user email "+user.getEmail());
        System.out.println("test user password " +user.getPassword());
        Response response = UserModuleCommonTests.login(user);
        log.info("token = {}", response.getHeader("Authorization"));
    }

    @Test
    @Order(23)
    public void test_get_user_details() {
        initAdminSession();
        Response response = getDocumentedGiven("user-get-user-detail-by-token")
                .contentType(ContentType.JSON)
                .headers(TestUtils.getHeaders())
                .when().get("/user/users")
                .then()
                .statusCode(200)
                .extract().response();
    }

    @Test
    @Order(27)
    public void test_password_reset_email_with_code() {
        UserDto user = new UserDto();
        user.setEmail(TestUtils.getData("user_email"));
        Response response = getDocumentedGiven("user-send-password-reset-email-code")
                .contentType(ContentType.JSON)
                .body(user)
                .when().post("/user/password-reset/otp")
                .then()
                .statusCode(200)
                .extract().response();
        String emailToken = response.body().jsonPath().getString("serverRef");
        log.info("email token = {}", emailToken);

        VerificationDataDto verificationDataDto = new VerificationDataDto();
        verificationDataDto.setOtp("55555");
        verificationDataDto.setServerRef(emailToken);

        Response response2 = getDocumentedGiven("user-create-password-reset-request-with-code")
                .contentType(ContentType.JSON)
                .body(verificationDataDto)
                .when().post("/user/password-reset/token")
                .then()
                .statusCode(200)
                .extract().response();
        String passwordResetToken = response2.jsonPath().getString("passwordResetToken");
        log.info("password reset token = {}", passwordResetToken);

        user = new UserDto();
        user.setPassword("1234");

        Response response3 = getDocumentedGiven("user-reset-password")
                .contentType(ContentType.JSON)
                .queryParam("token", passwordResetToken)
                .body(user)
                .when().post("/user/password-reset")
                .then()
                .statusCode(200)
                .extract().response();
    }

}
