package org.springframework.boot.autoconfigure.data.cassandra;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import java.util.List;
import java.util.Locale;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.boot.autoconfigure.AutoConfigurationPackages;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.cassandra.CassandraAutoConfiguration;
import org.springframework.boot.autoconfigure.cassandra.CassandraProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.domain.EntityScanPackages;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertyResolver;
import org.springframework.data.cassandra.config.CassandraEntityClassScanner;
import org.springframework.data.cassandra.config.CassandraSessionFactoryBean;
import org.springframework.data.cassandra.config.SchemaAction;
import org.springframework.data.cassandra.convert.CassandraConverter;
import org.springframework.data.cassandra.convert.MappingCassandraConverter;
import org.springframework.data.cassandra.core.CassandraAdminOperations;
import org.springframework.data.cassandra.core.CassandraTemplate;
import org.springframework.data.cassandra.mapping.BasicCassandraMappingContext;
import org.springframework.data.cassandra.mapping.CassandraMappingContext;
import org.springframework.data.cassandra.mapping.SimpleUserTypeResolver;
import org.springframework.util.StringUtils;

@EnableConfigurationProperties({CassandraProperties.class})
@Configuration
@ConditionalOnClass({Cluster.class, CassandraAdminOperations.class})
@AutoConfigureAfter({CassandraAutoConfiguration.class})
/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/data/cassandra/CassandraDataAutoConfiguration.class */
public class CassandraDataAutoConfiguration {
    private final BeanFactory beanFactory;
    private final CassandraProperties properties;
    private final Cluster cluster;
    private final PropertyResolver propertyResolver;

    public CassandraDataAutoConfiguration(BeanFactory beanFactory, CassandraProperties properties, Cluster cluster, Environment environment) {
        this.beanFactory = beanFactory;
        this.properties = properties;
        this.cluster = cluster;
        this.propertyResolver = new RelaxedPropertyResolver(environment, "spring.data.cassandra.");
    }

    @ConditionalOnMissingBean
    @Bean
    public CassandraMappingContext cassandraMapping() throws ClassNotFoundException {
        BasicCassandraMappingContext context = new BasicCassandraMappingContext();
        List<String> packages = EntityScanPackages.get(this.beanFactory).getPackageNames();
        if (packages.isEmpty() && AutoConfigurationPackages.has(this.beanFactory)) {
            packages = AutoConfigurationPackages.get(this.beanFactory);
        }
        if (!packages.isEmpty()) {
            context.setInitialEntitySet(CassandraEntityClassScanner.scan(packages));
        }
        if (StringUtils.hasText(this.properties.getKeyspaceName())) {
            context.setUserTypeResolver(new SimpleUserTypeResolver(this.cluster, this.properties.getKeyspaceName()));
        }
        return context;
    }

    @ConditionalOnMissingBean
    @Bean
    public CassandraConverter cassandraConverter(CassandraMappingContext mapping) {
        return new MappingCassandraConverter(mapping);
    }

    @ConditionalOnMissingBean({Session.class})
    @Bean
    public CassandraSessionFactoryBean session(CassandraConverter converter) throws Exception {
        CassandraSessionFactoryBean session = new CassandraSessionFactoryBean();
        session.setCluster(this.cluster);
        session.setConverter(converter);
        session.setKeyspaceName(this.properties.getKeyspaceName());
        String name = this.propertyResolver.getProperty("schemaAction", SchemaAction.NONE.name());
        SchemaAction schemaAction = SchemaAction.valueOf(name.toUpperCase(Locale.ENGLISH));
        session.setSchemaAction(schemaAction);
        return session;
    }

    @ConditionalOnMissingBean
    @Bean
    public CassandraTemplate cassandraTemplate(Session session, CassandraConverter converter) throws Exception {
        return new CassandraTemplate(session, converter);
    }
}
