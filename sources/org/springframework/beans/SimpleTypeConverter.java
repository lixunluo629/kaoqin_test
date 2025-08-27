package org.springframework.beans;

/* loaded from: spring-beans-4.3.25.RELEASE.jar:org/springframework/beans/SimpleTypeConverter.class */
public class SimpleTypeConverter extends TypeConverterSupport {
    public SimpleTypeConverter() {
        this.typeConverterDelegate = new TypeConverterDelegate(this);
        registerDefaultEditors();
    }
}
