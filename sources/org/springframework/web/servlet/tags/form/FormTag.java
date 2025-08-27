package org.springframework.web.servlet.tags.form;

import ch.qos.logback.core.joran.util.beans.BeanUtil;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import org.apache.poi.ss.util.CellUtil;
import org.springframework.core.Conventions;
import org.springframework.http.HttpMethod;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.support.RequestDataValueProcessor;
import org.springframework.web.servlet.tags.NestedPathTag;
import org.springframework.web.util.HtmlUtils;
import org.springframework.web.util.UriUtils;

/* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/tags/form/FormTag.class */
public class FormTag extends AbstractHtmlElementTag {
    private static final String DEFAULT_METHOD = "post";
    public static final String DEFAULT_COMMAND_NAME = "command";
    private static final String MODEL_ATTRIBUTE = "modelAttribute";
    public static final String MODEL_ATTRIBUTE_VARIABLE_NAME = Conventions.getQualifiedAttributeName(AbstractFormTag.class, MODEL_ATTRIBUTE);
    private static final String DEFAULT_METHOD_PARAM = "_method";
    private static final String FORM_TAG = "form";
    private static final String INPUT_TAG = "input";
    private static final String ACTION_ATTRIBUTE = "action";
    private static final String METHOD_ATTRIBUTE = "method";
    private static final String TARGET_ATTRIBUTE = "target";
    private static final String ENCTYPE_ATTRIBUTE = "enctype";
    private static final String ACCEPT_CHARSET_ATTRIBUTE = "accept-charset";
    private static final String ONSUBMIT_ATTRIBUTE = "onsubmit";
    private static final String ONRESET_ATTRIBUTE = "onreset";
    private static final String AUTOCOMPLETE_ATTRIBUTE = "autocomplete";
    private static final String NAME_ATTRIBUTE = "name";
    private static final String VALUE_ATTRIBUTE = "value";
    private static final String TYPE_ATTRIBUTE = "type";
    private TagWriter tagWriter;
    private String name;
    private String action;
    private String servletRelativeAction;
    private String target;
    private String enctype;
    private String acceptCharset;
    private String onsubmit;
    private String onreset;
    private String autocomplete;
    private String previousNestedPath;
    private String modelAttribute = "command";
    private String method = DEFAULT_METHOD;
    private String methodParam = "_method";

    public void setModelAttribute(String modelAttribute) {
        this.modelAttribute = modelAttribute;
    }

    protected String getModelAttribute() {
        return this.modelAttribute;
    }

    @Deprecated
    public void setCommandName(String commandName) {
        this.modelAttribute = commandName;
    }

    @Deprecated
    protected String getCommandName() {
        return this.modelAttribute;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override // org.springframework.web.servlet.tags.form.AbstractDataBoundFormElementTag
    protected String getName() throws JspException {
        return this.name;
    }

    public void setAction(String action) {
        this.action = action != null ? action : "";
    }

    protected String getAction() {
        return this.action;
    }

    public void setServletRelativeAction(String servletRelativeAction) {
        this.servletRelativeAction = servletRelativeAction != null ? servletRelativeAction : "";
    }

    protected String getServletRelativeAction() {
        return this.servletRelativeAction;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    protected String getMethod() {
        return this.method;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getTarget() {
        return this.target;
    }

    public void setEnctype(String enctype) {
        this.enctype = enctype;
    }

    protected String getEnctype() {
        return this.enctype;
    }

    public void setAcceptCharset(String acceptCharset) {
        this.acceptCharset = acceptCharset;
    }

    protected String getAcceptCharset() {
        return this.acceptCharset;
    }

    public void setOnsubmit(String onsubmit) {
        this.onsubmit = onsubmit;
    }

    protected String getOnsubmit() {
        return this.onsubmit;
    }

    public void setOnreset(String onreset) {
        this.onreset = onreset;
    }

    protected String getOnreset() {
        return this.onreset;
    }

    public void setAutocomplete(String autocomplete) {
        this.autocomplete = autocomplete;
    }

    protected String getAutocomplete() {
        return this.autocomplete;
    }

    public void setMethodParam(String methodParam) {
        this.methodParam = methodParam;
    }

    protected String getMethodParam() {
        return getMethodParameter();
    }

    @Deprecated
    protected String getMethodParameter() {
        return this.methodParam;
    }

    protected boolean isMethodBrowserSupported(String method) {
        return BeanUtil.PREFIX_GETTER_GET.equalsIgnoreCase(method) || DEFAULT_METHOD.equalsIgnoreCase(method);
    }

    @Override // org.springframework.web.servlet.tags.form.AbstractFormTag
    protected int writeTagContent(TagWriter tagWriter) throws JspException, IOException {
        this.tagWriter = tagWriter;
        tagWriter.startTag(FORM_TAG);
        writeDefaultAttributes(tagWriter);
        tagWriter.writeAttribute("action", resolveAction());
        writeOptionalAttribute(tagWriter, "method", getHttpMethod());
        writeOptionalAttribute(tagWriter, "target", getTarget());
        writeOptionalAttribute(tagWriter, ENCTYPE_ATTRIBUTE, getEnctype());
        writeOptionalAttribute(tagWriter, ACCEPT_CHARSET_ATTRIBUTE, getAcceptCharset());
        writeOptionalAttribute(tagWriter, ONSUBMIT_ATTRIBUTE, getOnsubmit());
        writeOptionalAttribute(tagWriter, ONRESET_ATTRIBUTE, getOnreset());
        writeOptionalAttribute(tagWriter, "autocomplete", getAutocomplete());
        tagWriter.forceBlock();
        if (!isMethodBrowserSupported(getMethod())) {
            assertHttpMethod(getMethod());
            String inputName = getMethodParam();
            tagWriter.startTag(INPUT_TAG);
            writeOptionalAttribute(tagWriter, "type", CellUtil.HIDDEN);
            writeOptionalAttribute(tagWriter, "name", inputName);
            writeOptionalAttribute(tagWriter, "value", processFieldValue(inputName, getMethod(), CellUtil.HIDDEN));
            tagWriter.endTag();
        }
        String modelAttribute = resolveModelAttribute();
        this.pageContext.setAttribute(MODEL_ATTRIBUTE_VARIABLE_NAME, modelAttribute, 2);
        this.previousNestedPath = (String) this.pageContext.getAttribute(NestedPathTag.NESTED_PATH_VARIABLE_NAME, 2);
        this.pageContext.setAttribute(NestedPathTag.NESTED_PATH_VARIABLE_NAME, modelAttribute + ".", 2);
        return 1;
    }

    private String getHttpMethod() {
        return isMethodBrowserSupported(getMethod()) ? getMethod() : DEFAULT_METHOD;
    }

    private void assertHttpMethod(String method) {
        for (HttpMethod httpMethod : HttpMethod.values()) {
            if (httpMethod.name().equalsIgnoreCase(method)) {
                return;
            }
        }
        throw new IllegalArgumentException("Invalid HTTP method: " + method);
    }

    @Override // org.springframework.web.servlet.tags.form.AbstractDataBoundFormElementTag
    protected String autogenerateId() throws JspException {
        return resolveModelAttribute();
    }

    protected String resolveModelAttribute() throws JspException {
        Object resolvedModelAttribute = evaluate(MODEL_ATTRIBUTE, getModelAttribute());
        if (resolvedModelAttribute == null) {
            throw new IllegalArgumentException("modelAttribute must not be null");
        }
        return (String) resolvedModelAttribute;
    }

    protected String resolveAction() throws JspException {
        String action = getAction();
        String servletRelativeAction = getServletRelativeAction();
        if (StringUtils.hasText(action)) {
            return processAction(getDisplayString(evaluate("action", action)));
        }
        if (StringUtils.hasText(servletRelativeAction)) {
            String pathToServlet = getRequestContext().getPathToServlet();
            if (servletRelativeAction.startsWith("/") && !servletRelativeAction.startsWith(getRequestContext().getContextPath())) {
                servletRelativeAction = pathToServlet + servletRelativeAction;
            }
            return processAction(getDisplayString(evaluate("action", servletRelativeAction)));
        }
        String requestUri = getRequestContext().getRequestUri();
        String encoding = this.pageContext.getResponse().getCharacterEncoding();
        try {
            requestUri = UriUtils.encodePath(requestUri, encoding);
        } catch (UnsupportedEncodingException e) {
        }
        ServletResponse response = this.pageContext.getResponse();
        if (response instanceof HttpServletResponse) {
            requestUri = ((HttpServletResponse) response).encodeURL(requestUri);
            String queryString = getRequestContext().getQueryString();
            if (StringUtils.hasText(queryString)) {
                requestUri = requestUri + "?" + HtmlUtils.htmlEscape(queryString);
            }
        }
        if (StringUtils.hasText(requestUri)) {
            return processAction(requestUri);
        }
        throw new IllegalArgumentException("Attribute 'action' is required. Attempted to resolve against current request URI but request URI was null.");
    }

    private String processAction(String action) {
        RequestDataValueProcessor processor = getRequestContext().getRequestDataValueProcessor();
        ServletRequest request = this.pageContext.getRequest();
        if (processor != null && (request instanceof HttpServletRequest)) {
            action = processor.processAction((HttpServletRequest) request, action, getHttpMethod());
        }
        return action;
    }

    public int doEndTag() throws JspException, IOException {
        RequestDataValueProcessor processor = getRequestContext().getRequestDataValueProcessor();
        ServletRequest request = this.pageContext.getRequest();
        if (processor != null && (request instanceof HttpServletRequest)) {
            writeHiddenFields(processor.getExtraHiddenFields((HttpServletRequest) request));
        }
        this.tagWriter.endTag();
        return 6;
    }

    private void writeHiddenFields(Map<String, String> hiddenFields) throws JspException, IOException {
        if (!CollectionUtils.isEmpty(hiddenFields)) {
            this.tagWriter.appendValue("<div>\n");
            for (String name : hiddenFields.keySet()) {
                this.tagWriter.appendValue("<input type=\"hidden\" ");
                this.tagWriter.appendValue("name=\"" + name + "\" value=\"" + hiddenFields.get(name) + "\" ");
                this.tagWriter.appendValue("/>\n");
            }
            this.tagWriter.appendValue("</div>");
        }
    }

    @Override // org.springframework.web.servlet.tags.form.AbstractDataBoundFormElementTag, org.springframework.web.servlet.tags.RequestContextAwareTag
    public void doFinally() {
        super.doFinally();
        this.pageContext.removeAttribute(MODEL_ATTRIBUTE_VARIABLE_NAME, 2);
        if (this.previousNestedPath != null) {
            this.pageContext.setAttribute(NestedPathTag.NESTED_PATH_VARIABLE_NAME, this.previousNestedPath, 2);
        } else {
            this.pageContext.removeAttribute(NestedPathTag.NESTED_PATH_VARIABLE_NAME, 2);
        }
        this.tagWriter = null;
        this.previousNestedPath = null;
    }

    @Override // org.springframework.web.servlet.tags.form.AbstractHtmlElementTag
    protected String resolveCssClass() throws JspException {
        return ObjectUtils.getDisplayString(evaluate("cssClass", getCssClass()));
    }

    @Override // org.springframework.web.servlet.tags.form.AbstractDataBoundFormElementTag
    public void setPath(String path) {
        throw new UnsupportedOperationException("The 'path' attribute is not supported for forms");
    }

    @Override // org.springframework.web.servlet.tags.form.AbstractHtmlElementTag
    public void setCssErrorClass(String cssErrorClass) {
        throw new UnsupportedOperationException("The 'cssErrorClass' attribute is not supported for forms");
    }
}
