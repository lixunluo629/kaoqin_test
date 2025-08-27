package org.springframework.boot.autoconfigure.data.jpa;

import javax.persistence.EntityManagerFactory;
import org.springframework.boot.autoconfigure.AbstractDependsOnBeanFactoryPostProcessor;
import org.springframework.orm.jpa.AbstractEntityManagerFactoryBean;

/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/data/jpa/EntityManagerFactoryDependsOnPostProcessor.class */
public class EntityManagerFactoryDependsOnPostProcessor extends AbstractDependsOnBeanFactoryPostProcessor {
    public EntityManagerFactoryDependsOnPostProcessor(String... dependsOn) {
        super(EntityManagerFactory.class, AbstractEntityManagerFactoryBean.class, dependsOn);
    }
}
