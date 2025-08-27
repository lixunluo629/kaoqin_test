package springfox.documentation.spi.service;

import org.springframework.plugin.core.Plugin;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.ParameterExpansionContext;

/* loaded from: springfox-spi-2.2.2.jar:springfox/documentation/spi/service/ExpandedParameterBuilderPlugin.class */
public interface ExpandedParameterBuilderPlugin extends Plugin<DocumentationType> {
    void apply(ParameterExpansionContext parameterExpansionContext);
}
