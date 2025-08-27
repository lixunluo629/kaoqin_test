package io.swagger.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.mysql.jdbc.MysqlErrorNumbers;
import io.swagger.models.properties.Property;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.xml.bind.annotation.XmlType;
import org.springframework.validation.DefaultBindingErrorProcessor;

@XmlType(propOrder = {"type", DefaultBindingErrorProcessor.MISSING_FIELD_ERROR_CODE, "discriminator", "properties"})
@JsonPropertyOrder({"type", DefaultBindingErrorProcessor.MISSING_FIELD_ERROR_CODE, "discriminator", "properties"})
/* loaded from: swagger-models-1.5.3.jar:io/swagger/models/ModelImpl.class */
public class ModelImpl extends AbstractModel {
    public static final String OBJECT = "object";
    private String type;
    private String format;
    private String name;
    private List<String> required;
    private Map<String, Property> properties;
    private boolean isSimple = false;
    private String description;
    private Object example;
    private Property additionalProperties;
    private String discriminator;
    private Xml xml;

    @JsonProperty("default")
    private String defaultValue;

    public ModelImpl discriminator(String discriminator) {
        setDiscriminator(discriminator);
        return this;
    }

    public ModelImpl type(String type) {
        setType(type);
        return this;
    }

    public ModelImpl format(String format) {
        setFormat(format);
        return this;
    }

    public ModelImpl name(String name) {
        setName(name);
        return this;
    }

    public ModelImpl description(String description) {
        setDescription(description);
        return this;
    }

    public ModelImpl property(String key, Property property) {
        addProperty(key, property);
        return this;
    }

    public ModelImpl example(Object example) {
        setExample(example);
        return this;
    }

    public ModelImpl additionalProperties(Property additionalProperties) {
        setAdditionalProperties(additionalProperties);
        return this;
    }

    public ModelImpl required(String name) {
        addRequired(name);
        return this;
    }

    public ModelImpl xml(Xml xml) {
        setXml(xml);
        return this;
    }

    public String getDiscriminator() {
        return this.discriminator;
    }

    public void setDiscriminator(String discriminator) {
        this.discriminator = discriminator;
    }

    @JsonIgnore
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override // io.swagger.models.Model
    public String getDescription() {
        return this.description;
    }

    @Override // io.swagger.models.Model
    public void setDescription(String description) {
        this.description = description;
    }

    @JsonIgnore
    public boolean isSimple() {
        return this.isSimple;
    }

    public void setSimple(boolean isSimple) {
        this.isSimple = isSimple;
    }

    public Property getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperties(Property additionalProperties) {
        type("object");
        this.additionalProperties = additionalProperties;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFormat() {
        return this.format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public void addRequired(String name) {
        Property p = this.properties.get(name);
        if (p != null) {
            p.setRequired(true);
        }
    }

    public List<String> getRequired() {
        List<String> output = new ArrayList<>();
        if (this.properties != null) {
            for (String key : this.properties.keySet()) {
                Property prop = this.properties.get(key);
                if (prop != null && prop.getRequired()) {
                    output.add(key);
                }
            }
        }
        Collections.sort(output);
        if (output.size() > 0) {
            return output;
        }
        return null;
    }

    public void setRequired(List<String> required) {
        Property p;
        this.required = required;
        for (String s : required) {
            if (this.properties != null && (p = this.properties.get(s)) != null) {
                p.setRequired(true);
            }
        }
    }

    public void addProperty(String key, Property property) {
        if (property == null) {
            return;
        }
        if (this.properties == null) {
            this.properties = new LinkedHashMap();
        }
        if (this.required != null) {
            for (String ek : this.required) {
                if (key.equals(ek)) {
                    property.setRequired(true);
                }
            }
        }
        this.properties.put(key, property);
    }

    @Override // io.swagger.models.Model
    public Map<String, Property> getProperties() {
        return this.properties;
    }

    @Override // io.swagger.models.Model
    public void setProperties(Map<String, Property> properties) {
        if (properties != null) {
            for (String key : properties.keySet()) {
                addProperty(key, properties.get(key));
            }
        }
    }

    @Override // io.swagger.models.Model
    public Object getExample() {
        if (this.example == null) {
        }
        return this.example;
    }

    @Override // io.swagger.models.Model
    public void setExample(Object example) {
        this.example = example;
    }

    public Xml getXml() {
        return this.xml;
    }

    public void setXml(Xml xml) {
        this.xml = xml;
    }

    public String getDefaultValue() {
        return this.defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    @Override // io.swagger.models.AbstractModel, io.swagger.models.Model
    public Object clone() {
        ModelImpl cloned = new ModelImpl();
        super.cloneTo(cloned);
        cloned.type = this.type;
        cloned.name = this.name;
        cloned.required = this.required;
        cloned.properties = this.properties;
        cloned.isSimple = this.isSimple;
        cloned.description = this.description;
        cloned.example = this.example;
        cloned.additionalProperties = this.additionalProperties;
        cloned.discriminator = this.discriminator;
        cloned.xml = this.xml;
        cloned.defaultValue = this.defaultValue;
        return cloned;
    }

    @Override // io.swagger.models.AbstractModel
    public int hashCode() {
        int result = super.hashCode();
        return (31 * ((31 * ((31 * ((31 * ((31 * ((31 * ((31 * ((31 * ((31 * ((31 * ((31 * ((31 * result) + (this.additionalProperties == null ? 0 : this.additionalProperties.hashCode()))) + (this.description == null ? 0 : this.description.hashCode()))) + (this.discriminator == null ? 0 : this.discriminator.hashCode()))) + (this.example == null ? 0 : this.example.hashCode()))) + (this.format == null ? 0 : this.format.hashCode()))) + (this.isSimple ? MysqlErrorNumbers.ER_WRONG_VALUE_FOR_VAR : MysqlErrorNumbers.ER_SLAVE_IGNORED_TABLE))) + (this.name == null ? 0 : this.name.hashCode()))) + (this.properties == null ? 0 : this.properties.hashCode()))) + (this.required == null ? 0 : this.required.hashCode()))) + (this.type == null ? 0 : this.type.hashCode()))) + (this.xml == null ? 0 : this.xml.hashCode()))) + (this.defaultValue == null ? 0 : this.defaultValue.hashCode());
    }

    @Override // io.swagger.models.AbstractModel
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj) || getClass() != obj.getClass()) {
            return false;
        }
        ModelImpl other = (ModelImpl) obj;
        if (this.additionalProperties == null) {
            if (other.additionalProperties != null) {
                return false;
            }
        } else if (!this.additionalProperties.equals(other.additionalProperties)) {
            return false;
        }
        if (this.description == null) {
            if (other.description != null) {
                return false;
            }
        } else if (!this.description.equals(other.description)) {
            return false;
        }
        if (this.discriminator == null) {
            if (other.discriminator != null) {
                return false;
            }
        } else if (!this.discriminator.equals(other.discriminator)) {
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
        if (this.isSimple != other.isSimple) {
            return false;
        }
        if (this.name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!this.name.equals(other.name)) {
            return false;
        }
        if (this.properties == null) {
            if (other.properties != null) {
                return false;
            }
        } else if (!this.properties.equals(other.properties)) {
            return false;
        }
        if (this.required == null) {
            if (other.required != null) {
                return false;
            }
        } else if (!this.required.equals(other.required)) {
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
        if (this.defaultValue == null) {
            if (other.defaultValue != null) {
                return false;
            }
            return true;
        }
        if (!this.defaultValue.equals(other.defaultValue)) {
            return false;
        }
        return true;
    }
}
