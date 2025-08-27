package com.itextpdf.layout.hyphenation;

import java.io.Serializable;

/* loaded from: layout-7.1.10.jar:com/itextpdf/layout/hyphenation/Hyphen.class */
public class Hyphen implements Serializable {
    private static final long serialVersionUID = 8989909741110279085L;
    public String preBreak;
    public String noBreak;
    public String postBreak;

    Hyphen(String pre, String no, String post) {
        this.preBreak = pre;
        this.noBreak = no;
        this.postBreak = post;
    }

    Hyphen(String pre) {
        this.preBreak = pre;
        this.noBreak = null;
        this.postBreak = null;
    }

    public String toString() {
        if (this.noBreak == null && this.postBreak == null && this.preBreak != null && this.preBreak.equals("-")) {
            return "-";
        }
        StringBuffer res = new StringBuffer("{");
        res.append(this.preBreak);
        res.append("}{");
        res.append(this.postBreak);
        res.append("}{");
        res.append(this.noBreak);
        res.append('}');
        return res.toString();
    }
}
