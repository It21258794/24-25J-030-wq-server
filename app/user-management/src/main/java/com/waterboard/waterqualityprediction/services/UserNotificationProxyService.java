package com.waterboard.waterqualityprediction.services;

import com.waterboard.waterqualityprediction.*;
import com.waterboard.waterqualityprediction.models.Mail;
import com.waterboard.waterqualityprediction.models.SMSMessage;
import com.waterboard.waterqualityprediction.models.user.User;
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
    private GlobalAppConfig globalAppConfig;

    @Autowired
    private AppStrings appStrings;

    @Autowired
    private NotificationModule notificationModule;

    @Autowired
    private UIConfigs uiConfigs;

    public String sendPasswordResetEmailCode(User user) {
        int code = globalAppConfig.isDebugModeOn() ? 55555 : Generator.getRandom5DigitNumber();
        JWTContent content = JWTContent.builder()
                .expiredIn(DateUtils.MILLIS_PER_MINUTE * 10)
                .mainSubject(user.getId())
                .payload(Map.of(UserModuleExtraKeys.ACTION, UserModuleExtraKeys.RESET_PASSWORD_REQUEST,
                        UserModuleExtraKeys.HASH, HashUtil.make(code + ""),
                        UserModuleExtraKeys.OTP_TOKEN,user.getMetaData().get(UserModuleExtraKeys.OTP_TOKEN).toString()

                ))
                .build();
        String token = JwtUtil.encode(content, globalAppConfig.getSecretKey());
        log.info("sending password reset code email to user = {}", user.getEmail());
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
        int code = globalAppConfig.isDebugModeOn() ? 55555 : Generator.getRandom5DigitNumber();
        JWTContent content = JWTContent.builder()
                .expiredIn(DateUtils.MILLIS_PER_MINUTE * 10)
                .mainSubject(user.getId())
                .payload(Map.of(UserModuleExtraKeys.ACTION, UserModuleExtraKeys.RESET_PASSWORD_REQUEST,
                        UserModuleExtraKeys.HASH, HashUtil.make(code + ""),
                        UserModuleExtraKeys.OTP_TOKEN,user.getMetaData().get(UserModuleExtraKeys.OTP_TOKEN).toString()
                ))
                .build();
        String token = JwtUtil.encode(content, globalAppConfig.getSecretKey());
        log.info("sending password reset sms to user = {}", user.getEmail());

        String message = appStrings.getFormattedString("sms-templates.otp-password-reset", Map.of("code", code));
        this.notificationModule.sendSmsAsync(new SMSMessage(user.getPhoneWithCountryCode(), message));
        return token;
    }

    public void sendEmailWithTemporaryPassword(String email, String firstName, String lastName, String tempPassword) {
        log.info("sending temporary password email to user = {}", email);
        Mail mail = Mail.builder()
                .htmlTemplate(new Mail.HtmlTemplate("emails/user_temp_password.html",
                        Map.of("firstName", firstName,
                                "lastName", lastName,
                                "email", email,
                                "tempPassword", tempPassword,
                                "login_url",uiConfigs.getUiUrl(),
                                "contactInfo",appStrings.getContactInformation()
                        )))
                .to(List.of(new Mail.MailAddress(email)))
                .subject(appStrings.getString("email-contents.temporary-password.subject"))
                .build();
        notificationModule.sendEmailAsync(mail);
    }

    public void sendAccountActivateEmail(User user) {
        log.info("sending temporary password email to user = {}", user.getEmail());
        Mail mail = Mail.builder()
                .htmlTemplate(new Mail.HtmlTemplate("emails/user_activation.html",
                        Map.of("user",user,
                                "login_url",uiConfigs.getUiUrl(),
                                "contactInfo",appStrings.getContactInformation()
                        )))
                .to(List.of(new Mail.MailAddress(user.getEmail())))
                .subject(appStrings.getString("email-contents.account-activation.subject"))
                .build();
        notificationModule.sendEmailAsync(mail);
    }

}
