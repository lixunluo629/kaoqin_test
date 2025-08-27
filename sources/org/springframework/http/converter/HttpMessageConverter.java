package org.springframework.http.converter;

import java.io.IOException;
import java.util.List;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;

/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/http/converter/HttpMessageConverter.class */
public interface HttpMessageConverter<T> {
    boolean canRead(Class<?> cls, MediaType mediaType);

    boolean canWrite(Class<?> cls, MediaType mediaType);

    List<MediaType> getSupportedMediaTypes();

    T read(Class<? extends T> cls, HttpInputMessage httpInputMessage) throws IOException, HttpMessageNotReadableException;

    void write(T t, MediaType mediaType, HttpOutputMessage httpOutputMessage) throws IOException, HttpMessageNotWritableException;
}
