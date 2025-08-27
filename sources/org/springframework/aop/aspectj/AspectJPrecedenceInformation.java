package org.springframework.aop.aspectj;

import org.springframework.core.Ordered;

/* loaded from: spring-aop-4.3.25.RELEASE.jar:org/springframework/aop/aspectj/AspectJPrecedenceInformation.class */
public interface AspectJPrecedenceInformation extends Ordered {
    String getAspectName();

    int getDeclarationOrder();

    boolean isBeforeAdvice();

    boolean isAfterAdvice();
}
