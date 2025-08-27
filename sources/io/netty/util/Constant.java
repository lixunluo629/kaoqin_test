package io.netty.util;

import io.netty.util.Constant;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/util/Constant.class */
public interface Constant<T extends Constant<T>> extends Comparable<T> {
    int id();

    String name();
}
