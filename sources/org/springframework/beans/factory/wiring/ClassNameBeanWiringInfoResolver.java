package org.springframework.beans.factory.wiring;

import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

/* loaded from: spring-beans-4.3.25.RELEASE.jar:org/springframework/beans/factory/wiring/ClassNameBeanWiringInfoResolver.class */
public class ClassNameBeanWiringInfoResolver implements BeanWiringInfoResolver {
    @Override // org.springframework.beans.factory.wiring.BeanWiringInfoResolver
    public BeanWiringInfo resolveWiringInfo(Object beanInstance) {
        Assert.notNull(beanInstance, "Bean instance must not be null");
        return new BeanWiringInfo(ClassUtils.getUserClass(beanInstance).getName(), true);
    }
}
