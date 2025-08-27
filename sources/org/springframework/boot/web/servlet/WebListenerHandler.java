package org.springframework.boot.web.servlet;

import java.util.Map;
import javax.servlet.annotation.WebListener;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ScannedGenericBeanDefinition;

/* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/web/servlet/WebListenerHandler.class */
class WebListenerHandler extends ServletComponentHandler {
    WebListenerHandler() {
        super(WebListener.class);
    }

    @Override // org.springframework.boot.web.servlet.ServletComponentHandler
    protected void doHandle(Map<String, Object> attributes, ScannedGenericBeanDefinition beanDefinition, BeanDefinitionRegistry registry) throws BeanDefinitionStoreException {
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition((Class<?>) ServletListenerRegistrationBean.class);
        builder.addPropertyValue("listener", beanDefinition);
        registry.registerBeanDefinition(beanDefinition.getBeanClassName(), builder.getBeanDefinition());
    }
}
