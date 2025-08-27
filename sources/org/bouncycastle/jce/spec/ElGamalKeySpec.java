package org.bouncycastle.jce.spec;

import java.security.spec.KeySpec;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/jce/spec/ElGamalKeySpec.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/jce/spec/ElGamalKeySpec.class */
public class ElGamalKeySpec implements KeySpec {
    private ElGamalParameterSpec spec;

    public ElGamalKeySpec(ElGamalParameterSpec elGamalParameterSpec) {
        this.spec = elGamalParameterSpec;
    }

    public ElGamalParameterSpec getParams() {
        return this.spec;
    }
}
