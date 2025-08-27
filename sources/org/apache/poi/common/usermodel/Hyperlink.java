package org.apache.poi.common.usermodel;

/* loaded from: poi-3.17.jar:org/apache/poi/common/usermodel/Hyperlink.class */
public interface Hyperlink {
    String getAddress();

    void setAddress(String str);

    String getLabel();

    void setLabel(String str);

    int getType();

    HyperlinkType getTypeEnum();
}
