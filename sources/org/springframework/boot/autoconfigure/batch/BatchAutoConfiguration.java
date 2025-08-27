package org.springframework.boot.autoconfigure.batch;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import org.springframework.batch.core.configuration.ListableJobLocator;
import org.springframework.batch.core.configuration.annotation.BatchConfigurer;
import org.springframework.batch.core.converter.JobParametersConverter;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.explore.support.JobExplorerFactoryBean;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.support.SimpleJobOperator;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.autoconfigure.transaction.TransactionManagerCustomizers;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.util.StringUtils;

@EnableConfigurationProperties({BatchProperties.class})
@Configuration
@ConditionalOnClass({JobLauncher.class, DataSource.class, JdbcOperations.class})
@AutoConfigureAfter({HibernateJpaAutoConfiguration.class})
@ConditionalOnBean({JobLauncher.class})
/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/batch/BatchAutoConfiguration.class */
public class BatchAutoConfiguration {
    private final BatchProperties properties;
    private final JobParametersConverter jobParametersConverter;

    public BatchAutoConfiguration(BatchProperties properties, ObjectProvider<JobParametersConverter> jobParametersConverter) {
        this.properties = properties;
        this.jobParametersConverter = jobParametersConverter.getIfAvailable();
    }

    @ConditionalOnMissingBean
    @ConditionalOnBean({DataSource.class})
    @Bean
    public BatchDatabaseInitializer batchDatabaseInitializer(DataSource dataSource, ResourceLoader resourceLoader) {
        return new BatchDatabaseInitializer(dataSource, resourceLoader, this.properties);
    }

    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "spring.batch.job", name = {"enabled"}, havingValue = "true", matchIfMissing = true)
    @Bean
    public JobLauncherCommandLineRunner jobLauncherCommandLineRunner(JobLauncher jobLauncher, JobExplorer jobExplorer) {
        JobLauncherCommandLineRunner runner = new JobLauncherCommandLineRunner(jobLauncher, jobExplorer);
        String jobNames = this.properties.getJob().getNames();
        if (StringUtils.hasText(jobNames)) {
            runner.setJobNames(jobNames);
        }
        return runner;
    }

    @ConditionalOnMissingBean({ExitCodeGenerator.class})
    @Bean
    public JobExecutionExitCodeGenerator jobExecutionExitCodeGenerator() {
        return new JobExecutionExitCodeGenerator();
    }

    @ConditionalOnMissingBean
    @ConditionalOnBean({DataSource.class})
    @Bean
    public JobExplorer jobExplorer(DataSource dataSource) throws Exception {
        JobExplorerFactoryBean factory = new JobExplorerFactoryBean();
        factory.setDataSource(dataSource);
        String tablePrefix = this.properties.getTablePrefix();
        if (StringUtils.hasText(tablePrefix)) {
            factory.setTablePrefix(tablePrefix);
        }
        factory.afterPropertiesSet();
        return factory.getObject();
    }

    @ConditionalOnMissingBean({JobOperator.class})
    @Bean
    public SimpleJobOperator jobOperator(JobExplorer jobExplorer, JobLauncher jobLauncher, ListableJobLocator jobRegistry, JobRepository jobRepository) throws Exception {
        SimpleJobOperator factory = new SimpleJobOperator();
        factory.setJobExplorer(jobExplorer);
        factory.setJobLauncher(jobLauncher);
        factory.setJobRegistry(jobRegistry);
        factory.setJobRepository(jobRepository);
        if (this.jobParametersConverter != null) {
            factory.setJobParametersConverter(this.jobParametersConverter);
        }
        return factory;
    }

    @EnableConfigurationProperties({BatchProperties.class})
    @ConditionalOnClass(value = {PlatformTransactionManager.class}, name = {"javax.persistence.EntityManagerFactory"})
    @Configuration
    @ConditionalOnMissingBean({BatchConfigurer.class})
    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/batch/BatchAutoConfiguration$JpaBatchConfiguration.class */
    protected static class JpaBatchConfiguration {
        private final BatchProperties properties;

        protected JpaBatchConfiguration(BatchProperties properties) {
            this.properties = properties;
        }

        @ConditionalOnBean(name = {"entityManagerFactory"})
        @Bean
        public BasicBatchConfigurer jpaBatchConfigurer(DataSource dataSource, EntityManagerFactory entityManagerFactory, ObjectProvider<TransactionManagerCustomizers> transactionManagerCustomizers) {
            return new BasicBatchConfigurer(this.properties, dataSource, entityManagerFactory, transactionManagerCustomizers.getIfAvailable());
        }

        @ConditionalOnMissingBean(name = {"entityManagerFactory"})
        @Bean
        public BasicBatchConfigurer basicBatchConfigurer(DataSource dataSource, ObjectProvider<TransactionManagerCustomizers> transactionManagerCustomizers) {
            return new BasicBatchConfigurer(this.properties, dataSource, transactionManagerCustomizers.getIfAvailable());
        }
    }
}
