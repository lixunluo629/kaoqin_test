package org.springframework.boot.autoconfigure.cassandra;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.QueryOptions;
import com.datastax.driver.core.SocketOptions;
import com.datastax.driver.core.policies.LoadBalancingPolicy;
import com.datastax.driver.core.policies.ReconnectionPolicy;
import com.datastax.driver.core.policies.RetryPolicy;
import java.util.List;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

@EnableConfigurationProperties({CassandraProperties.class})
@Configuration
@ConditionalOnClass({Cluster.class})
/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/cassandra/CassandraAutoConfiguration.class */
public class CassandraAutoConfiguration {
    private final CassandraProperties properties;
    private final List<ClusterBuilderCustomizer> builderCustomizers;

    public CassandraAutoConfiguration(CassandraProperties properties, ObjectProvider<List<ClusterBuilderCustomizer>> builderCustomizers) {
        this.properties = properties;
        this.builderCustomizers = builderCustomizers.getIfAvailable();
    }

    @ConditionalOnMissingBean
    @Bean
    public Cluster cluster() {
        CassandraProperties properties = this.properties;
        Cluster.Builder builder = Cluster.builder().withClusterName(properties.getClusterName()).withPort(properties.getPort());
        if (properties.getUsername() != null) {
            builder.withCredentials(properties.getUsername(), properties.getPassword());
        }
        if (properties.getCompression() != null) {
            builder.withCompression(properties.getCompression());
        }
        if (properties.getLoadBalancingPolicy() != null) {
            LoadBalancingPolicy policy = (LoadBalancingPolicy) instantiate(properties.getLoadBalancingPolicy());
            builder.withLoadBalancingPolicy(policy);
        }
        builder.withQueryOptions(getQueryOptions());
        if (properties.getReconnectionPolicy() != null) {
            ReconnectionPolicy policy2 = (ReconnectionPolicy) instantiate(properties.getReconnectionPolicy());
            builder.withReconnectionPolicy(policy2);
        }
        if (properties.getRetryPolicy() != null) {
            RetryPolicy policy3 = (RetryPolicy) instantiate(properties.getRetryPolicy());
            builder.withRetryPolicy(policy3);
        }
        builder.withSocketOptions(getSocketOptions());
        if (properties.isSsl()) {
            builder.withSSL();
        }
        String points = properties.getContactPoints();
        builder.addContactPoints(StringUtils.commaDelimitedListToStringArray(points));
        customize(builder);
        return builder.build();
    }

    private void customize(Cluster.Builder builder) {
        if (this.builderCustomizers != null) {
            for (ClusterBuilderCustomizer customizer : this.builderCustomizers) {
                customizer.customize(builder);
            }
        }
    }

    public static <T> T instantiate(Class<T> cls) {
        return (T) BeanUtils.instantiate(cls);
    }

    private QueryOptions getQueryOptions() {
        QueryOptions options = new QueryOptions();
        CassandraProperties properties = this.properties;
        if (properties.getConsistencyLevel() != null) {
            options.setConsistencyLevel(properties.getConsistencyLevel());
        }
        if (properties.getSerialConsistencyLevel() != null) {
            options.setSerialConsistencyLevel(properties.getSerialConsistencyLevel());
        }
        options.setFetchSize(properties.getFetchSize());
        return options;
    }

    private SocketOptions getSocketOptions() {
        SocketOptions options = new SocketOptions();
        options.setConnectTimeoutMillis(this.properties.getConnectTimeoutMillis());
        options.setReadTimeoutMillis(this.properties.getReadTimeoutMillis());
        return options;
    }
}
