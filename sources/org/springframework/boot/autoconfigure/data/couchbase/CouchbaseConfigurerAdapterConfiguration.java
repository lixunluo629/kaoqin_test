package org.springframework.boot.autoconfigure.data.couchbase;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.couchbase.CouchbaseAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.couchbase.config.CouchbaseConfigurer;

@Configuration
@ConditionalOnClass({CouchbaseConfigurer.class})
@ConditionalOnBean({CouchbaseAutoConfiguration.CouchbaseConfiguration.class})
/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/data/couchbase/CouchbaseConfigurerAdapterConfiguration.class */
class CouchbaseConfigurerAdapterConfiguration {
    private final CouchbaseAutoConfiguration.CouchbaseConfiguration configuration;

    CouchbaseConfigurerAdapterConfiguration(CouchbaseAutoConfiguration.CouchbaseConfiguration configuration) {
        this.configuration = configuration;
    }

    @ConditionalOnMissingBean
    @Bean
    public CouchbaseConfigurer springBootCouchbaseConfigurer() throws Exception {
        return new SpringBootCouchbaseConfigurer(this.configuration.couchbaseEnvironment(), this.configuration.couchbaseCluster(), this.configuration.couchbaseClusterInfo(), this.configuration.couchbaseClient());
    }
}
