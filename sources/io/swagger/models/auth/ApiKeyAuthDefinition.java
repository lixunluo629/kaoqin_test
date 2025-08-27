package io.swagger.models.auth;

/* loaded from: swagger-models-1.5.3.jar:io/swagger/models/auth/ApiKeyAuthDefinition.class */
public class ApiKeyAuthDefinition extends AbstractSecuritySchemeDefinition {
    private String type = "apiKey";
    private String name;
    private In in;

    public ApiKeyAuthDefinition() {
    }

    public ApiKeyAuthDefinition(String name, In in) {
        setName(name);
        setIn(in);
    }

    public ApiKeyAuthDefinition name(String name) {
        setName(name);
        return this;
    }

    public ApiKeyAuthDefinition in(In in) {
        setIn(in);
        return this;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public In getIn() {
        return this.in;
    }

    public void setIn(In in) {
        this.in = in;
    }

    @Override // io.swagger.models.auth.SecuritySchemeDefinition
    public String getType() {
        return this.type;
    }

    @Override // io.swagger.models.auth.SecuritySchemeDefinition
    public void setType(String type) {
        this.type = type;
    }

    @Override // io.swagger.models.auth.AbstractSecuritySchemeDefinition
    public int hashCode() {
        int result = super.hashCode();
        return (31 * ((31 * ((31 * result) + (this.in == null ? 0 : this.in.hashCode()))) + (this.name == null ? 0 : this.name.hashCode()))) + (this.type == null ? 0 : this.type.hashCode());
    }

    @Override // io.swagger.models.auth.AbstractSecuritySchemeDefinition
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || !super.equals(obj) || getClass() != obj.getClass()) {
            return false;
        }
        ApiKeyAuthDefinition other = (ApiKeyAuthDefinition) obj;
        if (this.in != other.in) {
            return false;
        }
        if (this.name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!this.name.equals(other.name)) {
            return false;
        }
        if (this.type == null) {
            if (other.type != null) {
                return false;
            }
            return true;
        }
        if (!this.type.equals(other.type)) {
            return false;
        }
        return true;
    }
}
