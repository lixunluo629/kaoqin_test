package org.springframework.aop.framework;

/* loaded from: spring-aop-4.3.25.RELEASE.jar:org/springframework/aop/framework/AdvisedSupportListener.class */
public interface AdvisedSupportListener {
    void activated(AdvisedSupport advisedSupport);

    void adviceChanged(AdvisedSupport advisedSupport);
}
