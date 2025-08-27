package springfox.documentation.spi.schema;

import org.springframework.plugin.core.Plugin;
import springfox.documentation.spi.DocumentationType;

/* loaded from: springfox-spi-2.2.2.jar:springfox/documentation/spi/schema/TypeNameProviderPlugin.class */
public interface TypeNameProviderPlugin extends Plugin<DocumentationType> {
    String nameFor(Class<?> cls);
}
