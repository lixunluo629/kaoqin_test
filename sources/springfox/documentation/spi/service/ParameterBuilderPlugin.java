package springfox.documentation.spi.service;

import org.springframework.plugin.core.Plugin;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.ParameterContext;

/* loaded from: springfox-spi-2.2.2.jar:springfox/documentation/spi/service/ParameterBuilderPlugin.class */
public interface ParameterBuilderPlugin extends Plugin<DocumentationType> {
    void apply(ParameterContext parameterContext);
}
