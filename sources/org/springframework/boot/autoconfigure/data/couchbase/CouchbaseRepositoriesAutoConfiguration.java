package org.springframework.boot.autoconfigure.data.couchbase;

import com.couchbase.client.java.Bucket;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.couchbase.repository.CouchbaseRepository;
import org.springframework.data.couchbase.repository.config.RepositoryOperationsMapping;
import org.springframework.data.couchbase.repository.support.CouchbaseRepositoryFactoryBean;

@Configuration
@ConditionalOnClass({Bucket.class, CouchbaseRepository.class})
@ConditionalOnMissingBean({CouchbaseRepositoryFactoryBean.class})
@ConditionalOnBean({RepositoryOperationsMapping.class})
@ConditionalOnProperty(prefix = "spring.data.couchbase.repositories", name = {"enabled"}, havingValue = "true", matchIfMissing = true)
@Import({CouchbaseRepositoriesRegistrar.class})
/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/data/couchbase/CouchbaseRepositoriesAutoConfiguration.class */
public class CouchbaseRepositoriesAutoConfiguration {
}
