package org.springframework.cache.interceptor;

import java.io.Serializable;
import java.util.Arrays;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/cache/interceptor/SimpleKey.class */
public class SimpleKey implements Serializable {
    public static final SimpleKey EMPTY = new SimpleKey(new Object[0]);
    private final Object[] params;
    private final int hashCode;

    public SimpleKey(Object... elements) {
        Assert.notNull(elements, "Elements must not be null");
        this.params = new Object[elements.length];
        System.arraycopy(elements, 0, this.params, 0, elements.length);
        this.hashCode = Arrays.deepHashCode(this.params);
    }

    public boolean equals(Object obj) {
        return this == obj || ((obj instanceof SimpleKey) && Arrays.deepEquals(this.params, ((SimpleKey) obj).params));
    }

    public final int hashCode() {
        return this.hashCode;
    }

    public String toString() {
        return getClass().getSimpleName() + " [" + StringUtils.arrayToCommaDelimitedString(this.params) + "]";
    }
}
