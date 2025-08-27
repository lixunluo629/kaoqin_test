package org.springframework.web.filter;

import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.apache.log4j.NDC;

@Deprecated
/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/web/filter/Log4jNestedDiagnosticContextFilter.class */
public class Log4jNestedDiagnosticContextFilter extends AbstractRequestLoggingFilter {
    protected final Logger log4jLogger = Logger.getLogger(getClass());

    @Override // org.springframework.web.filter.AbstractRequestLoggingFilter
    protected void beforeRequest(HttpServletRequest request, String message) throws IllegalArgumentException {
        if (this.log4jLogger.isDebugEnabled()) {
            this.log4jLogger.debug(message);
        }
        NDC.push(getNestedDiagnosticContextMessage(request));
    }

    protected String getNestedDiagnosticContextMessage(HttpServletRequest request) {
        return createMessage(request, "", "");
    }

    @Override // org.springframework.web.filter.AbstractRequestLoggingFilter
    protected void afterRequest(HttpServletRequest request, String message) throws IllegalArgumentException {
        NDC.pop();
        if (NDC.getDepth() == 0) {
            NDC.remove();
        }
        if (this.log4jLogger.isDebugEnabled()) {
            this.log4jLogger.debug(message);
        }
    }
}
