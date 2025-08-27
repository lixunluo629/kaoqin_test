package org.springframework.http.converter.json;

import com.fasterxml.jackson.databind.ser.FilterProvider;

/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/http/converter/json/MappingJacksonValue.class */
public class MappingJacksonValue {
    private Object value;
    private Class<?> serializationView;
    private FilterProvider filters;
    private String jsonpFunction;

    public MappingJacksonValue(Object value) {
        this.value = value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public Object getValue() {
        return this.value;
    }

    public void setSerializationView(Class<?> serializationView) {
        this.serializationView = serializationView;
    }

    public Class<?> getSerializationView() {
        return this.serializationView;
    }

    public void setFilters(FilterProvider filters) {
        this.filters = filters;
    }

    public FilterProvider getFilters() {
        return this.filters;
    }

    @Deprecated
    public void setJsonpFunction(String functionName) {
        this.jsonpFunction = functionName;
    }

    @Deprecated
    public String getJsonpFunction() {
        return this.jsonpFunction;
    }
}
