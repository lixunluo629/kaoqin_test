package org.springframework.web.servlet.mvc.multiaction;

import javax.servlet.http.HttpServletRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.Assert;
import org.springframework.web.util.UrlPathHelper;

@Deprecated
/* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/mvc/multiaction/AbstractUrlMethodNameResolver.class */
public abstract class AbstractUrlMethodNameResolver implements MethodNameResolver {
    protected final Log logger = LogFactory.getLog(getClass());
    private UrlPathHelper urlPathHelper = new UrlPathHelper();

    protected abstract String getHandlerMethodNameForUrlPath(String str);

    public void setAlwaysUseFullPath(boolean alwaysUseFullPath) {
        this.urlPathHelper.setAlwaysUseFullPath(alwaysUseFullPath);
    }

    public void setUrlDecode(boolean urlDecode) {
        this.urlPathHelper.setUrlDecode(urlDecode);
    }

    public void setUrlPathHelper(UrlPathHelper urlPathHelper) {
        Assert.notNull(urlPathHelper, "UrlPathHelper must not be null");
        this.urlPathHelper = urlPathHelper;
    }

    @Override // org.springframework.web.servlet.mvc.multiaction.MethodNameResolver
    public final String getHandlerMethodName(HttpServletRequest request) throws NoSuchRequestHandlingMethodException {
        String urlPath = this.urlPathHelper.getLookupPathForRequest(request);
        String name = getHandlerMethodNameForUrlPath(urlPath);
        if (name == null) {
            throw new NoSuchRequestHandlingMethodException(urlPath, request.getMethod(), request.getParameterMap());
        }
        if (this.logger.isDebugEnabled()) {
            this.logger.debug("Returning handler method name '" + name + "' for lookup path: " + urlPath);
        }
        return name;
    }
}
