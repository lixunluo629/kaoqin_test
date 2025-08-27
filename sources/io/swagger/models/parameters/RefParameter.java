package io.swagger.models.parameters;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.models.refs.GenericRef;
import io.swagger.models.refs.RefConstants;
import io.swagger.models.refs.RefFormat;
import io.swagger.models.refs.RefType;

/* loaded from: swagger-models-1.5.3.jar:io/swagger/models/parameters/RefParameter.class */
public class RefParameter extends AbstractParameter implements Parameter {
    private GenericRef genericRef;

    public RefParameter(String ref) {
        set$ref(ref);
    }

    public static boolean isType(String type, String format) {
        if ("$ref".equals(type)) {
            return true;
        }
        return false;
    }

    public RefParameter asDefault(String ref) {
        set$ref(RefConstants.INTERNAL_PARAMETER_PREFIX + ref);
        return this;
    }

    public RefParameter description(String description) {
        setDescription(description);
        return this;
    }

    public String get$ref() {
        return this.genericRef.getRef();
    }

    public void set$ref(String ref) {
        this.genericRef = new GenericRef(RefType.PARAMETER, ref);
    }

    @JsonIgnore
    public RefFormat getRefFormat() {
        return this.genericRef.getFormat();
    }

    @Override // io.swagger.models.parameters.AbstractParameter, io.swagger.models.parameters.Parameter
    @JsonIgnore
    public boolean getRequired() {
        return this.required;
    }

    @JsonIgnore
    public String getSimpleRef() {
        return this.genericRef.getSimpleRef();
    }

    @Override // io.swagger.models.parameters.AbstractParameter
    public int hashCode() {
        int result = super.hashCode();
        return (31 * result) + (this.genericRef == null ? 0 : this.genericRef.hashCode());
    }

    @Override // io.swagger.models.parameters.AbstractParameter
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj) || getClass() != obj.getClass()) {
            return false;
        }
        RefParameter other = (RefParameter) obj;
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
