package org.springframework.boot.autoconfigure.jdbc.metadata;

/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/jdbc/metadata/DataSourcePoolMetadata.class */
public interface DataSourcePoolMetadata {
    Float getUsage();

    Integer getActive();

    Integer getMax();

    Integer getMin();

    String getValidationQuery();
}
