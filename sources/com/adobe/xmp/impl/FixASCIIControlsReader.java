package com.adobe.xmp.impl;

import java.io.IOException;
import java.io.PushbackReader;
import java.io.Reader;

/* loaded from: xmpcore-5.1.3.jar:com/adobe/xmp/impl/FixASCIIControlsReader.class */
public class FixASCIIControlsReader extends PushbackReader {
    private static final int STATE_START = 0;
    private static final int STATE_AMP = 1;
    private static final int STATE_HASH = 2;
    private static final int STATE_HEX = 3;
    private static final int STATE_DIG1 = 4;
    private static final int STATE_ERROR = 5;
    private static final int BUFFER_SIZE = 8;
    private int state;
    private int control;
    private int digits;

    public FixASCIIControlsReader(Reader reader) {
        super(reader, 8);
        this.state = 0;
        this.control = 0;
        this.digits = 0;
    }

    @Override // java.io.PushbackReader, java.io.FilterReader, java.io.Reader
    public int read(char[] cArr, int i, int i2) throws IOException {
        int i3 = 0;
        int i4 = 0;
        int i5 = i;
        char[] cArr2 = new char[8];
        boolean z = true;
        while (z && i4 < i2) {
            z = super.read(cArr2, i3, 1) == 1;
            if (z) {
                char cProcessChar = processChar(cArr2[i3]);
                if (this.state == 0) {
                    if (Utils.isControlChar(cProcessChar)) {
                        cProcessChar = ' ';
                    }
                    int i6 = i5;
                    i5++;
                    cArr[i6] = cProcessChar;
                    i3 = 0;
                    i4++;
                } else if (this.state == 5) {
                    unread(cArr2, 0, i3 + 1);
                    i3 = 0;
                } else {
                    i3++;
                }
            } else if (i3 > 0) {
                unread(cArr2, 0, i3);
                this.state = 5;
                i3 = 0;
                z = true;
            }
        }
        if (i4 > 0 || z) {
            return i4;
        }
        return -1;
    }

    private char processChar(char c) {
        switch (this.state) {
            case 0:
                if (c == '&') {
                    this.state = 1;
                }
                break;
            case 1:
                if (c == '#') {
                    this.state = 2;
                } else {
                    this.state = 5;
                }
                break;
            case 2:
                if (c == 'x') {
                    this.control = 0;
                    this.digits = 0;
                    this.state = 3;
                } else if ('0' > c || c > '9') {
                    this.state = 5;
                } else {
                    this.control = Character.digit(c, 10);
                    this.digits = 1;
                    this.state = 4;
                }
                break;
            case 3:
                if (('0' <= c && c <= '9') || (('a' <= c && c <= 'f') || ('A' <= c && c <= 'F'))) {
                    this.control = (this.control * 16) + Character.digit(c, 16);
                    this.digits++;
                    if (this.digits <= 4) {
                        this.state = 3;
                    } else {
                        this.state = 5;
                    }
                } else if (c == ';' && Utils.isControlChar((char) this.control)) {
                    this.state = 0;
                    break;
                } else {
                    this.state = 5;
                }
                break;
            case 4:
                if ('0' <= c && c <= '9') {
                    this.control = (this.control * 10) + Character.digit(c, 10);
                    this.digits++;
                    if (this.digits <= 5) {
                        this.state = 4;
                    } else {
                        this.state = 5;
                    }
                } else if (c == ';' && Utils.isControlChar((char) this.control)) {
                    this.state = 0;
                    break;
                } else {
                    this.state = 5;
                }
                break;
            case 5:
                this.state = 0;
                break;
        }
        return c;
    }
}
