package org.springframework.boot.autoconfigure.session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.session.SessionRepository;
import org.springframework.session.data.mongo.config.annotation.web.http.MongoHttpSessionConfiguration;

@Configuration
@ConditionalOnMissingBean({SessionRepository.class})
@ConditionalOnBean({MongoOperations.class})
@Conditional({SessionCondition.class})
/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/session/MongoSessionConfiguration.class */
class MongoSessionConfiguration {
    MongoSessionConfiguration() {
    }

    @Configuration
    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/session/MongoSessionConfiguration$SpringBootMongoHttpSessionConfiguration.class */
    public static class SpringBootMongoHttpSessionConfiguration extends MongoHttpSessionConfiguration {
        @Autowired
        public void customize(SessionProperties sessionProperties) {
            Integer timeout = sessionProperties.getTimeout();
            if (timeout != null) {
                setMaxInactiveIntervalInSeconds(timeout);
            }
            setCollectionName(sessionProperties.getMongo().getCollectionName());
        }
    }
}
