package org.springframework.web;

import javax.servlet.ServletException;

/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/web/HttpSessionRequiredException.class */
public class HttpSessionRequiredException extends ServletException {
    private String expectedAttribute;

    public HttpSessionRequiredException(String msg) {
        super(msg);
    }

    public HttpSessionRequiredException(String msg, String expectedAttribute) {
        super(msg);
        this.expectedAttribute = expectedAttribute;
    }

    public String getExpectedAttribute() {
        return this.expectedAttribute;
    }
}
