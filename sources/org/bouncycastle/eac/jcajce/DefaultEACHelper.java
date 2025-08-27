package org.bouncycastle.eac.jcajce;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;

/* loaded from: bcpkix-jdk15on-1.64.jar:org/bouncycastle/eac/jcajce/DefaultEACHelper.class */
class DefaultEACHelper implements EACHelper {
    DefaultEACHelper() {
    }

    @Override // org.bouncycastle.eac.jcajce.EACHelper
    public KeyFactory createKeyFactory(String str) throws NoSuchAlgorithmException {
        return KeyFactory.getInstance(str);
    }
}
