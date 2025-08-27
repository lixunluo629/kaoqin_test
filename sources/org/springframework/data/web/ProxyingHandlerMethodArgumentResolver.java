package org.springframework.data.web;

import java.util.Arrays;
import java.util.List;
import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.annotation.ModelAttributeMethodProcessor;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/web/ProxyingHandlerMethodArgumentResolver.class */
public class ProxyingHandlerMethodArgumentResolver extends ModelAttributeMethodProcessor implements BeanFactoryAware, ResourceLoaderAware, BeanClassLoaderAware {
    private static final List<String> IGNORED_PACKAGES = Arrays.asList("java", "org.springframework");
    private final SpelAwareProxyProjectionFactory proxyFactory;
    private final ObjectFactory<ConversionService> conversionService;

    @Deprecated
    public ProxyingHandlerMethodArgumentResolver(final ConversionService conversionService) {
        this(new ObjectFactory<ConversionService>() { // from class: org.springframework.data.web.ProxyingHandlerMethodArgumentResolver.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.springframework.beans.factory.ObjectFactory
            public ConversionService getObject() throws BeansException {
                return conversionService;
            }
        }, true);
    }

    public ProxyingHandlerMethodArgumentResolver(ObjectFactory<ConversionService> conversionService, boolean annotationNotRequired) {
        super(annotationNotRequired);
        this.proxyFactory = new SpelAwareProxyProjectionFactory();
        this.conversionService = conversionService;
    }

    @Override // org.springframework.beans.factory.BeanFactoryAware
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.proxyFactory.setBeanFactory(beanFactory);
    }

    @Override // org.springframework.context.ResourceLoaderAware
    @Deprecated
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.proxyFactory.setResourceLoader(resourceLoader);
    }

    @Override // org.springframework.beans.factory.BeanClassLoaderAware
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.proxyFactory.setBeanClassLoader(classLoader);
    }

    @Override // org.springframework.web.method.annotation.ModelAttributeMethodProcessor, org.springframework.web.method.support.HandlerMethodArgumentResolver
    public boolean supportsParameter(MethodParameter parameter) {
        if (!super.supportsParameter(parameter)) {
            return false;
        }
        Class<?> type = parameter.getParameterType();
        if (!type.isInterface()) {
            return false;
        }
        if (parameter.getParameterAnnotation(ProjectedPayload.class) != null || AnnotatedElementUtils.findMergedAnnotation(type, ProjectedPayload.class) != null) {
            return true;
        }
        for (String prefix : IGNORED_PACKAGES) {
            if (ClassUtils.getPackageName(type).startsWith(prefix)) {
                return false;
            }
        }
        return true;
    }

    @Override // org.springframework.web.method.annotation.ModelAttributeMethodProcessor
    protected Object createAttribute(String attributeName, MethodParameter parameter, WebDataBinderFactory binderFactory, NativeWebRequest request) throws Exception {
        MapDataBinder binder = new MapDataBinder(parameter.getParameterType(), this.conversionService.getObject());
        binder.bind(new MutablePropertyValues(request.getParameterMap()));
        return this.proxyFactory.createProjection(parameter.getParameterType(), binder.getTarget());
    }

    @Override // org.springframework.web.method.annotation.ModelAttributeMethodProcessor
    protected void bindRequestParameters(WebDataBinder binder, NativeWebRequest request) {
    }
}
