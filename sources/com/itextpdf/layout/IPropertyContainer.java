package com.itextpdf.layout;

/* loaded from: layout-7.1.10.jar:com/itextpdf/layout/IPropertyContainer.class */
public interface IPropertyContainer {
    boolean hasProperty(int i);

    boolean hasOwnProperty(int i);

    <T1> T1 getProperty(int i);

    <T1> T1 getOwnProperty(int i);

    <T1> T1 getDefaultProperty(int i);

    void setProperty(int i, Object obj);

    void deleteOwnProperty(int i);
}
