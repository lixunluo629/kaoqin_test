package org.ehcache.core.spi.function;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/core/spi/function/BiFunction.class */
public interface BiFunction<A, B, T> {
    T apply(A a, B b);
}
