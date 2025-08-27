package springfox.documentation.schema.property;

import com.fasterxml.classmate.ResolvedType;
import com.fasterxml.classmate.TypeResolver;
import com.fasterxml.classmate.members.ResolvedField;
import com.fasterxml.classmate.members.ResolvedMethod;
import com.fasterxml.classmate.members.ResolvedParameterizedMember;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import springfox.documentation.annotations.Cacheable;
import springfox.documentation.builders.ModelPropertyBuilder;
import springfox.documentation.schema.ResolvedTypes;
import springfox.documentation.schema.TypeNameExtractor;
import springfox.documentation.schema.configuration.ObjectMapperConfigured;
import springfox.documentation.schema.plugins.SchemaPluginsManager;
import springfox.documentation.schema.property.bean.Accessors;
import springfox.documentation.schema.property.bean.AccessorsProvider;
import springfox.documentation.schema.property.bean.BeanModelProperty;
import springfox.documentation.schema.property.bean.ParameterModelProperty;
import springfox.documentation.schema.property.field.FieldModelProperty;
import springfox.documentation.schema.property.field.FieldProvider;
import springfox.documentation.spi.schema.contexts.ModelContext;
import springfox.documentation.spi.schema.contexts.ModelPropertyContext;

@Primary
@Component("optimized")
/* loaded from: springfox-schema-2.2.2.jar:springfox/documentation/schema/property/OptimizedModelPropertiesProvider.class */
public class OptimizedModelPropertiesProvider implements ModelPropertiesProvider {
    private static final Logger LOG = LoggerFactory.getLogger((Class<?>) OptimizedModelPropertiesProvider.class);
    private final AccessorsProvider accessors;
    private final FieldProvider fields;
    private final FactoryMethodProvider factoryMethods;
    private final TypeResolver typeResolver;
    private final BeanPropertyNamingStrategy namingStrategy;
    private final SchemaPluginsManager schemaPluginsManager;
    private final TypeNameExtractor typeNameExtractor;
    private ObjectMapper objectMapper;

    @Autowired
    public OptimizedModelPropertiesProvider(AccessorsProvider accessors, FieldProvider fields, FactoryMethodProvider factoryMethods, TypeResolver typeResolver, BeanPropertyNamingStrategy namingStrategy, SchemaPluginsManager schemaPluginsManager, TypeNameExtractor typeNameExtractor) {
        this.accessors = accessors;
        this.fields = fields;
        this.factoryMethods = factoryMethods;
        this.typeResolver = typeResolver;
        this.namingStrategy = namingStrategy;
        this.schemaPluginsManager = schemaPluginsManager;
        this.typeNameExtractor = typeNameExtractor;
    }

    @Override // org.springframework.context.ApplicationListener
    public void onApplicationEvent(ObjectMapperConfigured event) {
        this.objectMapper = event.getObjectMapper();
    }

    @Override // springfox.documentation.schema.property.ModelPropertiesProvider
    @Cacheable(value = "modelProperties", keyGenerator = ModelPropertiesKeyGenerator.class)
    public List<springfox.documentation.schema.ModelProperty> propertiesFor(ResolvedType type, ModelContext givenContext) {
        List<springfox.documentation.schema.ModelProperty> properties = Lists.newArrayList();
        BeanDescription beanDescription = beanDescription(type, givenContext);
        Map<String, BeanPropertyDefinition> propertyLookup = Maps.uniqueIndex(beanDescription.findProperties(), BeanPropertyDefinitions.beanPropertyByInternalName());
        for (Map.Entry<String, BeanPropertyDefinition> each : propertyLookup.entrySet()) {
            LOG.debug("Reading property {}", each.getKey());
            BeanPropertyDefinition jacksonProperty = each.getValue();
            Optional<AnnotatedMember> annotatedMember = Optional.fromNullable(jacksonProperty.getPrimaryMember());
            if (annotatedMember.isPresent()) {
                properties.addAll(candidateProperties(type, annotatedMember.get(), jacksonProperty, givenContext));
            }
        }
        return properties;
    }

    private Function<ResolvedMethod, List<springfox.documentation.schema.ModelProperty>> propertyFromBean(final ModelContext givenContext, final BeanPropertyDefinition jacksonProperty) {
        return new Function<ResolvedMethod, List<springfox.documentation.schema.ModelProperty>>() { // from class: springfox.documentation.schema.property.OptimizedModelPropertiesProvider.1
            @Override // com.google.common.base.Function
            public List<springfox.documentation.schema.ModelProperty> apply(ResolvedMethod input) {
                ResolvedType type = BeanModelProperty.paramOrReturnType(OptimizedModelPropertiesProvider.this.typeResolver, input);
                if (givenContext.hasSeenBefore(type)) {
                    return Lists.newArrayList();
                }
                return OptimizedModelPropertiesProvider.this.shouldUnwrap(input) ? OptimizedModelPropertiesProvider.this.propertiesFor(type, ModelContext.fromParent(givenContext, type)) : Lists.newArrayList(OptimizedModelPropertiesProvider.this.beanModelProperty(input, jacksonProperty, givenContext));
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Multi-variable type inference failed */
    public boolean shouldUnwrap(ResolvedMethod input) {
        return Iterables.any(Lists.newArrayList(((Method) input.getRawMember()).getDeclaredAnnotations()), ofType(JsonUnwrapped.class));
    }

    private Function<ResolvedField, List<springfox.documentation.schema.ModelProperty>> propertyFromField(final ModelContext givenContext, final BeanPropertyDefinition jacksonProperty) {
        return new Function<ResolvedField, List<springfox.documentation.schema.ModelProperty>>() { // from class: springfox.documentation.schema.property.OptimizedModelPropertiesProvider.2
            @Override // com.google.common.base.Function
            public List<springfox.documentation.schema.ModelProperty> apply(ResolvedField input) {
                List<Annotation> annotations = Lists.newArrayList(input.getRawMember().getAnnotations());
                return Iterables.any(annotations, OptimizedModelPropertiesProvider.this.ofType(JsonUnwrapped.class)) ? OptimizedModelPropertiesProvider.this.propertiesFor(input.getType(), ModelContext.fromParent(givenContext, input.getType())) : Lists.newArrayList(OptimizedModelPropertiesProvider.this.fieldModelProperty(input, jacksonProperty, givenContext));
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Predicate<? super Annotation> ofType(final Class<?> annotationType) {
        return new Predicate<Annotation>() { // from class: springfox.documentation.schema.property.OptimizedModelPropertiesProvider.3
            @Override // com.google.common.base.Predicate
            public boolean apply(Annotation input) {
                return annotationType.isAssignableFrom(input.getClass());
            }
        };
    }

    @VisibleForTesting
    List<springfox.documentation.schema.ModelProperty> candidateProperties(ResolvedType type, AnnotatedMember member, BeanPropertyDefinition jacksonProperty, ModelContext givenContext) {
        List<springfox.documentation.schema.ModelProperty> properties = Lists.newArrayList();
        if (member instanceof AnnotatedMethod) {
            properties.addAll((Collection) findAccessorMethod(type, jacksonProperty.getInternalName(), member).transform(propertyFromBean(givenContext, jacksonProperty)).or((Optional<V>) new ArrayList()));
        } else if (member instanceof AnnotatedField) {
            properties.addAll((Collection) findField(type, jacksonProperty.getInternalName()).transform(propertyFromField(givenContext, jacksonProperty)).or((Optional<V>) new ArrayList()));
        } else if (member instanceof AnnotatedParameter) {
            ModelContext modelContext = ModelContext.fromParent(givenContext, type);
            properties.addAll(fromFactoryMethod(type, jacksonProperty, (AnnotatedParameter) member, modelContext));
        }
        return FluentIterable.from(properties).filter(hiddenProperties()).toList();
    }

    private Predicate<? super springfox.documentation.schema.ModelProperty> hiddenProperties() {
        return new Predicate<springfox.documentation.schema.ModelProperty>() { // from class: springfox.documentation.schema.property.OptimizedModelPropertiesProvider.4
            @Override // com.google.common.base.Predicate
            public boolean apply(springfox.documentation.schema.ModelProperty input) {
                return !input.isHidden();
            }
        };
    }

    private Optional<ResolvedField> findField(ResolvedType resolvedType, final String fieldName) {
        return Iterables.tryFind(this.fields.in(resolvedType), new Predicate<ResolvedField>() { // from class: springfox.documentation.schema.property.OptimizedModelPropertiesProvider.5
            @Override // com.google.common.base.Predicate
            public boolean apply(ResolvedField input) {
                return fieldName.equals(input.getName());
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public springfox.documentation.schema.ModelProperty fieldModelProperty(ResolvedField childField, BeanPropertyDefinition jacksonProperty, ModelContext modelContext) {
        String fieldName = BeanPropertyDefinitions.name(jacksonProperty, modelContext.isReturnType(), this.namingStrategy);
        FieldModelProperty fieldModelProperty = new FieldModelProperty(fieldName, childField, modelContext.getAlternateTypeProvider());
        ModelPropertyBuilder propertyBuilder = new ModelPropertyBuilder().name(fieldModelProperty.getName()).type(fieldModelProperty.getType()).qualifiedType(fieldModelProperty.qualifiedTypeName()).position(fieldModelProperty.position()).required(Boolean.valueOf(fieldModelProperty.isRequired())).description(fieldModelProperty.propertyDescription()).allowableValues(fieldModelProperty.allowableValues());
        return this.schemaPluginsManager.property(new ModelPropertyContext(propertyBuilder, childField.getRawMember(), this.typeResolver, modelContext.getDocumentationType())).updateModelRef(ResolvedTypes.modelRefFactory(modelContext, this.typeNameExtractor));
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Multi-variable type inference failed */
    public springfox.documentation.schema.ModelProperty beanModelProperty(ResolvedMethod childProperty, BeanPropertyDefinition jacksonProperty, ModelContext modelContext) {
        String propertyName = BeanPropertyDefinitions.name(jacksonProperty, modelContext.isReturnType(), this.namingStrategy);
        BeanModelProperty beanModelProperty = new BeanModelProperty(propertyName, childProperty, Accessors.maybeAGetter((Method) childProperty.getRawMember()), this.typeResolver, modelContext.getAlternateTypeProvider());
        LOG.debug("Adding property {} to model", propertyName);
        ModelPropertyBuilder propertyBuilder = new ModelPropertyBuilder().name(beanModelProperty.getName()).type(beanModelProperty.getType()).qualifiedType(beanModelProperty.qualifiedTypeName()).position(beanModelProperty.position()).required(Boolean.valueOf(beanModelProperty.isRequired())).isHidden(false).description(beanModelProperty.propertyDescription()).allowableValues(beanModelProperty.allowableValues());
        return this.schemaPluginsManager.property(new ModelPropertyContext(propertyBuilder, jacksonProperty, this.typeResolver, modelContext.getDocumentationType())).updateModelRef(ResolvedTypes.modelRefFactory(modelContext, this.typeNameExtractor));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public springfox.documentation.schema.ModelProperty paramModelProperty(ResolvedParameterizedMember constructor, BeanPropertyDefinition jacksonProperty, AnnotatedParameter parameter, ModelContext modelContext) {
        String propertyName = BeanPropertyDefinitions.name(jacksonProperty, modelContext.isReturnType(), this.namingStrategy);
        ParameterModelProperty beanModelProperty = new ParameterModelProperty(propertyName, parameter, constructor, modelContext.getAlternateTypeProvider());
        LOG.debug("Adding property {} to model", propertyName);
        ModelPropertyBuilder propertyBuilder = new ModelPropertyBuilder().name(beanModelProperty.getName()).type(beanModelProperty.getType()).qualifiedType(beanModelProperty.qualifiedTypeName()).position(beanModelProperty.position()).required(Boolean.valueOf(beanModelProperty.isRequired())).isHidden(false).description(beanModelProperty.propertyDescription()).allowableValues(beanModelProperty.allowableValues());
        return this.schemaPluginsManager.property(new ModelPropertyContext(propertyBuilder, jacksonProperty, this.typeResolver, modelContext.getDocumentationType())).updateModelRef(ResolvedTypes.modelRefFactory(modelContext, this.typeNameExtractor));
    }

    private Optional<ResolvedMethod> findAccessorMethod(ResolvedType resolvedType, String propertyName, final AnnotatedMember member) {
        return Iterables.tryFind(this.accessors.in(resolvedType), new Predicate<ResolvedMethod>() { // from class: springfox.documentation.schema.property.OptimizedModelPropertiesProvider.6
            /* JADX WARN: Multi-variable type inference failed */
            @Override // com.google.common.base.Predicate
            public boolean apply(ResolvedMethod accessorMethod) {
                return ((Method) accessorMethod.getRawMember()).equals(member.getMember());
            }
        });
    }

    private List<springfox.documentation.schema.ModelProperty> fromFactoryMethod(ResolvedType resolvedType, final BeanPropertyDefinition beanProperty, final AnnotatedParameter member, final ModelContext givenContext) {
        Optional<V> optionalTransform = this.factoryMethods.in(resolvedType, FactoryMethodProvider.factoryMethodOf(member)).transform(new Function<ResolvedParameterizedMember, springfox.documentation.schema.ModelProperty>() { // from class: springfox.documentation.schema.property.OptimizedModelPropertiesProvider.7
            @Override // com.google.common.base.Function
            public springfox.documentation.schema.ModelProperty apply(ResolvedParameterizedMember input) {
                return OptimizedModelPropertiesProvider.this.paramModelProperty(input, beanProperty, member, givenContext);
            }
        });
        return optionalTransform.isPresent() ? Lists.newArrayList((springfox.documentation.schema.ModelProperty) optionalTransform.get()) : Lists.newArrayList();
    }

    private BeanDescription beanDescription(ResolvedType type, ModelContext context) {
        if (context.isReturnType()) {
            SerializationConfig serializationConfig = this.objectMapper.getSerializationConfig();
            return serializationConfig.introspect(TypeFactory.defaultInstance().constructType(type.getErasedType()));
        }
        DeserializationConfig serializationConfig2 = this.objectMapper.getDeserializationConfig();
        return serializationConfig2.introspect(TypeFactory.defaultInstance().constructType(type.getErasedType()));
    }
}
