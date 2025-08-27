package org.springframework.web.servlet.mvc.condition;

import org.springframework.http.MediaType;

/* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/mvc/condition/MediaTypeExpression.class */
public interface MediaTypeExpression {
    MediaType getMediaType();

    boolean isNegated();
}
