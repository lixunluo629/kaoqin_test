package springfox.documentation.schema;

import com.google.common.base.Optional;
import java.util.Map;
import springfox.documentation.spi.schema.contexts.ModelContext;

/* loaded from: springfox-schema-2.2.2.jar:springfox/documentation/schema/ModelProvider.class */
public interface ModelProvider {
    Optional<Model> modelFor(ModelContext modelContext);

    Map<String, Model> dependencies(ModelContext modelContext);
}
