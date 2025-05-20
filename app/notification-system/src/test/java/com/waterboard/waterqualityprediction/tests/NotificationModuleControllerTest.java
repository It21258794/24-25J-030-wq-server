package com.waterboard.waterqualityprediction.tests;

import com.waterboard.waterqualityprediction.dto.MailDto;
import com.waterboard.waterqualityprediction.dto.MessageDto;
import com.waterboard.waterqualityprediction.models.Mail;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class NotificationModuleControllerTest extends BaseTest {

    @Test
    @Order(1)
    public void test_send_sms() {
        initAdminSession();
        MessageDto message = new MessageDto();
        message.setMessage("This is a test message");
        message.setPhoneNumber("+94766974709");

        Response response = getDocumentedGiven("notification-system-send-sms")
                .contentType(ContentType.JSON)
                .headers(TestUtils.getHeaders())
                .body(message)
                .when().post("/system/send/sms")
                .then()
                .statusCode(200)
                .extract().response();
        TestUtils.extractToken(response);
    }

    @Test
    @Order(2)
    public void test_send_mail() {
        initAdminSession();
        String fromName = faker.name().firstName().toLowerCase();
        String toName = faker.name().firstName().toLowerCase();

        log.info("from - {} | to - {}", fromName, toName);

        Map<String, Object> properties = new HashMap<String, Object>();
        properties.put("name", "Developer!");
        properties.put("sign", "Java Developer");

        Mail mail = Mail.builder()
                .from(new Mail.MailAddress(fromName, fromName + "@mailinator.com"))
                .to(Arrays.asList(new Mail.MailAddress(toName,"kaveeshalankeshwara2001@gmail.com")))
                .subject("This is a sample email test")
                .htmlTemplate(new Mail.HtmlTemplate("sample", properties))
                .build();

        Response response = getDocumentedGiven("notification-system-send-mail")
                .contentType(ContentType.JSON)
                .headers(TestUtils.getHeaders())
                .body(MailDto.init(mail))
                .when().post("/system/send/mail")
                .then()
                .statusCode(200)
                .extract().response();
        TestUtils.extractToken(response);
    }
}
