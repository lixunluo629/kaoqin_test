package org.springframework.data.repository.query;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.data.repository.query.spi.EvaluationContextExtension;
import org.springframework.data.repository.query.spi.Function;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/repository/query/EvaluationContextExtensionInformation.class */
class EvaluationContextExtensionInformation {
    private final ExtensionTypeInformation extensionTypeInformation;
    private final RootObjectInformation rootObjectInformation;

    public EvaluationContextExtensionInformation(Class<? extends EvaluationContextExtension> type) {
        Assert.notNull(type, "Extension type must not be null!");
        Class<?> rootObjectType = getRootObjectMethod(type).getReturnType();
        this.rootObjectInformation = Object.class.equals(rootObjectType) ? null : new RootObjectInformation(rootObjectType);
        this.extensionTypeInformation = new ExtensionTypeInformation(type);
    }

    public ExtensionTypeInformation getExtensionTypeInformation() {
        return this.extensionTypeInformation;
    }

    public RootObjectInformation getRootObjectInformation(Object target) {
        if (target == null) {
            return RootObjectInformation.NONE;
        }
        return this.rootObjectInformation == null ? new RootObjectInformation(target.getClass()) : this.rootObjectInformation;
    }

    private static Method getRootObjectMethod(Class<?> type) {
        try {
            return type.getMethod("getRootObject", new Class[0]);
        } catch (Exception e) {
            return null;
        }
    }

    /* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/repository/query/EvaluationContextExtensionInformation$ExtensionTypeInformation.class */
    public static class ExtensionTypeInformation {
        private final Map<String, Object> properties;
        private final Map<String, Function> functions;

        public ExtensionTypeInformation(Class<? extends EvaluationContextExtension> type) {
            Assert.notNull(type, "Extension type must not be null!");
            this.functions = discoverDeclaredFunctions(type);
            this.properties = EvaluationContextExtensionInformation.discoverDeclaredProperties(type, PublicMethodAndFieldFilter.STATIC);
        }

        public Map<String, Object> getProperties() {
            return this.properties;
        }

        public Map<String, Function> getFunctions() {
            return this.functions;
        }

        private static Map<String, Function> discoverDeclaredFunctions(Class<?> type) throws SecurityException, IllegalArgumentException {
            final Map<String, Function> map = new HashMap<>();
            ReflectionUtils.doWithMethods(type, new ReflectionUtils.MethodCallback() { // from class: org.springframework.data.repository.query.EvaluationContextExtensionInformation.ExtensionTypeInformation.1
                @Override // org.springframework.util.ReflectionUtils.MethodCallback
                public void doWith(Method method) throws IllegalAccessException, IllegalArgumentException {
                    if (Modifier.isPublic(method.getModifiers()) && Modifier.isStatic(method.getModifiers())) {
                        map.put(method.getName(), new Function(method, null));
                    }
                }
            });
            return map.isEmpty() ? Collections.emptyMap() : Collections.unmodifiableMap(map);
        }

        /* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/repository/query/EvaluationContextExtensionInformation$ExtensionTypeInformation$PublicMethodAndFieldFilter.class */
        static class PublicMethodAndFieldFilter implements ReflectionUtils.MethodFilter, ReflectionUtils.FieldFilter {
            public static final PublicMethodAndFieldFilter STATIC = new PublicMethodAndFieldFilter(true);
            public static final PublicMethodAndFieldFilter NON_STATIC = new PublicMethodAndFieldFilter(false);
            private final boolean staticOnly;

            public PublicMethodAndFieldFilter(boolean forStatic) {
                this.staticOnly = forStatic;
            }

            @Override // org.springframework.util.ReflectionUtils.MethodFilter
            public boolean matches(Method method) {
                if (ReflectionUtils.isObjectMethod(method)) {
                    return false;
                }
                boolean methodStatic = Modifier.isStatic(method.getModifiers());
                boolean staticMatch = this.staticOnly ? methodStatic : !methodStatic;
                return Modifier.isPublic(method.getModifiers()) && staticMatch;
            }

            @Override // org.springframework.util.ReflectionUtils.FieldFilter
            public boolean matches(Field field) {
                boolean fieldStatic = Modifier.isStatic(field.getModifiers());
                boolean staticMatch = this.staticOnly ? fieldStatic : !fieldStatic;
                return Modifier.isPublic(field.getModifiers()) && staticMatch;
            }
        }
    }

    /* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/repository/query/EvaluationContextExtensionInformation$RootObjectInformation.class */
    static class RootObjectInformation {
        private static final RootObjectInformation NONE = new RootObjectInformation(Object.class);
        private final Map<String, Method> accessors;
        private final Collection<Method> methods;
        private final Collection<Field> fields;

        public RootObjectInformation(Class<?> type) throws BeansException, SecurityException, IllegalArgumentException {
            Assert.notNull(type, "Type must not be null!");
            this.accessors = new HashMap();
            this.methods = new HashSet();
            this.fields = new ArrayList();
            if (Object.class.equals(type)) {
                return;
            }
            final PropertyDescriptor[] descriptors = BeanUtils.getPropertyDescriptors(type);
            ReflectionUtils.doWithMethods(type, new ReflectionUtils.MethodCallback() { // from class: org.springframework.data.repository.query.EvaluationContextExtensionInformation.RootObjectInformation.1
                @Override // org.springframework.util.ReflectionUtils.MethodCallback
                public void doWith(Method method) {
                    RootObjectInformation.this.methods.add(method);
                    for (PropertyDescriptor descriptor : descriptors) {
                        if (method.equals(descriptor.getReadMethod())) {
                            RootObjectInformation.this.accessors.put(descriptor.getName(), method);
                        }
                    }
                }
            }, ExtensionTypeInformation.PublicMethodAndFieldFilter.NON_STATIC);
            ReflectionUtils.doWithFields(type, new ReflectionUtils.FieldCallback() { // from class: org.springframework.data.repository.query.EvaluationContextExtensionInformation.RootObjectInformation.2
                @Override // org.springframework.util.ReflectionUtils.FieldCallback
                public void doWith(Field field) {
                    RootObjectInformation.this.fields.add(field);
                }
            }, ExtensionTypeInformation.PublicMethodAndFieldFilter.NON_STATIC);
        }

        public Map<String, Function> getFunctions(Object target) {
            if (target == null) {
                return Collections.emptyMap();
            }
            Map<String, Function> functions = new HashMap<>(this.methods.size());
            for (Method method : this.methods) {
                functions.put(method.getName(), new Function(method, target));
            }
            return Collections.unmodifiableMap(functions);
        }

        public Map<String, Object> getProperties(Object target) {
            if (target == null) {
                return Collections.emptyMap();
            }
            Map<String, Object> properties = new HashMap<>();
            for (Map.Entry<String, Method> method : this.accessors.entrySet()) {
                properties.put(method.getKey(), new Function(method.getValue(), target));
            }
            for (Field field : this.fields) {
                properties.put(field.getName(), ReflectionUtils.getField(field, target));
            }
            return Collections.unmodifiableMap(properties);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Map<String, Object> discoverDeclaredProperties(Class<?> type, ReflectionUtils.FieldFilter filter) throws IllegalArgumentException {
        final Map<String, Object> map = new HashMap<>();
        ReflectionUtils.doWithFields(type, new ReflectionUtils.FieldCallback() { // from class: org.springframework.data.repository.query.EvaluationContextExtensionInformation.1
            @Override // org.springframework.util.ReflectionUtils.FieldCallback
            public void doWith(Field field) throws IllegalAccessException {
                map.put(field.getName(), field.get(null));
            }
        }, filter);
        return map.isEmpty() ? Collections.emptyMap() : Collections.unmodifiableMap(map);
    }
}
