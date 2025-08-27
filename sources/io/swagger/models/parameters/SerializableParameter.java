package io.swagger.models.parameters;

import io.swagger.models.properties.Property;
import java.util.List;

/* loaded from: swagger-models-1.5.3.jar:io/swagger/models/parameters/SerializableParameter.class */
public interface SerializableParameter extends Parameter {
    String getType();

    void setType(String str);

    Property getItems();

    void setItems(Property property);

    String getFormat();

    void setFormat(String str);

    String getCollectionFormat();

    void setCollectionFormat(String str);

    List<String> getEnum();

    void setEnum(List<String> list);
}
