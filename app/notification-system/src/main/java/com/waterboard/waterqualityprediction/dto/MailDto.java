package com.waterboard.waterqualityprediction.dto;

import com.waterboard.waterqualityprediction.models.Mail;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class MailDto {
    private Mail.MailAddress from;
    private List<Mail.MailAddress> to;
    private String subject;
    private Mail.HtmlTemplate htmlTemplate;
    private Map<String, File> attachments;

    public static MailDto init(Mail mail){
        var dto = new MailDto();
        dto.setFrom(mail.getFrom());
        dto.setTo(mail.getTo());
        dto.setSubject(mail.getSubject());
        dto.setHtmlTemplate(mail.getHtmlTemplate());
        dto.setAttachments(mail.getAttachments());
        return dto;

    }
}
