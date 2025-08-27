package org.springframework.data.auditing.config;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/auditing/config/AuditingConfiguration.class */
public interface AuditingConfiguration {
    String getAuditorAwareRef();

    boolean isSetDates();

    boolean isModifyOnCreate();

    String getDateTimeProviderRef();
}
