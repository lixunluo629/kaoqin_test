package org.springframework.expression;

/* loaded from: spring-expression-4.3.25.RELEASE.jar:org/springframework/expression/PropertyAccessor.class */
public interface PropertyAccessor {
    Class<?>[] getSpecificTargetClasses();

    boolean canRead(EvaluationContext evaluationContext, Object obj, String str) throws AccessException;

    TypedValue read(EvaluationContext evaluationContext, Object obj, String str) throws AccessException;

    boolean canWrite(EvaluationContext evaluationContext, Object obj, String str) throws AccessException;

    void write(EvaluationContext evaluationContext, Object obj, String str, Object obj2) throws AccessException;
}
