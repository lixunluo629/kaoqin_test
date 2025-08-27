package org.springframework.boot.autoconfigure.session;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.ExpiringSession;
import org.springframework.session.MapSessionRepository;
import org.springframework.session.SessionRepository;
import org.springframework.session.config.annotation.web.http.EnableSpringHttpSession;

@Configuration
@ConditionalOnMissingBean({SessionRepository.class})
@Conditional({SessionCondition.class})
@EnableSpringHttpSession
/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/session/HashMapSessionConfiguration.class */
class HashMapSessionConfiguration {
    HashMapSessionConfiguration() {
    }

    @Bean
    public SessionRepository<ExpiringSession> sessionRepository(SessionProperties properties) {
        MapSessionRepository repository = new MapSessionRepository();
        Integer timeout = properties.getTimeout();
        if (timeout != null) {
            repository.setDefaultMaxInactiveInterval(timeout.intValue());
        }
        return repository;
    }
}
