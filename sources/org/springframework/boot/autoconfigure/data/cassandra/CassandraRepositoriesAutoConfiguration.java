package org.springframework.boot.autoconfigure.data.cassandra;

import com.datastax.driver.core.Session;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.support.CassandraRepositoryFactoryBean;

@Configuration
@ConditionalOnClass({Session.class, CassandraRepository.class})
@ConditionalOnMissingBean({CassandraRepositoryFactoryBean.class})
@ConditionalOnProperty(prefix = "spring.data.cassandra.repositories", name = {"enabled"}, havingValue = "true", matchIfMissing = true)
@Import({CassandraRepositoriesAutoConfigureRegistrar.class})
/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/data/cassandra/CassandraRepositoriesAutoConfiguration.class */
public class CassandraRepositoriesAutoConfiguration {
}
