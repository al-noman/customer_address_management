package com.zertificon.address.management.zertificon_address_management.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.Date;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@JsonInclude(NON_NULL)
@JsonPropertyOrder({"date", "paramName", "message", "details"})
public class ExceptionResponse {
    private Date date;
    private String message;
    private String details;
    private String paramName;

    public ExceptionResponse() {
    }

    public ExceptionResponse(Date date, String message, String details) {
        this(date, null, message, details);
    }

    public ExceptionResponse(Date date, String paramName, String message, String details) {
        this.date = date;
        this.paramName = paramName;
        this.message = message;
        this.details = details;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }
}
