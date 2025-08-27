package org.springframework.aop.support;

import java.io.IOException;
import java.io.ObjectInputStream;
import org.aopalliance.aop.Advice;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.util.Assert;

/* loaded from: spring-aop-4.3.25.RELEASE.jar:org/springframework/aop/support/AbstractBeanFactoryPointcutAdvisor.class */
public abstract class AbstractBeanFactoryPointcutAdvisor extends AbstractPointcutAdvisor implements BeanFactoryAware {
    private String adviceBeanName;
    private BeanFactory beanFactory;
    private volatile transient Advice advice;
    private volatile transient Object adviceMonitor = new Object();

    public void setAdviceBeanName(String adviceBeanName) {
        this.adviceBeanName = adviceBeanName;
    }

    public String getAdviceBeanName() {
        return this.adviceBeanName;
    }

    @Override // org.springframework.beans.factory.BeanFactoryAware
    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
        resetAdviceMonitor();
    }

    private void resetAdviceMonitor() {
        if (this.beanFactory instanceof ConfigurableBeanFactory) {
            this.adviceMonitor = ((ConfigurableBeanFactory) this.beanFactory).getSingletonMutex();
        } else {
            this.adviceMonitor = new Object();
        }
    }

    public void setAdvice(Advice advice) {
        synchronized (this.adviceMonitor) {
            this.advice = advice;
        }
    }

    @Override // org.springframework.aop.Advisor
    public Advice getAdvice() {
        Advice advice;
        Advice advice2 = this.advice;
        if (advice2 != null || this.adviceBeanName == null) {
            return advice2;
        }
        Assert.state(this.beanFactory != null, "BeanFactory must be set to resolve 'adviceBeanName'");
        if (this.beanFactory.isSingleton(this.adviceBeanName)) {
            Advice advice3 = (Advice) this.beanFactory.getBean(this.adviceBeanName, Advice.class);
            this.advice = advice3;
            return advice3;
        }
        synchronized (this.adviceMonitor) {
            if (this.advice == null) {
                this.advice = (Advice) this.beanFactory.getBean(this.adviceBeanName, Advice.class);
            }
            advice = this.advice;
        }
        return advice;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(getClass().getName());
        sb.append(": advice ");
        if (this.adviceBeanName != null) {
            sb.append("bean '").append(this.adviceBeanName).append("'");
        } else {
            sb.append(this.advice);
        }
        return sb.toString();
    }

    private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException {
        ois.defaultReadObject();
        resetAdviceMonitor();
    }
}
