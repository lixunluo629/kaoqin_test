package io.netty.util;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/util/Mapping.class */
public interface Mapping<IN, OUT> {
    OUT map(IN in);
}
