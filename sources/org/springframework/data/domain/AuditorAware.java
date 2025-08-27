package org.springframework.data.domain;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/domain/AuditorAware.class */
public interface AuditorAware<T> {
    T getCurrentAuditor();
}
