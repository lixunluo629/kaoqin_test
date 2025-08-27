package io.swagger.models;

import io.swagger.models.properties.Property;
import java.util.Map;

/* loaded from: swagger-models-1.5.3.jar:io/swagger/models/Model.class */
public interface Model {
    String getDescription();

    void setDescription(String str);

    Map<String, Property> getProperties();

    void setProperties(Map<String, Property> map);

    Object getExample();

    void setExample(Object obj);

    ExternalDocs getExternalDocs();

    String getReference();

    void setReference(String str);

    Object clone();

    Map<String, Object> getVendorExtensions();
}
