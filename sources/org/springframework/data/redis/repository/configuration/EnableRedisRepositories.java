package org.springframework.data.redis.repository.configuration;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.data.keyvalue.repository.config.QueryCreatorType;
import org.springframework.data.redis.core.RedisKeyValueAdapter;
import org.springframework.data.redis.core.convert.KeyspaceConfiguration;
import org.springframework.data.redis.core.index.IndexConfiguration;
import org.springframework.data.redis.repository.query.RedisQueryCreator;
import org.springframework.data.redis.repository.support.RedisRepositoryFactoryBean;
import org.springframework.data.repository.config.DefaultRepositoryBaseClass;
import org.springframework.data.repository.query.QueryLookupStrategy;

@Target({ElementType.TYPE})
@Inherited
@QueryCreatorType(RedisQueryCreator.class)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({RedisRepositoriesRegistrar.class})
/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/repository/configuration/EnableRedisRepositories.class */
public @interface EnableRedisRepositories {
    String[] value() default {};

    String[] basePackages() default {};

    Class<?>[] basePackageClasses() default {};

    ComponentScan.Filter[] excludeFilters() default {};

    ComponentScan.Filter[] includeFilters() default {};

    String repositoryImplementationPostfix() default "Impl";

    String namedQueriesLocation() default "";

    QueryLookupStrategy.Key queryLookupStrategy() default QueryLookupStrategy.Key.CREATE_IF_NOT_FOUND;

    Class<?> repositoryFactoryBeanClass() default RedisRepositoryFactoryBean.class;

    Class<?> repositoryBaseClass() default DefaultRepositoryBaseClass.class;

    String keyValueTemplateRef() default "redisKeyValueTemplate";

    boolean considerNestedRepositories() default false;

    String redisTemplateRef() default "redisTemplate";

    Class<? extends IndexConfiguration> indexConfiguration() default IndexConfiguration.class;

    Class<? extends KeyspaceConfiguration> keyspaceConfiguration() default KeyspaceConfiguration.class;

    RedisKeyValueAdapter.EnableKeyspaceEvents enableKeyspaceEvents() default RedisKeyValueAdapter.EnableKeyspaceEvents.OFF;

    String keyspaceNotificationsConfigParameter() default "Ex";
}
