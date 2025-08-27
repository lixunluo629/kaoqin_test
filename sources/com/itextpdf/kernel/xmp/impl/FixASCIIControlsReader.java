package com.itextpdf.kernel.xmp.impl;

import java.io.IOException;
import java.io.PushbackReader;
import java.io.Reader;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/xmp/impl/FixASCIIControlsReader.class */
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

    public FixASCIIControlsReader(Reader input) {
        super(input, 8);
        this.state = 0;
        this.control = 0;
        this.digits = 0;
    }

    @Override // java.io.PushbackReader, java.io.FilterReader, java.io.Reader
    public int read(char[] cbuf, int off, int len) throws IOException {
        int readAhead = 0;
        int read = 0;
        int pos = off;
        char[] readAheadBuffer = new char[8];
        boolean available = true;
        while (available && read < len) {
            available = super.read(readAheadBuffer, readAhead, 1) == 1;
            if (available) {
                char c = processChar(readAheadBuffer[readAhead]);
                if (this.state == 0) {
                    if (Utils.isControlChar(c)) {
                        c = ' ';
                    }
                    int i = pos;
                    pos++;
                    cbuf[i] = c;
                    readAhead = 0;
                    read++;
                } else if (this.state == 5) {
                    unread(readAheadBuffer, 0, readAhead + 1);
                    readAhead = 0;
                } else {
                    readAhead++;
                }
            } else if (readAhead > 0) {
                unread(readAheadBuffer, 0, readAhead);
                this.state = 5;
                readAhead = 0;
                available = true;
            }
        }
        if (read > 0 || available) {
            return read;
        }
        return -1;
    }

    private char processChar(char ch2) {
        switch (this.state) {
            case 0:
                if (ch2 == '&') {
                    this.state = 1;
                }
                break;
            case 1:
                if (ch2 == '#') {
                    this.state = 2;
                } else {
                    this.state = 5;
                }
                break;
            case 2:
                if (ch2 == 'x') {
                    this.control = 0;
                    this.digits = 0;
                    this.state = 3;
                } else if ('0' <= ch2 && ch2 <= '9') {
                    this.control = Character.digit(ch2, 10);
                    this.digits = 1;
                    this.state = 4;
                } else {
                    this.state = 5;
                }
                break;
            case 3:
                if (('0' <= ch2 && ch2 <= '9') || (('a' <= ch2 && ch2 <= 'f') || ('A' <= ch2 && ch2 <= 'F'))) {
                    this.control = (this.control * 16) + Character.digit(ch2, 16);
                    this.digits++;
                    if (this.digits <= 4) {
                        this.state = 3;
                    } else {
                        this.state = 5;
                    }
                } else if (ch2 == ';' && Utils.isControlChar((char) this.control)) {
                    this.state = 0;
                    break;
                } else {
                    this.state = 5;
                }
                break;
            case 4:
                if ('0' <= ch2 && ch2 <= '9') {
                    this.control = (this.control * 10) + Character.digit(ch2, 10);
                    this.digits++;
                    if (this.digits <= 5) {
                        this.state = 4;
                    } else {
                        this.state = 5;
                    }
                } else if (ch2 == ';' && Utils.isControlChar((char) this.control)) {
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
        return ch2;
    }
}
