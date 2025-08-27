package org.bouncycastle.cert.dane;

import java.util.List;

/* loaded from: bcpkix-jdk15on-1.64.jar:org/bouncycastle/cert/dane/DANEEntryFetcher.class */
public interface DANEEntryFetcher {
    List getEntries() throws DANEException;
}
