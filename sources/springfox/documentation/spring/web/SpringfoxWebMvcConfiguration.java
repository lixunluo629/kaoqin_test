package springfox.documentation.spring.web;

import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.plugin.core.config.EnablePluginRegistries;
import springfox.documentation.schema.configuration.ModelsConfiguration;
import springfox.documentation.service.PathDecorator;
import springfox.documentation.spi.service.ApiListingBuilderPlugin;
import springfox.documentation.spi.service.DefaultsProviderPlugin;
import springfox.documentation.spi.service.DocumentationPlugin;
import springfox.documentation.spi.service.ExpandedParameterBuilderPlugin;
import springfox.documentation.spi.service.OperationBuilderPlugin;
import springfox.documentation.spi.service.OperationModelsProviderPlugin;
import springfox.documentation.spi.service.ParameterBuilderPlugin;
import springfox.documentation.spi.service.ResourceGroupingStrategy;
import springfox.documentation.spi.service.contexts.Defaults;
import springfox.documentation.spring.web.json.JacksonModuleRegistrar;
import springfox.documentation.spring.web.json.JsonSerializer;

@EnableAspectJAutoProxy
@Configuration
@EnablePluginRegistries({DocumentationPlugin.class, ApiListingBuilderPlugin.class, OperationBuilderPlugin.class, ParameterBuilderPlugin.class, ExpandedParameterBuilderPlugin.class, ResourceGroupingStrategy.class, OperationModelsProviderPlugin.class, DefaultsProviderPlugin.class, PathDecorator.class})
@Import({ModelsConfiguration.class})
@ComponentScan(basePackages = {"springfox.documentation.spring.web.scanners", "springfox.documentation.spring.web.readers.operation", "springfox.documentation.spring.web.readers.parameter", "springfox.documentation.spring.web.plugins", "springfox.documentation.spring.web.paths", "springfox.documentation.spring.web.caching"})
/* loaded from: springfox-spring-web-2.2.2.jar:springfox/documentation/spring/web/SpringfoxWebMvcConfiguration.class */
public class SpringfoxWebMvcConfiguration {
    @Bean
    public Defaults defaults() {
        return new Defaults();
    }

    @Bean
    public DocumentationCache resourceGroupCache() {
        return new DocumentationCache();
    }

    @Bean
    public static ObjectMapperConfigurer objectMapperConfigurer() {
        return new ObjectMapperConfigurer();
    }

    @Bean
    public JsonSerializer jsonSerializer(List<JacksonModuleRegistrar> moduleRegistrars) {
        return new JsonSerializer(moduleRegistrars);
    }
}
