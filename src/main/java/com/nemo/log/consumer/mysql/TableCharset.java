package com.nemo.log.consumer.mysql;

public enum TableCharset {
    UTF8("utf8"),
    UTF8MB4("utf8mb4");

    private String value;

    TableCharset(String value) {
        this.value = value;
    }

    public String value() {
        return this.value;
    }
}
