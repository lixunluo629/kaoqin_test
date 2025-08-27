package springfox.documentation.schema;

import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.schema.TypeNameProviderPlugin;

/* loaded from: springfox-schema-2.2.2.jar:springfox/documentation/schema/DefaultTypeNameProvider.class */
public class DefaultTypeNameProvider implements TypeNameProviderPlugin {
    @Override // org.springframework.plugin.core.Plugin
    public boolean supports(DocumentationType delimiter) {
        return true;
    }

    @Override // springfox.documentation.spi.schema.TypeNameProviderPlugin
    public String nameFor(Class<?> type) {
        return type.getSimpleName();
    }
}
