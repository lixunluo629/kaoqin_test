package io.netty.handler.codec.serialization;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/serialization/ClassResolver.class */
public interface ClassResolver {
    Class<?> resolve(String str) throws ClassNotFoundException;
}
