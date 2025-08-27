package org.bouncycastle.util;

/* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/util/Memoable.class */
public interface Memoable {
    Memoable copy();

    void reset(Memoable memoable);
}
