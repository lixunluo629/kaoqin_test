package io.swagger.models.parameters;

import io.swagger.models.Model;

/* loaded from: swagger-models-1.5.3.jar:io/swagger/models/parameters/BodyParameter.class */
public class BodyParameter extends AbstractParameter implements Parameter {
    Model schema;

    public BodyParameter() {
        super.setIn("body");
    }

    public BodyParameter schema(Model schema) {
        setSchema(schema);
        return this;
    }

    public BodyParameter description(String description) {
        setDescription(description);
        return this;
    }

    public BodyParameter name(String name) {
        setName(name);
        return this;
    }

    public Model getSchema() {
        return this.schema;
    }

    public void setSchema(Model schema) {
        this.schema = schema;
    }

    @Override // io.swagger.models.parameters.AbstractParameter
    public int hashCode() {
        int result = super.hashCode();
        return (31 * result) + (this.schema == null ? 0 : this.schema.hashCode());
    }

    @Override // io.swagger.models.parameters.AbstractParameter
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj) || getClass() != obj.getClass()) {
            return false;
        }
        BodyParameter other = (BodyParameter) obj;
        if (this.schema == null) {
            if (other.schema != null) {
                return false;
            }
            return true;
        }
        if (!this.schema.equals(other.schema)) {
            return false;
        }
        return true;
    }
}
