package net.sf.cglib.transform.impl;

/* loaded from: cglib-3.1.jar:net/sf/cglib/transform/impl/FieldProvider.class */
public interface FieldProvider {
    String[] getFieldNames();

    Class[] getFieldTypes();

    void setField(int i, Object obj);

    Object getField(int i);

    void setField(String str, Object obj);

    Object getField(String str);
}
