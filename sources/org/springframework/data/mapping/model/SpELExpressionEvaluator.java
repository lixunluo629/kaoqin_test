package org.springframework.data.mapping.model;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/mapping/model/SpELExpressionEvaluator.class */
public interface SpELExpressionEvaluator {
    <T> T evaluate(String str);
}
