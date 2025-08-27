package org.springframework.web.servlet.mvc.multiaction;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.web.util.WebUtils;

@Deprecated
/* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/mvc/multiaction/InternalPathMethodNameResolver.class */
public class InternalPathMethodNameResolver extends AbstractUrlMethodNameResolver {
    private String prefix = "";
    private String suffix = "";
    private final Map<String, String> methodNameCache = new ConcurrentHashMap(16);

    public void setPrefix(String prefix) {
        this.prefix = prefix != null ? prefix : "";
    }

    protected String getPrefix() {
        return this.prefix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix != null ? suffix : "";
    }

    protected String getSuffix() {
        return this.suffix;
    }

    @Override // org.springframework.web.servlet.mvc.multiaction.AbstractUrlMethodNameResolver
    protected String getHandlerMethodNameForUrlPath(String urlPath) {
        String methodName = this.methodNameCache.get(urlPath);
        if (methodName == null) {
            methodName = postProcessHandlerMethodName(extractHandlerMethodNameFromUrlPath(urlPath));
            this.methodNameCache.put(urlPath, methodName);
        }
        return methodName;
    }

    protected String extractHandlerMethodNameFromUrlPath(String uri) {
        return WebUtils.extractFilenameFromUrlPath(uri);
    }

    protected String postProcessHandlerMethodName(String methodName) {
        return getPrefix() + methodName + getSuffix();
    }
}
