package org.springframework.web.servlet.mvc.method.annotation;

import java.lang.reflect.Method;
import java.util.List;
import org.springframework.core.MethodParameter;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.annotation.ModelAttributeMethodProcessor;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.annotation.ModelAndViewResolver;

/* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/mvc/method/annotation/ModelAndViewResolverMethodReturnValueHandler.class */
public class ModelAndViewResolverMethodReturnValueHandler implements HandlerMethodReturnValueHandler {
    private final List<ModelAndViewResolver> mavResolvers;
    private final ModelAttributeMethodProcessor modelAttributeProcessor = new ModelAttributeMethodProcessor(true);

    public ModelAndViewResolverMethodReturnValueHandler(List<ModelAndViewResolver> mavResolvers) {
        this.mavResolvers = mavResolvers;
    }

    @Override // org.springframework.web.method.support.HandlerMethodReturnValueHandler
    public boolean supportsReturnType(MethodParameter returnType) {
        return true;
    }

    @Override // org.springframework.web.method.support.HandlerMethodReturnValueHandler
    public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer, NativeWebRequest webRequest) throws Exception {
        if (this.mavResolvers != null) {
            for (ModelAndViewResolver mavResolver : this.mavResolvers) {
                Class<?> handlerType = returnType.getContainingClass();
                Method method = returnType.getMethod();
                ExtendedModelMap model = (ExtendedModelMap) mavContainer.getModel();
                ModelAndView mav = mavResolver.resolveModelAndView(method, handlerType, returnValue, model, webRequest);
                if (mav != ModelAndViewResolver.UNRESOLVED) {
                    mavContainer.addAllAttributes(mav.getModel());
                    mavContainer.setViewName(mav.getViewName());
                    if (!mav.isReference()) {
                        mavContainer.setView(mav.getView());
                        return;
                    }
                    return;
                }
            }
        }
        if (this.modelAttributeProcessor.supportsReturnType(returnType)) {
            this.modelAttributeProcessor.handleReturnValue(returnValue, returnType, mavContainer, webRequest);
            return;
        }
        throw new UnsupportedOperationException("Unexpected return type: " + returnType.getParameterType().getName() + " in method: " + returnType.getMethod());
    }
}
