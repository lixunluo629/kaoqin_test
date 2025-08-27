package io.swagger.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.models.properties.Property;
import io.swagger.models.refs.GenericRef;
import io.swagger.models.refs.RefConstants;
import io.swagger.models.refs.RefFormat;
import io.swagger.models.refs.RefType;
import java.util.Map;

/* loaded from: swagger-models-1.5.3.jar:io/swagger/models/RefModel.class */
public class RefModel implements Model {
    private GenericRef genericRef;
    private String description;
    private ExternalDocs externalDocs;
    private Map<String, Property> properties;
    private Object example;

    public RefModel() {
    }

    public RefModel(String ref) {
        set$ref(ref);
    }

    public RefModel asDefault(String ref) {
        set$ref(RefConstants.INTERNAL_DEFINITION_PREFIX + ref);
        return this;
    }

    @Override // io.swagger.models.Model
    @JsonIgnore
    public String getDescription() {
        return this.description;
    }

    @Override // io.swagger.models.Model
    public void setDescription(String description) {
        this.description = description;
    }

    @Override // io.swagger.models.Model
    @JsonIgnore
    public Map<String, Property> getProperties() {
        return this.properties;
    }

    @Override // io.swagger.models.Model
    public void setProperties(Map<String, Property> properties) {
        this.properties = properties;
    }

    @JsonIgnore
    public String getSimpleRef() {
        return this.genericRef.getSimpleRef();
    }

    public String get$ref() {
        return this.genericRef.getRef();
    }

    public void set$ref(String ref) {
        this.genericRef = new GenericRef(RefType.DEFINITION, ref);
    }

    @JsonIgnore
    public RefFormat getRefFormat() {
        return this.genericRef.getFormat();
    }

    @Override // io.swagger.models.Model
    @JsonIgnore
    public Object getExample() {
        return this.example;
    }

    @Override // io.swagger.models.Model
    public void setExample(Object example) {
        this.example = example;
    }

    @Override // io.swagger.models.Model
    @JsonIgnore
    public ExternalDocs getExternalDocs() {
        return this.externalDocs;
    }

    public void setExternalDocs(ExternalDocs value) {
        this.externalDocs = value;
    }

    @Override // io.swagger.models.Model
    public Object clone() {
        RefModel cloned = new RefModel();
        cloned.genericRef = this.genericRef;
        cloned.description = this.description;
        cloned.properties = this.properties;
        cloned.example = this.example;
        return cloned;
    }

    @Override // io.swagger.models.Model
    @JsonIgnore
    public Map<String, Object> getVendorExtensions() {
        return null;
    }

    public int hashCode() {
        int result = (31 * 1) + (this.description == null ? 0 : this.description.hashCode());
        return (31 * ((31 * ((31 * ((31 * result) + (this.example == null ? 0 : this.example.hashCode()))) + (this.externalDocs == null ? 0 : this.externalDocs.hashCode()))) + (this.properties == null ? 0 : this.properties.hashCode()))) + (this.genericRef == null ? 0 : this.genericRef.hashCode());
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        RefModel other = (RefModel) obj;
        if (this.description == null) {
            if (other.description != null) {
                return false;
            }
        } else if (!this.description.equals(other.description)) {
            return false;
        }
        if (this.example == null) {
            if (other.example != null) {
                return false;
            }
        } else if (!this.example.equals(other.example)) {
            return false;
        }
        if (this.externalDocs == null) {
            if (other.externalDocs != null) {
                return false;
            }
        } else if (!this.externalDocs.equals(other.externalDocs)) {
            return false;
        }
        if (this.properties == null) {
            if (other.properties != null) {
                return false;
            }
        } else if (!this.properties.equals(other.properties)) {
            return false;
        }
        if (this.genericRef == null) {
            if (other.genericRef != null) {
                return false;
            }
            return true;
        }
        if (!this.genericRef.equals(other.genericRef)) {
            return false;
        }
        return true;
    }

    @Override // io.swagger.models.Model
    @JsonIgnore
    public String getReference() {
        return this.genericRef.getRef();
    }

    @Override // io.swagger.models.Model
    public void setReference(String reference) {
        this.genericRef = new GenericRef(RefType.DEFINITION, reference);
    }
}
