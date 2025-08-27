package org.springframework.boot.web.support;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
/* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/web/support/ErrorPageFilterConfiguration.class */
class ErrorPageFilterConfiguration {
    ErrorPageFilterConfiguration() {
    }

    @Bean
    public ErrorPageFilter errorPageFilter() {
        return new ErrorPageFilter();
    }
}
