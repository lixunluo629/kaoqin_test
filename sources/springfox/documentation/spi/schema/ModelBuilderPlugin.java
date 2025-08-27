package springfox.documentation.spi.schema;

import org.springframework.plugin.core.Plugin;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.schema.contexts.ModelContext;

/* loaded from: springfox-spi-2.2.2.jar:springfox/documentation/spi/schema/ModelBuilderPlugin.class */
public interface ModelBuilderPlugin extends Plugin<DocumentationType> {
    void apply(ModelContext modelContext);
}
