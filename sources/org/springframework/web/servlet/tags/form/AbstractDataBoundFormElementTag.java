package org.springframework.web.servlet.tags.form;

import java.beans.PropertyEditor;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import org.apache.commons.httpclient.cookie.Cookie2;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.support.BindStatus;
import org.springframework.web.servlet.support.RequestDataValueProcessor;
import org.springframework.web.servlet.tags.EditorAwareTag;

/* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/tags/form/AbstractDataBoundFormElementTag.class */
public abstract class AbstractDataBoundFormElementTag extends AbstractFormTag implements EditorAwareTag {
    protected static final String NESTED_PATH_VARIABLE_NAME = "nestedPath";
    private String path;
    private String id;
    private BindStatus bindStatus;

    public void setPath(String path) {
        this.path = path;
    }

    protected final String getPath() throws JspException {
        String resolvedPath = (String) evaluate(Cookie2.PATH, this.path);
        return resolvedPath != null ? resolvedPath : "";
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return this.id;
    }

    protected void writeDefaultAttributes(TagWriter tagWriter) throws JspException {
        writeOptionalAttribute(tagWriter, "id", resolveId());
        writeOptionalAttribute(tagWriter, "name", getName());
    }

    protected String resolveId() throws JspException {
        Object id = evaluate("id", getId());
        if (id != null) {
            String idString = id.toString();
            if (StringUtils.hasText(idString)) {
                return idString;
            }
            return null;
        }
        return autogenerateId();
    }

    protected String autogenerateId() throws JspException {
        return StringUtils.deleteAny(getName(), "[]");
    }

    protected String getName() throws JspException {
        return getPropertyPath();
    }

    protected BindStatus getBindStatus() throws JspException {
        if (this.bindStatus == null) {
            String nestedPath = getNestedPath();
            String pathToUse = nestedPath != null ? nestedPath + getPath() : getPath();
            if (pathToUse.endsWith(".")) {
                pathToUse = pathToUse.substring(0, pathToUse.length() - 1);
            }
            this.bindStatus = new BindStatus(getRequestContext(), pathToUse, false);
        }
        return this.bindStatus;
    }

    protected String getNestedPath() {
        return (String) this.pageContext.getAttribute("nestedPath", 2);
    }

    protected String getPropertyPath() throws JspException {
        String expression = getBindStatus().getExpression();
        return expression != null ? expression : "";
    }

    protected final Object getBoundValue() throws JspException {
        return getBindStatus().getValue();
    }

    protected PropertyEditor getPropertyEditor() throws JspException {
        return getBindStatus().getEditor();
    }

    @Override // org.springframework.web.servlet.tags.EditorAwareTag
    public final PropertyEditor getEditor() throws JspException {
        return getPropertyEditor();
    }

    protected String convertToDisplayString(Object value) throws JspException {
        PropertyEditor editor = value != null ? getBindStatus().findEditor(value.getClass()) : null;
        return getDisplayString(value, editor);
    }

    protected final String processFieldValue(String name, String value, String type) {
        RequestDataValueProcessor processor = getRequestContext().getRequestDataValueProcessor();
        ServletRequest request = this.pageContext.getRequest();
        if (processor != null && (request instanceof HttpServletRequest)) {
            value = processor.processFormFieldValue((HttpServletRequest) request, name, value, type);
        }
        return value;
    }

    @Override // org.springframework.web.servlet.tags.RequestContextAwareTag
    public void doFinally() {
        super.doFinally();
        this.bindStatus = null;
    }
}
