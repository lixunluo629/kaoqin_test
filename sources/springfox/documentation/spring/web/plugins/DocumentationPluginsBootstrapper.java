package springfox.documentation.spring.web.plugins;

import com.fasterxml.classmate.TypeResolver;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.servlet.ServletContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.DocumentationPlugin;
import springfox.documentation.spi.service.RequestHandlerProvider;
import springfox.documentation.spi.service.contexts.Defaults;
import springfox.documentation.spi.service.contexts.DocumentationContext;
import springfox.documentation.spi.service.contexts.DocumentationContextBuilder;
import springfox.documentation.spi.service.contexts.Orderings;
import springfox.documentation.spring.web.DocumentationCache;
import springfox.documentation.spring.web.scanners.ApiDocumentationScanner;

@Component
/* loaded from: springfox-spring-web-2.2.2.jar:springfox/documentation/spring/web/plugins/DocumentationPluginsBootstrapper.class */
public class DocumentationPluginsBootstrapper implements ApplicationListener<ContextRefreshedEvent> {
    private static final Logger log = LoggerFactory.getLogger((Class<?>) DocumentationPluginsBootstrapper.class);
    private final DocumentationPluginsManager documentationPluginsManager;
    private final RequestHandlerProvider handlerProvider;
    private final DocumentationCache scanned;
    private final ApiDocumentationScanner resourceListing;
    private final DefaultConfiguration defaultConfiguration;
    private AtomicBoolean initialized = new AtomicBoolean(false);

    @Autowired
    public DocumentationPluginsBootstrapper(DocumentationPluginsManager documentationPluginsManager, RequestHandlerProvider handlerProvider, DocumentationCache scanned, ApiDocumentationScanner resourceListing, TypeResolver typeResolver, Defaults defaults, ServletContext servletContext) {
        this.documentationPluginsManager = documentationPluginsManager;
        this.handlerProvider = handlerProvider;
        this.scanned = scanned;
        this.resourceListing = resourceListing;
        this.defaultConfiguration = new DefaultConfiguration(defaults, typeResolver, servletContext);
    }

    @Override // org.springframework.context.ApplicationListener
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        if (this.initialized.compareAndSet(false, true)) {
            log.info("Context refreshed");
            List<E> listSortedCopy = Orderings.pluginOrdering().sortedCopy(this.documentationPluginsManager.documentationPlugins());
            log.info("Found {0} custom documentation plugin(s)", Integer.valueOf(listSortedCopy.size()));
            for (E each : listSortedCopy) {
                DocumentationType documentationType = each.getDocumentationType();
                if (each.isEnabled()) {
                    scanDocumentation(buildContext(each));
                } else {
                    log.info("Skipping initializing disabled plugin bean {} v{}", documentationType.getName(), documentationType.getVersion());
                }
            }
        }
    }

    private DocumentationContext buildContext(DocumentationPlugin each) {
        return each.configure(defaultContextBuilder(each));
    }

    private void scanDocumentation(DocumentationContext context) {
        this.scanned.addDocumentation(this.resourceListing.scan(context));
    }

    private DocumentationContextBuilder defaultContextBuilder(DocumentationPlugin each) {
        DocumentationType documentationType = each.getDocumentationType();
        return this.documentationPluginsManager.createContextBuilder(documentationType, this.defaultConfiguration).requestHandlers(this.handlerProvider.requestHandlers());
    }
}
