package org.springframework.boot.autoconfigure.liquibase;

import java.lang.reflect.Method;
import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import liquibase.exception.LiquibaseException;
import liquibase.integration.spring.SpringLiquibase;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.jpa.EntityManagerFactoryDependsOnPostProcessor;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.orm.jpa.AbstractEntityManagerFactoryBean;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

@Configuration
@ConditionalOnClass({SpringLiquibase.class})
@AutoConfigureAfter({DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
@ConditionalOnBean({DataSource.class})
@ConditionalOnProperty(prefix = "liquibase", name = {"enabled"}, matchIfMissing = true)
/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/liquibase/LiquibaseAutoConfiguration.class */
public class LiquibaseAutoConfiguration {

    @EnableConfigurationProperties({LiquibaseProperties.class})
    @Configuration
    @ConditionalOnMissingBean({SpringLiquibase.class})
    @Import({LiquibaseJpaDependencyConfiguration.class})
    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/liquibase/LiquibaseAutoConfiguration$LiquibaseConfiguration.class */
    public static class LiquibaseConfiguration {
        private final LiquibaseProperties properties;
        private final ResourceLoader resourceLoader;
        private final DataSource dataSource;
        private final DataSource liquibaseDataSource;

        public LiquibaseConfiguration(LiquibaseProperties properties, ResourceLoader resourceLoader, ObjectProvider<DataSource> dataSource, @LiquibaseDataSource ObjectProvider<DataSource> liquibaseDataSource) {
            this.properties = properties;
            this.resourceLoader = resourceLoader;
            this.dataSource = dataSource.getIfUnique();
            this.liquibaseDataSource = liquibaseDataSource.getIfAvailable();
        }

        @PostConstruct
        public void checkChangelogExists() {
            if (this.properties.isCheckChangeLogLocation()) {
                Resource resource = this.resourceLoader.getResource(this.properties.getChangeLog());
                Assert.state(resource.exists(), "Cannot find changelog location: " + resource + " (please add changelog or check your Liquibase configuration)");
            }
        }

        @Bean
        public SpringLiquibase liquibase() {
            SpringLiquibase liquibase = createSpringLiquibase();
            liquibase.setChangeLog(this.properties.getChangeLog());
            liquibase.setContexts(this.properties.getContexts());
            liquibase.setDefaultSchema(this.properties.getDefaultSchema());
            liquibase.setDropFirst(this.properties.isDropFirst());
            liquibase.setShouldRun(this.properties.isEnabled());
            liquibase.setLabels(this.properties.getLabels());
            liquibase.setChangeLogParameters(this.properties.getParameters());
            liquibase.setRollbackFile(this.properties.getRollbackFile());
            return liquibase;
        }

        private SpringLiquibase createSpringLiquibase() {
            DataSource liquibaseDataSource = getDataSource();
            if (liquibaseDataSource != null) {
                SpringLiquibase liquibase = new SpringLiquibase();
                liquibase.setDataSource(liquibaseDataSource);
                return liquibase;
            }
            SpringLiquibase liquibase2 = new DataSourceClosingSpringLiquibase();
            liquibase2.setDataSource(createNewDataSource());
            return liquibase2;
        }

        private DataSource getDataSource() {
            if (this.liquibaseDataSource != null) {
                return this.liquibaseDataSource;
            }
            if (this.properties.getUrl() == null) {
                return this.dataSource;
            }
            return null;
        }

        private DataSource createNewDataSource() {
            return DataSourceBuilder.create().url(this.properties.getUrl()).username(this.properties.getUser()).password(this.properties.getPassword()).build();
        }
    }

    @Configuration
    @ConditionalOnClass({LocalContainerEntityManagerFactoryBean.class})
    @ConditionalOnBean({AbstractEntityManagerFactoryBean.class})
    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/liquibase/LiquibaseAutoConfiguration$LiquibaseJpaDependencyConfiguration.class */
    protected static class LiquibaseJpaDependencyConfiguration extends EntityManagerFactoryDependsOnPostProcessor {
        public LiquibaseJpaDependencyConfiguration() {
            super("liquibase");
        }
    }

    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/liquibase/LiquibaseAutoConfiguration$DataSourceClosingSpringLiquibase.class */
    private static final class DataSourceClosingSpringLiquibase extends SpringLiquibase {
        private DataSourceClosingSpringLiquibase() {
        }

        public void afterPropertiesSet() throws LiquibaseException {
            super.afterPropertiesSet();
            closeDataSource();
        }

        private void closeDataSource() {
            Class<?> dataSourceClass = getDataSource().getClass();
            Method closeMethod = ReflectionUtils.findMethod(dataSourceClass, "close");
            if (closeMethod != null) {
                ReflectionUtils.invokeMethod(closeMethod, getDataSource());
            }
        }
    }
}
