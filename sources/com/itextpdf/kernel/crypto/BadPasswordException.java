package com.itextpdf.kernel.crypto;

import com.itextpdf.kernel.PdfException;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/crypto/BadPasswordException.class */
public class BadPasswordException extends PdfException {
    private static final long serialVersionUID = -2278753672963132724L;
    public static final String PdfReaderNotOpenedWithOwnerPassword = "PdfReader is not opened with owner password";

    public BadPasswordException(String message, Throwable cause) {
        super(message, cause);
    }

    public BadPasswordException(String message) {
        super(message);
    }
}
