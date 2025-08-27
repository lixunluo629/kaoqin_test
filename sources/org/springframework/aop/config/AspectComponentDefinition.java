package org.springframework.aop.config;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanReference;
import org.springframework.beans.factory.parsing.CompositeComponentDefinition;

/* loaded from: spring-aop-4.3.25.RELEASE.jar:org/springframework/aop/config/AspectComponentDefinition.class */
public class AspectComponentDefinition extends CompositeComponentDefinition {
    private final BeanDefinition[] beanDefinitions;
    private final BeanReference[] beanReferences;

    public AspectComponentDefinition(String aspectName, BeanDefinition[] beanDefinitions, BeanReference[] beanReferences, Object source) {
        super(aspectName, source);
        this.beanDefinitions = beanDefinitions != null ? beanDefinitions : new BeanDefinition[0];
        this.beanReferences = beanReferences != null ? beanReferences : new BeanReference[0];
    }

    @Override // org.springframework.beans.factory.parsing.AbstractComponentDefinition, org.springframework.beans.factory.parsing.ComponentDefinition
    public BeanDefinition[] getBeanDefinitions() {
        return this.beanDefinitions;
    }

    @Override // org.springframework.beans.factory.parsing.AbstractComponentDefinition, org.springframework.beans.factory.parsing.ComponentDefinition
    public BeanReference[] getBeanReferences() {
        return this.beanReferences;
    }
}
