package org.springframework.boot.autoconfigure.data.redis;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.repository.support.RedisRepositoryFactoryBean;
import redis.clients.jedis.Jedis;

@Configuration
@ConditionalOnClass({Jedis.class, EnableRedisRepositories.class})
@AutoConfigureAfter({RedisAutoConfiguration.class})
@ConditionalOnMissingBean({RedisRepositoryFactoryBean.class})
@ConditionalOnProperty(prefix = "spring.data.redis.repositories", name = {"enabled"}, havingValue = "true", matchIfMissing = true)
@Import({RedisRepositoriesAutoConfigureRegistrar.class})
/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/data/redis/RedisRepositoriesAutoConfiguration.class */
public class RedisRepositoriesAutoConfiguration {
}
