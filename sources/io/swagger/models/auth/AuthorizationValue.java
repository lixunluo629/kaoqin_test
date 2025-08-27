package io.swagger.models.auth;

/* loaded from: swagger-models-1.5.3.jar:io/swagger/models/auth/AuthorizationValue.class */
public class AuthorizationValue {
    private String value;
    private String type;
    private String keyName;

    public AuthorizationValue() {
    }

    public AuthorizationValue(String keyName, String value, String type) {
        setKeyName(keyName);
        setValue(value);
        setType(type);
    }

    public AuthorizationValue value(String value) {
        this.value = value;
        return this;
    }

    public AuthorizationValue type(String type) {
        this.type = type;
        return this;
    }

    public AuthorizationValue keyName(String keyName) {
        this.keyName = keyName;
        return this;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getKeyName() {
        return this.keyName;
    }

    public void setKeyName(String keyName) {
        this.keyName = keyName;
    }

    public int hashCode() {
        int result = (31 * 1) + (this.keyName == null ? 0 : this.keyName.hashCode());
        return (31 * ((31 * result) + (this.type == null ? 0 : this.type.hashCode()))) + (this.value == null ? 0 : this.value.hashCode());
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        AuthorizationValue other = (AuthorizationValue) obj;
        if (this.keyName == null) {
            if (other.keyName != null) {
                return false;
            }
        } else if (!this.keyName.equals(other.keyName)) {
            return false;
        }
        if (this.type == null) {
            if (other.type != null) {
                return false;
            }
        } else if (!this.type.equals(other.type)) {
            return false;
        }
        if (this.value == null) {
            if (other.value != null) {
                return false;
            }
            return true;
        }
        if (!this.value.equals(other.value)) {
            return false;
        }
        return true;
    }
}
