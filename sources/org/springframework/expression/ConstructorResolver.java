package org.springframework.expression;

import java.util.List;
import org.springframework.core.convert.TypeDescriptor;

/* loaded from: spring-expression-4.3.25.RELEASE.jar:org/springframework/expression/ConstructorResolver.class */
public interface ConstructorResolver {
    ConstructorExecutor resolve(EvaluationContext evaluationContext, String str, List<TypeDescriptor> list) throws AccessException;
}
