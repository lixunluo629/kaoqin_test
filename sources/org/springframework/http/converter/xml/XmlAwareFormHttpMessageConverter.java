package org.springframework.http.converter.xml;

import org.springframework.http.converter.FormHttpMessageConverter;

@Deprecated
/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/http/converter/xml/XmlAwareFormHttpMessageConverter.class */
public class XmlAwareFormHttpMessageConverter extends FormHttpMessageConverter {
    public XmlAwareFormHttpMessageConverter() {
        addPartConverter(new SourceHttpMessageConverter());
    }
}
