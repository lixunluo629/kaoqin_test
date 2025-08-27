package org.springframework.boot.autoconfigure.data.mongo;

import com.mongodb.MongoClient;
import org.springframework.boot.autoconfigure.AbstractDependsOnBeanFactoryPostProcessor;
import org.springframework.core.annotation.Order;
import org.springframework.data.mongodb.core.MongoClientFactoryBean;

@Order(Integer.MAX_VALUE)
/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/data/mongo/MongoClientDependsOnBeanFactoryPostProcessor.class */
public class MongoClientDependsOnBeanFactoryPostProcessor extends AbstractDependsOnBeanFactoryPostProcessor {
    public MongoClientDependsOnBeanFactoryPostProcessor(String... dependsOn) {
        super(MongoClient.class, MongoClientFactoryBean.class, dependsOn);
    }
}
