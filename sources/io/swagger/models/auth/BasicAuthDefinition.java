package io.swagger.models.auth;

/* loaded from: swagger-models-1.5.3.jar:io/swagger/models/auth/BasicAuthDefinition.class */
public class BasicAuthDefinition extends AbstractSecuritySchemeDefinition {
    private String type = "basic";

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
        return (31 * result) + (this.type == null ? 0 : this.type.hashCode());
    }

    @Override // io.swagger.models.auth.AbstractSecuritySchemeDefinition
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || !super.equals(obj) || getClass() != obj.getClass()) {
            return false;
        }
        BasicAuthDefinition other = (BasicAuthDefinition) obj;
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
