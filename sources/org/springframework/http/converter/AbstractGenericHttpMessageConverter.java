package org.springframework.http.converter;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.StreamingHttpOutputMessage;

/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/http/converter/AbstractGenericHttpMessageConverter.class */
public abstract class AbstractGenericHttpMessageConverter<T> extends AbstractHttpMessageConverter<T> implements GenericHttpMessageConverter<T> {
    protected abstract void writeInternal(T t, Type type, HttpOutputMessage httpOutputMessage) throws IOException, HttpMessageNotWritableException;

    protected AbstractGenericHttpMessageConverter() {
    }

    protected AbstractGenericHttpMessageConverter(MediaType supportedMediaType) {
        super(supportedMediaType);
    }

    protected AbstractGenericHttpMessageConverter(MediaType... supportedMediaTypes) {
        super(supportedMediaTypes);
    }

    @Override // org.springframework.http.converter.AbstractHttpMessageConverter
    protected boolean supports(Class<?> clazz) {
        return true;
    }

    public boolean canRead(Type type, Class<?> contextClass, MediaType mediaType) {
        return type instanceof Class ? canRead((Class) type, mediaType) : canRead(mediaType);
    }

    @Override // org.springframework.http.converter.GenericHttpMessageConverter
    public boolean canWrite(Type type, Class<?> clazz, MediaType mediaType) {
        return canWrite(clazz, mediaType);
    }

    @Override // org.springframework.http.converter.GenericHttpMessageConverter
    public final void write(final T t, final Type type, MediaType contentType, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
        final HttpHeaders headers = outputMessage.getHeaders();
        addDefaultHeaders(headers, t, contentType);
        if (outputMessage instanceof StreamingHttpOutputMessage) {
            StreamingHttpOutputMessage streamingOutputMessage = (StreamingHttpOutputMessage) outputMessage;
            streamingOutputMessage.setBody(new StreamingHttpOutputMessage.Body() { // from class: org.springframework.http.converter.AbstractGenericHttpMessageConverter.1
                /* JADX WARN: Multi-variable type inference failed */
                @Override // org.springframework.http.StreamingHttpOutputMessage.Body
                public void writeTo(final OutputStream outputStream) throws IOException, HttpMessageNotWritableException {
                    AbstractGenericHttpMessageConverter.this.writeInternal(t, type, new HttpOutputMessage() { // from class: org.springframework.http.converter.AbstractGenericHttpMessageConverter.1.1
                        @Override // org.springframework.http.HttpOutputMessage
                        public OutputStream getBody() throws IOException {
                            return outputStream;
                        }

                        @Override // org.springframework.http.HttpMessage
                        public HttpHeaders getHeaders() {
                            return headers;
                        }
                    });
                }
            });
        } else {
            writeInternal(t, type, outputMessage);
            outputMessage.getBody().flush();
        }
    }

    @Override // org.springframework.http.converter.AbstractHttpMessageConverter
    protected void writeInternal(T t, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
        writeInternal(t, null, outputMessage);
    }
}
