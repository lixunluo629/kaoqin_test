package org.springframework.boot.autoconfigure.hazelcast;

import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import java.io.IOException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnSingleCandidate;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

@EnableConfigurationProperties({HazelcastProperties.class})
@Configuration
@ConditionalOnClass({HazelcastInstance.class})
@ConditionalOnMissingBean({HazelcastInstance.class})
/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/hazelcast/HazelcastAutoConfiguration.class */
public class HazelcastAutoConfiguration {

    @ConditionalOnMissingBean({Config.class})
    @Configuration
    @Conditional({ConfigAvailableCondition.class})
    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/hazelcast/HazelcastAutoConfiguration$HazelcastConfigFileConfiguration.class */
    static class HazelcastConfigFileConfiguration {
        private final HazelcastProperties hazelcastProperties;

        HazelcastConfigFileConfiguration(HazelcastProperties hazelcastProperties) {
            this.hazelcastProperties = hazelcastProperties;
        }

        @Bean
        public HazelcastInstance hazelcastInstance() throws IOException {
            Resource config = this.hazelcastProperties.resolveConfigLocation();
            if (config != null) {
                return new HazelcastInstanceFactory(config).getHazelcastInstance();
            }
            return Hazelcast.newHazelcastInstance();
        }
    }

    @Configuration
    @ConditionalOnSingleCandidate(Config.class)
    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/hazelcast/HazelcastAutoConfiguration$HazelcastConfigConfiguration.class */
    static class HazelcastConfigConfiguration {
        HazelcastConfigConfiguration() {
        }

        @Bean
        public HazelcastInstance hazelcastInstance(Config config) {
            return new HazelcastInstanceFactory(config).getHazelcastInstance();
        }
    }

    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/hazelcast/HazelcastAutoConfiguration$ConfigAvailableCondition.class */
    static class ConfigAvailableCondition extends HazelcastConfigResourceCondition {
        ConfigAvailableCondition() {
            super("spring.hazelcast", "config");
        }
    }
}
