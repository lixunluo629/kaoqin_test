package org.bouncycastle.util;

/* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/util/Objects.class */
public class Objects {
    public static boolean areEqual(Object obj, Object obj2) {
        return obj == obj2 || !(null == obj || null == obj2 || !obj.equals(obj2));
    }

    public static int hashCode(Object obj) {
        if (null == obj) {
            return 0;
        }
        return obj.hashCode();
    }
}
