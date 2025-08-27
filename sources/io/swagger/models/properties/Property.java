package io.swagger.models.properties;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.models.Xml;
import java.util.Map;

/* loaded from: swagger-models-1.5.3.jar:io/swagger/models/properties/Property.class */
public interface Property {
    Property title(String str);

    Property description(String str);

    String getType();

    String getFormat();

    String getTitle();

    void setTitle(String str);

    String getDescription();

    void setDescription(String str);

    @JsonIgnore
    String getName();

    void setName(String str);

    @JsonIgnore
    boolean getRequired();

    void setRequired(boolean z);

    String getExample();

    void setExample(String str);

    Boolean getReadOnly();

    void setReadOnly(Boolean bool);

    Integer getPosition();

    void setPosition(Integer num);

    Xml getXml();

    void setXml(Xml xml);

    void setDefault(String str);

    @JsonIgnore
    String getAccess();

    @JsonIgnore
    void setAccess(String str);

    Map<String, Object> getVendorExtensions();
}
