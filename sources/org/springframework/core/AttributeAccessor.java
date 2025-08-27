package org.springframework.core;

/* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/core/AttributeAccessor.class */
public interface AttributeAccessor {
    void setAttribute(String str, Object obj);

    Object getAttribute(String str);

    Object removeAttribute(String str);

    boolean hasAttribute(String str);

    String[] attributeNames();
}
