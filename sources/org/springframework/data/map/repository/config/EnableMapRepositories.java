package org.springframework.data.map.repository.config;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.data.keyvalue.repository.config.QueryCreatorType;
import org.springframework.data.keyvalue.repository.query.CachingKeyValuePartTreeQuery;
import org.springframework.data.keyvalue.repository.query.SpelQueryCreator;
import org.springframework.data.keyvalue.repository.support.KeyValueRepositoryFactoryBean;
import org.springframework.data.repository.config.DefaultRepositoryBaseClass;
import org.springframework.data.repository.query.QueryLookupStrategy;

@Target({ElementType.TYPE})
@Inherited
@QueryCreatorType(value = SpelQueryCreator.class, repositoryQueryType = CachingKeyValuePartTreeQuery.class)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({MapRepositoriesRegistrar.class})
/* loaded from: spring-data-keyvalue-1.2.23.RELEASE.jar:org/springframework/data/map/repository/config/EnableMapRepositories.class */
public @interface EnableMapRepositories {
    String[] value() default {};

    String[] basePackages() default {};

    Class<?>[] basePackageClasses() default {};

    ComponentScan.Filter[] excludeFilters() default {};

    ComponentScan.Filter[] includeFilters() default {};

    String repositoryImplementationPostfix() default "Impl";

    String namedQueriesLocation() default "";

    QueryLookupStrategy.Key queryLookupStrategy() default QueryLookupStrategy.Key.CREATE_IF_NOT_FOUND;

    Class<?> repositoryFactoryBeanClass() default KeyValueRepositoryFactoryBean.class;

    Class<?> repositoryBaseClass() default DefaultRepositoryBaseClass.class;

    String keyValueTemplateRef() default "mapKeyValueTemplate";

    boolean considerNestedRepositories() default false;

    Class<? extends Map> mapType() default ConcurrentHashMap.class;
}
