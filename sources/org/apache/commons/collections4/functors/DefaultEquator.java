package org.apache.commons.collections4.functors;

import java.io.Serializable;
import org.apache.commons.collections4.Equator;

/* loaded from: commons-collections4-4.1.jar:org/apache/commons/collections4/functors/DefaultEquator.class */
public class DefaultEquator<T> implements Equator<T>, Serializable {
    private static final long serialVersionUID = 825802648423525485L;
    public static final DefaultEquator INSTANCE = new DefaultEquator();
    public static final int HASHCODE_NULL = -1;

    public static <T> DefaultEquator<T> defaultEquator() {
        return INSTANCE;
    }

    private DefaultEquator() {
    }

    @Override // org.apache.commons.collections4.Equator
    public boolean equate(T o1, T o2) {
        return o1 == o2 || (o1 != null && o1.equals(o2));
    }

    @Override // org.apache.commons.collections4.Equator
    public int hash(T o) {
        if (o == null) {
            return -1;
        }
        return o.hashCode();
    }

    private Object readResolve() {
        return INSTANCE;
    }
}
