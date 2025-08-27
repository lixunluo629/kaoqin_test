package org.springframework.data.mapping.model;

import java.lang.reflect.Constructor;
import java.util.List;
import org.springframework.data.mapping.PersistentEntity;
import org.springframework.data.mapping.PreferredConstructor;
import org.springframework.util.StringUtils;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/mapping/model/MappingInstantiationException.class */
public class MappingInstantiationException extends RuntimeException {
    private static final long serialVersionUID = 822211065035487628L;
    private static final String TEXT_TEMPLATE = "Failed to instantiate %s using constructor %s with arguments %s";
    private final Class<?> entityType;
    private final Constructor<?> constructor;
    private final List<Object> constructorArguments;

    public MappingInstantiationException(PersistentEntity<?, ?> entity, List<Object> arguments, Exception cause) {
        this(entity, arguments, null, cause);
    }

    private MappingInstantiationException(PersistentEntity<?, ?> entity, List<Object> arguments, String message, Exception cause) {
        super(buildExceptionMessage(entity, arguments, message), cause);
        this.entityType = entity == null ? null : entity.getType();
        this.constructor = (entity == null || entity.getPersistenceConstructor() == null) ? null : entity.getPersistenceConstructor().getConstructor();
        this.constructorArguments = arguments;
    }

    private static final String buildExceptionMessage(PersistentEntity<?, ?> entity, List<Object> arguments, String defaultMessage) {
        if (entity == null) {
            return defaultMessage;
        }
        PreferredConstructor<?, P> persistenceConstructor = entity.getPersistenceConstructor();
        Object[] objArr = new Object[3];
        objArr[0] = entity.getType().getName();
        objArr[1] = persistenceConstructor == 0 ? "NO_CONSTRUCTOR" : persistenceConstructor.getConstructor().toString();
        objArr[2] = StringUtils.collectionToCommaDelimitedString(arguments);
        return String.format(TEXT_TEMPLATE, objArr);
    }

    public Class<?> getEntityType() {
        return this.entityType;
    }

    public Constructor<?> getConstructor() {
        return this.constructor;
    }

    public List<Object> getConstructorArguments() {
        return this.constructorArguments;
    }
}
