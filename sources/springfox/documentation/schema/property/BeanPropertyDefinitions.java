package springfox.documentation.schema.property;

import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.introspect.POJOPropertyBuilder;
import com.google.common.base.Function;

/* loaded from: springfox-schema-2.2.2.jar:springfox/documentation/schema/property/BeanPropertyDefinitions.class */
public class BeanPropertyDefinitions {
    private BeanPropertyDefinitions() {
        throw new UnsupportedOperationException();
    }

    public static Function<BeanPropertyDefinition, String> beanPropertyByInternalName() {
        return new Function<BeanPropertyDefinition, String>() { // from class: springfox.documentation.schema.property.BeanPropertyDefinitions.1
            @Override // com.google.common.base.Function
            public String apply(BeanPropertyDefinition input) {
                return input.getInternalName();
            }
        };
    }

    public static String name(BeanPropertyDefinition beanPropertyDefinition, boolean forSerialization, BeanPropertyNamingStrategy namingStrategy) {
        if (forSerialization) {
            return namingStrategy.nameForSerialization(beanPropertyDefinition);
        }
        return namingStrategy.nameForDeserialization(beanPropertyDefinition);
    }

    public static Function<PropertyNamingStrategy, String> overTheWireName(final BeanPropertyDefinition beanProperty, final MapperConfig<?> config) {
        return new Function<PropertyNamingStrategy, String>() { // from class: springfox.documentation.schema.property.BeanPropertyDefinitions.2
            @Override // com.google.common.base.Function
            public String apply(PropertyNamingStrategy strategy) {
                return BeanPropertyDefinitions.getName(strategy, beanProperty, config);
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String getName(PropertyNamingStrategy naming, BeanPropertyDefinition beanProperty, MapperConfig<?> config) {
        AnnotationIntrospector annotationIntrospector = config.isAnnotationProcessingEnabled() ? config.getAnnotationIntrospector() : null;
        POJOPropertyBuilder prop = new POJOPropertyBuilder(new PropertyName(beanProperty.getName()), annotationIntrospector, true);
        return naming.nameForField(config, prop.getField(), beanProperty.getName());
    }
}
