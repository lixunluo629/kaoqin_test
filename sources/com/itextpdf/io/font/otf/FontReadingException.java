package com.itextpdf.io.font.otf;

import java.io.IOException;

/* loaded from: io-7.1.10.jar:com/itextpdf/io/font/otf/FontReadingException.class */
public class FontReadingException extends IOException {
    private static final long serialVersionUID = -7058811479423740467L;

    public FontReadingException(String message) {
        super(message);
    }

    public FontReadingException(String message, Exception e) {
        super(message, e);
    }
}
