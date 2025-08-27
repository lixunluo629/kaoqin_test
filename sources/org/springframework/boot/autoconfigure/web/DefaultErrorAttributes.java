package org.springframework.boot.autoconfigure.web;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.httpclient.cookie.Cookie2;
import org.aspectj.weaver.model.AsmRelationshipUtils;
import org.hibernate.validator.internal.metadata.core.ConstraintHelper;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;
import org.springframework.web.servlet.tags.BindErrorsTag;
import org.springframework.web.servlet.tags.BindTag;

@Order(Integer.MIN_VALUE)
/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/web/DefaultErrorAttributes.class */
public class DefaultErrorAttributes implements ErrorAttributes, HandlerExceptionResolver, Ordered {
    private static final String ERROR_ATTRIBUTE = DefaultErrorAttributes.class.getName() + ".ERROR";

    @Override // org.springframework.core.Ordered
    public int getOrder() {
        return Integer.MIN_VALUE;
    }

    @Override // org.springframework.web.servlet.HandlerExceptionResolver
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        storeErrorAttributes(request, ex);
        return null;
    }

    private void storeErrorAttributes(HttpServletRequest request, Exception ex) {
        request.setAttribute(ERROR_ATTRIBUTE, ex);
    }

    @Override // org.springframework.boot.autoconfigure.web.ErrorAttributes
    public Map<String, Object> getErrorAttributes(RequestAttributes requestAttributes, boolean includeStackTrace) {
        Map<String, Object> errorAttributes = new LinkedHashMap<>();
        errorAttributes.put("timestamp", new Date());
        addStatus(errorAttributes, requestAttributes);
        addErrorDetails(errorAttributes, requestAttributes, includeStackTrace);
        addPath(errorAttributes, requestAttributes);
        return errorAttributes;
    }

    private void addStatus(Map<String, Object> errorAttributes, RequestAttributes requestAttributes) {
        Integer status = (Integer) getAttribute(requestAttributes, "javax.servlet.error.status_code");
        if (status == null) {
            errorAttributes.put(BindTag.STATUS_VARIABLE_NAME, 999);
            errorAttributes.put(AsmRelationshipUtils.DECLARE_ERROR, "None");
        } else {
            errorAttributes.put(BindTag.STATUS_VARIABLE_NAME, status);
            try {
                errorAttributes.put(AsmRelationshipUtils.DECLARE_ERROR, HttpStatus.valueOf(status.intValue()).getReasonPhrase());
            } catch (Exception e) {
                errorAttributes.put(AsmRelationshipUtils.DECLARE_ERROR, "Http Status " + status);
            }
        }
    }

    private void addErrorDetails(Map<String, Object> errorAttributes, RequestAttributes requestAttributes, boolean includeStackTrace) {
        Throwable error = getError(requestAttributes);
        if (error != null) {
            while ((error instanceof ServletException) && error.getCause() != null) {
                error = ((ServletException) error).getCause();
            }
            errorAttributes.put(SimpleMappingExceptionResolver.DEFAULT_EXCEPTION_ATTRIBUTE, error.getClass().getName());
            addErrorMessage(errorAttributes, error);
            if (includeStackTrace) {
                addStackTrace(errorAttributes, error);
            }
        }
        Object message = getAttribute(requestAttributes, "javax.servlet.error.message");
        if ((!StringUtils.isEmpty(message) || errorAttributes.get(ConstraintHelper.MESSAGE) == null) && !(error instanceof BindingResult)) {
            errorAttributes.put(ConstraintHelper.MESSAGE, StringUtils.isEmpty(message) ? "No message available" : message);
        }
    }

    private void addErrorMessage(Map<String, Object> errorAttributes, Throwable error) {
        BindingResult result = extractBindingResult(error);
        if (result == null) {
            errorAttributes.put(ConstraintHelper.MESSAGE, error.getMessage());
        } else if (result.getErrorCount() > 0) {
            errorAttributes.put(BindErrorsTag.ERRORS_VARIABLE_NAME, result.getAllErrors());
            errorAttributes.put(ConstraintHelper.MESSAGE, "Validation failed for object='" + result.getObjectName() + "'. Error count: " + result.getErrorCount());
        } else {
            errorAttributes.put(ConstraintHelper.MESSAGE, "No errors");
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private BindingResult extractBindingResult(Throwable th) {
        if (th instanceof BindingResult) {
            return (BindingResult) th;
        }
        if (th instanceof MethodArgumentNotValidException) {
            return ((MethodArgumentNotValidException) th).getBindingResult();
        }
        return null;
    }

    private void addStackTrace(Map<String, Object> errorAttributes, Throwable error) {
        StringWriter stackTrace = new StringWriter();
        error.printStackTrace(new PrintWriter(stackTrace));
        stackTrace.flush();
        errorAttributes.put("trace", stackTrace.toString());
    }

    private void addPath(Map<String, Object> errorAttributes, RequestAttributes requestAttributes) {
        String path = (String) getAttribute(requestAttributes, "javax.servlet.error.request_uri");
        if (path != null) {
            errorAttributes.put(Cookie2.PATH, path);
        }
    }

    @Override // org.springframework.boot.autoconfigure.web.ErrorAttributes
    public Throwable getError(RequestAttributes requestAttributes) {
        Throwable exception = (Throwable) getAttribute(requestAttributes, ERROR_ATTRIBUTE);
        if (exception == null) {
            exception = (Throwable) getAttribute(requestAttributes, "javax.servlet.error.exception");
        }
        return exception;
    }

    private <T> T getAttribute(RequestAttributes requestAttributes, String str) {
        return (T) requestAttributes.getAttribute(str, 0);
    }
}
