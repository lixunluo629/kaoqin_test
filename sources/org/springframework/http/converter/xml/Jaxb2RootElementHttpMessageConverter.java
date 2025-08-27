package org.springframework.http.converter.xml;

import java.io.IOException;
import java.io.StringReader;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.MarshalException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.bind.UnmarshalException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamSource;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.util.ClassUtils;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/http/converter/xml/Jaxb2RootElementHttpMessageConverter.class */
public class Jaxb2RootElementHttpMessageConverter extends AbstractJaxb2HttpMessageConverter<Object> {
    private boolean supportDtd = false;
    private boolean processExternalEntities = false;
    private static final EntityResolver NO_OP_ENTITY_RESOLVER = new EntityResolver() { // from class: org.springframework.http.converter.xml.Jaxb2RootElementHttpMessageConverter.1
        @Override // org.xml.sax.EntityResolver
        public InputSource resolveEntity(String publicId, String systemId) {
            return new InputSource(new StringReader(""));
        }
    };

    public void setSupportDtd(boolean supportDtd) {
        this.supportDtd = supportDtd;
    }

    public boolean isSupportDtd() {
        return this.supportDtd;
    }

    public void setProcessExternalEntities(boolean processExternalEntities) {
        this.processExternalEntities = processExternalEntities;
        if (processExternalEntities) {
            setSupportDtd(true);
        }
    }

    public boolean isProcessExternalEntities() {
        return this.processExternalEntities;
    }

    @Override // org.springframework.http.converter.AbstractHttpMessageConverter, org.springframework.http.converter.HttpMessageConverter
    public boolean canRead(Class<?> clazz, MediaType mediaType) {
        return (clazz.isAnnotationPresent(XmlRootElement.class) || clazz.isAnnotationPresent(XmlType.class)) && canRead(mediaType);
    }

    @Override // org.springframework.http.converter.AbstractHttpMessageConverter, org.springframework.http.converter.HttpMessageConverter
    public boolean canWrite(Class<?> clazz, MediaType mediaType) {
        return AnnotationUtils.findAnnotation(clazz, XmlRootElement.class) != null && canWrite(mediaType);
    }

    @Override // org.springframework.http.converter.AbstractHttpMessageConverter
    protected boolean supports(Class<?> clazz) {
        throw new UnsupportedOperationException();
    }

    @Override // org.springframework.http.converter.xml.AbstractXmlHttpMessageConverter
    protected Object readFromSource(Class<?> clazz, HttpHeaders headers, Source source) throws SAXException, IOException {
        try {
            Source source2 = processSource(source);
            Unmarshaller unmarshaller = createUnmarshaller(clazz);
            if (clazz.isAnnotationPresent(XmlRootElement.class)) {
                return unmarshaller.unmarshal(source2);
            }
            JAXBElement<?> jaxbElement = unmarshaller.unmarshal(source2, clazz);
            return jaxbElement.getValue();
        } catch (NullPointerException ex) {
            if (!isSupportDtd()) {
                throw new HttpMessageNotReadableException("NPE while unmarshalling. This can happen due to the presence of DTD declarations which are disabled.", ex);
            }
            throw ex;
        } catch (UnmarshalException ex2) {
            throw new HttpMessageNotReadableException("Could not unmarshal to [" + clazz + "]: " + ex2.getMessage(), ex2);
        } catch (JAXBException ex3) {
            throw new HttpMessageConversionException("Invalid JAXB setup: " + ex3.getMessage(), ex3);
        }
    }

    protected Source processSource(Source source) throws SAXException {
        if (source instanceof StreamSource) {
            StreamSource streamSource = (StreamSource) source;
            InputSource inputSource = new InputSource(streamSource.getInputStream());
            try {
                XMLReader xmlReader = XMLReaderFactory.createXMLReader();
                xmlReader.setFeature("http://apache.org/xml/features/disallow-doctype-decl", !isSupportDtd());
                xmlReader.setFeature("http://xml.org/sax/features/external-general-entities", isProcessExternalEntities());
                if (!isProcessExternalEntities()) {
                    xmlReader.setEntityResolver(NO_OP_ENTITY_RESOLVER);
                }
                return new SAXSource(xmlReader, inputSource);
            } catch (SAXException ex) {
                this.logger.warn("Processing of external entities could not be disabled", ex);
                return source;
            }
        }
        return source;
    }

    @Override // org.springframework.http.converter.xml.AbstractXmlHttpMessageConverter
    protected void writeToResult(Object o, HttpHeaders headers, Result result) throws PropertyException, IOException {
        try {
            Class<?> clazz = ClassUtils.getUserClass(o);
            Marshaller marshaller = createMarshaller(clazz);
            setCharset(headers.getContentType(), marshaller);
            marshaller.marshal(o, result);
        } catch (MarshalException ex) {
            throw new HttpMessageNotWritableException("Could not marshal [" + o + "]: " + ex.getMessage(), ex);
        } catch (JAXBException ex2) {
            throw new HttpMessageConversionException("Invalid JAXB setup: " + ex2.getMessage(), ex2);
        }
    }

    private void setCharset(MediaType contentType, Marshaller marshaller) throws PropertyException {
        if (contentType != null && contentType.getCharset() != null) {
            marshaller.setProperty("jaxb.encoding", contentType.getCharset().name());
        }
    }
}
