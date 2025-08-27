package springfox.documentation.schema.property;

import com.fasterxml.classmate.ResolvedType;
import java.util.List;
import org.springframework.context.ApplicationListener;
import springfox.documentation.schema.configuration.ObjectMapperConfigured;
import springfox.documentation.spi.schema.contexts.ModelContext;

/* loaded from: springfox-schema-2.2.2.jar:springfox/documentation/schema/property/ModelPropertiesProvider.class */
public interface ModelPropertiesProvider extends ApplicationListener<ObjectMapperConfigured> {
    List<springfox.documentation.schema.ModelProperty> propertiesFor(ResolvedType resolvedType, ModelContext modelContext);
}
