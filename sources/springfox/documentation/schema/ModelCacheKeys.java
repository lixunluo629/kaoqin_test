package springfox.documentation.schema;

import com.fasterxml.classmate.ResolvedType;
import com.fasterxml.classmate.TypeResolver;
import springfox.documentation.schema.property.ModelPropertiesKeyGenerator;
import springfox.documentation.schema.property.OptimizedModelPropertiesProvider;
import springfox.documentation.spi.schema.contexts.ModelContext;

/* loaded from: springfox-schema-2.2.2.jar:springfox/documentation/schema/ModelCacheKeys.class */
public class ModelCacheKeys {
    public static final String MODEL_CONTEXT_SPEL = "T(springfox.documentation.schema.ModelCacheKeys).modelContextKey(#modelContext)";
    public static final String MODEL_PROPERTIES_SPEL = "T(springfox.documentation.schema.ModelCacheKeys).modelPropertiesKey(#type, #givenContext)";

    private ModelCacheKeys() {
        throw new UnsupportedOperationException();
    }

    public static String modelContextKey(ModelContext givenContext) {
        return new ModelContextKeyGenerator(new TypeResolver()).generate(DefaultModelProvider.class, null, givenContext).toString();
    }

    public static String modelPropertiesKey(ResolvedType type, ModelContext givenContext) {
        return new ModelPropertiesKeyGenerator().generate(OptimizedModelPropertiesProvider.class, null, type, givenContext).toString();
    }
}
