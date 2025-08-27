package com.adobe.xmp.impl;

/* loaded from: xmpcore-5.1.3.jar:com/adobe/xmp/impl/QName.class */
public class QName {
    private String prefix;
    private String localName;

    public QName(String str) {
        int iIndexOf = str.indexOf(58);
        if (iIndexOf >= 0) {
            this.prefix = str.substring(0, iIndexOf);
            this.localName = str.substring(iIndexOf + 1);
        } else {
            this.prefix = "";
            this.localName = str;
        }
    }

    public QName(String str, String str2) {
        this.prefix = str;
        this.localName = str2;
    }

    public boolean hasPrefix() {
        return this.prefix != null && this.prefix.length() > 0;
    }

    public String getLocalName() {
        return this.localName;
    }

    public String getPrefix() {
        return this.prefix;
    }
}
