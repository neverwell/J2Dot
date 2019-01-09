package com.nemo.log.consumer.mysql;

public enum FieldType {
    TINYINT,
    SMALLINT,
    INT,
    BIGINT,
    VARCHAR,
    DOUBLE,
    TEXT,
    LONGTEXT,
    BIT,
    BLOB;

    private FieldType() {
    }
}
