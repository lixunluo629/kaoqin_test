package springfox.documentation.spi.service;

import org.springframework.plugin.core.Plugin;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.ApiListingContext;

/* loaded from: springfox-spi-2.2.2.jar:springfox/documentation/spi/service/ApiListingBuilderPlugin.class */
public interface ApiListingBuilderPlugin extends Plugin<DocumentationType> {
    void apply(ApiListingContext apiListingContext);
}
