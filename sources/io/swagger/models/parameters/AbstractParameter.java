package io.swagger.models.parameters;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.mysql.jdbc.MysqlErrorNumbers;
import java.util.HashMap;
import java.util.Map;

/* loaded from: swagger-models-1.5.3.jar:io/swagger/models/parameters/AbstractParameter.class */
public abstract class AbstractParameter {
    protected String in;
    protected String name;
    protected String description;
    protected String access;
    protected String pattern;
    private final Map<String, Object> vendorExtensions = new HashMap();
    protected boolean required = false;

    public String getIn() {
        return this.in;
    }

    public void setIn(String in) {
        this.in = in;
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

    public boolean getRequired() {
        return this.required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public String getAccess() {
        return this.access;
    }

    public void setAccess(String access) {
        this.access = access;
    }

    public String getPattern() {
        return this.pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    @JsonAnyGetter
    public Map<String, Object> getVendorExtensions() {
        return this.vendorExtensions;
    }

    @JsonAnySetter
    public void setVendorExtension(String name, Object value) {
        if (name.startsWith("x-")) {
            this.vendorExtensions.put(name, value);
        }
    }

    public int hashCode() {
        int result = (31 * 1) + (this.access == null ? 0 : this.access.hashCode());
        return (31 * ((31 * ((31 * ((31 * ((31 * ((31 * result) + (this.description == null ? 0 : this.description.hashCode()))) + (this.in == null ? 0 : this.in.hashCode()))) + (this.name == null ? 0 : this.name.hashCode()))) + (this.pattern == null ? 0 : this.pattern.hashCode()))) + (this.required ? MysqlErrorNumbers.ER_WRONG_VALUE_FOR_VAR : MysqlErrorNumbers.ER_SLAVE_IGNORED_TABLE))) + (this.vendorExtensions == null ? 0 : this.vendorExtensions.hashCode());
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        AbstractParameter other = (AbstractParameter) obj;
        if (this.access == null) {
            if (other.access != null) {
                return false;
            }
        } else if (!this.access.equals(other.access)) {
            return false;
        }
        if (this.description == null) {
            if (other.description != null) {
                return false;
            }
        } else if (!this.description.equals(other.description)) {
            return false;
        }
        if (this.in == null) {
            if (other.in != null) {
                return false;
            }
        } else if (!this.in.equals(other.in)) {
            return false;
        }
        if (this.name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!this.name.equals(other.name)) {
            return false;
        }
        if (this.pattern == null) {
            if (other.pattern != null) {
                return false;
            }
        } else if (!this.pattern.equals(other.pattern)) {
            return false;
        }
        if (this.required != other.required) {
            return false;
        }
        if (this.vendorExtensions == null) {
            if (other.vendorExtensions != null) {
                return false;
            }
            return true;
        }
        if (!this.vendorExtensions.equals(other.vendorExtensions)) {
            return false;
        }
        return true;
    }
}
