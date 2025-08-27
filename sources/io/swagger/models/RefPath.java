package io.swagger.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.models.refs.GenericRef;
import io.swagger.models.refs.RefFormat;
import io.swagger.models.refs.RefType;

/* loaded from: swagger-models-1.5.3.jar:io/swagger/models/RefPath.class */
public class RefPath extends Path {
    private GenericRef genericRef;

    public RefPath() {
    }

    public RefPath(String ref) {
        set$ref(ref);
    }

    public void set$ref(String ref) {
        this.genericRef = new GenericRef(RefType.PATH, ref);
    }

    public String get$ref() {
        return this.genericRef.getRef();
    }

    @Override // io.swagger.models.Path
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RefPath refPath = (RefPath) o;
        return this.genericRef == null ? refPath.genericRef == null : this.genericRef.equals(refPath.genericRef);
    }

    @Override // io.swagger.models.Path
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
