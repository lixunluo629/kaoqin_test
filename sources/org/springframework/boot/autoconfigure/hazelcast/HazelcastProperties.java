package org.springframework.boot.autoconfigure.hazelcast;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

@ConfigurationProperties(prefix = "spring.hazelcast")
/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/hazelcast/HazelcastProperties.class */
public class HazelcastProperties {
    private Resource config;

    public Resource getConfig() {
        return this.config;
    }

    public void setConfig(Resource config) {
        this.config = config;
    }

    public Resource resolveConfigLocation() {
        if (this.config == null) {
            return null;
        }
        Assert.isTrue(this.config.exists(), "Hazelcast configuration does not exist '" + this.config.getDescription() + "'");
        return this.config;
    }
}
