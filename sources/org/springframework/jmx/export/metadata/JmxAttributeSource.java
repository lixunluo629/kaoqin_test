package org.springframework.jmx.export.metadata;

import java.lang.reflect.Method;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/jmx/export/metadata/JmxAttributeSource.class */
public interface JmxAttributeSource {
    ManagedResource getManagedResource(Class<?> cls) throws InvalidMetadataException;

    ManagedAttribute getManagedAttribute(Method method) throws InvalidMetadataException;

    ManagedMetric getManagedMetric(Method method) throws InvalidMetadataException;

    ManagedOperation getManagedOperation(Method method) throws InvalidMetadataException;

    ManagedOperationParameter[] getManagedOperationParameters(Method method) throws InvalidMetadataException;

    ManagedNotification[] getManagedNotifications(Class<?> cls) throws InvalidMetadataException;
}
