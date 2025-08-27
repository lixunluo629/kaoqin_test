package org.springframework.data.projection;

import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.BeanUtils;
import org.springframework.core.type.MethodMetadata;
import org.springframework.data.type.MethodsMetadata;
import org.springframework.data.type.classreading.MethodsMetadataReader;
import org.springframework.data.type.classreading.MethodsMetadataReaderFactory;
import org.springframework.data.util.ReflectionUtils;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/projection/DefaultProjectionInformation.class */
class DefaultProjectionInformation implements ProjectionInformation {
    private final Class<?> projectionType;
    private final List<PropertyDescriptor> properties;

    DefaultProjectionInformation(Class<?> type) {
        Assert.notNull(type, "Projection type must not be null!");
        this.projectionType = type;
        this.properties = collectDescriptors(type);
    }

    @Override // org.springframework.data.projection.ProjectionInformation
    public Class<?> getType() {
        return this.projectionType;
    }

    @Override // org.springframework.data.projection.ProjectionInformation
    public List<PropertyDescriptor> getInputProperties() {
        List<PropertyDescriptor> result = new ArrayList<>();
        for (PropertyDescriptor descriptor : this.properties) {
            if (isInputProperty(descriptor)) {
                result.add(descriptor);
            }
        }
        return result;
    }

    @Override // org.springframework.data.projection.ProjectionInformation
    public boolean isClosed() {
        return this.properties.equals(getInputProperties());
    }

    protected boolean isInputProperty(PropertyDescriptor descriptor) {
        return true;
    }

    private static List<PropertyDescriptor> collectDescriptors(Class<?> type) {
        List<PropertyDescriptor> result = new ArrayList<>();
        MethodsMetadata metadata = getMetadata(type);
        final Map<String, Integer> orders = getMethodOrder(metadata);
        for (PropertyDescriptor descriptor : filterDefaultMethods(BeanUtils.getPropertyDescriptors(type))) {
            Method readMethod = descriptor.getReadMethod();
            if (readMethod != null && (metadata == null || orders.containsKey(readMethod.getName()))) {
                result.add(descriptor);
            }
        }
        if (metadata == null) {
            return result;
        }
        Collections.sort(result, new Comparator<PropertyDescriptor>() { // from class: org.springframework.data.projection.DefaultProjectionInformation.1
            @Override // java.util.Comparator
            public int compare(PropertyDescriptor left, PropertyDescriptor right) {
                return ((Integer) orders.get(left.getReadMethod().getName())).intValue() - ((Integer) orders.get(right.getReadMethod().getName())).intValue();
            }
        });
        for (String name : metadata.getInterfaceNames()) {
            result.addAll(collectDescriptors(loadClass(name, type.getClassLoader())));
        }
        return result;
    }

    private static Class<?> loadClass(String className, ClassLoader classLoader) {
        try {
            return ClassUtils.forName(className, classLoader);
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException(String.format("Cannot load class %s", className));
        }
    }

    private static Map<String, Integer> getMethodOrder(MethodsMetadata metadata) {
        if (metadata == null) {
            return Collections.emptyMap();
        }
        Set<MethodMetadata> methods = metadata.getMethods();
        Map<String, Integer> result = new HashMap<>(methods.size());
        int i = 0;
        for (MethodMetadata methodMetadata : methods) {
            String name = methodMetadata.getMethodName();
            if (!result.containsKey(name)) {
                int i2 = i;
                i++;
                result.put(name, Integer.valueOf(i2));
            }
        }
        return result;
    }

    private static MethodsMetadata getMetadata(Class<?> type) {
        try {
            MethodsMetadataReaderFactory factory = new MethodsMetadataReaderFactory(type.getClassLoader());
            MethodsMetadataReader metadataReader = factory.getMetadataReader(ClassUtils.getQualifiedName(type));
            return metadataReader.getMethodsMetadata();
        } catch (IOException e) {
            return null;
        }
    }

    private static List<PropertyDescriptor> filterDefaultMethods(PropertyDescriptor[] descriptors) {
        List<PropertyDescriptor> result = new ArrayList<>(descriptors.length);
        for (PropertyDescriptor descriptor : descriptors) {
            if (!hasDefaultGetter(descriptor)) {
                result.add(descriptor);
            }
        }
        return result;
    }

    private static boolean hasDefaultGetter(PropertyDescriptor descriptor) {
        Method method = descriptor.getReadMethod();
        return method != null && ReflectionUtils.isDefaultMethod(method);
    }
}
