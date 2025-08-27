package org.springframework.data.keyvalue.core.mapping;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;
import org.apache.xmlbeans.impl.jam.xml.JamXmlElements;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.style.ToStringCreator;
import org.springframework.data.annotation.Persistent;
import org.springframework.data.keyvalue.annotation.KeySpace;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.ObjectUtils;

/* loaded from: spring-data-keyvalue-1.2.23.RELEASE.jar:org/springframework/data/keyvalue/core/mapping/AnnotationBasedKeySpaceResolver.class */
enum AnnotationBasedKeySpaceResolver implements KeySpaceResolver {
    INSTANCE;

    @Override // org.springframework.data.keyvalue.core.mapping.KeySpaceResolver
    public String resolveKeySpace(Class<?> type) throws SecurityException {
        Assert.notNull(type, "Type for keyspace for null!");
        Class<?> userClass = ClassUtils.getUserClass(type);
        Object keySpace = getKeySpace(userClass);
        if (keySpace != null) {
            return keySpace.toString();
        }
        return null;
    }

    private static Object getKeySpace(Class<?> type) throws SecurityException {
        KeySpace keyspace = (KeySpace) AnnotatedElementUtils.findMergedAnnotation(type, KeySpace.class);
        if (keyspace != null) {
            return AnnotationUtils.getValue(keyspace);
        }
        MetaAnnotationUtils.AnnotationDescriptor<Persistent> descriptor = MetaAnnotationUtils.findAnnotationDescriptor(type, Persistent.class);
        if (descriptor != null && descriptor.getComposedAnnotation() != null) {
            Annotation composed = descriptor.getComposedAnnotation();
            for (Method method : descriptor.getComposedAnnotationType().getDeclaredMethods()) {
                if (((KeySpace) AnnotationUtils.findAnnotation(method, KeySpace.class)) != null) {
                    return AnnotationUtils.getValue(composed, method.getName());
                }
            }
            return null;
        }
        return null;
    }

    /* loaded from: spring-data-keyvalue-1.2.23.RELEASE.jar:org/springframework/data/keyvalue/core/mapping/AnnotationBasedKeySpaceResolver$MetaAnnotationUtils.class */
    static abstract class MetaAnnotationUtils {
        private MetaAnnotationUtils() {
        }

        public static <T extends Annotation> AnnotationDescriptor<T> findAnnotationDescriptor(Class<?> clazz, Class<T> annotationType) {
            return findAnnotationDescriptor(clazz, new HashSet(), annotationType);
        }

        /* JADX WARN: Multi-variable type inference failed */
        private static <T extends Annotation> AnnotationDescriptor<T> findAnnotationDescriptor(Class<?> clazz, Set<Annotation> visited, Class<T> cls) {
            AnnotationDescriptor<T> descriptor;
            Assert.notNull(cls, "Annotation type must not be null");
            if (clazz == null || clazz.equals(Object.class)) {
                return null;
            }
            if (AnnotationUtils.isAnnotationDeclaredLocally(cls, clazz)) {
                return new AnnotationDescriptor<>(clazz, clazz.getAnnotation(cls));
            }
            for (Annotation composedAnnotation : clazz.getDeclaredAnnotations()) {
                if (!AnnotationUtils.isInJavaLangAnnotationPackage(composedAnnotation) && visited.add(composedAnnotation) && (descriptor = findAnnotationDescriptor(composedAnnotation.annotationType(), visited, cls)) != null) {
                    return new AnnotationDescriptor<>(clazz, descriptor.getDeclaringClass(), composedAnnotation, descriptor.getAnnotation());
                }
            }
            return findAnnotationDescriptor(clazz.getSuperclass(), visited, cls);
        }

        public static UntypedAnnotationDescriptor findAnnotationDescriptorForTypes(Class<?> clazz, Class<? extends Annotation>... annotationTypes) {
            return findAnnotationDescriptorForTypes(clazz, new HashSet(), annotationTypes);
        }

        /* JADX WARN: Multi-variable type inference failed */
        private static UntypedAnnotationDescriptor findAnnotationDescriptorForTypes(Class<?> clazz, Set<Annotation> visited, Class<? extends Annotation>... clsArr) {
            UntypedAnnotationDescriptor descriptor;
            assertNonEmptyAnnotationTypeArray(clsArr, "The list of annotation types must not be empty");
            if (clazz == null || clazz.equals(Object.class)) {
                return null;
            }
            for (Class<A> cls : clsArr) {
                if (AnnotationUtils.isAnnotationDeclaredLocally(cls, clazz)) {
                    return new UntypedAnnotationDescriptor(clazz, clazz.getAnnotation(cls));
                }
            }
            for (Annotation composedAnnotation : clazz.getDeclaredAnnotations()) {
                if (!AnnotationUtils.isInJavaLangAnnotationPackage(composedAnnotation) && visited.add(composedAnnotation) && (descriptor = findAnnotationDescriptorForTypes(composedAnnotation.annotationType(), visited, clsArr)) != null) {
                    return new UntypedAnnotationDescriptor(clazz, descriptor.getDeclaringClass(), composedAnnotation, descriptor.getAnnotation());
                }
            }
            return findAnnotationDescriptorForTypes(clazz.getSuperclass(), visited, clsArr);
        }

        /* loaded from: spring-data-keyvalue-1.2.23.RELEASE.jar:org/springframework/data/keyvalue/core/mapping/AnnotationBasedKeySpaceResolver$MetaAnnotationUtils$AnnotationDescriptor.class */
        public static class AnnotationDescriptor<T extends Annotation> {
            private final Class<?> rootDeclaringClass;
            private final Class<?> declaringClass;
            private final Annotation composedAnnotation;
            private final T annotation;
            private final AnnotationAttributes annotationAttributes;

            public AnnotationDescriptor(Class<?> rootDeclaringClass, T annotation) {
                this(rootDeclaringClass, rootDeclaringClass, null, annotation);
            }

            public AnnotationDescriptor(Class<?> rootDeclaringClass, Class<?> declaringClass, Annotation composedAnnotation, T annotation) {
                Assert.notNull(rootDeclaringClass, "rootDeclaringClass must not be null");
                Assert.notNull(annotation, "annotation must not be null");
                this.rootDeclaringClass = rootDeclaringClass;
                this.declaringClass = declaringClass;
                this.composedAnnotation = composedAnnotation;
                this.annotation = annotation;
                this.annotationAttributes = AnnotatedElementUtils.findMergedAnnotationAttributes((AnnotatedElement) rootDeclaringClass, annotation.annotationType(), false, false);
            }

            public Class<?> getRootDeclaringClass() {
                return this.rootDeclaringClass;
            }

            public Class<?> getDeclaringClass() {
                return this.declaringClass;
            }

            public T getAnnotation() {
                return this.annotation;
            }

            public Class<? extends Annotation> getAnnotationType() {
                return this.annotation.annotationType();
            }

            public AnnotationAttributes getAnnotationAttributes() {
                return this.annotationAttributes;
            }

            public Annotation getComposedAnnotation() {
                return this.composedAnnotation;
            }

            public Class<? extends Annotation> getComposedAnnotationType() {
                if (this.composedAnnotation == null) {
                    return null;
                }
                return this.composedAnnotation.annotationType();
            }

            public String toString() {
                return new ToStringCreator(this).append("rootDeclaringClass", this.rootDeclaringClass).append("declaringClass", this.declaringClass).append("composedAnnotation", this.composedAnnotation).append(JamXmlElements.ANNOTATION, this.annotation).toString();
            }
        }

        /* loaded from: spring-data-keyvalue-1.2.23.RELEASE.jar:org/springframework/data/keyvalue/core/mapping/AnnotationBasedKeySpaceResolver$MetaAnnotationUtils$UntypedAnnotationDescriptor.class */
        public static class UntypedAnnotationDescriptor extends AnnotationDescriptor<Annotation> {
            public UntypedAnnotationDescriptor(Class<?> rootDeclaringClass, Annotation annotation) {
                this(rootDeclaringClass, rootDeclaringClass, null, annotation);
            }

            public UntypedAnnotationDescriptor(Class<?> rootDeclaringClass, Class<?> declaringClass, Annotation composedAnnotation, Annotation annotation) {
                super(rootDeclaringClass, declaringClass, composedAnnotation, annotation);
            }
        }

        private static void assertNonEmptyAnnotationTypeArray(Class<?>[] annotationTypes, String message) {
            if (ObjectUtils.isEmpty((Object[]) annotationTypes)) {
                throw new IllegalArgumentException(message);
            }
            for (Class<?> clazz : annotationTypes) {
                if (!Annotation.class.isAssignableFrom(clazz)) {
                    throw new IllegalArgumentException("Array elements must be of type Annotation");
                }
            }
        }
    }
}
