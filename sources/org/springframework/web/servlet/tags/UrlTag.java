package org.springframework.web.servlet.tags;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.support.RequestDataValueProcessor;
import org.springframework.web.util.JavaScriptUtils;
import org.springframework.web.util.TagUtils;
import org.springframework.web.util.UriUtils;

/* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/tags/UrlTag.class */
public class UrlTag extends HtmlEscapingAwareTag implements ParamAware {
    private static final String URL_TEMPLATE_DELIMITER_PREFIX = "{";
    private static final String URL_TEMPLATE_DELIMITER_SUFFIX = "}";
    private static final String URL_TYPE_ABSOLUTE = "://";
    private List<Param> params;
    private Set<String> templateParams;
    private UrlType type;
    private String value;
    private String context;
    private String var;
    private int scope = 1;
    private boolean javaScriptEscape = false;

    /* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/tags/UrlTag$UrlType.class */
    private enum UrlType {
        CONTEXT_RELATIVE,
        RELATIVE,
        ABSOLUTE
    }

    public void setValue(String value) {
        if (value.contains(URL_TYPE_ABSOLUTE)) {
            this.type = UrlType.ABSOLUTE;
            this.value = value;
        } else if (value.startsWith("/")) {
            this.type = UrlType.CONTEXT_RELATIVE;
            this.value = value;
        } else {
            this.type = UrlType.RELATIVE;
            this.value = value;
        }
    }

    public void setContext(String context) {
        if (context.startsWith("/")) {
            this.context = context;
        } else {
            this.context = "/" + context;
        }
    }

    public void setVar(String var) {
        this.var = var;
    }

    public void setScope(String scope) {
        this.scope = TagUtils.getScope(scope);
    }

    public void setJavaScriptEscape(boolean javaScriptEscape) throws JspException {
        this.javaScriptEscape = javaScriptEscape;
    }

    @Override // org.springframework.web.servlet.tags.ParamAware
    public void addParam(Param param) {
        this.params.add(param);
    }

    @Override // org.springframework.web.servlet.tags.RequestContextAwareTag
    public int doStartTagInternal() throws JspException {
        this.params = new LinkedList();
        this.templateParams = new HashSet();
        return 1;
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: javax.servlet.jsp.JspException */
    public int doEndTag() throws JspException {
        String url = createUrl();
        RequestDataValueProcessor processor = getRequestContext().getRequestDataValueProcessor();
        ServletRequest request = this.pageContext.getRequest();
        if (processor != null && (request instanceof HttpServletRequest)) {
            url = processor.processUrl((HttpServletRequest) request, url);
        }
        if (this.var == null) {
            try {
                this.pageContext.getOut().print(url);
                return 6;
            } catch (IOException ex) {
                throw new JspException(ex);
            }
        }
        this.pageContext.setAttribute(this.var, url, this.scope);
        return 6;
    }

    String createUrl() throws JspException {
        HttpServletRequest request = (HttpServletRequest) this.pageContext.getRequest();
        HttpServletResponse response = (HttpServletResponse) this.pageContext.getResponse();
        StringBuilder url = new StringBuilder();
        if (this.type == UrlType.CONTEXT_RELATIVE) {
            if (this.context == null) {
                url.append(request.getContextPath());
            } else if (this.context.endsWith("/")) {
                url.append(this.context.substring(0, this.context.length() - 1));
            } else {
                url.append(this.context);
            }
        }
        if (this.type != UrlType.RELATIVE && this.type != UrlType.ABSOLUTE && !this.value.startsWith("/")) {
            url.append("/");
        }
        url.append(replaceUriTemplateParams(this.value, this.params, this.templateParams));
        url.append(createQueryString(this.params, this.templateParams, url.indexOf("?") == -1));
        String urlStr = url.toString();
        if (this.type != UrlType.ABSOLUTE) {
            urlStr = response.encodeURL(urlStr);
        }
        String urlStr2 = htmlEscape(urlStr);
        return this.javaScriptEscape ? JavaScriptUtils.javaScriptEscape(urlStr2) : urlStr2;
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: javax.servlet.jsp.JspException */
    protected String createQueryString(List<Param> params, Set<String> usedParams, boolean includeQueryStringDelimiter) throws JspException {
        String encoding = this.pageContext.getResponse().getCharacterEncoding();
        StringBuilder qs = new StringBuilder();
        for (Param param : params) {
            if (!usedParams.contains(param.getName()) && StringUtils.hasLength(param.getName())) {
                if (includeQueryStringDelimiter && qs.length() == 0) {
                    qs.append("?");
                } else {
                    qs.append("&");
                }
                try {
                    qs.append(UriUtils.encodeQueryParam(param.getName(), encoding));
                    if (param.getValue() != null) {
                        qs.append(SymbolConstants.EQUAL_SYMBOL);
                        qs.append(UriUtils.encodeQueryParam(param.getValue(), encoding));
                    }
                } catch (UnsupportedEncodingException ex) {
                    throw new JspException(ex);
                }
            }
        }
        return qs.toString();
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: javax.servlet.jsp.JspException */
    protected String replaceUriTemplateParams(String uri, List<Param> params, Set<String> usedParams) throws JspException {
        String encoding = this.pageContext.getResponse().getCharacterEncoding();
        for (Param param : params) {
            String template = URL_TEMPLATE_DELIMITER_PREFIX + param.getName() + "}";
            if (uri.contains(template)) {
                usedParams.add(param.getName());
                try {
                    uri = uri.replace(template, UriUtils.encodePath(param.getValue(), encoding));
                } catch (UnsupportedEncodingException ex) {
                    throw new JspException(ex);
                }
            } else {
                String template2 = "{/" + param.getName() + "}";
                if (uri.contains(template2)) {
                    usedParams.add(param.getName());
                    try {
                        uri = uri.replace(template2, UriUtils.encodePathSegment(param.getValue(), encoding));
                    } catch (UnsupportedEncodingException ex2) {
                        throw new JspException(ex2);
                    }
                } else {
                    continue;
                }
            }
        }
        return uri;
    }
}
