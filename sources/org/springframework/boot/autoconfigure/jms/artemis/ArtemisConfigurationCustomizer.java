package org.springframework.boot.autoconfigure.jms.artemis;

import org.apache.activemq.artemis.core.config.Configuration;

/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/jms/artemis/ArtemisConfigurationCustomizer.class */
public interface ArtemisConfigurationCustomizer {
    void customize(Configuration configuration);
}
