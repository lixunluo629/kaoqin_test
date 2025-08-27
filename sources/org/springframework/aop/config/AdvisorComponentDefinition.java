package org.springframework.aop.config;

import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanReference;
import org.springframework.beans.factory.parsing.AbstractComponentDefinition;
import org.springframework.util.Assert;

/* loaded from: spring-aop-4.3.25.RELEASE.jar:org/springframework/aop/config/AdvisorComponentDefinition.class */
public class AdvisorComponentDefinition extends AbstractComponentDefinition {
    private final String advisorBeanName;
    private final BeanDefinition advisorDefinition;
    private String description;
    private BeanReference[] beanReferences;
    private BeanDefinition[] beanDefinitions;

    public AdvisorComponentDefinition(String advisorBeanName, BeanDefinition advisorDefinition) {
        this(advisorBeanName, advisorDefinition, null);
    }

    public AdvisorComponentDefinition(String advisorBeanName, BeanDefinition advisorDefinition, BeanDefinition pointcutDefinition) {
        Assert.notNull(advisorBeanName, "'advisorBeanName' must not be null");
        Assert.notNull(advisorDefinition, "'advisorDefinition' must not be null");
        this.advisorBeanName = advisorBeanName;
        this.advisorDefinition = advisorDefinition;
        unwrapDefinitions(advisorDefinition, pointcutDefinition);
    }

    private void unwrapDefinitions(BeanDefinition advisorDefinition, BeanDefinition pointcutDefinition) {
        MutablePropertyValues pvs = advisorDefinition.getPropertyValues();
        BeanReference adviceReference = (BeanReference) pvs.getPropertyValue("adviceBeanName").getValue();
        if (pointcutDefinition != null) {
            this.beanReferences = new BeanReference[]{adviceReference};
            this.beanDefinitions = new BeanDefinition[]{advisorDefinition, pointcutDefinition};
            this.description = buildDescription(adviceReference, pointcutDefinition);
        } else {
            BeanReference pointcutReference = (BeanReference) pvs.getPropertyValue("pointcut").getValue();
            this.beanReferences = new BeanReference[]{adviceReference, pointcutReference};
            this.beanDefinitions = new BeanDefinition[]{advisorDefinition};
            this.description = buildDescription(adviceReference, pointcutReference);
        }
    }

    private String buildDescription(BeanReference adviceReference, BeanDefinition pointcutDefinition) {
        return "Advisor <advice(ref)='" + adviceReference.getBeanName() + "', pointcut(expression)=[" + pointcutDefinition.getPropertyValues().getPropertyValue("expression").getValue() + "]>";
    }

    private String buildDescription(BeanReference adviceReference, BeanReference pointcutReference) {
        return "Advisor <advice(ref)='" + adviceReference.getBeanName() + "', pointcut(ref)='" + pointcutReference.getBeanName() + "'>";
    }

    @Override // org.springframework.beans.factory.parsing.ComponentDefinition
    public String getName() {
        return this.advisorBeanName;
    }

    @Override // org.springframework.beans.factory.parsing.AbstractComponentDefinition, org.springframework.beans.factory.parsing.ComponentDefinition
    public String getDescription() {
        return this.description;
    }

    @Override // org.springframework.beans.factory.parsing.AbstractComponentDefinition, org.springframework.beans.factory.parsing.ComponentDefinition
    public BeanDefinition[] getBeanDefinitions() {
        return this.beanDefinitions;
    }

    @Override // org.springframework.beans.factory.parsing.AbstractComponentDefinition, org.springframework.beans.factory.parsing.ComponentDefinition
    public BeanReference[] getBeanReferences() {
        return this.beanReferences;
    }

    @Override // org.springframework.beans.BeanMetadataElement
    public Object getSource() {
        return this.advisorDefinition.getSource();
    }
}
