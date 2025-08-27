package org.springframework.web.servlet.mvc.annotation;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.AbstractHandlerExceptionResolver;

/* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/mvc/annotation/ResponseStatusExceptionResolver.class */
public class ResponseStatusExceptionResolver extends AbstractHandlerExceptionResolver implements MessageSourceAware {
    private MessageSource messageSource;

    @Override // org.springframework.context.MessageSourceAware
    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Override // org.springframework.web.servlet.handler.AbstractHandlerExceptionResolver
    protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        ResponseStatus status = (ResponseStatus) AnnotatedElementUtils.findMergedAnnotation(ex.getClass(), ResponseStatus.class);
        if (status != null) {
            try {
                return resolveResponseStatus(status, request, response, handler, ex);
            } catch (Exception resolveEx) {
                this.logger.warn("ResponseStatus handling resulted in exception", resolveEx);
                return null;
            }
        }
        if (ex.getCause() instanceof Exception) {
            return doResolveException(request, response, handler, (Exception) ex.getCause());
        }
        return null;
    }

    protected ModelAndView resolveResponseStatus(ResponseStatus responseStatus, HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        int statusCode = responseStatus.code().value();
        String reason = responseStatus.reason();
        if (!StringUtils.hasLength(reason)) {
            response.sendError(statusCode);
        } else {
            String resolvedReason = this.messageSource != null ? this.messageSource.getMessage(reason, null, reason, LocaleContextHolder.getLocale()) : reason;
            response.sendError(statusCode, resolvedReason);
        }
        return new ModelAndView();
    }
}
