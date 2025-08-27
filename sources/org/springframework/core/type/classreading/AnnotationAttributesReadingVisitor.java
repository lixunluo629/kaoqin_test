package org.springframework.core.type.classreading;

import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.logging.LogConfigurationException;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.MultiValueMap;
import org.springframework.util.ObjectUtils;

/* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/core/type/classreading/AnnotationAttributesReadingVisitor.class */
final class AnnotationAttributesReadingVisitor extends RecursiveAnnotationAttributesVisitor {
    private final MultiValueMap<String, AnnotationAttributes> attributesMap;
    private final Map<String, Set<String>> metaAnnotationMap;

    public AnnotationAttributesReadingVisitor(String annotationType, MultiValueMap<String, AnnotationAttributes> attributesMap, Map<String, Set<String>> metaAnnotationMap, ClassLoader classLoader) {
        super(annotationType, new AnnotationAttributes(annotationType, classLoader), classLoader);
        this.attributesMap = attributesMap;
        this.metaAnnotationMap = metaAnnotationMap;
    }

    @Override // org.springframework.core.type.classreading.RecursiveAnnotationAttributesVisitor, org.springframework.asm.AnnotationVisitor
    public void visitEnd() throws LogConfigurationException {
        super.visitEnd();
        Class<? extends Annotation> annotationClass = this.attributes.annotationType();
        if (annotationClass != null) {
            List<AnnotationAttributes> attributeList = (List) this.attributesMap.get(this.annotationType);
            if (attributeList == null) {
                this.attributesMap.add(this.annotationType, this.attributes);
            } else {
                attributeList.add(0, this.attributes);
            }
            if (!AnnotationUtils.isInJavaLangAnnotationPackage(annotationClass.getName())) {
                Set<Annotation> visited = new LinkedHashSet<>();
                Annotation[] metaAnnotations = AnnotationUtils.getAnnotations(annotationClass);
                if (!ObjectUtils.isEmpty((Object[]) metaAnnotations)) {
                    for (Annotation metaAnnotation : metaAnnotations) {
                        recursivelyCollectMetaAnnotations(visited, metaAnnotation);
                    }
                }
                if (this.metaAnnotationMap != null) {
                    Set<String> metaAnnotationTypeNames = new LinkedHashSet<>(visited.size());
                    for (Annotation ann : visited) {
                        metaAnnotationTypeNames.add(ann.annotationType().getName());
                    }
                    this.metaAnnotationMap.put(annotationClass.getName(), metaAnnotationTypeNames);
                }
            }
        }
    }

    private void recursivelyCollectMetaAnnotations(Set<Annotation> visited, Annotation annotation) {
        Class<? extends Annotation> annotationType = annotation.annotationType();
        String annotationName = annotationType.getName();
        if (!AnnotationUtils.isInJavaLangAnnotationPackage(annotationName) && visited.add(annotation)) {
            try {
                if (Modifier.isPublic(annotationType.getModifiers())) {
                    this.attributesMap.add(annotationName, AnnotationUtils.getAnnotationAttributes(annotation, false, true));
                }
                for (Annotation metaMetaAnnotation : annotationType.getAnnotations()) {
                    recursivelyCollectMetaAnnotations(visited, metaMetaAnnotation);
                }
            } catch (Throwable ex) {
                if (this.logger.isDebugEnabled()) {
                    this.logger.debug("Failed to introspect meta-annotations on " + annotation + ": " + ex);
                }
            }
        }
    }
}
