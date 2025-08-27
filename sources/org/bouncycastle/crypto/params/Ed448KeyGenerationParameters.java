package org.bouncycastle.crypto.params;

import java.security.SecureRandom;
import org.apache.poi.ddf.EscherProperties;
import org.bouncycastle.crypto.KeyGenerationParameters;

/* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/crypto/params/Ed448KeyGenerationParameters.class */
public class Ed448KeyGenerationParameters extends KeyGenerationParameters {
    public Ed448KeyGenerationParameters(SecureRandom secureRandom) {
        super(secureRandom, EscherProperties.LINESTYLE__COLOR);
    }
}
