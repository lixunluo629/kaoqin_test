package com.itextpdf.io.font;

import java.io.Serializable;

/* loaded from: io-7.1.10.jar:com/itextpdf/io/font/FontIdentification.class */
public class FontIdentification implements Serializable {
    private static final long serialVersionUID = -6017656004487895604L;
    private String ttfVersion;
    private String ttfUniqueId;
    private Integer type1Xuid;
    private String panose;

    public String getTtfVersion() {
        return this.ttfVersion;
    }

    public String getTtfUniqueId() {
        return this.ttfUniqueId;
    }

    public Integer getType1Xuid() {
        return this.type1Xuid;
    }

    public String getPanose() {
        return this.panose;
    }

    protected void setTtfVersion(String ttfVersion) {
        this.ttfVersion = ttfVersion;
    }

    protected void setTtfUniqueId(String ttfUniqueId) {
        this.ttfUniqueId = ttfUniqueId;
    }

    protected void setType1Xuid(Integer type1Xuid) {
        this.type1Xuid = type1Xuid;
    }

    protected void setPanose(byte[] panose) {
        this.panose = new String(panose);
    }

    protected void setPanose(String panose) {
        this.panose = panose;
    }
}
