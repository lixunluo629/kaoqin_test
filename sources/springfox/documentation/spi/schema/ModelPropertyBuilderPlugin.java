package springfox.documentation.spi.schema;

import org.springframework.plugin.core.Plugin;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.schema.contexts.ModelPropertyContext;

/* loaded from: springfox-spi-2.2.2.jar:springfox/documentation/spi/schema/ModelPropertyBuilderPlugin.class */
public interface ModelPropertyBuilderPlugin extends Plugin<DocumentationType> {
    void apply(ModelPropertyContext modelPropertyContext);
}
