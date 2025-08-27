package org.springframework.data.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.data.geo.GeoModule;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/web/config/SpringDataJacksonConfiguration.class */
public class SpringDataJacksonConfiguration implements SpringDataJacksonModules {
    @Bean
    public GeoModule jacksonGeoModule() {
        return new GeoModule();
    }
}
