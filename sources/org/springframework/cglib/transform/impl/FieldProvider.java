package org.springframework.cglib.transform.impl;

/* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/cglib/transform/impl/FieldProvider.class */
public interface FieldProvider {
    String[] getFieldNames();

    Class[] getFieldTypes();

    void setField(int i, Object obj);

    Object getField(int i);

    void setField(String str, Object obj);

    Object getField(String str);
}
