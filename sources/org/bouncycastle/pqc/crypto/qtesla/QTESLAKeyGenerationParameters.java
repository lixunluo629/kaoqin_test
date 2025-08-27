package org.bouncycastle.pqc.crypto.qtesla;

import java.security.SecureRandom;
import org.bouncycastle.crypto.KeyGenerationParameters;

/* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/pqc/crypto/qtesla/QTESLAKeyGenerationParameters.class */
public class QTESLAKeyGenerationParameters extends KeyGenerationParameters {
    private final int securityCategory;

    public QTESLAKeyGenerationParameters(int i, SecureRandom secureRandom) {
        super(secureRandom, -1);
        QTESLASecurityCategory.getPrivateSize(i);
        this.securityCategory = i;
    }

    public int getSecurityCategory() {
        return this.securityCategory;
    }
}
