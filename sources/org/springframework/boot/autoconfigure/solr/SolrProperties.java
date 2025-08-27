package org.springframework.boot.autoconfigure.solr;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.data.solr")
/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/solr/SolrProperties.class */
public class SolrProperties {
    private String host = "http://127.0.0.1:8983/solr";
    private String zkHost;

    public String getHost() {
        return this.host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getZkHost() {
        return this.zkHost;
    }

    public void setZkHost(String zkHost) {
        this.zkHost = zkHost;
    }
}
