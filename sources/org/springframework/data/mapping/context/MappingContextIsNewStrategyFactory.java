package org.springframework.data.mapping.context;

import java.util.Arrays;
import org.springframework.data.mapping.PersistentEntity;
import org.springframework.data.mapping.PersistentProperty;
import org.springframework.data.mapping.PersistentPropertyAccessor;
import org.springframework.data.mapping.model.MappingException;
import org.springframework.data.support.IsNewStrategy;
import org.springframework.data.support.IsNewStrategyFactorySupport;
import org.springframework.util.Assert;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/mapping/context/MappingContextIsNewStrategyFactory.class */
public class MappingContextIsNewStrategyFactory extends IsNewStrategyFactorySupport {
    private final PersistentEntities context;

    @Deprecated
    public MappingContextIsNewStrategyFactory(MappingContext<? extends PersistentEntity<?, ?>, ?> context) {
        this(new PersistentEntities(Arrays.asList(context)));
    }

    public MappingContextIsNewStrategyFactory(PersistentEntities entities) {
        Assert.notNull(entities, "PersistentEntities must not be null!");
        this.context = entities;
    }

    @Override // org.springframework.data.support.IsNewStrategyFactorySupport
    protected IsNewStrategy doGetIsNewStrategy(Class<?> type) {
        PersistentEntity<?, ?> entity = this.context.getPersistentEntity(type);
        if (entity == null) {
            return null;
        }
        if (entity.hasVersionProperty()) {
            return new PropertyIsNullOrZeroNumberIsNewStrategy(entity.getVersionProperty());
        }
        if (entity.hasIdProperty()) {
            return new PropertyIsNullOrZeroNumberIsNewStrategy(entity.getIdProperty());
        }
        throw new MappingException(String.format("Cannot determine IsNewStrategy for type %s!", type));
    }

    /* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/mapping/context/MappingContextIsNewStrategyFactory$PersistentPropertyInspectingIsNewStrategy.class */
    static abstract class PersistentPropertyInspectingIsNewStrategy implements IsNewStrategy {
        private final PersistentProperty<?> property;

        protected abstract boolean decideIsNew(Object obj);

        public PersistentPropertyInspectingIsNewStrategy(PersistentProperty<?> property) {
            Assert.notNull(property, "PersistentProperty must not be null!");
            this.property = property;
        }

        @Override // org.springframework.data.support.IsNewStrategy
        public boolean isNew(Object entity) {
            PersistentPropertyAccessor accessor = this.property.getOwner().getPropertyAccessor(entity);
            Object propertyValue = accessor.getProperty(this.property);
            return decideIsNew(propertyValue);
        }
    }

    /* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/mapping/context/MappingContextIsNewStrategyFactory$PropertyIsNullOrZeroNumberIsNewStrategy.class */
    static class PropertyIsNullOrZeroNumberIsNewStrategy extends PersistentPropertyInspectingIsNewStrategy {
        public PropertyIsNullOrZeroNumberIsNewStrategy(PersistentProperty<?> property) {
            super(property);
        }

        @Override // org.springframework.data.mapping.context.MappingContextIsNewStrategyFactory.PersistentPropertyInspectingIsNewStrategy
        protected boolean decideIsNew(Object property) {
            if (property == null) {
                return true;
            }
            return (property instanceof Number) && ((Number) property).longValue() == 0;
        }
    }
}
