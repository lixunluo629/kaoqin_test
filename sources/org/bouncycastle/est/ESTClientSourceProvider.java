package org.bouncycastle.est;

import java.io.IOException;

/* loaded from: bcpkix-jdk15on-1.64.jar:org/bouncycastle/est/ESTClientSourceProvider.class */
public interface ESTClientSourceProvider {
    Source makeSource(String str, int i) throws IOException;
}
