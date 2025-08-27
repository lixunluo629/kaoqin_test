package org.bouncycastle.math.ec.endo;

import java.math.BigInteger;

/* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/math/ec/endo/GLVEndomorphism.class */
public interface GLVEndomorphism extends ECEndomorphism {
    BigInteger[] decomposeScalar(BigInteger bigInteger);
}
