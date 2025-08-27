package org.springframework.dao.annotation;

import java.lang.annotation.Annotation;
import org.springframework.aop.framework.autoproxy.AbstractBeanFactoryAwareAdvisingPostProcessor;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

/* loaded from: spring-tx-4.3.25.RELEASE.jar:org/springframework/dao/annotation/PersistenceExceptionTranslationPostProcessor.class */
public class PersistenceExceptionTranslationPostProcessor extends AbstractBeanFactoryAwareAdvisingPostProcessor {
    private Class<? extends Annotation> repositoryAnnotationType = Repository.class;

    public void setRepositoryAnnotationType(Class<? extends Annotation> repositoryAnnotationType) {
        Assert.notNull(repositoryAnnotationType, "'repositoryAnnotationType' must not be null");
        this.repositoryAnnotationType = repositoryAnnotationType;
    }

    @Override // org.springframework.aop.framework.autoproxy.AbstractBeanFactoryAwareAdvisingPostProcessor, org.springframework.beans.factory.BeanFactoryAware
    public void setBeanFactory(BeanFactory beanFactory) {
        super.setBeanFactory(beanFactory);
        if (!(beanFactory instanceof ListableBeanFactory)) {
            throw new IllegalArgumentException("Cannot use PersistenceExceptionTranslator autodetection without ListableBeanFactory");
        }
        this.advisor = new PersistenceExceptionTranslationAdvisor((ListableBeanFactory) beanFactory, this.repositoryAnnotationType);
    }
}
