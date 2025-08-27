package org.springframework.hateoas.mvc;

import java.beans.ConstructorProperties;
import java.lang.reflect.Field;
import lombok.NonNull;
import org.springframework.core.MethodParameter;
import org.springframework.core.ResolvableType;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;

/* loaded from: spring-hateoas-0.23.0.RELEASE.jar:org/springframework/hateoas/mvc/ResourceProcessorHandlerMethodReturnValueHandler.class */
public class ResourceProcessorHandlerMethodReturnValueHandler implements HandlerMethodReturnValueHandler {
    static final ResolvableType RESOURCE_TYPE = ResolvableType.forRawClass(Resource.class);
    static final ResolvableType RESOURCES_TYPE = ResolvableType.forRawClass(Resources.class);
    private static final ResolvableType HTTP_ENTITY_TYPE = ResolvableType.forRawClass(HttpEntity.class);
    static final Field CONTENT_FIELD = ReflectionUtils.findField(Resources.class, "content");

    @NonNull
    private final HandlerMethodReturnValueHandler delegate;

    @NonNull
    private final ResourceProcessorInvoker invoker;
    private boolean rootLinksAsHeaders = false;

    @ConstructorProperties({"delegate", "invoker"})
    public ResourceProcessorHandlerMethodReturnValueHandler(@NonNull HandlerMethodReturnValueHandler delegate, @NonNull ResourceProcessorInvoker invoker) {
        if (delegate == null) {
            throw new NullPointerException("delegate");
        }
        if (invoker == null) {
            throw new NullPointerException("invoker");
        }
        this.delegate = delegate;
        this.invoker = invoker;
    }

    static {
        ReflectionUtils.makeAccessible(CONTENT_FIELD);
    }

    public void setRootLinksAsHeaders(boolean rootLinksAsHeaders) {
        this.rootLinksAsHeaders = rootLinksAsHeaders;
    }

    @Override // org.springframework.web.method.support.HandlerMethodReturnValueHandler
    public boolean supportsReturnType(MethodParameter returnType) {
        return this.delegate.supportsReturnType(returnType);
    }

    @Override // org.springframework.web.method.support.HandlerMethodReturnValueHandler
    public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer, NativeWebRequest webRequest) throws Exception {
        Object value = returnValue;
        if (returnValue instanceof HttpEntity) {
            value = ((HttpEntity) returnValue).getBody();
        }
        if (!ResourceSupport.class.isInstance(value)) {
            this.delegate.handleReturnValue(returnValue, returnType, mavContainer, webRequest);
            return;
        }
        ResolvableType targetType = ResolvableType.forMethodReturnType(returnType.getMethod());
        if (HTTP_ENTITY_TYPE.isAssignableFrom(targetType)) {
            targetType = targetType.getGeneric(0);
        }
        ResolvableType returnValueType = ResolvableType.forClass(value.getClass());
        if (!getRawType(targetType).equals(getRawType(returnValueType))) {
            targetType = returnValueType;
        }
        ResourceSupport result = this.invoker.invokeProcessorsFor((ResourceProcessorInvoker) value, targetType);
        this.delegate.handleReturnValue(rewrapResult(result, returnValue), returnType, mavContainer, webRequest);
    }

    Object rewrapResult(ResourceSupport newBody, Object originalValue) {
        HttpEntity<ResourceSupport> entity;
        if (!(originalValue instanceof HttpEntity)) {
            return newBody;
        }
        if (originalValue instanceof ResponseEntity) {
            ResponseEntity<?> source = (ResponseEntity) originalValue;
            entity = new ResponseEntity(newBody, (MultiValueMap<String, String>) source.getHeaders(), source.getStatusCode());
        } else {
            entity = new HttpEntity<>(newBody, ((HttpEntity) originalValue).getHeaders());
        }
        return addLinksToHeaderWrapper(entity);
    }

    private HttpEntity<?> addLinksToHeaderWrapper(HttpEntity<ResourceSupport> entity) {
        return this.rootLinksAsHeaders ? HeaderLinksResponseEntity.wrap(entity) : entity;
    }

    private static Class<?> getRawType(ResolvableType type) {
        Class<?> rawType = type.getRawClass();
        return rawType == null ? Object.class : rawType;
    }
}
