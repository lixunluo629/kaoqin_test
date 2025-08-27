package org.springframework.boot.autoconfigure.data.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

@Order(0)
/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/data/rest/SpringBootRepositoryRestConfigurer.class */
class SpringBootRepositoryRestConfigurer extends RepositoryRestConfigurerAdapter {

    @Autowired(required = false)
    private Jackson2ObjectMapperBuilder objectMapperBuilder;

    @Autowired
    private RepositoryRestProperties properties;

    SpringBootRepositoryRestConfigurer() {
    }

    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
        this.properties.applyTo(config);
    }

    public void configureJacksonObjectMapper(ObjectMapper objectMapper) {
        if (this.objectMapperBuilder != null) {
            this.objectMapperBuilder.configure(objectMapper);
        }
    }
}
