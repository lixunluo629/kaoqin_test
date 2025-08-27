package org.springframework.data.history;

import java.lang.Comparable;
import java.lang.Number;
import java.lang.annotation.Annotation;
import org.joda.time.DateTime;
import org.springframework.data.util.AnnotationDetectionFieldCallback;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/history/AnnotationRevisionMetadata.class */
public class AnnotationRevisionMetadata<N extends Number & Comparable<N>> implements RevisionMetadata<N> {
    private final Object entity;
    private final N revisionNumber;
    private final DateTime revisionDate;

    public AnnotationRevisionMetadata(Object obj, Class<? extends Annotation> cls, Class<? extends Annotation> cls2) throws IllegalArgumentException {
        Assert.notNull(obj, "Entity must not be null!");
        this.entity = obj;
        if (cls != null) {
            AnnotationDetectionFieldCallback annotationDetectionFieldCallback = new AnnotationDetectionFieldCallback(cls);
            ReflectionUtils.doWithFields(obj.getClass(), annotationDetectionFieldCallback);
            this.revisionNumber = (N) ((Number) annotationDetectionFieldCallback.getValue(obj));
        } else {
            this.revisionNumber = null;
        }
        if (cls2 != null) {
            AnnotationDetectionFieldCallback annotationDetectionFieldCallback2 = new AnnotationDetectionFieldCallback(cls2);
            ReflectionUtils.doWithFields(obj.getClass(), annotationDetectionFieldCallback2);
            this.revisionDate = new DateTime(annotationDetectionFieldCallback2.getValue(obj));
            return;
        }
        this.revisionDate = null;
    }

    @Override // org.springframework.data.history.RevisionMetadata
    public N getRevisionNumber() {
        return this.revisionNumber;
    }

    @Override // org.springframework.data.history.RevisionMetadata
    public DateTime getRevisionDate() {
        return this.revisionDate;
    }

    @Override // org.springframework.data.history.RevisionMetadata
    public <T> T getDelegate() {
        return (T) this.entity;
    }
}
