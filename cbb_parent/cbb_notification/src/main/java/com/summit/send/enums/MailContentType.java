package com.summit.send.enums;

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
