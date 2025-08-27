package org.springframework.hateoas.core;

import java.util.HashMap;
import java.util.Map;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.Order;
import org.springframework.hateoas.RelProvider;

@Order(100)
/* loaded from: spring-hateoas-0.23.0.RELEASE.jar:org/springframework/hateoas/core/AnnotationRelProvider.class */
public class AnnotationRelProvider implements RelProvider {
    private final Map<Class<?>, Relation> annotationCache = new HashMap();

    @Override // org.springframework.hateoas.RelProvider
    public String getCollectionResourceRelFor(Class<?> type) {
        Relation annotation = lookupAnnotation(type);
        if (annotation == null || "".equals(annotation.collectionRelation())) {
            return null;
        }
        return annotation.collectionRelation();
    }

    @Override // org.springframework.hateoas.RelProvider
    public String getItemResourceRelFor(Class<?> type) {
        Relation annotation = lookupAnnotation(type);
        if (annotation == null || "".equals(annotation.value())) {
            return null;
        }
        return annotation.value();
    }

    @Override // org.springframework.plugin.core.Plugin
    public boolean supports(Class<?> delimiter) {
        return lookupAnnotation(delimiter) != null;
    }

    private Relation lookupAnnotation(Class<?> type) {
        Relation relation = this.annotationCache.get(type);
        if (relation != null) {
            return relation;
        }
        Relation relation2 = (Relation) AnnotationUtils.getAnnotation(type, Relation.class);
        if (relation2 != null) {
            this.annotationCache.put(type, relation2);
        }
        return relation2;
    }
}
