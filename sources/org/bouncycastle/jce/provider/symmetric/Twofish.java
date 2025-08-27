package org.bouncycastle.jce.provider.symmetric;

import java.util.HashMap;
import org.bouncycastle.crypto.CipherKeyGenerator;
import org.bouncycastle.crypto.engines.TwofishEngine;
import org.bouncycastle.jce.provider.JCEBlockCipher;
import org.bouncycastle.jce.provider.JCEKeyGenerator;
import org.bouncycastle.jce.provider.JDKAlgorithmParameters;

/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/jce/provider/symmetric/Twofish.class */
public final class Twofish {

    /* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/jce/provider/symmetric/Twofish$AlgParams.class */
    public static class AlgParams extends JDKAlgorithmParameters.IVAlgorithmParameters {
        @Override // org.bouncycastle.jce.provider.JDKAlgorithmParameters.IVAlgorithmParameters, java.security.AlgorithmParametersSpi
        protected String engineToString() {
            return "Twofish IV";
        }
    }

    /* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/jce/provider/symmetric/Twofish$ECB.class */
    public static class ECB extends JCEBlockCipher {
        public ECB() {
            super(new TwofishEngine());
        }
    }

    /* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/jce/provider/symmetric/Twofish$KeyGen.class */
    public static class KeyGen extends JCEKeyGenerator {
        public KeyGen() {
            super("Twofish", 256, new CipherKeyGenerator());
        }
    }

    /* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/jce/provider/symmetric/Twofish$Mappings.class */
    public static class Mappings extends HashMap {
        public Mappings() {
            put("Cipher.Twofish", "org.bouncycastle.jce.provider.symmetric.Twofish$ECB");
            put("KeyGenerator.Twofish", "org.bouncycastle.jce.provider.symmetric.Twofish$KeyGen");
            put("AlgorithmParameters.Twofish", "org.bouncycastle.jce.provider.symmetric.Twofish$AlgParams");
        }
    }

    private Twofish() {
    }
}
