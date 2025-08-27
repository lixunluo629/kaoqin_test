package org.springframework.data.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.spi.mapper.JacksonMappingProvider;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.core.ResolvableType;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.Assert;
import org.springframework.util.ConcurrentReferenceHashMap;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/web/ProjectingJackson2HttpMessageConverter.class */
public class ProjectingJackson2HttpMessageConverter extends MappingJackson2HttpMessageConverter implements BeanClassLoaderAware, BeanFactoryAware {
    private final SpelAwareProxyProjectionFactory projectionFactory;
    private final Map<Class<?>, Boolean> supportedTypesCache;

    public ProjectingJackson2HttpMessageConverter() {
        this.supportedTypesCache = new ConcurrentReferenceHashMap();
        this.projectionFactory = initProjectionFactory(getObjectMapper());
    }

    public ProjectingJackson2HttpMessageConverter(ObjectMapper mapper) {
        super(mapper);
        this.supportedTypesCache = new ConcurrentReferenceHashMap();
        this.projectionFactory = initProjectionFactory(mapper);
    }

    private static SpelAwareProxyProjectionFactory initProjectionFactory(ObjectMapper mapper) {
        Assert.notNull(mapper, "ObjectMapper must not be null!");
        SpelAwareProxyProjectionFactory projectionFactory = new SpelAwareProxyProjectionFactory();
        projectionFactory.registerMethodInvokerFactory(new JsonProjectingMethodInterceptorFactory(new JacksonMappingProvider(mapper)));
        return projectionFactory;
    }

    @Override // org.springframework.beans.factory.BeanClassLoaderAware
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.projectionFactory.setBeanClassLoader(classLoader);
    }

    @Override // org.springframework.beans.factory.BeanFactoryAware
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.projectionFactory.setBeanFactory(beanFactory);
    }

    @Override // org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter, org.springframework.http.converter.AbstractGenericHttpMessageConverter, org.springframework.http.converter.GenericHttpMessageConverter
    public boolean canRead(Type type, Class<?> contextClass, MediaType mediaType) {
        if (!canRead(mediaType)) {
            return false;
        }
        ResolvableType owner = contextClass == null ? null : ResolvableType.forClass(contextClass);
        Class<?> rawType = ResolvableType.forType(type, owner).resolve(Object.class);
        Boolean result = this.supportedTypesCache.get(rawType);
        if (result != null) {
            return result.booleanValue();
        }
        Boolean result2 = Boolean.valueOf(rawType.isInterface() && AnnotationUtils.findAnnotation(rawType, ProjectedPayload.class) != null);
        this.supportedTypesCache.put(rawType, result2);
        return result2.booleanValue();
    }

    @Override // org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter, org.springframework.http.converter.AbstractHttpMessageConverter, org.springframework.http.converter.HttpMessageConverter
    public boolean canWrite(Class<?> clazz, MediaType mediaType) {
        return false;
    }

    @Override // org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter, org.springframework.http.converter.GenericHttpMessageConverter
    public Object read(Type type, Class<?> contextClass, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        return this.projectionFactory.createProjection(ResolvableType.forType(type).getRawClass(), inputMessage.getBody());
    }
}
