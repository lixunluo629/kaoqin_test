package org.springframework.data.web;

import java.io.IOException;
import java.util.Map;
import javax.xml.parsers.DocumentBuilderFactory;
import org.springframework.core.ResolvableType;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.util.Assert;
import org.springframework.util.ConcurrentReferenceHashMap;
import org.xml.sax.SAXParseException;
import org.xmlbeam.XBProjector;
import org.xmlbeam.config.DefaultXMLFactoriesConfig;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/web/XmlBeamHttpMessageConverter.class */
public class XmlBeamHttpMessageConverter extends AbstractHttpMessageConverter<Object> {
    private final XBProjector projectionFactory;
    private final Map<Class<?>, Boolean> supportedTypesCache;

    public XmlBeamHttpMessageConverter() {
        this(new XBProjector(new DefaultXMLFactoriesConfig() { // from class: org.springframework.data.web.XmlBeamHttpMessageConverter.1
            private static final long serialVersionUID = -1324345769124477493L;

            public DocumentBuilderFactory createDocumentBuilderFactory() throws IllegalArgumentException {
                DocumentBuilderFactory factory = super.createDocumentBuilderFactory();
                factory.setAttribute("http://apache.org/xml/features/disallow-doctype-decl", true);
                factory.setAttribute("http://xml.org/sax/features/external-general-entities", false);
                return factory;
            }
        }, new XBProjector.Flags[0]));
    }

    public XmlBeamHttpMessageConverter(XBProjector projector) {
        super(MediaType.APPLICATION_XML, MediaType.parseMediaType("application/*+xml"));
        this.supportedTypesCache = new ConcurrentReferenceHashMap();
        Assert.notNull(projector, "XBProjector must not be null!");
        this.projectionFactory = projector;
    }

    @Override // org.springframework.http.converter.AbstractHttpMessageConverter
    protected boolean supports(Class<?> type) {
        Class<?> rawType = ResolvableType.forType(type).getRawClass();
        Boolean result = this.supportedTypesCache.get(rawType);
        if (result != null) {
            return result.booleanValue();
        }
        Boolean result2 = Boolean.valueOf(rawType.isInterface() && AnnotationUtils.findAnnotation(rawType, ProjectedPayload.class) != null);
        this.supportedTypesCache.put(rawType, result2);
        return result2.booleanValue();
    }

    @Override // org.springframework.http.converter.AbstractHttpMessageConverter, org.springframework.http.converter.HttpMessageConverter
    public boolean canWrite(Class<?> clazz, MediaType mediaType) {
        return false;
    }

    @Override // org.springframework.http.converter.AbstractHttpMessageConverter
    protected Object readInternal(Class<? extends Object> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        try {
            return this.projectionFactory.io().stream(inputMessage.getBody()).read(clazz);
        } catch (RuntimeException o_O) {
            Throwable cause = o_O.getCause();
            if (SAXParseException.class.isInstance(cause)) {
                throw new HttpMessageNotReadableException("Cannot read input message!", cause);
            }
            throw o_O;
        }
    }

    @Override // org.springframework.http.converter.AbstractHttpMessageConverter
    protected void writeInternal(Object t, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
    }
}
