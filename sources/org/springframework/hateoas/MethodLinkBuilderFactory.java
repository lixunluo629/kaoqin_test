package org.springframework.hateoas;

import java.lang.reflect.Method;
import org.springframework.hateoas.LinkBuilder;

/* loaded from: spring-hateoas-0.23.0.RELEASE.jar:org/springframework/hateoas/MethodLinkBuilderFactory.class */
public interface MethodLinkBuilderFactory<T extends LinkBuilder> extends LinkBuilderFactory<T> {
    T linkTo(Method method, Object... objArr);

    T linkTo(Class<?> cls, Method method, Object... objArr);

    T linkTo(Object obj);
}
