package org.springframework.http.converter.support;

import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.xml.Jaxb2RootElementHttpMessageConverter;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
import org.springframework.http.converter.xml.SourceHttpMessageConverter;
import org.springframework.util.ClassUtils;

/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/http/converter/support/AllEncompassingFormHttpMessageConverter.class */
public class AllEncompassingFormHttpMessageConverter extends FormHttpMessageConverter {
    private static final boolean jaxb2Present = ClassUtils.isPresent("javax.xml.bind.Binder", AllEncompassingFormHttpMessageConverter.class.getClassLoader());
    private static final boolean jackson2Present;
    private static final boolean jackson2XmlPresent;
    private static final boolean gsonPresent;

    static {
        jackson2Present = ClassUtils.isPresent("com.fasterxml.jackson.databind.ObjectMapper", AllEncompassingFormHttpMessageConverter.class.getClassLoader()) && ClassUtils.isPresent("com.fasterxml.jackson.core.JsonGenerator", AllEncompassingFormHttpMessageConverter.class.getClassLoader());
        jackson2XmlPresent = ClassUtils.isPresent("com.fasterxml.jackson.dataformat.xml.XmlMapper", AllEncompassingFormHttpMessageConverter.class.getClassLoader());
        gsonPresent = ClassUtils.isPresent("com.google.gson.Gson", AllEncompassingFormHttpMessageConverter.class.getClassLoader());
    }

    public AllEncompassingFormHttpMessageConverter() {
        addPartConverter(new SourceHttpMessageConverter());
        if (jaxb2Present && !jackson2XmlPresent) {
            addPartConverter(new Jaxb2RootElementHttpMessageConverter());
        }
        if (jackson2Present) {
            addPartConverter(new MappingJackson2HttpMessageConverter());
        } else if (gsonPresent) {
            addPartConverter(new GsonHttpMessageConverter());
        }
        if (jackson2XmlPresent) {
            addPartConverter(new MappingJackson2XmlHttpMessageConverter());
        }
    }
}
