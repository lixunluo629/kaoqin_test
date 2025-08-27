package org.springframework.data.web.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.List;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.SpringFactoriesLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.data.querydsl.QueryDslUtils;
import org.springframework.util.ClassUtils;

@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Import({SpringDataWebConfigurationImportSelector.class, QuerydslActivator.class})
/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/web/config/EnableSpringDataWebSupport.class */
public @interface EnableSpringDataWebSupport {

    /* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/web/config/EnableSpringDataWebSupport$SpringDataWebConfigurationImportSelector.class */
    public static class SpringDataWebConfigurationImportSelector implements ImportSelector, EnvironmentAware, ResourceLoaderAware {
        private Environment environment;
        private ResourceLoader resourceLoader;

        @Override // org.springframework.context.EnvironmentAware
        public void setEnvironment(Environment environment) {
            this.environment = environment;
        }

        @Override // org.springframework.context.ResourceLoaderAware
        public void setResourceLoader(ResourceLoader resourceLoader) {
            this.resourceLoader = resourceLoader;
        }

        @Override // org.springframework.context.annotation.ImportSelector
        public String[] selectImports(AnnotationMetadata importingClassMetadata) {
            String name;
            List<String> imports = new ArrayList<>();
            imports.add(ProjectingArgumentResolverRegistrar.class.getName());
            if (ClassUtils.isPresent("org.springframework.hateoas.Link", this.resourceLoader.getClassLoader())) {
                name = HateoasAwareSpringDataWebConfiguration.class.getName();
            } else {
                name = SpringDataWebConfiguration.class.getName();
            }
            imports.add(name);
            if (ClassUtils.isPresent("com.fasterxml.jackson.databind.ObjectMapper", this.resourceLoader.getClassLoader())) {
                imports.addAll(SpringFactoriesLoader.loadFactoryNames(SpringDataJacksonModules.class, this.resourceLoader.getClassLoader()));
            }
            return (String[]) imports.toArray(new String[imports.size()]);
        }
    }

    /* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/web/config/EnableSpringDataWebSupport$QuerydslActivator.class */
    public static class QuerydslActivator implements ImportSelector {
        @Override // org.springframework.context.annotation.ImportSelector
        public String[] selectImports(AnnotationMetadata importingClassMetadata) {
            return QueryDslUtils.QUERY_DSL_PRESENT ? new String[]{QuerydslWebConfiguration.class.getName()} : new String[0];
        }
    }
}
