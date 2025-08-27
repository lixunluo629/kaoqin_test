package org.bouncycastle.math.ec;

/* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/math/ec/ECLookupTable.class */
public interface ECLookupTable {
    int getSize();

    ECPoint lookup(int i);

    ECPoint lookupVar(int i);
}
