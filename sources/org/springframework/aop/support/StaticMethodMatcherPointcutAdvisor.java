package org.springframework.aop.support;

import java.io.Serializable;
import org.aopalliance.aop.Advice;
import org.springframework.aop.Pointcut;
import org.springframework.aop.PointcutAdvisor;
import org.springframework.core.Ordered;
import org.springframework.util.Assert;

/* loaded from: spring-aop-4.3.25.RELEASE.jar:org/springframework/aop/support/StaticMethodMatcherPointcutAdvisor.class */
public abstract class StaticMethodMatcherPointcutAdvisor extends StaticMethodMatcherPointcut implements PointcutAdvisor, Ordered, Serializable {
    private Advice advice;
    private int order = Integer.MAX_VALUE;

    public StaticMethodMatcherPointcutAdvisor() {
    }

    public StaticMethodMatcherPointcutAdvisor(Advice advice) {
        Assert.notNull(advice, "Advice must not be null");
        this.advice = advice;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    @Override // org.springframework.core.Ordered
    public int getOrder() {
        return this.order;
    }

    public void setAdvice(Advice advice) {
        this.advice = advice;
    }

    @Override // org.springframework.aop.Advisor
    public Advice getAdvice() {
        return this.advice;
    }

    @Override // org.springframework.aop.Advisor
    public boolean isPerInstance() {
        return true;
    }

    @Override // org.springframework.aop.PointcutAdvisor
    public Pointcut getPointcut() {
        return this;
    }
}
