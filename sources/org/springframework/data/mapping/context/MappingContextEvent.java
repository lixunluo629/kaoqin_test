package org.springframework.data.mapping.context;

import org.springframework.context.ApplicationEvent;
import org.springframework.data.mapping.PersistentEntity;
import org.springframework.data.mapping.PersistentProperty;
import org.springframework.util.Assert;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/mapping/context/MappingContextEvent.class */
public class MappingContextEvent<E extends PersistentEntity<?, P>, P extends PersistentProperty<P>> extends ApplicationEvent {
    private static final long serialVersionUID = 1336466833846092490L;
    private final MappingContext<?, ?> source;
    private final E entity;

    public MappingContextEvent(MappingContext<?, ?> source, E entity) {
        super(source);
        Assert.notNull(source, "Source MappingContext must not be null!");
        Assert.notNull(entity, "Entity must not be null!");
        this.source = source;
        this.entity = entity;
    }

    public E getPersistentEntity() {
        return this.entity;
    }

    public boolean wasEmittedBy(MappingContext<?, ?> context) {
        return this.source.equals(context);
    }
}
