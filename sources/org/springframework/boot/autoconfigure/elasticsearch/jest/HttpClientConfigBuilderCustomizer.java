package org.springframework.boot.autoconfigure.elasticsearch.jest;

import io.searchbox.client.config.HttpClientConfig;

/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/elasticsearch/jest/HttpClientConfigBuilderCustomizer.class */
public interface HttpClientConfigBuilderCustomizer {
    void customize(HttpClientConfig.Builder builder);
}
