package springfox.documentation.schema.property;

import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;

/* loaded from: springfox-schema-2.2.2.jar:springfox/documentation/schema/property/BeanPropertyNamingStrategy.class */
public interface BeanPropertyNamingStrategy {
    String nameForSerialization(BeanPropertyDefinition beanPropertyDefinition);

    String nameForDeserialization(BeanPropertyDefinition beanPropertyDefinition);
}
