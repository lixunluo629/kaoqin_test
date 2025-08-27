package org.bouncycastle.cert.dane;

/* loaded from: bcpkix-jdk15on-1.64.jar:org/bouncycastle/cert/dane/DANEEntryStoreBuilder.class */
public class DANEEntryStoreBuilder {
    private final DANEEntryFetcherFactory daneEntryFetcher;

    public DANEEntryStoreBuilder(DANEEntryFetcherFactory dANEEntryFetcherFactory) {
        this.daneEntryFetcher = dANEEntryFetcherFactory;
    }

    public DANEEntryStore build(String str) throws DANEException {
        return new DANEEntryStore(this.daneEntryFetcher.build(str).getEntries());
    }
}
