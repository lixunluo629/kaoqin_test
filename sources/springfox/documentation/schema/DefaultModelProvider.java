package springfox.documentation.schema;

import com.fasterxml.classmate.ResolvedType;
import com.fasterxml.classmate.TypeResolver;
import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import springfox.documentation.annotations.Cacheable;
import springfox.documentation.schema.plugins.SchemaPluginsManager;
import springfox.documentation.schema.property.ModelPropertiesProvider;
import springfox.documentation.spi.schema.contexts.ModelContext;

@Component
/* loaded from: springfox-schema-2.2.2.jar:springfox/documentation/schema/DefaultModelProvider.class */
public class DefaultModelProvider implements ModelProvider {
    private static final Logger LOG = LoggerFactory.getLogger((Class<?>) DefaultModelProvider.class);
    private final TypeResolver resolver;
    private final ModelPropertiesProvider propertiesProvider;
    private final ModelDependencyProvider dependencyProvider;
    private final SchemaPluginsManager schemaPluginsManager;
    private final TypeNameExtractor typeNameExtractor;

    @Autowired
    public DefaultModelProvider(TypeResolver resolver, ModelPropertiesProvider propertiesProvider, ModelDependencyProvider dependencyProvider, SchemaPluginsManager schemaPluginsManager, TypeNameExtractor typeNameExtractor) {
        this.resolver = resolver;
        this.propertiesProvider = propertiesProvider;
        this.dependencyProvider = dependencyProvider;
        this.schemaPluginsManager = schemaPluginsManager;
        this.typeNameExtractor = typeNameExtractor;
    }

    @Override // springfox.documentation.schema.ModelProvider
    @Cacheable(value = "models", keyGenerator = ModelContextKeyGenerator.class)
    public Optional<Model> modelFor(ModelContext modelContext) {
        ResolvedType propertiesHost = modelContext.alternateFor(modelContext.resolvedType(this.resolver));
        if (Collections.isContainerType(propertiesHost) || Maps.isMapType(propertiesHost) || propertiesHost.getErasedType().isEnum() || Types.isBaseType(Types.typeNameFor(propertiesHost.getErasedType())) || modelContext.hasSeenBefore(propertiesHost)) {
            LOG.debug("Skipping model of type {} as its either a container type, map, enum or base type, or its already been handled", ResolvedTypes.resolvedTypeSignature(propertiesHost).or((Optional<String>) "<null>"));
            return Optional.absent();
        }
        ImmutableMap<String, ModelProperty> propertiesIndex = com.google.common.collect.Maps.uniqueIndex(properties(modelContext, propertiesHost), byPropertyName());
        LOG.debug("Inferred {} properties. Properties found {}", Integer.valueOf(propertiesIndex.size()), Joiner.on(", ").join(propertiesIndex.keySet()));
        Map<String, ModelProperty> properties = com.google.common.collect.Maps.newTreeMap();
        properties.putAll(propertiesIndex);
        return Optional.of(modelBuilder(propertiesHost, properties, modelContext));
    }

    private Model modelBuilder(ResolvedType propertiesHost, Map<String, ModelProperty> properties, ModelContext modelContext) {
        String typeName = this.typeNameExtractor.typeName(ModelContext.fromParent(modelContext, propertiesHost));
        modelContext.getBuilder().id(typeName).type(propertiesHost).name(typeName).qualifiedType(ResolvedTypes.simpleQualifiedTypeName(propertiesHost)).properties(properties).description("").baseModel("").discriminator("").subTypes(new ArrayList());
        return this.schemaPluginsManager.model(modelContext);
    }

    @Override // springfox.documentation.schema.ModelProvider
    public Map<String, Model> dependencies(ModelContext modelContext) {
        Map<String, Model> models = com.google.common.collect.Maps.newHashMap();
        for (ResolvedType resolvedType : this.dependencyProvider.dependentModels(modelContext)) {
            ModelContext parentContext = ModelContext.fromParent(modelContext, resolvedType);
            Optional<Model> model = modelFor(parentContext).or(mapModel(parentContext, resolvedType));
            if (model.isPresent()) {
                models.put(model.get().getName(), model.get());
            }
        }
        return models;
    }

    private Optional<Model> mapModel(ModelContext parentContext, ResolvedType resolvedType) {
        if (Maps.isMapType(resolvedType) && !parentContext.hasSeenBefore(resolvedType)) {
            String typeName = this.typeNameExtractor.typeName(parentContext);
            return Optional.of(parentContext.getBuilder().id(typeName).type(resolvedType).name(typeName).qualifiedType(ResolvedTypes.simpleQualifiedTypeName(resolvedType)).properties(new HashMap()).description("").baseModel("").discriminator("").subTypes(new ArrayList()).build());
        }
        return Optional.absent();
    }

    private Function<ModelProperty, String> byPropertyName() {
        return new Function<ModelProperty, String>() { // from class: springfox.documentation.schema.DefaultModelProvider.1
            @Override // com.google.common.base.Function
            public String apply(ModelProperty input) {
                return input.getName();
            }
        };
    }

    private List<ModelProperty> properties(ModelContext context, ResolvedType propertiesHost) {
        return this.propertiesProvider.propertiesFor(propertiesHost, context);
    }
}
