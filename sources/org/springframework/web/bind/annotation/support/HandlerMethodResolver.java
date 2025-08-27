package org.springframework.web.bind.annotation.support;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.core.BridgeMethodResolver;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

@Deprecated
/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/web/bind/annotation/support/HandlerMethodResolver.class */
public class HandlerMethodResolver {
    private RequestMapping typeLevelMapping;
    private boolean sessionAttributesFound;
    private final Set<Method> handlerMethods = new LinkedHashSet();
    private final Set<Method> initBinderMethods = new LinkedHashSet();
    private final Set<Method> modelAttributeMethods = new LinkedHashSet();
    private final Set<String> sessionAttributeNames = new HashSet();
    private final Set<Class<?>> sessionAttributeTypes = new HashSet();
    private final Set<String> actualSessionAttributeNames = Collections.newSetFromMap(new ConcurrentHashMap(4));

    public void init(Class<?> handlerType) throws SecurityException, IllegalArgumentException {
        Set<Class<?>> handlerTypes = new LinkedHashSet<>();
        Class<?> specificHandlerType = null;
        if (!Proxy.isProxyClass(handlerType)) {
            handlerTypes.add(handlerType);
            specificHandlerType = handlerType;
        }
        handlerTypes.addAll(Arrays.asList(handlerType.getInterfaces()));
        for (Class<?> currentHandlerType : handlerTypes) {
            final Class<?> targetClass = specificHandlerType != null ? specificHandlerType : currentHandlerType;
            ReflectionUtils.doWithMethods(currentHandlerType, new ReflectionUtils.MethodCallback() { // from class: org.springframework.web.bind.annotation.support.HandlerMethodResolver.1
                @Override // org.springframework.util.ReflectionUtils.MethodCallback
                public void doWith(Method method) throws SecurityException, IllegalArgumentException {
                    Method specificMethod = ClassUtils.getMostSpecificMethod(method, targetClass);
                    Method bridgedMethod = BridgeMethodResolver.findBridgedMethod(specificMethod);
                    if (HandlerMethodResolver.this.isHandlerMethod(specificMethod) && (bridgedMethod == specificMethod || !HandlerMethodResolver.this.isHandlerMethod(bridgedMethod))) {
                        HandlerMethodResolver.this.handlerMethods.add(specificMethod);
                        return;
                    }
                    if (HandlerMethodResolver.this.isInitBinderMethod(specificMethod) && (bridgedMethod == specificMethod || !HandlerMethodResolver.this.isInitBinderMethod(bridgedMethod))) {
                        HandlerMethodResolver.this.initBinderMethods.add(specificMethod);
                    } else if (HandlerMethodResolver.this.isModelAttributeMethod(specificMethod)) {
                        if (bridgedMethod == specificMethod || !HandlerMethodResolver.this.isModelAttributeMethod(bridgedMethod)) {
                            HandlerMethodResolver.this.modelAttributeMethods.add(specificMethod);
                        }
                    }
                }
            }, ReflectionUtils.USER_DECLARED_METHODS);
        }
        this.typeLevelMapping = (RequestMapping) AnnotationUtils.findAnnotation(handlerType, RequestMapping.class);
        SessionAttributes sessionAttributes = (SessionAttributes) AnnotationUtils.findAnnotation(handlerType, SessionAttributes.class);
        this.sessionAttributesFound = sessionAttributes != null;
        if (this.sessionAttributesFound) {
            this.sessionAttributeNames.addAll(Arrays.asList(sessionAttributes.names()));
            this.sessionAttributeTypes.addAll(Arrays.asList(sessionAttributes.types()));
        }
    }

    protected boolean isHandlerMethod(Method method) {
        return AnnotationUtils.findAnnotation(method, RequestMapping.class) != null;
    }

    protected boolean isInitBinderMethod(Method method) {
        return AnnotationUtils.findAnnotation(method, InitBinder.class) != null;
    }

    protected boolean isModelAttributeMethod(Method method) {
        return AnnotationUtils.findAnnotation(method, ModelAttribute.class) != null;
    }

    public final boolean hasHandlerMethods() {
        return !this.handlerMethods.isEmpty();
    }

    public final Set<Method> getHandlerMethods() {
        return this.handlerMethods;
    }

    public final Set<Method> getInitBinderMethods() {
        return this.initBinderMethods;
    }

    public final Set<Method> getModelAttributeMethods() {
        return this.modelAttributeMethods;
    }

    public boolean hasTypeLevelMapping() {
        return this.typeLevelMapping != null;
    }

    public RequestMapping getTypeLevelMapping() {
        return this.typeLevelMapping;
    }

    public boolean hasSessionAttributes() {
        return this.sessionAttributesFound;
    }

    public boolean isSessionAttribute(String attrName, Class<?> attrType) {
        if (this.sessionAttributeNames.contains(attrName) || this.sessionAttributeTypes.contains(attrType)) {
            this.actualSessionAttributeNames.add(attrName);
            return true;
        }
        return false;
    }

    public Set<String> getActualSessionAttributeNames() {
        return this.actualSessionAttributeNames;
    }
}
