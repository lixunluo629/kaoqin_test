package com.itextpdf.io.font.otf;

import java.io.Serializable;

/* loaded from: io-7.1.10.jar:com/itextpdf/io/font/otf/LanguageRecord.class */
public class LanguageRecord implements Serializable {
    private static final long serialVersionUID = 5698484177818271465L;
    public String tag;
    public int featureRequired;
    public int[] features;
}
