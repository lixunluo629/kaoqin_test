package org.springframework.data.auditing.config;

import java.lang.annotation.Annotation;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.Assert;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/auditing/config/AnnotationAuditingConfiguration.class */
public class AnnotationAuditingConfiguration implements AuditingConfiguration {
    private final AnnotationAttributes attributes;

    public AnnotationAuditingConfiguration(AnnotationMetadata metadata, Class<? extends Annotation> annotation) {
        Assert.notNull(metadata, "AnnotationMetadata must not be null!");
        Assert.notNull(annotation, "Annotation must not be null!");
        this.attributes = new AnnotationAttributes(metadata.getAnnotationAttributes(annotation.getName()));
    }

    @Override // org.springframework.data.auditing.config.AuditingConfiguration
    public String getAuditorAwareRef() {
        return this.attributes.getString("auditorAwareRef");
    }

    @Override // org.springframework.data.auditing.config.AuditingConfiguration
    public boolean isSetDates() {
        return this.attributes.getBoolean("setDates");
    }

    @Override // org.springframework.data.auditing.config.AuditingConfiguration
    public String getDateTimeProviderRef() {
        return this.attributes.getString("dateTimeProviderRef");
    }

    @Override // org.springframework.data.auditing.config.AuditingConfiguration
    public boolean isModifyOnCreate() {
        return this.attributes.getBoolean("modifyOnCreate");
    }
}
