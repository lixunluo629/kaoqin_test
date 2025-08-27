package org.springframework.beans.factory.config;

import org.springframework.beans.factory.ObjectFactory;

/* loaded from: spring-beans-4.3.25.RELEASE.jar:org/springframework/beans/factory/config/Scope.class */
public interface Scope {
    Object get(String str, ObjectFactory<?> objectFactory);

    Object remove(String str);

    void registerDestructionCallback(String str, Runnable runnable);

    Object resolveContextualObject(String str);

    String getConversationId();
}
