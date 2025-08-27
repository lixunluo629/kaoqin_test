package org.springframework.boot.autoconfigure.cassandra;

import com.datastax.driver.core.Cluster;

/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/cassandra/ClusterBuilderCustomizer.class */
public interface ClusterBuilderCustomizer {
    void customize(Cluster.Builder builder);
}
