package org.springframework.data.convert;

import org.springframework.core.convert.ConversionService;
import org.springframework.data.mapping.PersistentEntity;
import org.springframework.data.mapping.PersistentProperty;
import org.springframework.data.mapping.context.MappingContext;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/convert/EntityConverter.class */
public interface EntityConverter<E extends PersistentEntity<?, P>, P extends PersistentProperty<P>, T, S> extends EntityReader<T, S>, EntityWriter<T, S> {
    MappingContext<? extends E, P> getMappingContext();

    ConversionService getConversionService();
}
