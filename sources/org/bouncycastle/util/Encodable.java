package org.bouncycastle.util;

import java.io.IOException;

/* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/util/Encodable.class */
public interface Encodable {
    byte[] getEncoded() throws IOException;
}
