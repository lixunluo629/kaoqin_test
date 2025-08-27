package org.bouncycastle.crypto.tls;

import java.io.EOFException;

/* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/crypto/tls/TlsNoCloseNotifyException.class */
public class TlsNoCloseNotifyException extends EOFException {
    public TlsNoCloseNotifyException() {
        super("No close_notify alert received before connection closed");
    }
}
