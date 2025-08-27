package io.swagger.models.parameters;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Map;

/* loaded from: swagger-models-1.5.3.jar:io/swagger/models/parameters/Parameter.class */
public interface Parameter {
    String getIn();

    void setIn(String str);

    @JsonIgnore
    String getAccess();

    @JsonIgnore
    void setAccess(String str);

    String getName();

    void setName(String str);

    String getDescription();

    void setDescription(String str);

    boolean getRequired();

    void setRequired(boolean z);

    String getPattern();

    void setPattern(String str);

    Map<String, Object> getVendorExtensions();
}
