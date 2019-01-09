package com.nemo.log.consumer.mysql.annotation;

import com.nemo.log.consumer.mysql.FieldType;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Inherited
public @interface Column {
    FieldType fieldType();

    String commit() default "";

    boolean allowNull() default false;

    boolean autoIncrement() default false;

    int size() default 0;

    String colName() default "";

    String index() default "";
}
