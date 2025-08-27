package org.springframework.http.converter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.util.ClassUtils;
import org.springframework.util.StreamUtils;

/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/http/converter/ResourceHttpMessageConverter.class */
public class ResourceHttpMessageConverter extends AbstractHttpMessageConverter<Resource> {
    private static final boolean jafPresent = ClassUtils.isPresent("javax.activation.FileTypeMap", ResourceHttpMessageConverter.class.getClassLoader());

    public ResourceHttpMessageConverter() {
        super(MediaType.ALL);
    }

    @Override // org.springframework.http.converter.AbstractHttpMessageConverter
    protected boolean supports(Class<?> clazz) {
        return Resource.class.isAssignableFrom(clazz);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.springframework.http.converter.AbstractHttpMessageConverter
    public Resource readInternal(Class<? extends Resource> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        if (InputStreamResource.class == clazz) {
            return new InputStreamResource(inputMessage.getBody());
        }
        if (clazz.isAssignableFrom(ByteArrayResource.class)) {
            byte[] body = StreamUtils.copyToByteArray(inputMessage.getBody());
            return new ByteArrayResource(body);
        }
        throw new IllegalStateException("Unsupported resource class: " + clazz);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.springframework.http.converter.AbstractHttpMessageConverter
    public MediaType getDefaultContentType(Resource resource) {
        if (jafPresent) {
            return ActivationMediaTypeFactory.getMediaType(resource);
        }
        return MediaType.APPLICATION_OCTET_STREAM;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.springframework.http.converter.AbstractHttpMessageConverter
    public Long getContentLength(Resource resource, MediaType contentType) throws IOException {
        if (InputStreamResource.class == resource.getClass()) {
            return null;
        }
        long contentLength = resource.contentLength();
        if (contentLength < 0) {
            return null;
        }
        return Long.valueOf(contentLength);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.springframework.http.converter.AbstractHttpMessageConverter
    public void writeInternal(Resource resource, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
        writeContent(resource, outputMessage);
    }

    protected void writeContent(Resource resource, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
        try {
            InputStream in = resource.getInputStream();
            try {
                StreamUtils.copy(in, outputMessage.getBody());
                try {
                    in.close();
                } catch (Throwable th) {
                }
            } catch (NullPointerException e) {
                try {
                    in.close();
                } catch (Throwable th2) {
                }
            } catch (Throwable th3) {
                try {
                    in.close();
                } catch (Throwable th4) {
                }
                throw th3;
            }
        } catch (FileNotFoundException e2) {
        }
    }
}
