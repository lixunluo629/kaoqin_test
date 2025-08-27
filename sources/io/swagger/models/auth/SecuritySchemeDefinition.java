package io.swagger.models.auth;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import java.util.Map;

/* loaded from: swagger-models-1.5.3.jar:io/swagger/models/auth/SecuritySchemeDefinition.class */
public interface SecuritySchemeDefinition {
    String getType();

    void setType(String str);

    @JsonAnyGetter
    Map<String, Object> getVendorExtensions();

    @JsonAnySetter
    void setVendorExtension(String str, Object obj);
}
