package springfox.documentation.spring.web.readers.parameter;

import com.fasterxml.classmate.ResolvedType;
import com.fasterxml.classmate.TypeResolver;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.base.Strings;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Sets;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.schema.Types;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.schema.AlternateTypeProvider;
import springfox.documentation.spi.service.contexts.DocumentationContext;
import springfox.documentation.spi.service.contexts.ParameterExpansionContext;
import springfox.documentation.spring.web.plugins.DocumentationPluginsManager;

@Component
/* loaded from: springfox-spring-web-2.2.2.jar:springfox/documentation/spring/web/readers/parameter/ModelAttributeParameterExpander.class */
public class ModelAttributeParameterExpander {
    private static final Logger LOG = LoggerFactory.getLogger((Class<?>) ModelAttributeParameterExpander.class);
    private final TypeResolver resolver;

    @Autowired
    protected DocumentationPluginsManager pluginsManager;

    @Autowired
    public ModelAttributeParameterExpander(TypeResolver resolver) {
        this.resolver = resolver;
    }

    public void expand(String parentName, Class<?> paramType, List<Parameter> parameters, DocumentationContext documentationContext) {
        Set<String> beanPropNames = getBeanPropertyNames(paramType);
        Iterable<Field> fields = FluentIterable.from(getInstanceFields(paramType)).filter(onlyBeanProperties(beanPropNames));
        LOG.debug("Expanding parameter type: {}", paramType);
        AlternateTypeProvider alternateTypeProvider = documentationContext.getAlternateTypeProvider();
        FluentIterable<ModelAttributeField> expendables = FluentIterable.from(fields).transform(toModelAttributeField(alternateTypeProvider)).filter(Predicates.not(simpleType())).filter(Predicates.not(recursiveType(paramType)));
        Iterator<ModelAttributeField> it = expendables.iterator();
        while (it.hasNext()) {
            ModelAttributeField each = it.next();
            LOG.debug("Attempting to expand expandable field: {}", each.getField());
            expand(nestedParentName(parentName, each.getField()), each.getFieldType(), parameters, documentationContext);
        }
        FluentIterable<ModelAttributeField> simpleFields = FluentIterable.from(fields).transform(toModelAttributeField(alternateTypeProvider)).filter(simpleType());
        Iterator<ModelAttributeField> it2 = simpleFields.iterator();
        while (it2.hasNext()) {
            ModelAttributeField each2 = it2.next();
            LOG.debug("Attempting to expand field: {}", each2);
            String dataTypeName = (String) Optional.fromNullable(Types.typeNameFor(each2.getFieldType())).or((Optional) each2.getFieldType().getSimpleName());
            LOG.debug("Building parameter for field: {}, with type: ", each2, each2.getFieldType());
            ParameterExpansionContext parameterExpansionContext = new ParameterExpansionContext(dataTypeName, parentName, each2.getField(), documentationContext.getDocumentationType(), new ParameterBuilder());
            parameters.add(this.pluginsManager.expandParameter(parameterExpansionContext));
        }
    }

    private Predicate<ModelAttributeField> recursiveType(final Class<?> paramType) {
        return new Predicate<ModelAttributeField>() { // from class: springfox.documentation.spring.web.readers.parameter.ModelAttributeParameterExpander.1
            @Override // com.google.common.base.Predicate
            public boolean apply(ModelAttributeField input) {
                return input.getFieldType() == paramType;
            }
        };
    }

    private Predicate<ModelAttributeField> simpleType() {
        return Predicates.or(Predicates.or(belongsToJavaPackage(), Predicates.or(isCollection(), isMap())), isEnum());
    }

    private Predicate<ModelAttributeField> isCollection() {
        return new Predicate<ModelAttributeField>() { // from class: springfox.documentation.spring.web.readers.parameter.ModelAttributeParameterExpander.2
            @Override // com.google.common.base.Predicate
            public boolean apply(ModelAttributeField input) {
                return Collection.class.isAssignableFrom(input.getFieldType());
            }
        };
    }

    private Predicate<ModelAttributeField> isMap() {
        return new Predicate<ModelAttributeField>() { // from class: springfox.documentation.spring.web.readers.parameter.ModelAttributeParameterExpander.3
            @Override // com.google.common.base.Predicate
            public boolean apply(ModelAttributeField input) {
                return Map.class.isAssignableFrom(input.getFieldType());
            }
        };
    }

    private Predicate<ModelAttributeField> isEnum() {
        return new Predicate<ModelAttributeField>() { // from class: springfox.documentation.spring.web.readers.parameter.ModelAttributeParameterExpander.4
            @Override // com.google.common.base.Predicate
            public boolean apply(ModelAttributeField input) {
                return input.getFieldType().isEnum();
            }
        };
    }

    private Predicate<ModelAttributeField> belongsToJavaPackage() {
        return new Predicate<ModelAttributeField>() { // from class: springfox.documentation.spring.web.readers.parameter.ModelAttributeParameterExpander.5
            @Override // com.google.common.base.Predicate
            public boolean apply(ModelAttributeField input) {
                return ModelAttributeParameterExpander.this.packageName(input.getFieldType()).startsWith("java");
            }
        };
    }

    private Function<Field, ModelAttributeField> toModelAttributeField(final AlternateTypeProvider alternateTypeProvider) {
        return new Function<Field, ModelAttributeField>() { // from class: springfox.documentation.spring.web.readers.parameter.ModelAttributeParameterExpander.6
            @Override // com.google.common.base.Function
            public ModelAttributeField apply(Field input) {
                return new ModelAttributeField(ModelAttributeParameterExpander.this.fieldType(alternateTypeProvider, input), input);
            }
        };
    }

    private Predicate<Field> onlyBeanProperties(final Set<String> beanPropNames) {
        return new Predicate<Field>() { // from class: springfox.documentation.spring.web.readers.parameter.ModelAttributeParameterExpander.7
            @Override // com.google.common.base.Predicate
            public boolean apply(Field input) {
                return beanPropNames.contains(input.getName());
            }
        };
    }

    private String nestedParentName(String parentName, Field field) {
        if (Strings.isNullOrEmpty(parentName)) {
            return field.getName();
        }
        return String.format("%s.%s", parentName, field.getName());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Class<?> fieldType(AlternateTypeProvider alternateTypeProvider, Field field) throws NegativeArraySizeException {
        Class<?> type = field.getType();
        ResolvedType resolvedType = this.resolver.resolve(type, new Type[0]);
        ResolvedType alternativeType = alternateTypeProvider.alternateFor(resolvedType);
        Class<?> erasedType = alternativeType.getErasedType();
        if (type != erasedType) {
            LOG.debug("Found alternative type [{}] for field: [{}-{}]", erasedType, field, type);
        }
        return erasedType;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String packageName(Class<?> type) {
        return (String) Optional.fromNullable(type.getPackage()).transform(toPackageName()).or((Optional) "java");
    }

    private Function<Package, String> toPackageName() {
        return new Function<Package, String>() { // from class: springfox.documentation.spring.web.readers.parameter.ModelAttributeParameterExpander.8
            @Override // com.google.common.base.Function
            public String apply(Package input) {
                return input.getName();
            }
        };
    }

    private List<Field> getInstanceFields(Class<?> type) {
        List<Field> result = new ArrayList<>();
        Class<?> superclass = type;
        while (true) {
            Class<?> i = superclass;
            if (!rootType(i)) {
                result.addAll(Arrays.asList(i.getDeclaredFields()));
                superclass = i.getSuperclass();
            } else {
                return FluentIterable.from(result).filter(Predicates.not(staticField())).filter(Predicates.not(syntheticFields())).toList();
            }
        }
    }

    private Predicate<Field> syntheticFields() {
        return new Predicate<Field>() { // from class: springfox.documentation.spring.web.readers.parameter.ModelAttributeParameterExpander.9
            @Override // com.google.common.base.Predicate
            public boolean apply(Field input) {
                return input.isSynthetic();
            }
        };
    }

    private Predicate<Field> staticField() {
        return new Predicate<Field>() { // from class: springfox.documentation.spring.web.readers.parameter.ModelAttributeParameterExpander.10
            @Override // com.google.common.base.Predicate
            public boolean apply(Field input) {
                return Modifier.isStatic(input.getModifiers());
            }
        };
    }

    private boolean rootType(Class clazz) {
        return Optional.fromNullable(clazz).or((Optional) Object.class) == Object.class;
    }

    private Set<String> getBeanPropertyNames(Class<?> clazz) {
        try {
            Set<String> beanProps = new HashSet<>();
            PropertyDescriptor[] propDescriptors = getBeanInfo(clazz).getPropertyDescriptors();
            for (PropertyDescriptor propDescriptor : propDescriptors) {
                if (propDescriptor.getReadMethod() != null && propDescriptor.getWriteMethod() != null) {
                    beanProps.add(propDescriptor.getName());
                }
            }
            return beanProps;
        } catch (IntrospectionException e) {
            LOG.warn(String.format("Failed to get bean properties on (%s)", clazz), e);
            return Sets.newHashSet();
        }
    }

    @VisibleForTesting
    BeanInfo getBeanInfo(Class<?> clazz) throws IntrospectionException {
        return Introspector.getBeanInfo(clazz);
    }
}
