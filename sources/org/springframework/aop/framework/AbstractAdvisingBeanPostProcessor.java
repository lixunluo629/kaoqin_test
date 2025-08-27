package org.springframework.aop.framework;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.aop.Advisor;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.config.BeanPostProcessor;

/* loaded from: spring-aop-4.3.25.RELEASE.jar:org/springframework/aop/framework/AbstractAdvisingBeanPostProcessor.class */
public abstract class AbstractAdvisingBeanPostProcessor extends ProxyProcessorSupport implements BeanPostProcessor {
    protected Advisor advisor;
    protected boolean beforeExistingAdvisors = false;
    private final Map<Class<?>, Boolean> eligibleBeans = new ConcurrentHashMap(256);

    public void setBeforeExistingAdvisors(boolean beforeExistingAdvisors) {
        this.beforeExistingAdvisors = beforeExistingAdvisors;
    }

    @Override // org.springframework.beans.factory.config.BeanPostProcessor
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        return bean;
    }

    @Override // org.springframework.beans.factory.config.BeanPostProcessor
    public Object postProcessAfterInitialization(Object bean, String beanName) throws AopConfigException {
        if (bean instanceof AopInfrastructureBean) {
            return bean;
        }
        if (bean instanceof Advised) {
            Advised advised = (Advised) bean;
            if (!advised.isFrozen() && isEligible(AopUtils.getTargetClass(bean))) {
                if (this.beforeExistingAdvisors) {
                    advised.addAdvisor(0, this.advisor);
                } else {
                    advised.addAdvisor(this.advisor);
                }
                return bean;
            }
        }
        if (isEligible(bean, beanName)) {
            ProxyFactory proxyFactory = prepareProxyFactory(bean, beanName);
            if (!proxyFactory.isProxyTargetClass()) {
                evaluateProxyInterfaces(bean.getClass(), proxyFactory);
            }
            proxyFactory.addAdvisor(this.advisor);
            customizeProxyFactory(proxyFactory);
            return proxyFactory.getProxy(getProxyClassLoader());
        }
        return bean;
    }

    protected boolean isEligible(Object bean, String beanName) {
        return isEligible(bean.getClass());
    }

    protected boolean isEligible(Class<?> targetClass) {
        Boolean eligible = this.eligibleBeans.get(targetClass);
        if (eligible != null) {
            return eligible.booleanValue();
        }
        Boolean eligible2 = Boolean.valueOf(AopUtils.canApply(this.advisor, targetClass));
        this.eligibleBeans.put(targetClass, eligible2);
        return eligible2.booleanValue();
    }

    protected ProxyFactory prepareProxyFactory(Object bean, String beanName) {
        ProxyFactory proxyFactory = new ProxyFactory();
        proxyFactory.copyFrom(this);
        proxyFactory.setTarget(bean);
        return proxyFactory;
    }

    protected void customizeProxyFactory(ProxyFactory proxyFactory) {
    }
}
