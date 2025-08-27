package io.swagger.models;

/* loaded from: swagger-models-1.5.3.jar:io/swagger/models/SecurityScope.class */
public class SecurityScope {
    private String name;
    private String description;

    public SecurityScope() {
    }

    public SecurityScope(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public SecurityScope name(String name) {
        setName(name);
        return this;
    }

    public SecurityScope description(String description) {
        setDescription(description);
        return this;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int hashCode() {
        int result = (31 * 1) + (this.description == null ? 0 : this.description.hashCode());
        return (31 * result) + (this.name == null ? 0 : this.name.hashCode());
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        SecurityScope other = (SecurityScope) obj;
        if (this.description == null) {
            if (other.description != null) {
                return false;
            }
        } else if (!this.description.equals(other.description)) {
            return false;
        }
        if (this.name == null) {
            if (other.name != null) {
                return false;
            }
            return true;
        }
        if (!this.name.equals(other.name)) {
            return false;
        }
        return true;
    }
}
