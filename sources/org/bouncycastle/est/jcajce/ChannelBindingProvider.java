package org.bouncycastle.est.jcajce;

import java.net.Socket;

/* loaded from: bcpkix-jdk15on-1.64.jar:org/bouncycastle/est/jcajce/ChannelBindingProvider.class */
public interface ChannelBindingProvider {
    boolean canAccessChannelBinding(Socket socket);

    byte[] getChannelBinding(Socket socket, String str);
}
