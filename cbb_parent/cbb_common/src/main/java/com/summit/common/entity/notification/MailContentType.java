package com.summit.common.entity.notification;

public enum MailContentType {

    TEXT("text"),
    HTML("html"),
    TEMPLATE("template");


    private String value;

    MailContentType(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
