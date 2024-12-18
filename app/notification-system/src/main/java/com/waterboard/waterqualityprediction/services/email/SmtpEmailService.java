package com.waterboard.waterqualityprediction.services.email;

import com.waterboard.waterqualityprediction.JsonUtils;
import com.waterboard.waterqualityprediction.NotificationModuleConfigs;
import com.waterboard.waterqualityprediction.dto.MailDto;
import com.waterboard.waterqualityprediction.coreExceptions.notification.MailNotSendException;
import com.waterboard.waterqualityprediction.models.Mail;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;


import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
public class SmtpEmailService implements EmailService {
    @Autowired
    JavaMailSender emailSender;

    @Autowired
    private SpringTemplateEngine templateEngine;

    @Autowired
    private NotificationModuleConfigs notificationModuleConfigs;


    @Override
    public MailDto sendEmail(Mail mail) {
        log.info("sending email with smpt. address = {}", JsonUtils.objectToString(mail.getTo()));
        String[] mailToArr = getMailToFromList(mail.getTo());
        try {
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message,
                    MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name());
            String htmlContent = getHtmlContent(mail);
            helper.setTo(mailToArr);
            helper.setText(htmlContent, true);
            helper.setSubject(mail.getSubject());
            helper.setFrom(new InternetAddress(
                    notificationModuleConfigs.getNotificationEmailSenderEmail(),
                    notificationModuleConfigs.getNotificationEmailSenderName()));
            if (mail.getAttachments() != null && !mail.getAttachments().isEmpty()) {
                mail.getAttachments().forEach((key, value) -> {
                    try {
                        helper.addAttachment(key, value);
                    } catch (MessagingException e) {
                        e.printStackTrace();
                        log.error("error in file attachment email={} file={}", mailToArr, key);
                    }
                });
            }
            emailSender.send(message);
            log.info("sending email success with smpt. address = {}", JsonUtils.objectToString(mail.getTo()));
        } catch (Exception e) {
            log.error("email sending error" + e.getMessage());
            throw new MailNotSendException(e.getMessage());
        }

        return MailDto.init(mail);
    }

    private String[] getMailToFromList(List<Mail.MailAddress> to) {
        Mail.MailAddress[] mailReciversArray = new Mail.MailAddress[to.size()];
        mailReciversArray = to.toArray(mailReciversArray);
        String[] emails = Arrays.stream(mailReciversArray)
                .map(reciver -> reciver.getEmail())
                .toArray(size -> new String[to.size()]);
        return emails;
    }

    private String getHtmlContent(Mail mail) {
        Context context = new Context();
        context.setVariables(mail.getHtmlTemplate().getProps());
        return templateEngine.process(mail.getHtmlTemplate().getTemplate(), context);
    }
}
