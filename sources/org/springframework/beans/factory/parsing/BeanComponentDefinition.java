package org.springframework.beans.factory.parsing;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.BeanReference;

/* loaded from: spring-beans-4.3.25.RELEASE.jar:org/springframework/beans/factory/parsing/BeanComponentDefinition.class */
public class BeanComponentDefinition extends BeanDefinitionHolder implements ComponentDefinition {
    private BeanDefinition[] innerBeanDefinitions;
    private BeanReference[] beanReferences;

    public BeanComponentDefinition(BeanDefinition beanDefinition, String beanName) {
        super(beanDefinition, beanName);
        findInnerBeanDefinitionsAndBeanReferences(beanDefinition);
    }

    public BeanComponentDefinition(BeanDefinition beanDefinition, String beanName, String[] aliases) {
        super(beanDefinition, beanName, aliases);
        findInnerBeanDefinitionsAndBeanReferences(beanDefinition);
    }

    public BeanComponentDefinition(BeanDefinitionHolder holder) {
        super(holder);
        findInnerBeanDefinitionsAndBeanReferences(holder.getBeanDefinition());
    }

    private void findInnerBeanDefinitionsAndBeanReferences(BeanDefinition beanDefinition) {
        List<BeanDefinition> innerBeans = new ArrayList<>();
        List<BeanReference> references = new ArrayList<>();
        PropertyValues propertyValues = beanDefinition.getPropertyValues();
        for (PropertyValue propertyValue : propertyValues.getPropertyValues()) {
            Object value = propertyValue.getValue();
            if (value instanceof BeanDefinitionHolder) {
                innerBeans.add(((BeanDefinitionHolder) value).getBeanDefinition());
            } else if (value instanceof BeanDefinition) {
                innerBeans.add((BeanDefinition) value);
            } else if (value instanceof BeanReference) {
                references.add((BeanReference) value);
            }
        }
        this.innerBeanDefinitions = (BeanDefinition[]) innerBeans.toArray(new BeanDefinition[innerBeans.size()]);
        this.beanReferences = (BeanReference[]) references.toArray(new BeanReference[references.size()]);
    }

    @Override // org.springframework.beans.factory.parsing.ComponentDefinition
    public String getName() {
        return getBeanName();
    }

    @Override // org.springframework.beans.factory.parsing.ComponentDefinition
    public String getDescription() {
        return getShortDescription();
    }

    @Override // org.springframework.beans.factory.parsing.ComponentDefinition
    public BeanDefinition[] getBeanDefinitions() {
        return new BeanDefinition[]{getBeanDefinition()};
    }

    @Override // org.springframework.beans.factory.parsing.ComponentDefinition
    public BeanDefinition[] getInnerBeanDefinitions() {
        return this.innerBeanDefinitions;
    }

    @Override // org.springframework.beans.factory.parsing.ComponentDefinition
    public BeanReference[] getBeanReferences() {
        return this.beanReferences;
    }

    @Override // org.springframework.beans.factory.config.BeanDefinitionHolder
    public String toString() {
        return getDescription();
    }

    @Override // org.springframework.beans.factory.config.BeanDefinitionHolder
    public boolean equals(Object other) {
        return this == other || ((other instanceof BeanComponentDefinition) && super.equals(other));
    }
}
