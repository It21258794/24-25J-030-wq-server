package com.waterboard.waterqualityprediction.services;

import com.waterboard.waterqualityprediction.*;
import com.waterboard.waterqualityprediction.models.Mail;
import com.waterboard.waterqualityprediction.models.SMSMessage;
import com.waterboard.waterqualityprediction.models.user.User;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class UserNotificationProxyService {

    @Autowired
    private GlobalConfigs globalConfigs;

    @Autowired
    private AppStrings appStrings;

    @Autowired
    private NotificationModule notificationModule;

    public String sendPasswordResetEmailCode(User user) {
        int code = globalConfigs.isDebugModeOn() ? 55555 : Generator.getRandom5DigitNumber();
        JWTContent content = JWTContent.builder()
                .expiredIn(DateUtils.MILLIS_PER_MINUTE * 10)
                .subject(user.getId())
                .payload(Map.of(UserModuleExtraKeys.ACTION, UserModuleExtraKeys.RESET_PASSWORD_REQUEST,
                        UserModuleExtraKeys.HASH, Hash.make(code + ""),
                        UserModuleExtraKeys.OTP_TOKEN,user.getMetaData().get(UserModuleExtraKeys.OTP_TOKEN).toString()

                ))
                .build();
        String token = JWT.encode(content, globalConfigs.getSecretKey());
        log.info("sending password reset code email to user = {}", appStrings.getContactInformation().getSupportEmail());
        Mail mail = Mail.builder()
                .htmlTemplate(new Mail.HtmlTemplate("emails/user_password_reset_request_code.html",
                        Map.of("user", user,
                                "code", code,
                                "contactInfo",appStrings.getContactInformation()
                        )))
                .to(List.of(new Mail.MailAddress(user.getEmail())))
                .subject(appStrings.getString("email-contents.reset-password.subject"))
                .build();
        notificationModule.sendEmailAsync(mail);
        return token;
    }

    public String sendPasswordResetSMS(User user) {
        int code = globalConfigs.isDebugModeOn() ? 55555 : Generator.getRandom5DigitNumber();
        JWTContent content = JWTContent.builder()
                .expiredIn(DateUtils.MILLIS_PER_MINUTE * 10)
                .subject(user.getId())
                .payload(Map.of(UserModuleExtraKeys.ACTION, UserModuleExtraKeys.RESET_PASSWORD_REQUEST,
                        UserModuleExtraKeys.HASH, Hash.make(code + ""),
                        UserModuleExtraKeys.OTP_TOKEN,user.getMetaData().get(UserModuleExtraKeys.OTP_TOKEN).toString()
                ))
                .build();
        String token = JWT.encode(content, globalConfigs.getSecretKey());
        log.info("sending password reset sms to user = {}", user.getEmail());

        String message = appStrings.getFormattedString("sms-templates.otp-password-reset", Map.of("code", code));
        this.notificationModule.sendSmsAsync(new SMSMessage(user.getPhoneWithCountryCode(), message));
        return token;
    }
}
