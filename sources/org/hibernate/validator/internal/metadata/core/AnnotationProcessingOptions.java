package org.hibernate.validator.internal.metadata.core;

import java.lang.reflect.Member;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/metadata/core/AnnotationProcessingOptions.class */
public interface AnnotationProcessingOptions {
    boolean areClassLevelConstraintsIgnoredFor(Class<?> cls);

    boolean areMemberConstraintsIgnoredFor(Member member);

    boolean areReturnValueConstraintsIgnoredFor(Member member);

    boolean areCrossParameterConstraintsIgnoredFor(Member member);

    boolean areParameterConstraintsIgnoredFor(Member member, int i);

    void merge(AnnotationProcessingOptions annotationProcessingOptions);
}
