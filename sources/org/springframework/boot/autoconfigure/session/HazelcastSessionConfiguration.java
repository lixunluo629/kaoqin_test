package org.springframework.boot.autoconfigure.session;

import com.hazelcast.core.HazelcastInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.session.SessionProperties;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.SessionRepository;
import org.springframework.session.hazelcast.config.annotation.web.http.HazelcastHttpSessionConfiguration;

@Configuration
@ConditionalOnMissingBean({SessionRepository.class})
@ConditionalOnBean({HazelcastInstance.class})
@Conditional({SessionCondition.class})
/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/session/HazelcastSessionConfiguration.class */
class HazelcastSessionConfiguration {
    HazelcastSessionConfiguration() {
    }

    @Configuration
    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/session/HazelcastSessionConfiguration$SpringBootHazelcastHttpSessionConfiguration.class */
    public static class SpringBootHazelcastHttpSessionConfiguration extends HazelcastHttpSessionConfiguration {
        @Autowired
        public void customize(SessionProperties sessionProperties) {
            Integer timeout = sessionProperties.getTimeout();
            if (timeout != null) {
                setMaxInactiveIntervalInSeconds(timeout.intValue());
            }
            SessionProperties.Hazelcast hazelcast = sessionProperties.getHazelcast();
            setSessionMapName(hazelcast.getMapName());
            setHazelcastFlushMode(hazelcast.getFlushMode());
        }
    }
}
