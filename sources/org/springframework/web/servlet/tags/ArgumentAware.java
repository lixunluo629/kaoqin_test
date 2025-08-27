package org.springframework.web.servlet.tags;

import javax.servlet.jsp.JspTagException;

/* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/tags/ArgumentAware.class */
public interface ArgumentAware {
    void addArgument(Object obj) throws JspTagException;
}
