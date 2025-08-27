package springfox.documentation.schema.configuration;

import com.fasterxml.classmate.TypeResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.plugin.core.config.EnablePluginRegistries;
import springfox.documentation.spi.schema.ModelBuilderPlugin;
import springfox.documentation.spi.schema.ModelPropertyBuilderPlugin;
import springfox.documentation.spi.schema.TypeNameProviderPlugin;

@Configuration
@EnablePluginRegistries({ModelBuilderPlugin.class, ModelPropertyBuilderPlugin.class, TypeNameProviderPlugin.class})
@ComponentScan(basePackages = {"springfox.documentation.schema"})
/* loaded from: springfox-schema-2.2.2.jar:springfox/documentation/schema/configuration/ModelsConfiguration.class */
public class ModelsConfiguration {
    @Bean
    public TypeResolver typeResolver() {
        return new TypeResolver();
    }
}
