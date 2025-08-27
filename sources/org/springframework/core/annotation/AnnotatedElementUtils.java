package org.springframework.core.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.logging.LogConfigurationException;
import org.springframework.core.BridgeMethodResolver;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/core/annotation/AnnotatedElementUtils.class */
public class AnnotatedElementUtils {
    private static final Boolean CONTINUE = null;
    private static final Annotation[] EMPTY_ANNOTATION_ARRAY = new Annotation[0];
    private static final Processor<Boolean> alwaysTrueAnnotationProcessor = new AlwaysTrueBooleanAnnotationProcessor();

    /* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/core/annotation/AnnotatedElementUtils$Processor.class */
    private interface Processor<T> {
        T process(AnnotatedElement annotatedElement, Annotation annotation, int i);

        void postProcess(AnnotatedElement annotatedElement, Annotation annotation, T t);

        boolean alwaysProcesses();

        boolean aggregates();

        List<T> getAggregatedResults();
    }

    public static AnnotatedElement forAnnotations(final Annotation... annotations) {
        return new AnnotatedElement() { // from class: org.springframework.core.annotation.AnnotatedElementUtils.1
            @Override // java.lang.reflect.AnnotatedElement
            public <T extends Annotation> T getAnnotation(Class<T> cls) {
                for (Annotation annotation : annotations) {
                    T t = (T) annotation;
                    if (t.annotationType() == cls) {
                        return t;
                    }
                }
                return null;
            }

            @Override // java.lang.reflect.AnnotatedElement
            public Annotation[] getAnnotations() {
                return annotations;
            }

            @Override // java.lang.reflect.AnnotatedElement
            public Annotation[] getDeclaredAnnotations() {
                return annotations;
            }
        };
    }

    public static Set<String> getMetaAnnotationTypes(AnnotatedElement element, Class<? extends Annotation> annotationType) {
        Assert.notNull(element, "AnnotatedElement must not be null");
        Assert.notNull(annotationType, "'annotationType' must not be null");
        return getMetaAnnotationTypes(element, element.getAnnotation(annotationType));
    }

    public static Set<String> getMetaAnnotationTypes(AnnotatedElement element, String annotationName) {
        Assert.notNull(element, "AnnotatedElement must not be null");
        Assert.hasLength(annotationName, "'annotationName' must not be null or empty");
        return getMetaAnnotationTypes(element, AnnotationUtils.getAnnotation(element, annotationName));
    }

    private static Set<String> getMetaAnnotationTypes(AnnotatedElement element, Annotation composed) {
        if (composed == null) {
            return null;
        }
        try {
            final Set<String> types = new LinkedHashSet<>();
            searchWithGetSemantics(composed.annotationType(), null, null, null, new SimpleAnnotationProcessor<Object>(true) { // from class: org.springframework.core.annotation.AnnotatedElementUtils.2
                @Override // org.springframework.core.annotation.AnnotatedElementUtils.Processor
                public Object process(AnnotatedElement annotatedElement, Annotation annotation, int metaDepth) {
                    types.add(annotation.annotationType().getName());
                    return AnnotatedElementUtils.CONTINUE;
                }
            }, new HashSet(), 1);
            if (types.isEmpty()) {
                return null;
            }
            return types;
        } catch (Throwable ex) {
            AnnotationUtils.rethrowAnnotationConfigurationException(ex);
            throw new IllegalStateException("Failed to introspect annotations on " + element, ex);
        }
    }

    public static boolean hasMetaAnnotationTypes(AnnotatedElement element, Class<? extends Annotation> annotationType) {
        Assert.notNull(element, "AnnotatedElement must not be null");
        Assert.notNull(annotationType, "'annotationType' must not be null");
        return hasMetaAnnotationTypes(element, annotationType, null);
    }

    public static boolean hasMetaAnnotationTypes(AnnotatedElement element, String annotationName) {
        Assert.notNull(element, "AnnotatedElement must not be null");
        Assert.hasLength(annotationName, "'annotationName' must not be null or empty");
        return hasMetaAnnotationTypes(element, null, annotationName);
    }

    private static boolean hasMetaAnnotationTypes(AnnotatedElement element, Class<? extends Annotation> annotationType, String annotationName) {
        return Boolean.TRUE.equals(searchWithGetSemantics(element, annotationType, annotationName, new SimpleAnnotationProcessor<Boolean>() { // from class: org.springframework.core.annotation.AnnotatedElementUtils.3
            @Override // org.springframework.core.annotation.AnnotatedElementUtils.Processor
            public Boolean process(AnnotatedElement annotatedElement, Annotation annotation, int metaDepth) {
                return metaDepth > 0 ? Boolean.TRUE : AnnotatedElementUtils.CONTINUE;
            }
        }));
    }

    public static boolean isAnnotated(AnnotatedElement element, Class<? extends Annotation> annotationType) {
        Assert.notNull(element, "AnnotatedElement must not be null");
        Assert.notNull(annotationType, "'annotationType' must not be null");
        if (element.isAnnotationPresent(annotationType)) {
            return true;
        }
        return Boolean.TRUE.equals(searchWithGetSemantics(element, annotationType, null, alwaysTrueAnnotationProcessor));
    }

    public static boolean isAnnotated(AnnotatedElement element, String annotationName) {
        Assert.notNull(element, "AnnotatedElement must not be null");
        Assert.hasLength(annotationName, "'annotationName' must not be null or empty");
        return Boolean.TRUE.equals(searchWithGetSemantics(element, null, annotationName, alwaysTrueAnnotationProcessor));
    }

    @Deprecated
    public static AnnotationAttributes getAnnotationAttributes(AnnotatedElement element, String annotationName) {
        return getMergedAnnotationAttributes(element, annotationName);
    }

    @Deprecated
    public static AnnotationAttributes getAnnotationAttributes(AnnotatedElement element, String annotationName, boolean classValuesAsString, boolean nestedAnnotationsAsMap) {
        return getMergedAnnotationAttributes(element, annotationName, classValuesAsString, nestedAnnotationsAsMap);
    }

    public static AnnotationAttributes getMergedAnnotationAttributes(AnnotatedElement element, Class<? extends Annotation> annotationType) {
        Assert.notNull(annotationType, "'annotationType' must not be null");
        AnnotationAttributes attributes = (AnnotationAttributes) searchWithGetSemantics(element, annotationType, null, new MergedAnnotationAttributesProcessor());
        AnnotationUtils.postProcessAnnotationAttributes(element, attributes, false, false);
        return attributes;
    }

    public static AnnotationAttributes getMergedAnnotationAttributes(AnnotatedElement element, String annotationName) {
        return getMergedAnnotationAttributes(element, annotationName, false, false);
    }

    public static AnnotationAttributes getMergedAnnotationAttributes(AnnotatedElement element, String annotationName, boolean classValuesAsString, boolean nestedAnnotationsAsMap) {
        Assert.hasLength(annotationName, "'annotationName' must not be null or empty");
        AnnotationAttributes attributes = (AnnotationAttributes) searchWithGetSemantics(element, null, annotationName, new MergedAnnotationAttributesProcessor(classValuesAsString, nestedAnnotationsAsMap));
        AnnotationUtils.postProcessAnnotationAttributes(element, attributes, classValuesAsString, nestedAnnotationsAsMap);
        return attributes;
    }

    public static <A extends Annotation> A getMergedAnnotation(AnnotatedElement annotatedElement, Class<A> cls) {
        Annotation annotation;
        Assert.notNull(cls, "'annotationType' must not be null");
        if (!(annotatedElement instanceof Class) && (annotation = annotatedElement.getAnnotation(cls)) != null) {
            return (A) AnnotationUtils.synthesizeAnnotation(annotation, annotatedElement);
        }
        return (A) AnnotationUtils.synthesizeAnnotation(getMergedAnnotationAttributes(annotatedElement, (Class<? extends Annotation>) cls), cls, annotatedElement);
    }

    public static <A extends Annotation> Set<A> getAllMergedAnnotations(AnnotatedElement element, Class<A> annotationType) {
        Assert.notNull(element, "AnnotatedElement must not be null");
        Assert.notNull(annotationType, "'annotationType' must not be null");
        MergedAnnotationAttributesProcessor processor = new MergedAnnotationAttributesProcessor(false, false, true);
        searchWithGetSemantics(element, annotationType, null, processor);
        return postProcessAndSynthesizeAggregatedResults(element, annotationType, processor.getAggregatedResults());
    }

    public static <A extends Annotation> Set<A> getMergedRepeatableAnnotations(AnnotatedElement element, Class<A> annotationType) {
        return getMergedRepeatableAnnotations(element, annotationType, null);
    }

    public static <A extends Annotation> Set<A> getMergedRepeatableAnnotations(AnnotatedElement element, Class<A> annotationType, Class<? extends Annotation> containerType) {
        Assert.notNull(element, "AnnotatedElement must not be null");
        Assert.notNull(annotationType, "'annotationType' must not be null");
        if (containerType == null) {
            containerType = resolveContainerType(annotationType);
        } else {
            validateContainerType(annotationType, containerType);
        }
        MergedAnnotationAttributesProcessor processor = new MergedAnnotationAttributesProcessor(false, false, true);
        searchWithGetSemantics(element, annotationType, null, containerType, processor);
        return postProcessAndSynthesizeAggregatedResults(element, annotationType, processor.getAggregatedResults());
    }

    public static MultiValueMap<String, Object> getAllAnnotationAttributes(AnnotatedElement element, String annotationName) {
        return getAllAnnotationAttributes(element, annotationName, false, false);
    }

    public static MultiValueMap<String, Object> getAllAnnotationAttributes(AnnotatedElement element, String annotationName, final boolean classValuesAsString, final boolean nestedAnnotationsAsMap) {
        final MultiValueMap<String, Object> attributesMap = new LinkedMultiValueMap<>();
        searchWithGetSemantics(element, null, annotationName, new SimpleAnnotationProcessor<Object>() { // from class: org.springframework.core.annotation.AnnotatedElementUtils.4
            @Override // org.springframework.core.annotation.AnnotatedElementUtils.Processor
            public Object process(AnnotatedElement annotatedElement, Annotation annotation, int metaDepth) {
                AnnotationAttributes annotationAttributes = AnnotationUtils.getAnnotationAttributes(annotation, classValuesAsString, nestedAnnotationsAsMap);
                for (Map.Entry<String, Object> entry : annotationAttributes.entrySet()) {
                    attributesMap.add(entry.getKey(), entry.getValue());
                }
                return AnnotatedElementUtils.CONTINUE;
            }
        });
        if (attributesMap.isEmpty()) {
            return null;
        }
        return attributesMap;
    }

    public static boolean hasAnnotation(AnnotatedElement element, Class<? extends Annotation> annotationType) {
        Assert.notNull(element, "AnnotatedElement must not be null");
        Assert.notNull(annotationType, "'annotationType' must not be null");
        if (element.isAnnotationPresent(annotationType)) {
            return true;
        }
        return Boolean.TRUE.equals(searchWithFindSemantics(element, annotationType, null, alwaysTrueAnnotationProcessor));
    }

    public static AnnotationAttributes findMergedAnnotationAttributes(AnnotatedElement element, Class<? extends Annotation> annotationType, boolean classValuesAsString, boolean nestedAnnotationsAsMap) {
        AnnotationAttributes attributes = (AnnotationAttributes) searchWithFindSemantics(element, annotationType, null, new MergedAnnotationAttributesProcessor(classValuesAsString, nestedAnnotationsAsMap));
        AnnotationUtils.postProcessAnnotationAttributes(element, attributes, classValuesAsString, nestedAnnotationsAsMap);
        return attributes;
    }

    public static AnnotationAttributes findMergedAnnotationAttributes(AnnotatedElement element, String annotationName, boolean classValuesAsString, boolean nestedAnnotationsAsMap) {
        AnnotationAttributes attributes = (AnnotationAttributes) searchWithFindSemantics(element, null, annotationName, new MergedAnnotationAttributesProcessor(classValuesAsString, nestedAnnotationsAsMap));
        AnnotationUtils.postProcessAnnotationAttributes(element, attributes, classValuesAsString, nestedAnnotationsAsMap);
        return attributes;
    }

    public static <A extends Annotation> A findMergedAnnotation(AnnotatedElement annotatedElement, Class<A> cls) {
        Annotation annotation;
        Assert.notNull(cls, "'annotationType' must not be null");
        if (!(annotatedElement instanceof Class) && (annotation = annotatedElement.getAnnotation(cls)) != null) {
            return (A) AnnotationUtils.synthesizeAnnotation(annotation, annotatedElement);
        }
        return (A) AnnotationUtils.synthesizeAnnotation(findMergedAnnotationAttributes(annotatedElement, (Class<? extends Annotation>) cls, false, false), cls, annotatedElement);
    }

    @Deprecated
    public static <A extends Annotation> A findMergedAnnotation(AnnotatedElement annotatedElement, String str) {
        AnnotationAttributes annotationAttributesFindMergedAnnotationAttributes = findMergedAnnotationAttributes(annotatedElement, str, false, false);
        return (A) AnnotationUtils.synthesizeAnnotation(annotationAttributesFindMergedAnnotationAttributes, annotationAttributesFindMergedAnnotationAttributes.annotationType(), annotatedElement);
    }

    public static <A extends Annotation> Set<A> findAllMergedAnnotations(AnnotatedElement element, Class<A> annotationType) {
        Assert.notNull(element, "AnnotatedElement must not be null");
        Assert.notNull(annotationType, "'annotationType' must not be null");
        MergedAnnotationAttributesProcessor processor = new MergedAnnotationAttributesProcessor(false, false, true);
        searchWithFindSemantics(element, annotationType, null, processor);
        return postProcessAndSynthesizeAggregatedResults(element, annotationType, processor.getAggregatedResults());
    }

    public static <A extends Annotation> Set<A> findMergedRepeatableAnnotations(AnnotatedElement element, Class<A> annotationType) {
        return findMergedRepeatableAnnotations(element, annotationType, null);
    }

    public static <A extends Annotation> Set<A> findMergedRepeatableAnnotations(AnnotatedElement element, Class<A> annotationType, Class<? extends Annotation> containerType) throws LogConfigurationException {
        Assert.notNull(element, "AnnotatedElement must not be null");
        Assert.notNull(annotationType, "'annotationType' must not be null");
        if (containerType == null) {
            containerType = resolveContainerType(annotationType);
        } else {
            validateContainerType(annotationType, containerType);
        }
        MergedAnnotationAttributesProcessor processor = new MergedAnnotationAttributesProcessor(false, false, true);
        searchWithFindSemantics(element, annotationType, null, containerType, processor);
        return postProcessAndSynthesizeAggregatedResults(element, annotationType, processor.getAggregatedResults());
    }

    private static <T> T searchWithGetSemantics(AnnotatedElement annotatedElement, Class<? extends Annotation> cls, String str, Processor<T> processor) {
        return (T) searchWithGetSemantics(annotatedElement, cls, str, null, processor);
    }

    private static <T> T searchWithGetSemantics(AnnotatedElement annotatedElement, Class<? extends Annotation> cls, String str, Class<? extends Annotation> cls2, Processor<T> processor) {
        try {
            return (T) searchWithGetSemantics(annotatedElement, cls, str, cls2, processor, new HashSet(), 0);
        } catch (Throwable th) {
            AnnotationUtils.rethrowAnnotationConfigurationException(th);
            throw new IllegalStateException("Failed to introspect annotations on " + annotatedElement, th);
        }
    }

    private static <T> T searchWithGetSemantics(AnnotatedElement annotatedElement, Class<? extends Annotation> cls, String str, Class<? extends Annotation> cls2, Processor<T> processor, Set<AnnotatedElement> set, int i) throws LogConfigurationException {
        Assert.notNull(annotatedElement, "AnnotatedElement must not be null");
        if (set.add(annotatedElement)) {
            try {
                List listAsList = Arrays.asList(annotatedElement.getDeclaredAnnotations());
                T t = (T) searchWithGetSemanticsInAnnotations(annotatedElement, listAsList, cls, str, cls2, processor, set, i);
                if (t != null) {
                    return t;
                }
                if (annotatedElement instanceof Class) {
                    ArrayList arrayList = new ArrayList();
                    for (Annotation annotation : annotatedElement.getAnnotations()) {
                        if (!listAsList.contains(annotation)) {
                            arrayList.add(annotation);
                        }
                    }
                    T t2 = (T) searchWithGetSemanticsInAnnotations(annotatedElement, arrayList, cls, str, cls2, processor, set, i);
                    if (t2 != null) {
                        return t2;
                    }
                    return null;
                }
                return null;
            } catch (Throwable th) {
                AnnotationUtils.handleIntrospectionFailure(annotatedElement, th);
                return null;
            }
        }
        return null;
    }

    private static <T> T searchWithGetSemanticsInAnnotations(AnnotatedElement annotatedElement, List<Annotation> list, Class<? extends Annotation> cls, String str, Class<? extends Annotation> cls2, Processor<T> processor, Set<AnnotatedElement> set, int i) throws LogConfigurationException {
        T t;
        for (Annotation annotation : list) {
            Class<? extends Annotation> clsAnnotationType = annotation.annotationType();
            if (!AnnotationUtils.isInJavaLangAnnotationPackage(clsAnnotationType)) {
                if (clsAnnotationType == cls || clsAnnotationType.getName().equals(str) || processor.alwaysProcesses()) {
                    T tProcess = processor.process(annotatedElement, annotation, i);
                    if (tProcess == null) {
                        continue;
                    } else if (processor.aggregates() && i == 0) {
                        processor.getAggregatedResults().add(tProcess);
                    } else {
                        return tProcess;
                    }
                } else if (clsAnnotationType == cls2) {
                    for (Annotation annotation2 : getRawAnnotationsFromContainer(annotatedElement, annotation)) {
                        T tProcess2 = processor.process(annotatedElement, annotation2, i);
                        if (tProcess2 != null) {
                            processor.getAggregatedResults().add(tProcess2);
                        }
                    }
                }
            }
        }
        for (Annotation annotation3 : list) {
            Class<? extends Annotation> clsAnnotationType2 = annotation3.annotationType();
            if (!AnnotationUtils.isInJavaLangAnnotationPackage(clsAnnotationType2) && (t = (T) searchWithGetSemantics(clsAnnotationType2, cls, str, cls2, processor, set, i + 1)) != null) {
                processor.postProcess(annotatedElement, annotation3, t);
                if (processor.aggregates() && i == 0) {
                    processor.getAggregatedResults().add(t);
                } else {
                    return t;
                }
            }
        }
        return null;
    }

    private static <T> T searchWithFindSemantics(AnnotatedElement annotatedElement, Class<? extends Annotation> cls, String str, Processor<T> processor) {
        return (T) searchWithFindSemantics(annotatedElement, cls, str, null, processor);
    }

    private static <T> T searchWithFindSemantics(AnnotatedElement annotatedElement, Class<? extends Annotation> cls, String str, Class<? extends Annotation> cls2, Processor<T> processor) {
        if (cls2 != null && !processor.aggregates()) {
            throw new IllegalArgumentException("Searches for repeatable annotations must supply an aggregating Processor");
        }
        try {
            return (T) searchWithFindSemantics(annotatedElement, cls, str, cls2, processor, new HashSet(), 0);
        } catch (Throwable th) {
            AnnotationUtils.rethrowAnnotationConfigurationException(th);
            throw new IllegalStateException("Failed to introspect annotations on " + annotatedElement, th);
        }
    }

    private static <T> T searchWithFindSemantics(AnnotatedElement annotatedElement, Class<? extends Annotation> cls, String str, Class<? extends Annotation> cls2, Processor<T> processor, Set<AnnotatedElement> set, int i) throws LogConfigurationException {
        T t;
        T t2;
        T t3;
        T t4;
        T t5;
        T t6;
        Assert.notNull(annotatedElement, "AnnotatedElement must not be null");
        if (set.add(annotatedElement)) {
            try {
                Annotation[] declaredAnnotations = annotatedElement.getDeclaredAnnotations();
                if (declaredAnnotations.length > 0) {
                    ArrayList arrayList = processor.aggregates() ? new ArrayList() : null;
                    for (Annotation annotation : declaredAnnotations) {
                        Class<? extends Annotation> clsAnnotationType = annotation.annotationType();
                        if (!AnnotationUtils.isInJavaLangAnnotationPackage(clsAnnotationType)) {
                            if (clsAnnotationType == cls || clsAnnotationType.getName().equals(str) || processor.alwaysProcesses()) {
                                T tProcess = processor.process(annotatedElement, annotation, i);
                                if (tProcess != null) {
                                    if (arrayList != null && i == 0) {
                                        arrayList.add(tProcess);
                                    } else {
                                        return tProcess;
                                    }
                                }
                            } else if (clsAnnotationType == cls2) {
                                for (Annotation annotation2 : getRawAnnotationsFromContainer(annotatedElement, annotation)) {
                                    T tProcess2 = processor.process(annotatedElement, annotation2, i);
                                    if (arrayList != null && tProcess2 != null) {
                                        arrayList.add(tProcess2);
                                    }
                                }
                            }
                        }
                    }
                    for (Annotation annotation3 : declaredAnnotations) {
                        Class<? extends Annotation> clsAnnotationType2 = annotation3.annotationType();
                        if (!AnnotationUtils.isInJavaLangAnnotationPackage(clsAnnotationType2) && (t6 = (T) searchWithFindSemantics(clsAnnotationType2, cls, str, cls2, processor, set, i + 1)) != null) {
                            processor.postProcess(clsAnnotationType2, annotation3, t6);
                            if (arrayList != null && i == 0) {
                                arrayList.add(t6);
                            } else {
                                return t6;
                            }
                        }
                    }
                    if (!CollectionUtils.isEmpty(arrayList)) {
                        processor.getAggregatedResults().addAll(0, arrayList);
                    }
                }
                if (annotatedElement instanceof Method) {
                    Method method = (Method) annotatedElement;
                    Method methodFindBridgedMethod = BridgeMethodResolver.findBridgedMethod(method);
                    if (methodFindBridgedMethod != method && (t5 = (T) searchWithFindSemantics(methodFindBridgedMethod, cls, str, cls2, processor, set, i)) != null) {
                        return t5;
                    }
                    Class<?>[] interfaces = method.getDeclaringClass().getInterfaces();
                    if (interfaces.length > 0 && (t4 = (T) searchOnInterfaces(method, cls, str, cls2, processor, set, i, interfaces)) != null) {
                        return t4;
                    }
                    Class<?> declaringClass = method.getDeclaringClass();
                    do {
                        declaringClass = declaringClass.getSuperclass();
                        if (declaringClass != null && Object.class != declaringClass) {
                            try {
                                t3 = (T) searchWithFindSemantics(BridgeMethodResolver.findBridgedMethod(declaringClass.getDeclaredMethod(method.getName(), method.getParameterTypes())), cls, str, cls2, processor, set, i);
                            } catch (NoSuchMethodException e) {
                            }
                            if (t3 != null) {
                                return t3;
                            }
                            t2 = (T) searchOnInterfaces(method, cls, str, cls2, processor, set, i, declaringClass.getInterfaces());
                        }
                        return null;
                    } while (t2 == null);
                    return t2;
                }
                if (annotatedElement instanceof Class) {
                    Class cls3 = (Class) annotatedElement;
                    for (Class<?> cls4 : cls3.getInterfaces()) {
                        T t7 = (T) searchWithFindSemantics(cls4, cls, str, cls2, processor, set, i);
                        if (t7 != null) {
                            return t7;
                        }
                    }
                    Class<? super T> superclass = cls3.getSuperclass();
                    if (superclass != null && Object.class != superclass && (t = (T) searchWithFindSemantics(superclass, cls, str, cls2, processor, set, i)) != null) {
                        return t;
                    }
                    return null;
                }
                return null;
            } catch (Throwable th) {
                AnnotationUtils.handleIntrospectionFailure(annotatedElement, th);
                return null;
            }
        }
        return null;
    }

    private static <T> T searchOnInterfaces(Method method, Class<? extends Annotation> cls, String str, Class<? extends Annotation> cls2, Processor<T> processor, Set<AnnotatedElement> set, int i, Class<?>[] clsArr) {
        for (Class<?> cls3 : clsArr) {
            if (AnnotationUtils.isInterfaceWithAnnotatedMethods(cls3)) {
                try {
                    T t = (T) searchWithFindSemantics(cls3.getMethod(method.getName(), method.getParameterTypes()), cls, str, cls2, processor, set, i);
                    if (t != null) {
                        return t;
                    }
                } catch (NoSuchMethodException e) {
                }
            }
        }
        return null;
    }

    private static <A extends Annotation> A[] getRawAnnotationsFromContainer(AnnotatedElement annotatedElement, Annotation annotation) throws LogConfigurationException {
        try {
            return (A[]) ((Annotation[]) AnnotationUtils.getValue(annotation));
        } catch (Throwable th) {
            AnnotationUtils.handleIntrospectionFailure(annotatedElement, th);
            return (A[]) EMPTY_ANNOTATION_ARRAY;
        }
    }

    private static Class<? extends Annotation> resolveContainerType(Class<? extends Annotation> annotationType) throws LogConfigurationException {
        Class<? extends Annotation> containerType = AnnotationUtils.resolveContainerAnnotationType(annotationType);
        if (containerType == null) {
            throw new IllegalArgumentException("Annotation type must be a repeatable annotation: failed to resolve container type for " + annotationType.getName());
        }
        return containerType;
    }

    private static void validateContainerType(Class<? extends Annotation> annotationType, Class<? extends Annotation> containerType) {
        try {
            Method method = containerType.getDeclaredMethod("value", new Class[0]);
            Class<?> returnType = method.getReturnType();
            if (!returnType.isArray() || returnType.getComponentType() != annotationType) {
                String msg = String.format("Container type [%s] must declare a 'value' attribute for an array of type [%s]", containerType.getName(), annotationType.getName());
                throw new AnnotationConfigurationException(msg);
            }
        } catch (Throwable ex) {
            AnnotationUtils.rethrowAnnotationConfigurationException(ex);
            String msg2 = String.format("Invalid declaration of container type [%s] for repeatable annotation [%s]", containerType.getName(), annotationType.getName());
            throw new AnnotationConfigurationException(msg2, ex);
        }
    }

    private static <A extends Annotation> Set<A> postProcessAndSynthesizeAggregatedResults(AnnotatedElement element, Class<A> annotationType, List<AnnotationAttributes> aggregatedResults) {
        LinkedHashSet linkedHashSet = new LinkedHashSet();
        for (AnnotationAttributes attributes : aggregatedResults) {
            AnnotationUtils.postProcessAnnotationAttributes(element, attributes, false, false);
            linkedHashSet.add(AnnotationUtils.synthesizeAnnotation(attributes, annotationType, element));
        }
        return linkedHashSet;
    }

    /* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/core/annotation/AnnotatedElementUtils$SimpleAnnotationProcessor.class */
    private static abstract class SimpleAnnotationProcessor<T> implements Processor<T> {
        private final boolean alwaysProcesses;

        public SimpleAnnotationProcessor() {
            this(false);
        }

        public SimpleAnnotationProcessor(boolean alwaysProcesses) {
            this.alwaysProcesses = alwaysProcesses;
        }

        @Override // org.springframework.core.annotation.AnnotatedElementUtils.Processor
        public final boolean alwaysProcesses() {
            return this.alwaysProcesses;
        }

        @Override // org.springframework.core.annotation.AnnotatedElementUtils.Processor
        public final void postProcess(AnnotatedElement annotatedElement, Annotation annotation, T result) {
        }

        @Override // org.springframework.core.annotation.AnnotatedElementUtils.Processor
        public final boolean aggregates() {
            return false;
        }

        @Override // org.springframework.core.annotation.AnnotatedElementUtils.Processor
        public final List<T> getAggregatedResults() {
            throw new UnsupportedOperationException("SimpleAnnotationProcessor does not support aggregated results");
        }
    }

    /* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/core/annotation/AnnotatedElementUtils$AlwaysTrueBooleanAnnotationProcessor.class */
    static class AlwaysTrueBooleanAnnotationProcessor extends SimpleAnnotationProcessor<Boolean> {
        AlwaysTrueBooleanAnnotationProcessor() {
        }

        @Override // org.springframework.core.annotation.AnnotatedElementUtils.Processor
        public final Boolean process(AnnotatedElement annotatedElement, Annotation annotation, int metaDepth) {
            return Boolean.TRUE;
        }
    }

    /* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/core/annotation/AnnotatedElementUtils$MergedAnnotationAttributesProcessor.class */
    private static class MergedAnnotationAttributesProcessor implements Processor<AnnotationAttributes> {
        private final boolean classValuesAsString;
        private final boolean nestedAnnotationsAsMap;
        private final boolean aggregates;
        private final List<AnnotationAttributes> aggregatedResults;

        MergedAnnotationAttributesProcessor() {
            this(false, false, false);
        }

        MergedAnnotationAttributesProcessor(boolean classValuesAsString, boolean nestedAnnotationsAsMap) {
            this(classValuesAsString, nestedAnnotationsAsMap, false);
        }

        MergedAnnotationAttributesProcessor(boolean classValuesAsString, boolean nestedAnnotationsAsMap, boolean aggregates) {
            this.classValuesAsString = classValuesAsString;
            this.nestedAnnotationsAsMap = nestedAnnotationsAsMap;
            this.aggregates = aggregates;
            this.aggregatedResults = aggregates ? new ArrayList() : null;
        }

        @Override // org.springframework.core.annotation.AnnotatedElementUtils.Processor
        public boolean alwaysProcesses() {
            return false;
        }

        @Override // org.springframework.core.annotation.AnnotatedElementUtils.Processor
        public boolean aggregates() {
            return this.aggregates;
        }

        @Override // org.springframework.core.annotation.AnnotatedElementUtils.Processor
        public List<AnnotationAttributes> getAggregatedResults() {
            return this.aggregatedResults;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // org.springframework.core.annotation.AnnotatedElementUtils.Processor
        public AnnotationAttributes process(AnnotatedElement annotatedElement, Annotation annotation, int metaDepth) {
            return AnnotationUtils.retrieveAnnotationAttributes(annotatedElement, annotation, this.classValuesAsString, this.nestedAnnotationsAsMap);
        }

        @Override // org.springframework.core.annotation.AnnotatedElementUtils.Processor
        public void postProcess(AnnotatedElement element, Annotation annotation, AnnotationAttributes attributes) throws LogConfigurationException {
            Annotation annotation2 = AnnotationUtils.synthesizeAnnotation(annotation, element);
            Class<? extends Annotation> targetAnnotationType = attributes.annotationType();
            Set<String> valuesAlreadyReplaced = new HashSet<>();
            for (Method attributeMethod : AnnotationUtils.getAttributeMethods(annotation2.annotationType())) {
                String attributeName = attributeMethod.getName();
                String attributeOverrideName = AnnotationUtils.getAttributeOverrideName(attributeMethod, targetAnnotationType);
                if (attributeOverrideName != null) {
                    if (!valuesAlreadyReplaced.contains(attributeOverrideName)) {
                        List<String> targetAttributeNames = new ArrayList<>();
                        targetAttributeNames.add(attributeOverrideName);
                        valuesAlreadyReplaced.add(attributeOverrideName);
                        List<String> aliases = AnnotationUtils.getAttributeAliasMap(targetAnnotationType).get(attributeOverrideName);
                        if (aliases != null) {
                            for (String alias : aliases) {
                                if (!valuesAlreadyReplaced.contains(alias)) {
                                    targetAttributeNames.add(alias);
                                    valuesAlreadyReplaced.add(alias);
                                }
                            }
                        }
                        overrideAttributes(element, annotation2, attributes, attributeName, targetAttributeNames);
                    }
                } else if (!"value".equals(attributeName) && attributes.containsKey(attributeName)) {
                    overrideAttribute(element, annotation2, attributes, attributeName, attributeName);
                }
            }
        }

        private void overrideAttributes(AnnotatedElement element, Annotation annotation, AnnotationAttributes attributes, String sourceAttributeName, List<String> targetAttributeNames) throws LogConfigurationException {
            Object adaptedValue = getAdaptedValue(element, annotation, sourceAttributeName);
            for (String targetAttributeName : targetAttributeNames) {
                attributes.put(targetAttributeName, adaptedValue);
            }
        }

        private void overrideAttribute(AnnotatedElement element, Annotation annotation, AnnotationAttributes attributes, String sourceAttributeName, String targetAttributeName) {
            attributes.put(targetAttributeName, getAdaptedValue(element, annotation, sourceAttributeName));
        }

        private Object getAdaptedValue(AnnotatedElement element, Annotation annotation, String sourceAttributeName) throws LogConfigurationException {
            Object value = AnnotationUtils.getValue(annotation, sourceAttributeName);
            return AnnotationUtils.adaptValue(element, value, this.classValuesAsString, this.nestedAnnotationsAsMap);
        }
    }
}
