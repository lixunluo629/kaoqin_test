package com.itextpdf.kernel;

import java.io.Serializable;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/VersionInfo.class */
public class VersionInfo implements Serializable {
    private static final long serialVersionUID = 1514128839876564529L;
    private final String productName;
    private final String releaseNumber;
    private final String producerLine;
    private final String licenseKey;

    public VersionInfo(String productName, String releaseNumber, String producerLine, String licenseKey) {
        this.productName = productName;
        this.releaseNumber = releaseNumber;
        this.producerLine = producerLine;
        this.licenseKey = licenseKey;
    }

    public String getProduct() {
        return this.productName;
    }

    public String getRelease() {
        return this.releaseNumber;
    }

    public String getVersion() {
        return this.producerLine;
    }

    public String getKey() {
        return this.licenseKey;
    }
}
