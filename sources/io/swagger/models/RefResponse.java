package io.swagger.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.models.refs.GenericRef;
import io.swagger.models.refs.RefFormat;
import io.swagger.models.refs.RefType;

/* loaded from: swagger-models-1.5.3.jar:io/swagger/models/RefResponse.class */
public class RefResponse extends Response {
    private GenericRef genericRef;

    public RefResponse() {
    }

    public RefResponse(String ref) {
        set$ref(ref);
    }

    public void set$ref(String ref) {
        this.genericRef = new GenericRef(RefType.RESPONSE, ref);
    }

    public String get$ref() {
        return this.genericRef.getRef();
    }

    @Override // io.swagger.models.Response
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RefResponse refResponse = (RefResponse) o;
        return this.genericRef == null ? refResponse.genericRef == null : this.genericRef.equals(refResponse.genericRef);
    }

    @Override // io.swagger.models.Response
    public int hashCode() {
        if (this.genericRef != null) {
            return this.genericRef.hashCode();
        }
        return 0;
    }

    @JsonIgnore
    public RefFormat getRefFormat() {
        return this.genericRef.getFormat();
    }
}
