package org.springframework.expression;

import org.springframework.core.convert.TypeDescriptor;

/* loaded from: spring-expression-4.3.25.RELEASE.jar:org/springframework/expression/Expression.class */
public interface Expression {
    String getExpressionString();

    Object getValue() throws EvaluationException;

    <T> T getValue(Class<T> cls) throws EvaluationException;

    Object getValue(Object obj) throws EvaluationException;

    <T> T getValue(Object obj, Class<T> cls) throws EvaluationException;

    Object getValue(EvaluationContext evaluationContext) throws EvaluationException;

    Object getValue(EvaluationContext evaluationContext, Object obj) throws EvaluationException;

    <T> T getValue(EvaluationContext evaluationContext, Class<T> cls) throws EvaluationException;

    <T> T getValue(EvaluationContext evaluationContext, Object obj, Class<T> cls) throws EvaluationException;

    Class<?> getValueType() throws EvaluationException;

    Class<?> getValueType(Object obj) throws EvaluationException;

    Class<?> getValueType(EvaluationContext evaluationContext) throws EvaluationException;

    Class<?> getValueType(EvaluationContext evaluationContext, Object obj) throws EvaluationException;

    TypeDescriptor getValueTypeDescriptor() throws EvaluationException;

    TypeDescriptor getValueTypeDescriptor(Object obj) throws EvaluationException;

    TypeDescriptor getValueTypeDescriptor(EvaluationContext evaluationContext) throws EvaluationException;

    TypeDescriptor getValueTypeDescriptor(EvaluationContext evaluationContext, Object obj) throws EvaluationException;

    boolean isWritable(Object obj) throws EvaluationException;

    boolean isWritable(EvaluationContext evaluationContext) throws EvaluationException;

    boolean isWritable(EvaluationContext evaluationContext, Object obj) throws EvaluationException;

    void setValue(Object obj, Object obj2) throws EvaluationException;

    void setValue(EvaluationContext evaluationContext, Object obj) throws EvaluationException;

    void setValue(EvaluationContext evaluationContext, Object obj, Object obj2) throws EvaluationException;
}
