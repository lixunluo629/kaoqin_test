package org.springframework.web.servlet.tags;

import java.beans.PropertyEditor;
import javax.servlet.jsp.JspException;

/* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/tags/EditorAwareTag.class */
public interface EditorAwareTag {
    PropertyEditor getEditor() throws JspException;
}
