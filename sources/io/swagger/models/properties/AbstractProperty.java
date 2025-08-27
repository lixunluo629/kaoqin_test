package io.swagger.models.properties;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.mysql.jdbc.MysqlErrorNumbers;
import io.swagger.models.Xml;
import java.util.HashMap;
import java.util.Map;

/* loaded from: swagger-models-1.5.3.jar:io/swagger/models/properties/AbstractProperty.class */
public abstract class AbstractProperty implements Property {
    String name;
    String type;
    String format;
    String example;
    Xml xml;
    boolean required;
    Integer position;
    String description;
    String title;
    Boolean readOnly;
    private String access;
    private final Map<String, Object> vendorExtensions = new HashMap();

    @Override // io.swagger.models.properties.Property
    public Property title(String title) {
        setTitle(title);
        return this;
    }

    @Override // io.swagger.models.properties.Property
    public Property description(String description) {
        setDescription(description);
        return this;
    }

    public Property readOnly() {
        setReadOnly(Boolean.TRUE);
        return this;
    }

    @Override // io.swagger.models.properties.Property
    public String getName() {
        return this.name;
    }

    @Override // io.swagger.models.properties.Property
    public void setName(String name) {
        this.name = name;
    }

    @Override // io.swagger.models.properties.Property
    public String getExample() {
        return this.example;
    }

    @Override // io.swagger.models.properties.Property
    public void setExample(String example) {
        this.example = example;
    }

    @Override // io.swagger.models.properties.Property
    public Integer getPosition() {
        return this.position;
    }

    @Override // io.swagger.models.properties.Property
    public void setPosition(Integer position) {
        this.position = position;
    }

    @Override // io.swagger.models.properties.Property
    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override // io.swagger.models.properties.Property
    public String getFormat() {
        return this.format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    @Override // io.swagger.models.properties.Property
    public Xml getXml() {
        return this.xml;
    }

    @Override // io.swagger.models.properties.Property
    public void setXml(Xml xml) {
        this.xml = xml;
    }

    @Override // io.swagger.models.properties.Property
    public boolean getRequired() {
        return this.required;
    }

    @Override // io.swagger.models.properties.Property
    public void setRequired(boolean required) {
        this.required = required;
    }

    @Override // io.swagger.models.properties.Property
    public String getTitle() {
        return this.title;
    }

    @Override // io.swagger.models.properties.Property
    public void setTitle(String title) {
        this.title = title;
    }

    @Override // io.swagger.models.properties.Property
    public String getDescription() {
        return this.description;
    }

    @Override // io.swagger.models.properties.Property
    public void setDescription(String description) {
        this.description = description;
    }

    @Override // io.swagger.models.properties.Property
    public Boolean getReadOnly() {
        return this.readOnly;
    }

    @Override // io.swagger.models.properties.Property
    public void setReadOnly(Boolean readOnly) {
        if (Boolean.FALSE.equals(readOnly)) {
            this.readOnly = null;
        } else {
            this.readOnly = readOnly;
        }
    }

    @Override // io.swagger.models.properties.Property
    public void setDefault(String _default) {
    }

    @Override // io.swagger.models.properties.Property
    public String getAccess() {
        return this.access;
    }

    @Override // io.swagger.models.properties.Property
    public void setAccess(String access) {
        this.access = access;
    }

    @Override // io.swagger.models.properties.Property
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

    public void setVendorExtensionMap(Map<String, Object> vendorExtensionMap) {
        this.vendorExtensions.putAll(vendorExtensionMap);
    }

    public int hashCode() {
        int result = (31 * 1) + (this.access == null ? 0 : this.access.hashCode());
        return (31 * ((31 * ((31 * ((31 * ((31 * ((31 * ((31 * ((31 * ((31 * ((31 * ((31 * result) + (this.description == null ? 0 : this.description.hashCode()))) + (this.example == null ? 0 : this.example.hashCode()))) + (this.format == null ? 0 : this.format.hashCode()))) + (this.name == null ? 0 : this.name.hashCode()))) + (this.position == null ? 0 : this.position.hashCode()))) + (this.readOnly == null ? 0 : this.readOnly.hashCode()))) + (this.required ? MysqlErrorNumbers.ER_WRONG_VALUE_FOR_VAR : MysqlErrorNumbers.ER_SLAVE_IGNORED_TABLE))) + (this.title == null ? 0 : this.title.hashCode()))) + (this.type == null ? 0 : this.type.hashCode()))) + (this.xml == null ? 0 : this.xml.hashCode()))) + (this.vendorExtensions == null ? 0 : this.vendorExtensions.hashCode());
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        AbstractProperty other = (AbstractProperty) obj;
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
        if (this.example == null) {
            if (other.example != null) {
                return false;
            }
        } else if (!this.example.equals(other.example)) {
            return false;
        }
        if (this.format == null) {
            if (other.format != null) {
                return false;
            }
        } else if (!this.format.equals(other.format)) {
            return false;
        }
        if (this.name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!this.name.equals(other.name)) {
            return false;
        }
        if (this.position == null) {
            if (other.position != null) {
                return false;
            }
        } else if (!this.position.equals(other.position)) {
            return false;
        }
        if (this.readOnly == null) {
            if (other.readOnly != null) {
                return false;
            }
        } else if (!this.readOnly.equals(other.readOnly)) {
            return false;
        }
        if (this.required != other.required) {
            return false;
        }
        if (this.title == null) {
            if (other.title != null) {
                return false;
            }
        } else if (!this.title.equals(other.title)) {
            return false;
        }
        if (this.type == null) {
            if (other.type != null) {
                return false;
            }
        } else if (!this.type.equals(other.type)) {
            return false;
        }
        if (this.xml == null) {
            if (other.xml != null) {
                return false;
            }
        } else if (!this.xml.equals(other.xml)) {
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
