package org.springframework.hateoas.mvc;

import java.beans.ConstructorProperties;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import lombok.NonNull;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.HandlerMethodReturnValueHandlerComposite;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

/* loaded from: spring-hateoas-0.23.0.RELEASE.jar:org/springframework/hateoas/mvc/ResourceProcessorInvokingHandlerAdapter.class */
public class ResourceProcessorInvokingHandlerAdapter extends RequestMappingHandlerAdapter {
    private static final Method RETURN_VALUE_HANDLER_METHOD = ReflectionUtils.findMethod(ResourceProcessorInvokingHandlerAdapter.class, "getReturnValueHandlers");

    @NonNull
    private final ResourceProcessorInvoker invoker;

    @ConstructorProperties({"invoker"})
    public ResourceProcessorInvokingHandlerAdapter(@NonNull ResourceProcessorInvoker invoker) {
        if (invoker == null) {
            throw new NullPointerException("invoker");
        }
        this.invoker = invoker;
    }

    @Override // org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter, org.springframework.beans.factory.InitializingBean
    public void afterPropertiesSet() {
        super.afterPropertiesSet();
        HandlerMethodReturnValueHandlerComposite oldHandlers = getReturnValueHandlersComposite();
        List<HandlerMethodReturnValueHandler> newHandlers = new ArrayList<>();
        newHandlers.add(new ResourceProcessorHandlerMethodReturnValueHandler(oldHandlers, this.invoker));
        setReturnValueHandlers(newHandlers);
    }

    private HandlerMethodReturnValueHandlerComposite getReturnValueHandlersComposite() {
        Object handlers = ReflectionUtils.invokeMethod(RETURN_VALUE_HANDLER_METHOD, this);
        if (handlers instanceof HandlerMethodReturnValueHandlerComposite) {
            return (HandlerMethodReturnValueHandlerComposite) handlers;
        }
        return new HandlerMethodReturnValueHandlerComposite().addHandlers((List) handlers);
    }
}
