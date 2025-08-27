package com.adobe.xmp.impl;

import com.adobe.xmp.XMPException;

/* loaded from: xmpcore-5.1.3.jar:com/adobe/xmp/impl/ParseState.class */
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

    public char ch(int i) {
        if (i < this.str.length()) {
            return this.str.charAt(i);
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

    public int gatherInt(String str, int i) throws XMPException {
        int i2 = 0;
        boolean z = false;
        char cCh = ch(this.pos);
        while (true) {
            char c = cCh;
            if ('0' > c || c > '9') {
                break;
            }
            i2 = (i2 * 10) + (c - '0');
            z = true;
            this.pos++;
            cCh = ch(this.pos);
        }
        if (!z) {
            throw new XMPException(str, 5);
        }
        if (i2 > i) {
            return i;
        }
        if (i2 < 0) {
            return 0;
        }
        return i2;
    }
}
