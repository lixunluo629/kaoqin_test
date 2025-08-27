package springfox.documentation.spring.web.plugins;

import com.fasterxml.classmate.TypeResolver;
import javax.servlet.ServletContext;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.DefaultsProviderPlugin;
import springfox.documentation.spi.service.contexts.ApiSelector;
import springfox.documentation.spi.service.contexts.Defaults;
import springfox.documentation.spi.service.contexts.DocumentationContextBuilder;
import springfox.documentation.spring.web.paths.RelativePathProvider;

/* loaded from: springfox-spring-web-2.2.2.jar:springfox/documentation/spring/web/plugins/DefaultConfiguration.class */
public class DefaultConfiguration implements DefaultsProviderPlugin {
    private final Defaults defaults;
    private final TypeResolver typeResolver;
    private final ServletContext servletContext;

    public DefaultConfiguration(Defaults defaults, TypeResolver typeResolver, ServletContext servletContext) {
        this.servletContext = servletContext;
        this.defaults = defaults;
        this.typeResolver = typeResolver;
    }

    @Override // springfox.documentation.spi.service.DefaultsProviderPlugin
    public DocumentationContextBuilder create(DocumentationType documentationType) {
        return new DocumentationContextBuilder(documentationType).operationOrdering(this.defaults.operationOrdering()).apiDescriptionOrdering(this.defaults.apiDescriptionOrdering()).apiListingReferenceOrdering(this.defaults.apiListingReferenceOrdering()).additionalIgnorableTypes(this.defaults.defaultIgnorableParameterTypes()).rules(this.defaults.defaultRules(this.typeResolver)).defaultResponseMessages(this.defaults.defaultResponseMessages()).pathProvider(new RelativePathProvider(this.servletContext)).typeResolver(this.typeResolver).enableUrlTemplating(false).selector(ApiSelector.DEFAULT);
    }

    @Override // org.springframework.plugin.core.Plugin
    public boolean supports(DocumentationType delimiter) {
        return true;
    }
}
