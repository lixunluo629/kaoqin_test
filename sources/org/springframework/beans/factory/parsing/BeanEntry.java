package org.springframework.beans.factory.parsing;

import org.springframework.beans.factory.parsing.ParseState;

/* loaded from: spring-beans-4.3.25.RELEASE.jar:org/springframework/beans/factory/parsing/BeanEntry.class */
public class BeanEntry implements ParseState.Entry {
    private String beanDefinitionName;

    public BeanEntry(String beanDefinitionName) {
        this.beanDefinitionName = beanDefinitionName;
    }

    public String toString() {
        return "Bean '" + this.beanDefinitionName + "'";
    }
}
