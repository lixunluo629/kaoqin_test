package org.springframework.http.converter.xml;

import java.io.IOException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.oxm.Marshaller;
import org.springframework.oxm.MarshallingFailureException;
import org.springframework.oxm.Unmarshaller;
import org.springframework.oxm.UnmarshallingFailureException;
import org.springframework.oxm.XmlMappingException;
import org.springframework.util.Assert;

/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/http/converter/xml/MarshallingHttpMessageConverter.class */
public class MarshallingHttpMessageConverter extends AbstractXmlHttpMessageConverter<Object> {
    private Marshaller marshaller;
    private Unmarshaller unmarshaller;

    public MarshallingHttpMessageConverter() {
    }

    public MarshallingHttpMessageConverter(Marshaller marshaller) {
        Assert.notNull(marshaller, "Marshaller must not be null");
        this.marshaller = marshaller;
        if (marshaller instanceof Unmarshaller) {
            this.unmarshaller = (Unmarshaller) marshaller;
        }
    }

    public MarshallingHttpMessageConverter(Marshaller marshaller, Unmarshaller unmarshaller) {
        Assert.notNull(marshaller, "Marshaller must not be null");
        Assert.notNull(unmarshaller, "Unmarshaller must not be null");
        this.marshaller = marshaller;
        this.unmarshaller = unmarshaller;
    }

    public void setMarshaller(Marshaller marshaller) {
        this.marshaller = marshaller;
    }

    public void setUnmarshaller(Unmarshaller unmarshaller) {
        this.unmarshaller = unmarshaller;
    }

    @Override // org.springframework.http.converter.AbstractHttpMessageConverter, org.springframework.http.converter.HttpMessageConverter
    public boolean canRead(Class<?> clazz, MediaType mediaType) {
        return canRead(mediaType) && this.unmarshaller != null && this.unmarshaller.supports(clazz);
    }

    @Override // org.springframework.http.converter.AbstractHttpMessageConverter, org.springframework.http.converter.HttpMessageConverter
    public boolean canWrite(Class<?> clazz, MediaType mediaType) {
        return canWrite(mediaType) && this.marshaller != null && this.marshaller.supports(clazz);
    }

    @Override // org.springframework.http.converter.AbstractHttpMessageConverter
    protected boolean supports(Class<?> clazz) {
        throw new UnsupportedOperationException();
    }

    @Override // org.springframework.http.converter.xml.AbstractXmlHttpMessageConverter
    protected Object readFromSource(Class<? extends Object> cls, HttpHeaders headers, Source source) throws XmlMappingException, IOException {
        Assert.notNull(this.unmarshaller, "Property 'unmarshaller' is required");
        try {
            Object result = this.unmarshaller.unmarshal(source);
            if (!cls.isInstance(result)) {
                throw new TypeMismatchException(result, cls);
            }
            return result;
        } catch (UnmarshallingFailureException ex) {
            throw new HttpMessageNotReadableException("Could not read [" + cls + "]", ex);
        }
    }

    @Override // org.springframework.http.converter.xml.AbstractXmlHttpMessageConverter
    protected void writeToResult(Object o, HttpHeaders headers, Result result) throws XmlMappingException, IOException {
        Assert.notNull(this.marshaller, "Property 'marshaller' is required");
        try {
            this.marshaller.marshal(o, result);
        } catch (MarshallingFailureException ex) {
            throw new HttpMessageNotWritableException("Could not write [" + o + "]", ex);
        }
    }
}
