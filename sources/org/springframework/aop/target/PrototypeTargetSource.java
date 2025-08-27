package org.springframework.aop.target;

import org.springframework.beans.BeansException;

/* loaded from: spring-aop-4.3.25.RELEASE.jar:org/springframework/aop/target/PrototypeTargetSource.class */
public class PrototypeTargetSource extends AbstractPrototypeBasedTargetSource {
    @Override // org.springframework.aop.TargetSource
    public Object getTarget() throws BeansException {
        return newPrototypeInstance();
    }

    @Override // org.springframework.aop.target.AbstractBeanFactoryBasedTargetSource, org.springframework.aop.TargetSource
    public void releaseTarget(Object target) {
        destroyPrototypeInstance(target);
    }

    @Override // org.springframework.aop.target.AbstractBeanFactoryBasedTargetSource
    public String toString() {
        return "PrototypeTargetSource for target bean with name '" + getTargetBeanName() + "'";
    }
}
