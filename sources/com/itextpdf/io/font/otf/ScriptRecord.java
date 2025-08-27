package com.itextpdf.io.font.otf;

import java.io.Serializable;

/* loaded from: io-7.1.10.jar:com/itextpdf/io/font/otf/ScriptRecord.class */
public class ScriptRecord implements Serializable {
    private static final long serialVersionUID = 1670929244968728679L;
    public String tag;
    public LanguageRecord defaultLanguage;
    public LanguageRecord[] languages;
}
