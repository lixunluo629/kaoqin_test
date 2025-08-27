package springfox.documentation.spi.service;

import org.springframework.plugin.core.Plugin;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.DocumentationContextBuilder;

/* loaded from: springfox-spi-2.2.2.jar:springfox/documentation/spi/service/DefaultsProviderPlugin.class */
public interface DefaultsProviderPlugin extends Plugin<DocumentationType> {
    DocumentationContextBuilder create(DocumentationType documentationType);
}
