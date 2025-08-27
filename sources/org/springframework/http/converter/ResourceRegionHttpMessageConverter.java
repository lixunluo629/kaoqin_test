package org.springframework.http.converter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import org.springframework.core.io.support.ResourceRegion;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.MimeTypeUtils;
import org.springframework.util.StreamUtils;

/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/http/converter/ResourceRegionHttpMessageConverter.class */
public class ResourceRegionHttpMessageConverter extends AbstractGenericHttpMessageConverter<Object> {
    private static final boolean jafPresent = ClassUtils.isPresent("javax.activation.FileTypeMap", ResourceHttpMessageConverter.class.getClassLoader());

    @Override // org.springframework.http.converter.AbstractHttpMessageConverter
    protected /* bridge */ /* synthetic */ Object readInternal(Class cls, HttpInputMessage httpInputMessage) throws IOException, HttpMessageNotReadableException {
        return readInternal((Class<?>) cls, httpInputMessage);
    }

    public ResourceRegionHttpMessageConverter() {
        super(MediaType.ALL);
    }

    @Override // org.springframework.http.converter.AbstractHttpMessageConverter
    protected MediaType getDefaultContentType(Object object) {
        if (jafPresent) {
            if (object instanceof ResourceRegion) {
                return ActivationMediaTypeFactory.getMediaType(((ResourceRegion) object).getResource());
            }
            Collection<ResourceRegion> regions = (Collection) object;
            if (!regions.isEmpty()) {
                return ActivationMediaTypeFactory.getMediaType(regions.iterator().next().getResource());
            }
        }
        return MediaType.APPLICATION_OCTET_STREAM;
    }

    @Override // org.springframework.http.converter.AbstractHttpMessageConverter, org.springframework.http.converter.HttpMessageConverter
    public boolean canRead(Class<?> clazz, MediaType mediaType) {
        return false;
    }

    @Override // org.springframework.http.converter.AbstractGenericHttpMessageConverter, org.springframework.http.converter.GenericHttpMessageConverter
    public boolean canRead(Type type, Class<?> contextClass, MediaType mediaType) {
        return false;
    }

    @Override // org.springframework.http.converter.GenericHttpMessageConverter
    public Object read(Type type, Class<?> contextClass, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        throw new UnsupportedOperationException();
    }

    @Override // org.springframework.http.converter.AbstractHttpMessageConverter
    protected ResourceRegion readInternal(Class<?> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        throw new UnsupportedOperationException();
    }

    @Override // org.springframework.http.converter.AbstractHttpMessageConverter, org.springframework.http.converter.HttpMessageConverter
    public boolean canWrite(Class<?> clazz, MediaType mediaType) {
        return canWrite(clazz, null, mediaType);
    }

    @Override // org.springframework.http.converter.AbstractGenericHttpMessageConverter, org.springframework.http.converter.GenericHttpMessageConverter
    public boolean canWrite(Type type, Class<?> clazz, MediaType mediaType) {
        if (!(type instanceof ParameterizedType)) {
            return ResourceRegion.class.isAssignableFrom((Class) type);
        }
        ParameterizedType parameterizedType = (ParameterizedType) type;
        if (!(parameterizedType.getRawType() instanceof Class)) {
            return false;
        }
        Class<?> rawType = (Class) parameterizedType.getRawType();
        if (!Collection.class.isAssignableFrom(rawType) || parameterizedType.getActualTypeArguments().length != 1) {
            return false;
        }
        Type typeArgument = parameterizedType.getActualTypeArguments()[0];
        if (!(typeArgument instanceof Class)) {
            return false;
        }
        Class<?> typeArgumentClass = (Class) typeArgument;
        return typeArgumentClass.isAssignableFrom(ResourceRegion.class);
    }

    @Override // org.springframework.http.converter.AbstractGenericHttpMessageConverter
    protected void writeInternal(Object object, Type type, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
        if (object instanceof ResourceRegion) {
            writeResourceRegion((ResourceRegion) object, outputMessage);
            return;
        }
        Collection<ResourceRegion> regions = (Collection) object;
        if (regions.size() == 1) {
            writeResourceRegion(regions.iterator().next(), outputMessage);
        } else {
            writeResourceRegionCollection((Collection) object, outputMessage);
        }
    }

    protected void writeResourceRegion(ResourceRegion region, HttpOutputMessage outputMessage) throws IOException {
        Assert.notNull(region, "ResourceRegion must not be null");
        HttpHeaders responseHeaders = outputMessage.getHeaders();
        long start = region.getPosition();
        long end = (start + region.getCount()) - 1;
        Long resourceLength = Long.valueOf(region.getResource().contentLength());
        long end2 = Math.min(end, resourceLength.longValue() - 1);
        long rangeLength = (end2 - start) + 1;
        responseHeaders.add("Content-Range", "bytes " + start + '-' + end2 + '/' + resourceLength);
        responseHeaders.setContentLength(rangeLength);
        InputStream in = region.getResource().getInputStream();
        try {
            StreamUtils.copyRange(in, outputMessage.getBody(), start, end2);
        } finally {
            try {
                in.close();
            } catch (IOException e) {
            }
        }
    }

    private void writeResourceRegionCollection(Collection<ResourceRegion> resourceRegions, HttpOutputMessage outputMessage) throws IOException {
        Assert.notNull(resourceRegions, "Collection of ResourceRegion should not be null");
        HttpHeaders responseHeaders = outputMessage.getHeaders();
        MediaType contentType = responseHeaders.getContentType();
        String boundaryString = MimeTypeUtils.generateMultipartBoundaryString();
        responseHeaders.set("Content-Type", "multipart/byteranges; boundary=" + boundaryString);
        OutputStream out = outputMessage.getBody();
        for (ResourceRegion region : resourceRegions) {
            long start = region.getPosition();
            long end = (start + region.getCount()) - 1;
            InputStream in = region.getResource().getInputStream();
            try {
                println(out);
                print(out, ScriptUtils.DEFAULT_COMMENT_PREFIX + boundaryString);
                println(out);
                if (contentType != null) {
                    print(out, "Content-Type: " + contentType.toString());
                    println(out);
                }
                Long resourceLength = Long.valueOf(region.getResource().contentLength());
                long end2 = Math.min(end, resourceLength.longValue() - 1);
                print(out, "Content-Range: bytes " + start + '-' + end2 + '/' + resourceLength);
                println(out);
                println(out);
                StreamUtils.copyRange(in, out, start, end2);
                try {
                    in.close();
                } catch (IOException e) {
                }
            } catch (Throwable th) {
                try {
                    in.close();
                } catch (IOException e2) {
                }
                throw th;
            }
        }
        println(out);
        print(out, ScriptUtils.DEFAULT_COMMENT_PREFIX + boundaryString + ScriptUtils.DEFAULT_COMMENT_PREFIX);
    }

    private static void println(OutputStream os) throws IOException {
        os.write(13);
        os.write(10);
    }

    private static void print(OutputStream os, String buf) throws IOException {
        os.write(buf.getBytes("US-ASCII"));
    }
}
