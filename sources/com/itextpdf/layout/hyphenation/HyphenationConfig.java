package com.itextpdf.layout.hyphenation;

/* loaded from: layout-7.1.10.jar:com/itextpdf/layout/hyphenation/HyphenationConfig.class */
public class HyphenationConfig {
    protected Hyphenator hyphenator;
    protected char hyphenSymbol = '-';

    public HyphenationConfig(int leftMin, int rightMin) {
        this.hyphenator = new Hyphenator(null, null, leftMin, rightMin);
    }

    public HyphenationConfig(Hyphenator hyphenator) {
        this.hyphenator = hyphenator;
    }

    public HyphenationConfig(String lang, String country, int leftMin, int rightMin) {
        this.hyphenator = new Hyphenator(lang, country, leftMin, rightMin);
    }

    public Hyphenation hyphenate(String word) {
        if (this.hyphenator != null) {
            return this.hyphenator.hyphenate(word);
        }
        return null;
    }

    public char getHyphenSymbol() {
        return this.hyphenSymbol;
    }

    public void setHyphenSymbol(char hyphenSymbol) {
        this.hyphenSymbol = hyphenSymbol;
    }
}
