package org.springframework.boot.autoconfigure.session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.session.SessionProperties;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.session.SessionRepository;
import org.springframework.session.data.redis.config.annotation.web.http.RedisHttpSessionConfiguration;

@Configuration
@ConditionalOnClass({RedisTemplate.class})
@ConditionalOnMissingBean({SessionRepository.class})
@ConditionalOnBean({RedisConnectionFactory.class})
@Conditional({SessionCondition.class})
/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/session/RedisSessionConfiguration.class */
class RedisSessionConfiguration {
    RedisSessionConfiguration() {
    }

    @Configuration
    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/session/RedisSessionConfiguration$SpringBootRedisHttpSessionConfiguration.class */
    public static class SpringBootRedisHttpSessionConfiguration extends RedisHttpSessionConfiguration {
        @Autowired
        public void customize(SessionProperties sessionProperties) {
            Integer timeout = sessionProperties.getTimeout();
            if (timeout != null) {
                setMaxInactiveIntervalInSeconds(timeout.intValue());
            }
            SessionProperties.Redis redis2 = sessionProperties.getRedis();
            setRedisNamespace(redis2.getNamespace());
            setRedisFlushMode(redis2.getFlushMode());
        }
    }
}
