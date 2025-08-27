package org.springframework.jmx.export.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.annotation.AnnotationBeanUtils;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.config.EmbeddedValueResolver;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.jmx.export.metadata.InvalidMetadataException;
import org.springframework.jmx.export.metadata.JmxAttributeSource;
import org.springframework.util.StringValueResolver;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/jmx/export/annotation/AnnotationJmxAttributeSource.class */
public class AnnotationJmxAttributeSource implements JmxAttributeSource, BeanFactoryAware {
    private StringValueResolver embeddedValueResolver;

    @Override // org.springframework.beans.factory.BeanFactoryAware
    public void setBeanFactory(BeanFactory beanFactory) {
        if (beanFactory instanceof ConfigurableBeanFactory) {
            this.embeddedValueResolver = new EmbeddedValueResolver((ConfigurableBeanFactory) beanFactory);
        }
    }

    @Override // org.springframework.jmx.export.metadata.JmxAttributeSource
    public org.springframework.jmx.export.metadata.ManagedResource getManagedResource(Class<?> beanClass) throws SecurityException, InvalidMetadataException {
        ManagedResource ann = (ManagedResource) AnnotationUtils.findAnnotation(beanClass, ManagedResource.class);
        if (ann == null) {
            return null;
        }
        Class<?> declaringClass = AnnotationUtils.findAnnotationDeclaringClass(ManagedResource.class, beanClass);
        Class<?> target = (declaringClass == null || declaringClass.isInterface()) ? beanClass : declaringClass;
        if (!Modifier.isPublic(target.getModifiers())) {
            throw new InvalidMetadataException("@ManagedResource class '" + target.getName() + "' must be public");
        }
        org.springframework.jmx.export.metadata.ManagedResource managedResource = new org.springframework.jmx.export.metadata.ManagedResource();
        AnnotationBeanUtils.copyPropertiesToBean(ann, managedResource, this.embeddedValueResolver, new String[0]);
        return managedResource;
    }

    @Override // org.springframework.jmx.export.metadata.JmxAttributeSource
    public org.springframework.jmx.export.metadata.ManagedAttribute getManagedAttribute(Method method) throws SecurityException, InvalidMetadataException {
        ManagedAttribute ann = (ManagedAttribute) AnnotationUtils.findAnnotation(method, ManagedAttribute.class);
        if (ann == null) {
            return null;
        }
        org.springframework.jmx.export.metadata.ManagedAttribute managedAttribute = new org.springframework.jmx.export.metadata.ManagedAttribute();
        AnnotationBeanUtils.copyPropertiesToBean(ann, managedAttribute, "defaultValue");
        if (ann.defaultValue().length() > 0) {
            managedAttribute.setDefaultValue(ann.defaultValue());
        }
        return managedAttribute;
    }

    @Override // org.springframework.jmx.export.metadata.JmxAttributeSource
    public org.springframework.jmx.export.metadata.ManagedMetric getManagedMetric(Method method) throws InvalidMetadataException {
        ManagedMetric ann = (ManagedMetric) AnnotationUtils.findAnnotation(method, ManagedMetric.class);
        return (org.springframework.jmx.export.metadata.ManagedMetric) copyPropertiesToBean(ann, org.springframework.jmx.export.metadata.ManagedMetric.class);
    }

    @Override // org.springframework.jmx.export.metadata.JmxAttributeSource
    public org.springframework.jmx.export.metadata.ManagedOperation getManagedOperation(Method method) throws InvalidMetadataException {
        ManagedOperation ann = (ManagedOperation) AnnotationUtils.findAnnotation(method, ManagedOperation.class);
        return (org.springframework.jmx.export.metadata.ManagedOperation) copyPropertiesToBean(ann, org.springframework.jmx.export.metadata.ManagedOperation.class);
    }

    @Override // org.springframework.jmx.export.metadata.JmxAttributeSource
    public org.springframework.jmx.export.metadata.ManagedOperationParameter[] getManagedOperationParameters(Method method) throws InvalidMetadataException {
        Set<ManagedOperationParameter> anns = AnnotationUtils.getRepeatableAnnotations(method, ManagedOperationParameter.class, ManagedOperationParameters.class);
        return (org.springframework.jmx.export.metadata.ManagedOperationParameter[]) copyPropertiesToBeanArray(anns, org.springframework.jmx.export.metadata.ManagedOperationParameter.class);
    }

    @Override // org.springframework.jmx.export.metadata.JmxAttributeSource
    public org.springframework.jmx.export.metadata.ManagedNotification[] getManagedNotifications(Class<?> clazz) throws InvalidMetadataException {
        Set<ManagedNotification> anns = AnnotationUtils.getRepeatableAnnotations(clazz, ManagedNotification.class, ManagedNotifications.class);
        return (org.springframework.jmx.export.metadata.ManagedNotification[]) copyPropertiesToBeanArray(anns, org.springframework.jmx.export.metadata.ManagedNotification.class);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private static <T> T[] copyPropertiesToBeanArray(Collection<? extends Annotation> collection, Class<T> cls) {
        T[] tArr = (T[]) ((Object[]) Array.newInstance((Class<?>) cls, collection.size()));
        int i = 0;
        Iterator<? extends Annotation> it = collection.iterator();
        while (it.hasNext()) {
            int i2 = i;
            i++;
            tArr[i2] = copyPropertiesToBean(it.next(), cls);
        }
        return tArr;
    }

    private static <T> T copyPropertiesToBean(Annotation annotation, Class<T> cls) throws SecurityException {
        if (annotation == null) {
            return null;
        }
        T t = (T) BeanUtils.instantiateClass(cls);
        AnnotationBeanUtils.copyPropertiesToBean(annotation, t, new String[0]);
        return t;
    }
}
