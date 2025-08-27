package io.netty.util;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/util/AttributeMap.class */
public interface AttributeMap {
    <T> Attribute<T> attr(AttributeKey<T> attributeKey);

    <T> boolean hasAttr(AttributeKey<T> attributeKey);
}
