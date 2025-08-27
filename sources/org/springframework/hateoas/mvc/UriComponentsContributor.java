package org.springframework.hateoas.mvc;

import org.springframework.core.MethodParameter;
import org.springframework.web.util.UriComponentsBuilder;

/* loaded from: spring-hateoas-0.23.0.RELEASE.jar:org/springframework/hateoas/mvc/UriComponentsContributor.class */
public interface UriComponentsContributor {
    boolean supportsParameter(MethodParameter methodParameter);

    void enhance(UriComponentsBuilder uriComponentsBuilder, MethodParameter methodParameter, Object obj);
}
