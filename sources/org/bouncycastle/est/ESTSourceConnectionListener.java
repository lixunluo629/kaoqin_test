package org.bouncycastle.est;

import java.io.IOException;

/* loaded from: bcpkix-jdk15on-1.64.jar:org/bouncycastle/est/ESTSourceConnectionListener.class */
public interface ESTSourceConnectionListener<T, I> {
    ESTRequest onConnection(Source<T> source, ESTRequest eSTRequest) throws IOException;
}
