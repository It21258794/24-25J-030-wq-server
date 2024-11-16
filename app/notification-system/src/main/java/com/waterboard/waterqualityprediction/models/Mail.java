package com.waterboard.waterqualityprediction.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.waterboard.waterqualityprediction.dto.MailDto;
import lombok.*;

import java.io.File;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
public class Mail {

    @Getter
    @Setter
    public static class MailAddress{
        private String name;
        private String email;

        public MailAddress() {
        }

        public MailAddress(String name, String email) {
            this.name = name;
            this.email = email;
        }

        public MailAddress(String email) {
            this.email = email;
        }
    }

    @Getter
    @Setter
    public static class HtmlTemplate{
        private String template;
        private Map<String, Object> props;

        public HtmlTemplate() {
        }

        public HtmlTemplate(String template, Map<String, Object> props) {
            this.template = template;
            this.props = props;
        }
    }

    private MailAddress from;
    private List<MailAddress> to;
    private String subject;
    private HtmlTemplate htmlTemplate;
    private Map<String, File> attachments;
    public Mail() {
    }

    public static Mail init(MailDto mailDto){
        var dto = Mail.builder()
                .from(mailDto.getFrom())
                .to(mailDto.getTo())
                .subject(mailDto.getSubject())
                .htmlTemplate(mailDto.getHtmlTemplate()).build();

        return dto;
    }

    @JsonIgnore
    public String getToAsString() {
        if(this.getTo() == null) {
            return "";
        }
        String tos = "";
        for (MailAddress mailAddress : this.getTo()) {
            tos += mailAddress.getEmail() + ",";
        }
        return tos;
    }
}
