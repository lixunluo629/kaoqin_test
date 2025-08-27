package io.swagger.models.auth;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import java.util.HashMap;
import java.util.Map;

/* loaded from: swagger-models-1.5.3.jar:io/swagger/models/auth/AbstractSecuritySchemeDefinition.class */
public abstract class AbstractSecuritySchemeDefinition implements SecuritySchemeDefinition {
    private final Map<String, Object> vendorExtensions = new HashMap();

    @Override // io.swagger.models.auth.SecuritySchemeDefinition
    @JsonAnyGetter
    public Map<String, Object> getVendorExtensions() {
        return this.vendorExtensions;
    }

    @Override // io.swagger.models.auth.SecuritySchemeDefinition
    @JsonAnySetter
    public void setVendorExtension(String name, Object value) {
        if (name.startsWith("x-")) {
            this.vendorExtensions.put(name, value);
        }
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AbstractSecuritySchemeDefinition that = (AbstractSecuritySchemeDefinition) o;
        return this.vendorExtensions == null ? that.vendorExtensions == null : this.vendorExtensions.equals(that.vendorExtensions);
    }

    public int hashCode() {
        if (this.vendorExtensions != null) {
            return this.vendorExtensions.hashCode();
        }
        return 0;
    }
}
