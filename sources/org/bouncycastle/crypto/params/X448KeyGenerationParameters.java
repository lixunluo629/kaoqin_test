package org.bouncycastle.crypto.params;

import java.security.SecureRandom;
import org.apache.poi.ddf.EscherProperties;
import org.bouncycastle.crypto.KeyGenerationParameters;

/* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/crypto/params/X448KeyGenerationParameters.class */
public class X448KeyGenerationParameters extends KeyGenerationParameters {
    public X448KeyGenerationParameters(SecureRandom secureRandom) {
        super(secureRandom, EscherProperties.LINESTYLE__COLOR);
    }
}
