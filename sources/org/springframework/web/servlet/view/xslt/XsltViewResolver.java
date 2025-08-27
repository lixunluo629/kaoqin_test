package org.springframework.web.servlet.view.xslt;

import java.util.Properties;
import javax.xml.transform.ErrorListener;
import javax.xml.transform.URIResolver;
import org.springframework.web.servlet.view.AbstractUrlBasedView;
import org.springframework.web.servlet.view.UrlBasedViewResolver;

/* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/view/xslt/XsltViewResolver.class */
public class XsltViewResolver extends UrlBasedViewResolver {
    private String sourceKey;
    private URIResolver uriResolver;
    private ErrorListener errorListener;
    private Properties outputProperties;
    private boolean indent = true;
    private boolean cacheTemplates = true;

    public XsltViewResolver() {
        setViewClass(requiredViewClass());
    }

    @Override // org.springframework.web.servlet.view.UrlBasedViewResolver
    protected Class<?> requiredViewClass() {
        return XsltView.class;
    }

    public void setSourceKey(String sourceKey) {
        this.sourceKey = sourceKey;
    }

    public void setUriResolver(URIResolver uriResolver) {
        this.uriResolver = uriResolver;
    }

    public void setErrorListener(ErrorListener errorListener) {
        this.errorListener = errorListener;
    }

    public void setIndent(boolean indent) {
        this.indent = indent;
    }

    public void setOutputProperties(Properties outputProperties) {
        this.outputProperties = outputProperties;
    }

    public void setCacheTemplates(boolean cacheTemplates) {
        this.cacheTemplates = cacheTemplates;
    }

    @Override // org.springframework.web.servlet.view.UrlBasedViewResolver
    protected AbstractUrlBasedView buildView(String viewName) throws Exception {
        XsltView view = (XsltView) super.buildView(viewName);
        view.setSourceKey(this.sourceKey);
        if (this.uriResolver != null) {
            view.setUriResolver(this.uriResolver);
        }
        if (this.errorListener != null) {
            view.setErrorListener(this.errorListener);
        }
        view.setIndent(this.indent);
        view.setOutputProperties(this.outputProperties);
        view.setCacheTemplates(this.cacheTemplates);
        return view;
    }
}
