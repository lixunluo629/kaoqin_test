package org.springframework.beans;

import java.awt.Image;
import java.beans.BeanDescriptor;
import java.beans.BeanInfo;
import java.beans.EventSetDescriptor;
import java.beans.IndexedPropertyDescriptor;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.MethodDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.ObjectUtils;

/* loaded from: spring-beans-4.3.25.RELEASE.jar:org/springframework/beans/ExtendedBeanInfo.class */
class ExtendedBeanInfo implements BeanInfo {
    private static final Log logger = LogFactory.getLog(ExtendedBeanInfo.class);
    private final BeanInfo delegate;
    private final Set<PropertyDescriptor> propertyDescriptors = new TreeSet(new PropertyDescriptorComparator());

    public ExtendedBeanInfo(BeanInfo delegate) throws IntrospectionException {
        this.delegate = delegate;
        for (IndexedPropertyDescriptor indexedPropertyDescriptor : delegate.getPropertyDescriptors()) {
            try {
                this.propertyDescriptors.add(indexedPropertyDescriptor instanceof IndexedPropertyDescriptor ? new SimpleIndexedPropertyDescriptor(indexedPropertyDescriptor) : new SimplePropertyDescriptor(indexedPropertyDescriptor));
            } catch (IntrospectionException ex) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Ignoring invalid bean property '" + indexedPropertyDescriptor.getName() + "': " + ex.getMessage());
                }
            }
        }
        MethodDescriptor[] methodDescriptors = delegate.getMethodDescriptors();
        if (methodDescriptors != null) {
            for (Method method : findCandidateWriteMethods(methodDescriptors)) {
                try {
                    handleCandidateWriteMethod(method);
                } catch (IntrospectionException ex2) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Ignoring candidate write method [" + method + "]: " + ex2.getMessage());
                    }
                }
            }
        }
    }

    private List<Method> findCandidateWriteMethods(MethodDescriptor[] methodDescriptors) {
        List<Method> matches = new ArrayList<>();
        for (MethodDescriptor methodDescriptor : methodDescriptors) {
            Method method = methodDescriptor.getMethod();
            if (isCandidateWriteMethod(method)) {
                matches.add(method);
            }
        }
        Collections.sort(matches, new Comparator<Method>() { // from class: org.springframework.beans.ExtendedBeanInfo.1
            @Override // java.util.Comparator
            public int compare(Method m1, Method m2) {
                return m2.toString().compareTo(m1.toString());
            }
        });
        return matches;
    }

    public static boolean isCandidateWriteMethod(Method method) {
        String methodName = method.getName();
        Class<?>[] parameterTypes = method.getParameterTypes();
        int nParams = parameterTypes.length;
        return methodName.length() > 3 && methodName.startsWith("set") && Modifier.isPublic(method.getModifiers()) && (!Void.TYPE.isAssignableFrom(method.getReturnType()) || Modifier.isStatic(method.getModifiers())) && (nParams == 1 || (nParams == 2 && Integer.TYPE == parameterTypes[0]));
    }

    private void handleCandidateWriteMethod(Method method) throws IntrospectionException {
        int nParams = method.getParameterTypes().length;
        String propertyName = propertyNameFor(method);
        Class<?> propertyType = method.getParameterTypes()[nParams - 1];
        IndexedPropertyDescriptor indexedPropertyDescriptorFindExistingPropertyDescriptor = findExistingPropertyDescriptor(propertyName, propertyType);
        if (nParams == 1) {
            if (indexedPropertyDescriptorFindExistingPropertyDescriptor == null) {
                this.propertyDescriptors.add(new SimplePropertyDescriptor(propertyName, null, method));
                return;
            } else {
                indexedPropertyDescriptorFindExistingPropertyDescriptor.setWriteMethod(method);
                return;
            }
        }
        if (nParams == 2) {
            if (indexedPropertyDescriptorFindExistingPropertyDescriptor == null) {
                this.propertyDescriptors.add(new SimpleIndexedPropertyDescriptor(propertyName, null, null, null, method));
                return;
            } else if (indexedPropertyDescriptorFindExistingPropertyDescriptor instanceof IndexedPropertyDescriptor) {
                indexedPropertyDescriptorFindExistingPropertyDescriptor.setIndexedWriteMethod(method);
                return;
            } else {
                this.propertyDescriptors.remove(indexedPropertyDescriptorFindExistingPropertyDescriptor);
                this.propertyDescriptors.add(new SimpleIndexedPropertyDescriptor(propertyName, indexedPropertyDescriptorFindExistingPropertyDescriptor.getReadMethod(), indexedPropertyDescriptorFindExistingPropertyDescriptor.getWriteMethod(), null, method));
                return;
            }
        }
        throw new IllegalArgumentException("Write method must have exactly 1 or 2 parameters: " + method);
    }

    private PropertyDescriptor findExistingPropertyDescriptor(String propertyName, Class<?> propertyType) {
        Iterator<PropertyDescriptor> it = this.propertyDescriptors.iterator();
        while (it.hasNext()) {
            IndexedPropertyDescriptor indexedPropertyDescriptor = (PropertyDescriptor) it.next();
            String candidateName = indexedPropertyDescriptor.getName();
            if (indexedPropertyDescriptor instanceof IndexedPropertyDescriptor) {
                IndexedPropertyDescriptor ipd = indexedPropertyDescriptor;
                Class<?> candidateType = ipd.getIndexedPropertyType();
                if (candidateName.equals(propertyName) && (candidateType.equals(propertyType) || candidateType.equals(propertyType.getComponentType()))) {
                    return indexedPropertyDescriptor;
                }
            } else {
                Class<?> candidateType2 = indexedPropertyDescriptor.getPropertyType();
                if (candidateName.equals(propertyName) && (candidateType2.equals(propertyType) || propertyType.equals(candidateType2.getComponentType()))) {
                    return indexedPropertyDescriptor;
                }
            }
        }
        return null;
    }

    private String propertyNameFor(Method method) {
        return Introspector.decapitalize(method.getName().substring(3, method.getName().length()));
    }

    public PropertyDescriptor[] getPropertyDescriptors() {
        return (PropertyDescriptor[]) this.propertyDescriptors.toArray(new PropertyDescriptor[this.propertyDescriptors.size()]);
    }

    public BeanInfo[] getAdditionalBeanInfo() {
        return this.delegate.getAdditionalBeanInfo();
    }

    public BeanDescriptor getBeanDescriptor() {
        return this.delegate.getBeanDescriptor();
    }

    public int getDefaultEventIndex() {
        return this.delegate.getDefaultEventIndex();
    }

    public int getDefaultPropertyIndex() {
        return this.delegate.getDefaultPropertyIndex();
    }

    public EventSetDescriptor[] getEventSetDescriptors() {
        return this.delegate.getEventSetDescriptors();
    }

    public Image getIcon(int iconKind) {
        return this.delegate.getIcon(iconKind);
    }

    public MethodDescriptor[] getMethodDescriptors() {
        return this.delegate.getMethodDescriptors();
    }

    /* loaded from: spring-beans-4.3.25.RELEASE.jar:org/springframework/beans/ExtendedBeanInfo$SimplePropertyDescriptor.class */
    static class SimplePropertyDescriptor extends PropertyDescriptor {
        private Method readMethod;
        private Method writeMethod;
        private Class<?> propertyType;
        private Class<?> propertyEditorClass;

        public SimplePropertyDescriptor(PropertyDescriptor original) throws IntrospectionException {
            this(original.getName(), original.getReadMethod(), original.getWriteMethod());
            PropertyDescriptorUtils.copyNonMethodProperties(original, this);
        }

        public SimplePropertyDescriptor(String propertyName, Method readMethod, Method writeMethod) throws IntrospectionException {
            super(propertyName, (Method) null, (Method) null);
            this.readMethod = readMethod;
            this.writeMethod = writeMethod;
            this.propertyType = PropertyDescriptorUtils.findPropertyType(readMethod, writeMethod);
        }

        public Method getReadMethod() {
            return this.readMethod;
        }

        public void setReadMethod(Method readMethod) {
            this.readMethod = readMethod;
        }

        public Method getWriteMethod() {
            return this.writeMethod;
        }

        public void setWriteMethod(Method writeMethod) {
            this.writeMethod = writeMethod;
        }

        public Class<?> getPropertyType() {
            if (this.propertyType == null) {
                try {
                    this.propertyType = PropertyDescriptorUtils.findPropertyType(this.readMethod, this.writeMethod);
                } catch (IntrospectionException e) {
                }
            }
            return this.propertyType;
        }

        public Class<?> getPropertyEditorClass() {
            return this.propertyEditorClass;
        }

        public void setPropertyEditorClass(Class<?> propertyEditorClass) {
            this.propertyEditorClass = propertyEditorClass;
        }

        public boolean equals(Object other) {
            return this == other || ((other instanceof PropertyDescriptor) && PropertyDescriptorUtils.equals(this, (PropertyDescriptor) other));
        }

        public int hashCode() {
            return (ObjectUtils.nullSafeHashCode(getReadMethod()) * 29) + ObjectUtils.nullSafeHashCode(getWriteMethod());
        }

        public String toString() {
            return String.format("%s[name=%s, propertyType=%s, readMethod=%s, writeMethod=%s]", getClass().getSimpleName(), getName(), getPropertyType(), this.readMethod, this.writeMethod);
        }
    }

    /* loaded from: spring-beans-4.3.25.RELEASE.jar:org/springframework/beans/ExtendedBeanInfo$SimpleIndexedPropertyDescriptor.class */
    static class SimpleIndexedPropertyDescriptor extends IndexedPropertyDescriptor {
        private Method readMethod;
        private Method writeMethod;
        private Class<?> propertyType;
        private Method indexedReadMethod;
        private Method indexedWriteMethod;
        private Class<?> indexedPropertyType;
        private Class<?> propertyEditorClass;

        public SimpleIndexedPropertyDescriptor(IndexedPropertyDescriptor original) throws IntrospectionException {
            this(original.getName(), original.getReadMethod(), original.getWriteMethod(), original.getIndexedReadMethod(), original.getIndexedWriteMethod());
            PropertyDescriptorUtils.copyNonMethodProperties(original, this);
        }

        public SimpleIndexedPropertyDescriptor(String propertyName, Method readMethod, Method writeMethod, Method indexedReadMethod, Method indexedWriteMethod) throws IntrospectionException {
            super(propertyName, (Method) null, (Method) null, (Method) null, (Method) null);
            this.readMethod = readMethod;
            this.writeMethod = writeMethod;
            this.propertyType = PropertyDescriptorUtils.findPropertyType(readMethod, writeMethod);
            this.indexedReadMethod = indexedReadMethod;
            this.indexedWriteMethod = indexedWriteMethod;
            this.indexedPropertyType = PropertyDescriptorUtils.findIndexedPropertyType(propertyName, this.propertyType, indexedReadMethod, indexedWriteMethod);
        }

        public Method getReadMethod() {
            return this.readMethod;
        }

        public void setReadMethod(Method readMethod) {
            this.readMethod = readMethod;
        }

        public Method getWriteMethod() {
            return this.writeMethod;
        }

        public void setWriteMethod(Method writeMethod) {
            this.writeMethod = writeMethod;
        }

        public Class<?> getPropertyType() {
            if (this.propertyType == null) {
                try {
                    this.propertyType = PropertyDescriptorUtils.findPropertyType(this.readMethod, this.writeMethod);
                } catch (IntrospectionException e) {
                }
            }
            return this.propertyType;
        }

        public Method getIndexedReadMethod() {
            return this.indexedReadMethod;
        }

        public void setIndexedReadMethod(Method indexedReadMethod) throws IntrospectionException {
            this.indexedReadMethod = indexedReadMethod;
        }

        public Method getIndexedWriteMethod() {
            return this.indexedWriteMethod;
        }

        public void setIndexedWriteMethod(Method indexedWriteMethod) throws IntrospectionException {
            this.indexedWriteMethod = indexedWriteMethod;
        }

        public Class<?> getIndexedPropertyType() {
            if (this.indexedPropertyType == null) {
                try {
                    this.indexedPropertyType = PropertyDescriptorUtils.findIndexedPropertyType(getName(), getPropertyType(), this.indexedReadMethod, this.indexedWriteMethod);
                } catch (IntrospectionException e) {
                }
            }
            return this.indexedPropertyType;
        }

        public Class<?> getPropertyEditorClass() {
            return this.propertyEditorClass;
        }

        public void setPropertyEditorClass(Class<?> propertyEditorClass) {
            this.propertyEditorClass = propertyEditorClass;
        }

        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof IndexedPropertyDescriptor)) {
                return false;
            }
            IndexedPropertyDescriptor otherPd = (IndexedPropertyDescriptor) other;
            return ObjectUtils.nullSafeEquals(getIndexedReadMethod(), otherPd.getIndexedReadMethod()) && ObjectUtils.nullSafeEquals(getIndexedWriteMethod(), otherPd.getIndexedWriteMethod()) && ObjectUtils.nullSafeEquals(getIndexedPropertyType(), otherPd.getIndexedPropertyType()) && PropertyDescriptorUtils.equals(this, otherPd);
        }

        public int hashCode() {
            int hashCode = ObjectUtils.nullSafeHashCode(getReadMethod());
            return (29 * ((29 * ((29 * hashCode) + ObjectUtils.nullSafeHashCode(getWriteMethod()))) + ObjectUtils.nullSafeHashCode(getIndexedReadMethod()))) + ObjectUtils.nullSafeHashCode(getIndexedWriteMethod());
        }

        public String toString() {
            return String.format("%s[name=%s, propertyType=%s, indexedPropertyType=%s, readMethod=%s, writeMethod=%s, indexedReadMethod=%s, indexedWriteMethod=%s]", getClass().getSimpleName(), getName(), getPropertyType(), getIndexedPropertyType(), this.readMethod, this.writeMethod, this.indexedReadMethod, this.indexedWriteMethod);
        }
    }

    /* loaded from: spring-beans-4.3.25.RELEASE.jar:org/springframework/beans/ExtendedBeanInfo$PropertyDescriptorComparator.class */
    static class PropertyDescriptorComparator implements Comparator<PropertyDescriptor> {
        PropertyDescriptorComparator() {
        }

        @Override // java.util.Comparator
        public int compare(PropertyDescriptor desc1, PropertyDescriptor desc2) {
            String left = desc1.getName();
            String right = desc2.getName();
            for (int i = 0; i < left.length(); i++) {
                if (right.length() == i) {
                    return 1;
                }
                int result = left.getBytes()[i] - right.getBytes()[i];
                if (result != 0) {
                    return result;
                }
            }
            return left.length() - right.length();
        }
    }
}
