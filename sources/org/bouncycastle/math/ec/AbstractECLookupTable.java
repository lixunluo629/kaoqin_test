package org.bouncycastle.math.ec;

/* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/math/ec/AbstractECLookupTable.class */
public abstract class AbstractECLookupTable implements ECLookupTable {
    @Override // org.bouncycastle.math.ec.ECLookupTable
    public ECPoint lookupVar(int i) {
        return lookup(i);
    }
}
