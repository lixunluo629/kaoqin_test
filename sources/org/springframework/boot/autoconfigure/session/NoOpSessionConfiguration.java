package org.springframework.boot.autoconfigure.session;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.SessionRepository;

@ConditionalOnMissingBean({SessionRepository.class})
@Configuration
@Conditional({SessionCondition.class})
/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/session/NoOpSessionConfiguration.class */
class NoOpSessionConfiguration {
    NoOpSessionConfiguration() {
    }
}
