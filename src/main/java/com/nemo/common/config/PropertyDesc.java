package com.nemo.common.config;

import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Method;

@Getter
@Setter
/**
 * 类的一个属性的名字 类型 get和set方法
 * 简略版的PropertyDescriptor
 * @see java.beans.PropertyDescriptor
 */
public class PropertyDesc {
    public static final PropertyDesc NULL = new PropertyDesc();
    private String name;
    private Class<?> propertyType;
    private Method writeMethod;
    private Method readMethod;
}
