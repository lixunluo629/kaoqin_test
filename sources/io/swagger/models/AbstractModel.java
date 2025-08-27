package io.swagger.models;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.HashMap;
import java.util.Map;

/* loaded from: swagger-models-1.5.3.jar:io/swagger/models/AbstractModel.class */
public abstract class AbstractModel implements Model {
    private ExternalDocs externalDocs;
    private String reference;
    private final Map<String, Object> vendorExtensions = new HashMap();

    @Override // io.swagger.models.Model
    public ExternalDocs getExternalDocs() {
        return this.externalDocs;
    }

    public void setExternalDocs(ExternalDocs value) {
        this.externalDocs = value;
    }

    @Override // io.swagger.models.Model
    @JsonAnyGetter
    public Map<String, Object> getVendorExtensions() {
        return this.vendorExtensions;
    }

    @JsonAnySetter
    public void setVendorExtension(String name, Object value) {
        if (name.startsWith("x-")) {
            this.vendorExtensions.put(name, value);
        }
    }

    public void cloneTo(Object clone) {
        AbstractModel cloned = (AbstractModel) clone;
        cloned.externalDocs = this.externalDocs;
    }

    @Override // io.swagger.models.Model
    public Object clone() {
        return null;
    }

    public int hashCode() {
        int result = (31 * 1) + (this.externalDocs == null ? 0 : this.externalDocs.hashCode());
        return (31 * result) + (this.vendorExtensions == null ? 0 : this.vendorExtensions.hashCode());
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        AbstractModel other = (AbstractModel) obj;
        if (this.externalDocs == null) {
            if (other.externalDocs != null) {
                return false;
            }
        } else if (!this.externalDocs.equals(other.externalDocs)) {
            return false;
        }
        if (this.vendorExtensions == null) {
            if (other.vendorExtensions != null) {
                return false;
            }
            return true;
        }
        if (!this.vendorExtensions.equals(other.vendorExtensions)) {
            return false;
        }
        return true;
    }

    @Override // io.swagger.models.Model
    @JsonIgnore
    public String getReference() {
        return this.reference;
    }

    @Override // io.swagger.models.Model
    public void setReference(String reference) {
        this.reference = reference;
    }
}
