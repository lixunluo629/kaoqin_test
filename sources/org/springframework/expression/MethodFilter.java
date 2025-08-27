package org.springframework.expression;

import java.lang.reflect.Method;
import java.util.List;

/* loaded from: spring-expression-4.3.25.RELEASE.jar:org/springframework/expression/MethodFilter.class */
public interface MethodFilter {
    List<Method> filter(List<Method> list);
}
