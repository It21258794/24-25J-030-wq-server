package com.waterboard.waterqualityprediction.coreExceptions.http;

import com.waterboard.waterqualityprediction.JsonUtils;
import com.waterboard.waterqualityprediction.coreExceptions.ExceptionType;
import lombok.Getter;
import lombok.Setter;
import org.json.simple.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Getter
@Setter
public class BaseException extends RuntimeException {
    private String message;
    private ExceptionType type;
    private Exception rootException;

    private List<String> errors;

    public BaseException() {
    }

    public BaseException(Exception rootException, String message, Object... params) {
        super(message);
        this.message = formattedMessage(message, params);
        this.rootException = rootException;
    }


    public BaseException(String message, Object... params) {
        super(message);
        this.message = formattedMessage(message, params);
    }

    public BaseException(String message, List<String> params) {
        super(message);
        this.message = message;
        this.errors = params;
    }

    public BaseException(String message) {
        super(message);
        this.message = message;
    }

    public BaseException(ExceptionType type, String message) {
        super(message);
        this.type = type;
        this.message = message;
    }

    public BaseException(ExceptionType type, String message, Object... params) {
        super(message);
        this.type = type;
        this.message = formattedMessage(message, params);
    }

    public BaseException(Exception rootException, ExceptionType type, String message, Object... params) {
        super(message);
        this.type = type;
        this.message = formattedMessage(message, params);
        this.rootException = rootException;
    }

    public HttpStatus getCode() {
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }


    public String getJsonAsString(String hash) {
        JSONObject object = new JSONObject();
        object.put("message", this.message);
        object.put("type", this.type.getType());
        object.put("hash", hash);
        if (this.errors != null && this.errors.size() > 0) {
            object.put("fieldErrors", this.errors);
        }
        return JsonUtils.objectToString(object);
    }

    public ResponseEntity<Object> getJsonAsResponse() {
        return ResponseEntity.status(this.getCode().value())
                .contentType(MediaType.APPLICATION_JSON)
                .body(this.getJsonAsString(null));
    }

    public ResponseEntity<Object> getJsonAsResponse(String requestHash) {
        return ResponseEntity.status(this.getCode().value())
                .contentType(MediaType.APPLICATION_JSON)
                .body(this.getJsonAsString(requestHash));
    }

    private String formattedMessage(String msg, Object[] params) {
        for (Object param : params) {
            msg = msg.replaceFirst("\\{\\}", param != null ? param.toString() : "null");
        }
        return msg;
    }

    public String getMessage() {
        if(rootException == null) {
            return this.message;
        }
        return this.message + " : root exception = " + rootException.getMessage();
    }
}
