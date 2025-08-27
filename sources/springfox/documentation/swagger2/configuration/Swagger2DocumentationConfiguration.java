package springfox.documentation.swagger2.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import springfox.documentation.spring.web.SpringfoxWebMvcConfiguration;
import springfox.documentation.spring.web.json.JacksonModuleRegistrar;
import springfox.documentation.swagger.configuration.SwaggerCommonConfiguration;

@Configuration
@Import({SpringfoxWebMvcConfiguration.class, SwaggerCommonConfiguration.class})
@ComponentScan(basePackages = {"springfox.documentation.swagger2.readers.parameter", "springfox.documentation.swagger2.web", "springfox.documentation.swagger2.mappers"})
/* loaded from: springfox-swagger2-2.2.2.jar:springfox/documentation/swagger2/configuration/Swagger2DocumentationConfiguration.class */
public class Swagger2DocumentationConfiguration {
    @Bean
    public JacksonModuleRegistrar swagger2Module() {
        return new Swagger2JacksonModule();
    }
}
