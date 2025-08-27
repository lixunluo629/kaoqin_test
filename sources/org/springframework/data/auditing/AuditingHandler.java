package org.springframework.data.auditing;

import java.util.Arrays;
import java.util.Calendar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.mapping.PersistentEntity;
import org.springframework.data.mapping.PersistentProperty;
import org.springframework.data.mapping.context.MappingContext;
import org.springframework.data.mapping.context.PersistentEntities;
import org.springframework.util.Assert;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/auditing/AuditingHandler.class */
public class AuditingHandler implements InitializingBean {
    private static final Logger LOGGER = LoggerFactory.getLogger((Class<?>) AuditingHandler.class);
    private final DefaultAuditableBeanWrapperFactory factory;
    private DateTimeProvider dateTimeProvider;
    private AuditorAware<?> auditorAware;
    private boolean dateTimeForNow;
    private boolean modifyOnCreation;

    @Deprecated
    public AuditingHandler(MappingContext<? extends PersistentEntity<?, ?>, ? extends PersistentProperty<?>> mappingContext) {
        this(new PersistentEntities(Arrays.asList(mappingContext)));
    }

    public AuditingHandler(PersistentEntities entities) {
        this.dateTimeProvider = CurrentDateTimeProvider.INSTANCE;
        this.dateTimeForNow = true;
        this.modifyOnCreation = true;
        Assert.notNull(entities, "PersistentEntities must not be null!");
        this.factory = new MappingAuditableBeanWrapperFactory(entities);
    }

    public void setAuditorAware(AuditorAware<?> auditorAware) {
        Assert.notNull(auditorAware, "AuditorAware must not be null!");
        this.auditorAware = auditorAware;
    }

    public void setDateTimeForNow(boolean dateTimeForNow) {
        this.dateTimeForNow = dateTimeForNow;
    }

    public void setModifyOnCreation(boolean modifyOnCreation) {
        this.modifyOnCreation = modifyOnCreation;
    }

    public void setDateTimeProvider(DateTimeProvider dateTimeProvider) {
        this.dateTimeProvider = dateTimeProvider == null ? CurrentDateTimeProvider.INSTANCE : dateTimeProvider;
    }

    public void markCreated(Object source) {
        touch(source, true);
    }

    public void markModified(Object source) {
        touch(source, false);
    }

    protected final boolean isAuditable(Object source) {
        return this.factory.getBeanWrapperFor(source) != null;
    }

    private void touch(Object target, boolean isNew) {
        AuditableBeanWrapper wrapper = this.factory.getBeanWrapperFor(target);
        if (wrapper == null) {
            return;
        }
        Object auditor = touchAuditor(wrapper, isNew);
        Calendar now = this.dateTimeForNow ? touchDate(wrapper, isNew) : null;
        Object defaultedNow = now == null ? "not set" : now;
        Object defaultedAuditor = auditor == null ? "unknown" : auditor;
        LOGGER.debug("Touched {} - Last modification at {} by {}", target, defaultedNow, defaultedAuditor);
    }

    private Object touchAuditor(AuditableBeanWrapper wrapper, boolean isNew) {
        if (null == this.auditorAware) {
            return null;
        }
        Object auditor = this.auditorAware.getCurrentAuditor();
        if (isNew) {
            wrapper.setCreatedBy(auditor);
            if (!this.modifyOnCreation) {
                return auditor;
            }
        }
        wrapper.setLastModifiedBy(auditor);
        return auditor;
    }

    private Calendar touchDate(AuditableBeanWrapper wrapper, boolean isNew) {
        Calendar now = this.dateTimeProvider.getNow();
        if (isNew) {
            wrapper.setCreatedDate(now);
            if (!this.modifyOnCreation) {
                return now;
            }
        }
        wrapper.setLastModifiedDate(now);
        return now;
    }

    @Override // org.springframework.beans.factory.InitializingBean
    public void afterPropertiesSet() {
        if (this.auditorAware == null) {
            LOGGER.debug("No AuditorAware set! Auditing will not be applied!");
        }
    }
}
