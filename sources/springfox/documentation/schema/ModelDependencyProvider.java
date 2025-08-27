package springfox.documentation.schema;

import com.fasterxml.classmate.ResolvedType;
import com.fasterxml.classmate.TypeResolver;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import springfox.documentation.annotations.Cacheable;
import springfox.documentation.schema.property.ModelPropertiesProvider;
import springfox.documentation.spi.schema.contexts.ModelContext;

@Component
/* loaded from: springfox-schema-2.2.2.jar:springfox/documentation/schema/ModelDependencyProvider.class */
public class ModelDependencyProvider {
    private static final Logger LOG = LoggerFactory.getLogger((Class<?>) ModelDependencyProvider.class);
    private final TypeResolver typeResolver;
    private final ModelPropertiesProvider propertiesProvider;
    private final TypeNameExtractor nameExtractor;

    @Autowired
    public ModelDependencyProvider(TypeResolver typeResolver, ModelPropertiesProvider propertiesProvider, TypeNameExtractor nameExtractor) {
        this.typeResolver = typeResolver;
        this.propertiesProvider = propertiesProvider;
        this.nameExtractor = nameExtractor;
    }

    @Cacheable(value = "modelDependencies", keyGenerator = ModelContextKeyGenerator.class)
    public Set<ResolvedType> dependentModels(ModelContext modelContext) {
        return FluentIterable.from(resolvedDependencies(modelContext)).filter(ignorableTypes(modelContext)).filter(Predicates.not(baseTypes(modelContext))).toSet();
    }

    private Predicate<ResolvedType> baseTypes(final ModelContext modelContext) {
        return new Predicate<ResolvedType>() { // from class: springfox.documentation.schema.ModelDependencyProvider.1
            @Override // com.google.common.base.Predicate
            public boolean apply(ResolvedType resolvedType) {
                return ModelDependencyProvider.this.isBaseType(ModelContext.fromParent(modelContext, resolvedType));
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isBaseType(ModelContext modelContext) {
        String typeName = this.nameExtractor.typeName(modelContext);
        return Types.isBaseType(typeName);
    }

    private Predicate<ResolvedType> ignorableTypes(final ModelContext modelContext) {
        return new Predicate<ResolvedType>() { // from class: springfox.documentation.schema.ModelDependencyProvider.2
            @Override // com.google.common.base.Predicate
            public boolean apply(ResolvedType input) {
                return !modelContext.hasSeenBefore(input);
            }
        };
    }

    private List<ResolvedType> resolvedDependencies(ModelContext modelContext) {
        ResolvedType resolvedType = modelContext.alternateFor(modelContext.resolvedType(this.typeResolver));
        if (isBaseType(ModelContext.fromParent(modelContext, resolvedType))) {
            LOG.debug("Marking base type {} as seen", resolvedType.getSignature());
            modelContext.seen(resolvedType);
            return Lists.newArrayList();
        }
        List<ResolvedType> dependencies = Lists.newArrayList(resolvedTypeParameters(modelContext, resolvedType));
        dependencies.addAll(resolvedPropertiesAndFields(modelContext, resolvedType));
        return dependencies;
    }

    private List<? extends ResolvedType> resolvedTypeParameters(ModelContext modelContext, ResolvedType resolvedType) {
        List<ResolvedType> parameters = Lists.newArrayList();
        for (ResolvedType parameter : resolvedType.getTypeParameters()) {
            LOG.debug("Adding type for parameter {}", parameter.getSignature());
            parameters.add(modelContext.alternateFor(parameter));
            LOG.debug("Recursively resolving dependencies for parameter {}", parameter.getSignature());
            parameters.addAll(resolvedDependencies(ModelContext.fromParent(modelContext, parameter)));
        }
        return parameters;
    }

    private List<ResolvedType> resolvedPropertiesAndFields(ModelContext modelContext, ResolvedType resolvedType) {
        if (modelContext.hasSeenBefore(resolvedType) || resolvedType.getErasedType().isEnum()) {
            return Lists.newArrayList();
        }
        modelContext.seen(resolvedType);
        List<ResolvedType> properties = Lists.newArrayList();
        Iterator<ModelProperty> it = nonTrivialProperties(modelContext, resolvedType).iterator();
        while (it.hasNext()) {
            ModelProperty property = it.next();
            LOG.debug("Adding type {} for parameter {}", property.getType().getSignature(), property.getName());
            properties.add(property.getType());
            properties.addAll(maybeFromCollectionElementType(modelContext, property));
            properties.addAll(maybeFromMapValueType(modelContext, property));
            properties.addAll(maybeFromRegularType(modelContext, property));
        }
        return properties;
    }

    private FluentIterable<ModelProperty> nonTrivialProperties(ModelContext modelContext, ResolvedType resolvedType) {
        return FluentIterable.from(propertiesFor(modelContext, resolvedType)).filter(Predicates.not(baseProperty(modelContext)));
    }

    private Predicate<? super ModelProperty> baseProperty(final ModelContext modelContext) {
        return new Predicate<ModelProperty>() { // from class: springfox.documentation.schema.ModelDependencyProvider.3
            @Override // com.google.common.base.Predicate
            public boolean apply(ModelProperty input) {
                return ModelDependencyProvider.this.isBaseType(ModelContext.fromParent(modelContext, input.getType()));
            }
        };
    }

    private List<ResolvedType> maybeFromRegularType(ModelContext modelContext, ModelProperty property) {
        if (Collections.isContainerType(property.getType()) || Maps.isMapType(property.getType())) {
            return Lists.newArrayList();
        }
        LOG.debug("Recursively resolving dependencies for type {}", ResolvedTypes.resolvedTypeSignature(property.getType()).or((Optional<String>) "<null>"));
        return Lists.newArrayList(resolvedDependencies(ModelContext.fromParent(modelContext, property.getType())));
    }

    private List<ResolvedType> maybeFromCollectionElementType(ModelContext modelContext, ModelProperty property) {
        List<ResolvedType> dependencies = Lists.newArrayList();
        if (Collections.isContainerType(property.getType())) {
            ResolvedType collectionElementType = Collections.collectionElementType(property.getType());
            String resolvedTypeSignature = ResolvedTypes.resolvedTypeSignature(collectionElementType).or((Optional<String>) "<null>");
            if (!isBaseType(ModelContext.fromParent(modelContext, collectionElementType))) {
                LOG.debug("Adding collectionElement type {}", resolvedTypeSignature);
                dependencies.add(collectionElementType);
            }
            LOG.debug("Recursively resolving dependencies for collectionElement type {}", resolvedTypeSignature);
            dependencies.addAll(resolvedDependencies(ModelContext.fromParent(modelContext, collectionElementType)));
        }
        return dependencies;
    }

    private List<ResolvedType> maybeFromMapValueType(ModelContext modelContext, ModelProperty property) {
        List<ResolvedType> dependencies = Lists.newArrayList();
        if (Maps.isMapType(property.getType())) {
            ResolvedType valueType = Maps.mapValueType(property.getType());
            String resolvedTypeSignature = ResolvedTypes.resolvedTypeSignature(valueType).or((Optional<String>) "<null>");
            if (!isBaseType(ModelContext.fromParent(modelContext, valueType))) {
                LOG.debug("Adding value type {}", resolvedTypeSignature);
                dependencies.add(valueType);
            }
            LOG.debug("Recursively resolving dependencies for value type {}", resolvedTypeSignature);
            dependencies.addAll(resolvedDependencies(ModelContext.fromParent(modelContext, valueType)));
        }
        return dependencies;
    }

    private List<ModelProperty> propertiesFor(ModelContext modelContext, ResolvedType resolvedType) {
        return this.propertiesProvider.propertiesFor(resolvedType, modelContext);
    }
}
