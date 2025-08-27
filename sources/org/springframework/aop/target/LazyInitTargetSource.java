package org.springframework.aop.target;

import org.springframework.beans.BeansException;

/* loaded from: spring-aop-4.3.25.RELEASE.jar:org/springframework/aop/target/LazyInitTargetSource.class */
public class LazyInitTargetSource extends AbstractBeanFactoryBasedTargetSource {
    private Object target;

    @Override // org.springframework.aop.TargetSource
    public synchronized Object getTarget() throws BeansException {
        if (this.target == null) {
            this.target = getBeanFactory().getBean(getTargetBeanName());
            postProcessTargetObject(this.target);
        }
        return this.target;
    }

    protected void postProcessTargetObject(Object targetObject) {
    }
}
