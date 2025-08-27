package org.springframework.aop.framework.autoproxy;

import java.util.List;
import org.springframework.aop.Advisor;
import org.springframework.aop.TargetSource;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;

/* loaded from: spring-aop-4.3.25.RELEASE.jar:org/springframework/aop/framework/autoproxy/AbstractAdvisorAutoProxyCreator.class */
public abstract class AbstractAdvisorAutoProxyCreator extends AbstractAutoProxyCreator {
    private BeanFactoryAdvisorRetrievalHelper advisorRetrievalHelper;

    @Override // org.springframework.aop.framework.autoproxy.AbstractAutoProxyCreator, org.springframework.beans.factory.BeanFactoryAware
    public void setBeanFactory(BeanFactory beanFactory) {
        super.setBeanFactory(beanFactory);
        if (!(beanFactory instanceof ConfigurableListableBeanFactory)) {
            throw new IllegalArgumentException("AdvisorAutoProxyCreator requires a ConfigurableListableBeanFactory: " + beanFactory);
        }
        initBeanFactory((ConfigurableListableBeanFactory) beanFactory);
    }

    protected void initBeanFactory(ConfigurableListableBeanFactory beanFactory) {
        this.advisorRetrievalHelper = new BeanFactoryAdvisorRetrievalHelperAdapter(beanFactory);
    }

    @Override // org.springframework.aop.framework.autoproxy.AbstractAutoProxyCreator
    protected Object[] getAdvicesAndAdvisorsForBean(Class<?> beanClass, String beanName, TargetSource targetSource) {
        List<Advisor> advisors = findEligibleAdvisors(beanClass, beanName);
        if (advisors.isEmpty()) {
            return DO_NOT_PROXY;
        }
        return advisors.toArray();
    }

    protected List<Advisor> findEligibleAdvisors(Class<?> beanClass, String beanName) {
        List<Advisor> candidateAdvisors = findCandidateAdvisors();
        List<Advisor> eligibleAdvisors = findAdvisorsThatCanApply(candidateAdvisors, beanClass, beanName);
        extendAdvisors(eligibleAdvisors);
        if (!eligibleAdvisors.isEmpty()) {
            eligibleAdvisors = sortAdvisors(eligibleAdvisors);
        }
        return eligibleAdvisors;
    }

    protected List<Advisor> findCandidateAdvisors() {
        return this.advisorRetrievalHelper.findAdvisorBeans();
    }

    protected List<Advisor> findAdvisorsThatCanApply(List<Advisor> candidateAdvisors, Class<?> beanClass, String beanName) {
        ProxyCreationContext.setCurrentProxiedBeanName(beanName);
        try {
            List<Advisor> listFindAdvisorsThatCanApply = AopUtils.findAdvisorsThatCanApply(candidateAdvisors, beanClass);
            ProxyCreationContext.setCurrentProxiedBeanName(null);
            return listFindAdvisorsThatCanApply;
        } catch (Throwable th) {
            ProxyCreationContext.setCurrentProxiedBeanName(null);
            throw th;
        }
    }

    protected boolean isEligibleAdvisorBean(String beanName) {
        return true;
    }

    protected List<Advisor> sortAdvisors(List<Advisor> advisors) {
        AnnotationAwareOrderComparator.sort(advisors);
        return advisors;
    }

    protected void extendAdvisors(List<Advisor> candidateAdvisors) {
    }

    @Override // org.springframework.aop.framework.autoproxy.AbstractAutoProxyCreator
    protected boolean advisorsPreFiltered() {
        return true;
    }

    /* loaded from: spring-aop-4.3.25.RELEASE.jar:org/springframework/aop/framework/autoproxy/AbstractAdvisorAutoProxyCreator$BeanFactoryAdvisorRetrievalHelperAdapter.class */
    private class BeanFactoryAdvisorRetrievalHelperAdapter extends BeanFactoryAdvisorRetrievalHelper {
        public BeanFactoryAdvisorRetrievalHelperAdapter(ConfigurableListableBeanFactory beanFactory) {
            super(beanFactory);
        }

        @Override // org.springframework.aop.framework.autoproxy.BeanFactoryAdvisorRetrievalHelper
        protected boolean isEligibleBean(String beanName) {
            return AbstractAdvisorAutoProxyCreator.this.isEligibleAdvisorBean(beanName);
        }
    }
}
