package org.springframework.web.servlet.tags;

import java.beans.PropertyEditor;
import javax.servlet.jsp.JspTagException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.support.BindStatus;

/* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/tags/BindTag.class */
public class BindTag extends HtmlEscapingAwareTag implements EditorAwareTag {
    public static final String STATUS_VARIABLE_NAME = "status";
    private String path;
    private boolean ignoreNestedPath = false;
    private BindStatus status;
    private Object previousPageStatus;
    private Object previousRequestStatus;

    public void setPath(String path) {
        this.path = path;
    }

    public String getPath() {
        return this.path;
    }

    public void setIgnoreNestedPath(boolean ignoreNestedPath) {
        this.ignoreNestedPath = ignoreNestedPath;
    }

    public boolean isIgnoreNestedPath() {
        return this.ignoreNestedPath;
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: javax.servlet.jsp.JspTagException */
    @Override // org.springframework.web.servlet.tags.RequestContextAwareTag
    protected final int doStartTagInternal() throws Exception {
        String nestedPath;
        String resolvedPath = getPath();
        if (!isIgnoreNestedPath() && (nestedPath = (String) this.pageContext.getAttribute(NestedPathTag.NESTED_PATH_VARIABLE_NAME, 2)) != null && !resolvedPath.startsWith(nestedPath) && !resolvedPath.equals(nestedPath.substring(0, nestedPath.length() - 1))) {
            resolvedPath = nestedPath + resolvedPath;
        }
        try {
            this.status = new BindStatus(getRequestContext(), resolvedPath, isHtmlEscape());
            this.previousPageStatus = this.pageContext.getAttribute(STATUS_VARIABLE_NAME, 1);
            this.previousRequestStatus = this.pageContext.getAttribute(STATUS_VARIABLE_NAME, 2);
            this.pageContext.removeAttribute(STATUS_VARIABLE_NAME, 1);
            this.pageContext.setAttribute(STATUS_VARIABLE_NAME, this.status, 2);
            return 1;
        } catch (IllegalStateException ex) {
            throw new JspTagException(ex.getMessage());
        }
    }

    public int doEndTag() {
        if (this.previousPageStatus != null) {
            this.pageContext.setAttribute(STATUS_VARIABLE_NAME, this.previousPageStatus, 1);
        }
        if (this.previousRequestStatus != null) {
            this.pageContext.setAttribute(STATUS_VARIABLE_NAME, this.previousRequestStatus, 2);
            return 6;
        }
        this.pageContext.removeAttribute(STATUS_VARIABLE_NAME, 2);
        return 6;
    }

    public final String getProperty() {
        return this.status.getExpression();
    }

    public final Errors getErrors() {
        return this.status.getErrors();
    }

    @Override // org.springframework.web.servlet.tags.EditorAwareTag
    public final PropertyEditor getEditor() {
        return this.status.getEditor();
    }

    @Override // org.springframework.web.servlet.tags.RequestContextAwareTag
    public void doFinally() {
        super.doFinally();
        this.status = null;
        this.previousPageStatus = null;
        this.previousRequestStatus = null;
    }
}
