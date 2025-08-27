package org.springframework.aop.target;

/* loaded from: spring-aop-4.3.25.RELEASE.jar:org/springframework/aop/target/SimpleBeanTargetSource.class */
public class SimpleBeanTargetSource extends AbstractBeanFactoryBasedTargetSource {
    @Override // org.springframework.aop.TargetSource
    public Object getTarget() throws Exception {
        return getBeanFactory().getBean(getTargetBeanName());
    }
}
