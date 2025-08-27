package org.apache.poi.ss.usermodel;

/* loaded from: poi-3.17.jar:org/apache/poi/ss/usermodel/PictureData.class */
public interface PictureData {
    byte[] getData();

    String suggestFileExtension();

    String getMimeType();

    int getPictureType();
}
