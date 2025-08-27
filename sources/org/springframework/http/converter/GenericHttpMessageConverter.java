package org.springframework.http.converter;

import java.io.IOException;
import java.lang.reflect.Type;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;

/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/http/converter/GenericHttpMessageConverter.class */
public interface GenericHttpMessageConverter<T> extends HttpMessageConverter<T> {
    boolean canRead(Type type, Class<?> cls, MediaType mediaType);

    T read(Type type, Class<?> cls, HttpInputMessage httpInputMessage) throws IOException, HttpMessageNotReadableException;

    boolean canWrite(Type type, Class<?> cls, MediaType mediaType);

    void write(T t, Type type, MediaType mediaType, HttpOutputMessage httpOutputMessage) throws IOException, HttpMessageNotWritableException;
}
