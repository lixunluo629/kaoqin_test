package org.springframework.data.redis.config;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/config/RedisNamespaceHandler.class */
class RedisNamespaceHandler extends NamespaceHandlerSupport {
    RedisNamespaceHandler() {
    }

    @Override // org.springframework.beans.factory.xml.NamespaceHandler
    public void init() {
        registerBeanDefinitionParser("listener-container", new RedisListenerContainerParser());
        registerBeanDefinitionParser("collection", new RedisCollectionParser());
    }
}
