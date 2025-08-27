package org.bouncycastle.operator;

import java.io.OutputStream;

/* loaded from: bcpkix-jdk15on-1.64.jar:org/bouncycastle/operator/AADProcessor.class */
public interface AADProcessor {
    OutputStream getAADStream();

    byte[] getMAC();
}
