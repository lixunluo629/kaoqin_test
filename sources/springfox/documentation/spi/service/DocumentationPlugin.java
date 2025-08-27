package springfox.documentation.spi.service;

import org.springframework.plugin.core.Plugin;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.DocumentationContext;
import springfox.documentation.spi.service.contexts.DocumentationContextBuilder;

/* loaded from: springfox-spi-2.2.2.jar:springfox/documentation/spi/service/DocumentationPlugin.class */
public interface DocumentationPlugin extends Plugin<DocumentationType> {
    boolean isEnabled();

    DocumentationType getDocumentationType();

    DocumentationContext configure(DocumentationContextBuilder documentationContextBuilder);

    String getGroupName();
}
