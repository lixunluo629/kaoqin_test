package org.springframework.data.auditing;

import java.util.Calendar;
import java.util.Map;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.auditing.DefaultAuditableBeanWrapperFactory;
import org.springframework.data.domain.Auditable;
import org.springframework.data.mapping.PersistentEntity;
import org.springframework.data.mapping.PersistentProperty;
import org.springframework.data.mapping.PersistentPropertyAccessor;
import org.springframework.data.mapping.context.PersistentEntities;
import org.springframework.util.Assert;
import org.springframework.util.ConcurrentReferenceHashMap;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/auditing/MappingAuditableBeanWrapperFactory.class */
public class MappingAuditableBeanWrapperFactory extends DefaultAuditableBeanWrapperFactory {
    private final PersistentEntities entities;
    private final Map<Class<?>, MappingAuditingMetadata> metadataCache;

    public MappingAuditableBeanWrapperFactory(PersistentEntities entities) {
        Assert.notNull(entities, "PersistentEntities must not be null!");
        this.entities = entities;
        this.metadataCache = new ConcurrentReferenceHashMap();
    }

    @Override // org.springframework.data.auditing.DefaultAuditableBeanWrapperFactory, org.springframework.data.auditing.AuditableBeanWrapperFactory
    public AuditableBeanWrapper getBeanWrapperFor(Object source) {
        if (source == null) {
            return null;
        }
        if (source instanceof Auditable) {
            return super.getBeanWrapperFor(source);
        }
        Class<?> type = source.getClass();
        PersistentEntity<?, ?> entity = this.entities.getPersistentEntity(type);
        if (entity == null) {
            return super.getBeanWrapperFor(source);
        }
        MappingAuditingMetadata metadata = this.metadataCache.get(type);
        if (metadata == null) {
            metadata = new MappingAuditingMetadata(entity);
            this.metadataCache.put(type, metadata);
        }
        PersistentPropertyAccessor accessor = entity.getPropertyAccessor(source);
        if (metadata.isAuditable()) {
            return new MappingMetadataAuditableBeanWrapper(accessor, metadata);
        }
        return null;
    }

    /* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/auditing/MappingAuditableBeanWrapperFactory$MappingAuditingMetadata.class */
    static class MappingAuditingMetadata {
        private final PersistentProperty<?> createdByProperty;
        private final PersistentProperty<?> createdDateProperty;
        private final PersistentProperty<?> lastModifiedByProperty;
        private final PersistentProperty<?> lastModifiedDateProperty;

        public MappingAuditingMetadata(PersistentEntity<?, ? extends PersistentProperty<?>> entity) {
            Assert.notNull(entity, "PersistentEntity must not be null!");
            this.createdByProperty = entity.getPersistentProperty(CreatedBy.class);
            this.createdDateProperty = entity.getPersistentProperty(CreatedDate.class);
            this.lastModifiedByProperty = entity.getPersistentProperty(LastModifiedBy.class);
            this.lastModifiedDateProperty = entity.getPersistentProperty(LastModifiedDate.class);
        }

        public boolean isAuditable() {
            return (this.createdByProperty == null && this.createdDateProperty == null && this.lastModifiedByProperty == null && this.lastModifiedDateProperty == null) ? false : true;
        }
    }

    /* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/auditing/MappingAuditableBeanWrapperFactory$MappingMetadataAuditableBeanWrapper.class */
    static class MappingMetadataAuditableBeanWrapper extends DefaultAuditableBeanWrapperFactory.DateConvertingAuditableBeanWrapper {
        private final PersistentPropertyAccessor accessor;
        private final MappingAuditingMetadata metadata;

        public MappingMetadataAuditableBeanWrapper(PersistentPropertyAccessor accessor, MappingAuditingMetadata metadata) {
            Assert.notNull(accessor, "Target object must not be null!");
            Assert.notNull(metadata, "Auditing metadata must not be null!");
            this.accessor = accessor;
            this.metadata = metadata;
        }

        @Override // org.springframework.data.auditing.AuditableBeanWrapper
        public void setCreatedBy(Object value) {
            if (this.metadata.createdByProperty != null) {
                this.accessor.setProperty(this.metadata.createdByProperty, value);
            }
        }

        @Override // org.springframework.data.auditing.AuditableBeanWrapper
        public void setCreatedDate(Calendar value) {
            PersistentProperty<?> property = this.metadata.createdDateProperty;
            if (property != null) {
                this.accessor.setProperty(property, getDateValueToSet(value, property.getType(), property));
            }
        }

        @Override // org.springframework.data.auditing.AuditableBeanWrapper
        public void setLastModifiedBy(Object value) {
            if (this.metadata.lastModifiedByProperty != null) {
                this.accessor.setProperty(this.metadata.lastModifiedByProperty, value);
            }
        }

        @Override // org.springframework.data.auditing.AuditableBeanWrapper
        public Calendar getLastModifiedDate() {
            PersistentProperty<?> property = this.metadata.lastModifiedDateProperty;
            if (property == null) {
                return null;
            }
            return getAsCalendar(this.accessor.getProperty(property));
        }

        @Override // org.springframework.data.auditing.AuditableBeanWrapper
        public void setLastModifiedDate(Calendar value) {
            PersistentProperty<?> property = this.metadata.lastModifiedDateProperty;
            if (property != null) {
                this.accessor.setProperty(property, getDateValueToSet(value, property.getType(), property));
            }
        }
    }
}
