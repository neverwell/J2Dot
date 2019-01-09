package com.nemo.log.consumer.mysql.annotation;

import com.nemo.log.consumer.mysql.TableCharset;
import com.nemo.log.consumer.mysql.TableCycle;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Target({ElementType.TYPE})
public @interface Table {
    String primaryKey() default "id";

    TableCycle cycle() default TableCycle.SINGLE;

    String tableName() default "";

    TableCharset charset() default TableCharset.UTF8MB4;
}
