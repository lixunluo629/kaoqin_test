package org.springframework.web.servlet.mvc.method.annotation;

import org.springframework.core.MethodParameter;
import org.springframework.util.PatternMatchUtils;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.SmartView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.UrlBasedViewResolver;

/* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/mvc/method/annotation/ModelAndViewMethodReturnValueHandler.class */
public class ModelAndViewMethodReturnValueHandler implements HandlerMethodReturnValueHandler {
    private String[] redirectPatterns;

    public void setRedirectPatterns(String... redirectPatterns) {
        this.redirectPatterns = redirectPatterns;
    }

    public String[] getRedirectPatterns() {
        return this.redirectPatterns;
    }

    @Override // org.springframework.web.method.support.HandlerMethodReturnValueHandler
    public boolean supportsReturnType(MethodParameter returnType) {
        return ModelAndView.class.isAssignableFrom(returnType.getParameterType());
    }

    @Override // org.springframework.web.method.support.HandlerMethodReturnValueHandler
    public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer, NativeWebRequest webRequest) throws Exception {
        if (returnValue == null) {
            mavContainer.setRequestHandled(true);
            return;
        }
        ModelAndView mav = (ModelAndView) returnValue;
        if (mav.isReference()) {
            String viewName = mav.getViewName();
            mavContainer.setViewName(viewName);
            if (viewName != null && isRedirectViewName(viewName)) {
                mavContainer.setRedirectModelScenario(true);
            }
        } else {
            View view = mav.getView();
            mavContainer.setView(view);
            if ((view instanceof SmartView) && ((SmartView) view).isRedirectView()) {
                mavContainer.setRedirectModelScenario(true);
            }
        }
        mavContainer.setStatus(mav.getStatus());
        mavContainer.addAllAttributes(mav.getModel());
    }

    protected boolean isRedirectViewName(String viewName) {
        if (PatternMatchUtils.simpleMatch(this.redirectPatterns, viewName)) {
            return true;
        }
        return viewName.startsWith(UrlBasedViewResolver.REDIRECT_URL_PREFIX);
    }
}
