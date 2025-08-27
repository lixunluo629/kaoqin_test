package org.springframework.web.context.request;

import org.apache.log4j.Logger;
import org.apache.log4j.NDC;
import org.springframework.ui.ModelMap;

@Deprecated
/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/web/context/request/Log4jNestedDiagnosticContextInterceptor.class */
public class Log4jNestedDiagnosticContextInterceptor implements AsyncWebRequestInterceptor {
    protected final Logger log4jLogger = Logger.getLogger(getClass());
    private boolean includeClientInfo = false;

    public void setIncludeClientInfo(boolean includeClientInfo) {
        this.includeClientInfo = includeClientInfo;
    }

    protected boolean isIncludeClientInfo() {
        return this.includeClientInfo;
    }

    @Override // org.springframework.web.context.request.WebRequestInterceptor
    public void preHandle(WebRequest request) throws Exception {
        NDC.push(getNestedDiagnosticContextMessage(request));
    }

    protected String getNestedDiagnosticContextMessage(WebRequest request) {
        return request.getDescription(isIncludeClientInfo());
    }

    @Override // org.springframework.web.context.request.WebRequestInterceptor
    public void postHandle(WebRequest request, ModelMap model) throws Exception {
    }

    @Override // org.springframework.web.context.request.WebRequestInterceptor
    public void afterCompletion(WebRequest request, Exception ex) throws Exception {
        NDC.pop();
        if (NDC.getDepth() == 0) {
            NDC.remove();
        }
    }

    @Override // org.springframework.web.context.request.AsyncWebRequestInterceptor
    public void afterConcurrentHandlingStarted(WebRequest request) throws IllegalArgumentException {
        NDC.pop();
        if (NDC.getDepth() == 0) {
            NDC.remove();
        }
    }
}
