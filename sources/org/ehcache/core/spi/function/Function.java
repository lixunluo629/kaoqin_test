package org.ehcache.core.spi.function;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/core/spi/function/Function.class */
public interface Function<A, T> {
    T apply(A a);
}
