package org.springframework.data.mapping;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/mapping/PersistentPropertyAccessor.class */
public interface PersistentPropertyAccessor {
    void setProperty(PersistentProperty<?> persistentProperty, Object obj);

    Object getProperty(PersistentProperty<?> persistentProperty);

    Object getBean();
}
