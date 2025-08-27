package org.bouncycastle.util;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/util/Selector.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/util/Selector.class */
public interface Selector extends Cloneable {
    boolean match(Object obj);

    Object clone();
}
