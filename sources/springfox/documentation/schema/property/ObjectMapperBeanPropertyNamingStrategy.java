package springfox.documentation.schema.property;

import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.google.common.base.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import springfox.documentation.schema.configuration.ObjectMapperConfigured;

@Component
/* loaded from: springfox-schema-2.2.2.jar:springfox/documentation/schema/property/ObjectMapperBeanPropertyNamingStrategy.class */
public class ObjectMapperBeanPropertyNamingStrategy implements BeanPropertyNamingStrategy, ApplicationListener<ObjectMapperConfigured> {
    private static final Logger LOG = LoggerFactory.getLogger((Class<?>) ObjectMapperBeanPropertyNamingStrategy.class);
    private ObjectMapper objectMapper;

    @Override // springfox.documentation.schema.property.BeanPropertyNamingStrategy
    public String nameForSerialization(BeanPropertyDefinition beanProperty) {
        SerializationConfig serializationConfig = this.objectMapper.getSerializationConfig();
        Optional<PropertyNamingStrategy> namingStrategy = Optional.fromNullable(serializationConfig.getPropertyNamingStrategy());
        String newName = (String) namingStrategy.transform(BeanPropertyDefinitions.overTheWireName(beanProperty, serializationConfig)).or((Optional) beanProperty.getName());
        LOG.debug("Name '{}' renamed to '{}'", beanProperty.getName(), newName);
        return newName;
    }

    @Override // springfox.documentation.schema.property.BeanPropertyNamingStrategy
    public String nameForDeserialization(BeanPropertyDefinition beanProperty) {
        DeserializationConfig deserializationConfig = this.objectMapper.getDeserializationConfig();
        Optional<PropertyNamingStrategy> namingStrategy = Optional.fromNullable(deserializationConfig.getPropertyNamingStrategy());
        String newName = (String) namingStrategy.transform(BeanPropertyDefinitions.overTheWireName(beanProperty, deserializationConfig)).or((Optional) beanProperty.getName());
        LOG.debug("Name '{}' renamed to '{}'", beanProperty.getName(), newName);
        return newName;
    }

    @Override // org.springframework.context.ApplicationListener
    public void onApplicationEvent(ObjectMapperConfigured event) {
        this.objectMapper = event.getObjectMapper();
    }
}
