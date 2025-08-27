package com.itextpdf.kernel.xmp.impl;

import com.itextpdf.kernel.xmp.XMPException;

/* compiled from: ISO8601Converter.java */
/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/xmp/impl/ParseState.class */
class ParseState {
    private String str;
    private int pos = 0;

    public ParseState(String str) {
        this.str = str;
    }

    public int length() {
        return this.str.length();
    }

    public boolean hasNext() {
        return this.pos < this.str.length();
    }

    public char ch(int index) {
        if (index < this.str.length()) {
            return this.str.charAt(index);
        }
        return (char) 0;
    }

    public char ch() {
        if (this.pos < this.str.length()) {
            return this.str.charAt(this.pos);
        }
        return (char) 0;
    }

    public void skip() {
        this.pos++;
    }

    public int pos() {
        return this.pos;
    }

    public int gatherInt(String errorMsg, int maxValue) throws XMPException {
        int value = 0;
        boolean success = false;
        char cCh = ch(this.pos);
        while (true) {
            char ch2 = cCh;
            if ('0' > ch2 || ch2 > '9') {
                break;
            }
            value = (value * 10) + (ch2 - '0');
            success = true;
            this.pos++;
            cCh = ch(this.pos);
        }
        if (success) {
            if (value > maxValue) {
                return maxValue;
            }
            if (value < 0) {
                return 0;
            }
            return value;
        }
        throw new XMPException(errorMsg, 5);
    }
}
