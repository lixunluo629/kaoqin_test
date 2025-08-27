package org.springframework.http.converter;

import java.io.IOException;
import java.nio.charset.Charset;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.util.Assert;

/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/http/converter/ObjectToStringHttpMessageConverter.class */
public class ObjectToStringHttpMessageConverter extends AbstractHttpMessageConverter<Object> {
    private final ConversionService conversionService;
    private final StringHttpMessageConverter stringHttpMessageConverter;

    public ObjectToStringHttpMessageConverter(ConversionService conversionService) {
        this(conversionService, StringHttpMessageConverter.DEFAULT_CHARSET);
    }

    public ObjectToStringHttpMessageConverter(ConversionService conversionService, Charset defaultCharset) {
        super(defaultCharset, MediaType.TEXT_PLAIN);
        Assert.notNull(conversionService, "ConversionService is required");
        this.conversionService = conversionService;
        this.stringHttpMessageConverter = new StringHttpMessageConverter(defaultCharset);
    }

    public void setWriteAcceptCharset(boolean writeAcceptCharset) {
        this.stringHttpMessageConverter.setWriteAcceptCharset(writeAcceptCharset);
    }

    @Override // org.springframework.http.converter.AbstractHttpMessageConverter, org.springframework.http.converter.HttpMessageConverter
    public boolean canRead(Class<?> clazz, MediaType mediaType) {
        return this.conversionService.canConvert(String.class, clazz) && canRead(mediaType);
    }

    @Override // org.springframework.http.converter.AbstractHttpMessageConverter, org.springframework.http.converter.HttpMessageConverter
    public boolean canWrite(Class<?> clazz, MediaType mediaType) {
        return this.conversionService.canConvert(clazz, String.class) && canWrite(mediaType);
    }

    @Override // org.springframework.http.converter.AbstractHttpMessageConverter
    protected boolean supports(Class<?> clazz) {
        throw new UnsupportedOperationException();
    }

    @Override // org.springframework.http.converter.AbstractHttpMessageConverter
    protected Object readInternal(Class<? extends Object> cls, HttpInputMessage inputMessage) throws IOException {
        String value = this.stringHttpMessageConverter.readInternal(String.class, inputMessage);
        return this.conversionService.convert(value, cls);
    }

    @Override // org.springframework.http.converter.AbstractHttpMessageConverter
    protected void writeInternal(Object obj, HttpOutputMessage outputMessage) throws IOException {
        String value = (String) this.conversionService.convert(obj, String.class);
        this.stringHttpMessageConverter.writeInternal(value, outputMessage);
    }

    @Override // org.springframework.http.converter.AbstractHttpMessageConverter
    protected Long getContentLength(Object obj, MediaType contentType) {
        String value = (String) this.conversionService.convert(obj, String.class);
        return this.stringHttpMessageConverter.getContentLength(value, contentType);
    }
}
