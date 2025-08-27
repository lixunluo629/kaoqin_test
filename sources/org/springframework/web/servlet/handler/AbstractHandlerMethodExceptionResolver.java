package org.springframework.web.servlet.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;

/* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/handler/AbstractHandlerMethodExceptionResolver.class */
public abstract class AbstractHandlerMethodExceptionResolver extends AbstractHandlerExceptionResolver {
    protected abstract ModelAndView doResolveHandlerMethodException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, HandlerMethod handlerMethod, Exception exc);

    @Override // org.springframework.web.servlet.handler.AbstractHandlerExceptionResolver
    protected boolean shouldApplyTo(HttpServletRequest request, Object handler) {
        if (handler == null) {
            return super.shouldApplyTo(request, handler);
        }
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            return super.shouldApplyTo(request, handlerMethod.getBean());
        }
        return false;
    }

    @Override // org.springframework.web.servlet.handler.AbstractHandlerExceptionResolver
    protected final ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        return doResolveHandlerMethodException(request, response, (HandlerMethod) handler, ex);
    }
}
