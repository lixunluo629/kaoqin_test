package org.springframework.hateoas;

import java.util.Map;
import org.springframework.hateoas.LinkBuilder;

/* loaded from: spring-hateoas-0.23.0.RELEASE.jar:org/springframework/hateoas/LinkBuilderFactory.class */
public interface LinkBuilderFactory<T extends LinkBuilder> {
    T linkTo(Class<?> cls);

    T linkTo(Class<?> cls, Object... objArr);

    T linkTo(Class<?> cls, Map<String, ?> map);
}
