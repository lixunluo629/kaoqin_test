package io.swagger.models.properties;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.models.refs.GenericRef;
import io.swagger.models.refs.RefConstants;
import io.swagger.models.refs.RefFormat;
import io.swagger.models.refs.RefType;

/* loaded from: swagger-models-1.5.3.jar:io/swagger/models/properties/RefProperty.class */
public class RefProperty extends AbstractProperty implements Property {
    private static final String TYPE = "ref";
    private GenericRef genericRef;

    public RefProperty() {
        setType("ref");
    }

    public RefProperty(String ref) {
        this();
        set$ref(ref);
    }

    public static boolean isType(String type, String format) {
        if ("ref".equals(type)) {
            return true;
        }
        return false;
    }

    public RefProperty asDefault(String ref) {
        set$ref(RefConstants.INTERNAL_DEFINITION_PREFIX + ref);
        return this;
    }

    @Override // io.swagger.models.properties.AbstractProperty, io.swagger.models.properties.Property
    public RefProperty description(String description) {
        setDescription(description);
        return this;
    }

    @Override // io.swagger.models.properties.AbstractProperty, io.swagger.models.properties.Property
    @JsonIgnore
    public String getType() {
        return this.type;
    }

    @Override // io.swagger.models.properties.AbstractProperty
    @JsonIgnore
    public void setType(String type) {
        this.type = type;
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

    @JsonIgnore
    public String getSimpleRef() {
        return this.genericRef.getSimpleRef();
    }

    @Override // io.swagger.models.properties.AbstractProperty
    public int hashCode() {
        int result = super.hashCode();
        return (31 * result) + (this.genericRef == null ? 0 : this.genericRef.hashCode());
    }

    @Override // io.swagger.models.properties.AbstractProperty
    public boolean equals(Object obj) {
        if (!super.equals(obj) || !(obj instanceof RefProperty)) {
            return false;
        }
        RefProperty other = (RefProperty) obj;
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
}
