package org.springframework.expression;

/* loaded from: spring-expression-4.3.25.RELEASE.jar:org/springframework/expression/TypeComparator.class */
public interface TypeComparator {
    boolean canCompare(Object obj, Object obj2);

    int compare(Object obj, Object obj2) throws EvaluationException;
}
