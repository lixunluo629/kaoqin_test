package org.springframework.expression;

import java.util.List;
import org.springframework.core.convert.TypeDescriptor;

/* loaded from: spring-expression-4.3.25.RELEASE.jar:org/springframework/expression/MethodResolver.class */
public interface MethodResolver {
    MethodExecutor resolve(EvaluationContext evaluationContext, Object obj, String str, List<TypeDescriptor> list) throws AccessException;
}
