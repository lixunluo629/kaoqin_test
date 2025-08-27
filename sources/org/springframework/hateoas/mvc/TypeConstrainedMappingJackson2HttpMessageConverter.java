package org.springframework.hateoas.mvc;

import java.lang.reflect.Type;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.Assert;

/* loaded from: spring-hateoas-0.23.0.RELEASE.jar:org/springframework/hateoas/mvc/TypeConstrainedMappingJackson2HttpMessageConverter.class */
public class TypeConstrainedMappingJackson2HttpMessageConverter extends MappingJackson2HttpMessageConverter {
    private final Class<?> type;

    public TypeConstrainedMappingJackson2HttpMessageConverter(Class<?> type) {
        Assert.notNull(type, "Type must not be null!");
        this.type = type;
    }

    @Override // org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter, org.springframework.http.converter.AbstractHttpMessageConverter, org.springframework.http.converter.HttpMessageConverter
    public boolean canRead(Class<?> clazz, MediaType mediaType) {
        return this.type.isAssignableFrom(clazz) && super.canRead(clazz, mediaType);
    }

    @Override // org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter, org.springframework.http.converter.AbstractGenericHttpMessageConverter, org.springframework.http.converter.GenericHttpMessageConverter
    public boolean canRead(Type type, Class<?> contextClass, MediaType mediaType) {
        return this.type.isAssignableFrom(getJavaType(type, contextClass).getRawClass()) && super.canRead(type, contextClass, mediaType);
    }

    @Override // org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter, org.springframework.http.converter.AbstractHttpMessageConverter, org.springframework.http.converter.HttpMessageConverter
    public boolean canWrite(Class<?> clazz, MediaType mediaType) {
        return this.type.isAssignableFrom(clazz) && super.canWrite(clazz, mediaType);
    }
}
