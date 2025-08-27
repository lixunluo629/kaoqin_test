package org.springframework.boot.autoconfigure.couchbase;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.CouchbaseBucket;
import com.couchbase.client.java.CouchbaseCluster;
import com.couchbase.client.java.cluster.ClusterInfo;
import com.couchbase.client.java.env.DefaultCouchbaseEnvironment;
import org.springframework.boot.autoconfigure.condition.AnyNestedCondition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.couchbase.CouchbaseProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ConfigurationCondition;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;

@EnableConfigurationProperties({CouchbaseProperties.class})
@Configuration
@ConditionalOnClass({CouchbaseBucket.class, Cluster.class})
@Conditional({CouchbaseCondition.class})
/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/couchbase/CouchbaseAutoConfiguration.class */
public class CouchbaseAutoConfiguration {

    @ConditionalOnMissingBean(value = {CouchbaseConfiguration.class}, type = {"org.springframework.data.couchbase.config.CouchbaseConfigurer"})
    @Configuration
    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/couchbase/CouchbaseAutoConfiguration$CouchbaseConfiguration.class */
    public static class CouchbaseConfiguration {
        private final CouchbaseProperties properties;

        public CouchbaseConfiguration(CouchbaseProperties properties) {
            this.properties = properties;
        }

        @Bean
        @Primary
        public DefaultCouchbaseEnvironment couchbaseEnvironment() throws Exception {
            return initializeEnvironmentBuilder(this.properties).build();
        }

        @Bean
        @Primary
        public Cluster couchbaseCluster() throws Exception {
            return CouchbaseCluster.create(couchbaseEnvironment(), this.properties.getBootstrapHosts());
        }

        @DependsOn({"couchbaseClient"})
        @Bean
        @Primary
        public ClusterInfo couchbaseClusterInfo() throws Exception {
            return couchbaseCluster().clusterManager(this.properties.getBucket().getName(), this.properties.getBucket().getPassword()).info();
        }

        @Bean
        @Primary
        public Bucket couchbaseClient() throws Exception {
            return couchbaseCluster().openBucket(this.properties.getBucket().getName(), this.properties.getBucket().getPassword());
        }

        protected DefaultCouchbaseEnvironment.Builder initializeEnvironmentBuilder(CouchbaseProperties properties) {
            CouchbaseProperties.Endpoints endpoints = properties.getEnv().getEndpoints();
            CouchbaseProperties.Timeouts timeouts = properties.getEnv().getTimeouts();
            DefaultCouchbaseEnvironment.Builder builder = DefaultCouchbaseEnvironment.builder().connectTimeout(timeouts.getConnect()).kvEndpoints(endpoints.getKeyValue()).kvTimeout(timeouts.getKeyValue()).queryEndpoints(endpoints.getQuery()).queryTimeout(timeouts.getQuery()).viewEndpoints(endpoints.getView()).socketConnectTimeout(timeouts.getSocketConnect()).viewTimeout(timeouts.getView());
            CouchbaseProperties.Ssl ssl = properties.getEnv().getSsl();
            if (ssl.getEnabled().booleanValue()) {
                builder.sslEnabled(true);
                if (ssl.getKeyStore() != null) {
                    builder.sslKeystoreFile(ssl.getKeyStore());
                }
                if (ssl.getKeyStorePassword() != null) {
                    builder.sslKeystorePassword(ssl.getKeyStorePassword());
                }
            }
            return builder;
        }
    }

    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/couchbase/CouchbaseAutoConfiguration$CouchbaseCondition.class */
    static class CouchbaseCondition extends AnyNestedCondition {
        CouchbaseCondition() {
            super(ConfigurationCondition.ConfigurationPhase.REGISTER_BEAN);
        }

        @Conditional({OnBootstrapHostsCondition.class})
        /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/couchbase/CouchbaseAutoConfiguration$CouchbaseCondition$BootstrapHostsProperty.class */
        static class BootstrapHostsProperty {
            BootstrapHostsProperty() {
            }
        }

        @ConditionalOnBean(type = {"org.springframework.data.couchbase.config.CouchbaseConfigurer"})
        /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/couchbase/CouchbaseAutoConfiguration$CouchbaseCondition$CouchbaseConfigurerAvailable.class */
        static class CouchbaseConfigurerAvailable {
            CouchbaseConfigurerAvailable() {
            }
        }
    }
}
