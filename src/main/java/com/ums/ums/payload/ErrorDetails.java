package com.ums.ums.payload;

import java.util.Date;

public class ErrorDetails {
    private String message;
    private Date date;

    private String description;
    //construct
    public ErrorDetails(String message, Date date, String description) {
        this.message = message;
        this.date = date;
        this.description = description;
    }
    //getters
    public String getMessage() {
        return message;
    }
    public Date getDate() {
        return date;
    }
    public String getDescription() {
        return description;
    }
}
