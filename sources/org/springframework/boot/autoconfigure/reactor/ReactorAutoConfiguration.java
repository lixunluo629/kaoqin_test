package org.springframework.boot.autoconfigure.reactor;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.Environment;
import reactor.bus.EventBus;
import reactor.spring.context.config.EnableReactor;

@Configuration
@ConditionalOnClass({EnableReactor.class, Environment.class})
@AutoConfigureAfter({WebMvcAutoConfiguration.class})
/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/reactor/ReactorAutoConfiguration.class */
public class ReactorAutoConfiguration {
    @ConditionalOnMissingBean({EventBus.class})
    @Bean
    public EventBus eventBus(Environment environment) {
        return EventBus.create(environment);
    }

    @ConditionalOnMissingBean({Environment.class})
    @EnableReactor
    @Configuration
    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/reactor/ReactorAutoConfiguration$ReactorConfiguration.class */
    protected static class ReactorConfiguration {
        protected ReactorConfiguration() {
        }
    }
}
