package springfox.documentation.swagger.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

@Configuration
@ComponentScan(basePackages = {"springfox.documentation.swagger.schema", "springfox.documentation.swagger.readers", "springfox.documentation.swagger.web"})
/* loaded from: springfox-swagger-common-2.2.2.jar:springfox/documentation/swagger/configuration/SwaggerCommonConfiguration.class */
public class SwaggerCommonConfiguration {
    @Bean
    public static PropertySourcesPlaceholderConfigurer swaggerProperties() {
        PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer = new PropertySourcesPlaceholderConfigurer();
        propertySourcesPlaceholderConfigurer.setIgnoreUnresolvablePlaceholders(true);
        return propertySourcesPlaceholderConfigurer;
    }
}
