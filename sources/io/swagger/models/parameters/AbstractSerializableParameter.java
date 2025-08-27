package io.swagger.models.parameters;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.models.parameters.AbstractSerializableParameter;
import io.swagger.models.properties.ArrayProperty;
import io.swagger.models.properties.Property;
import io.swagger.models.properties.StringProperty;
import java.util.List;
import org.springframework.validation.DefaultBindingErrorProcessor;

@JsonPropertyOrder({"name", "in", "description", DefaultBindingErrorProcessor.MISSING_FIELD_ERROR_CODE, "type", "items", "collectionFormat", "default", "maximum", "exclusiveMaximum", "minimum", "exclusiveMinimum"})
/* loaded from: swagger-models-1.5.3.jar:io/swagger/models/parameters/AbstractSerializableParameter.class */
public abstract class AbstractSerializableParameter<T extends AbstractSerializableParameter<T>> extends AbstractParameter implements SerializableParameter {
    protected String type;
    protected String format;
    protected String collectionFormat;
    protected Property items;
    protected List<String> _enum;
    protected Boolean exclusiveMaximum;
    protected Double maximum;
    protected Boolean exclusiveMinimum;
    protected Double minimum;

    @JsonProperty("default")
    protected String defaultValue;

    public T property(Property property) {
        setProperty(property);
        return (T) castThis();
    }

    public T type(String str) {
        setType(str);
        return (T) castThis();
    }

    public T format(String str) {
        setFormat(str);
        return (T) castThis();
    }

    public T description(String str) {
        setDescription(str);
        return (T) castThis();
    }

    public T name(String str) {
        setName(str);
        return (T) castThis();
    }

    public T required(boolean z) {
        setRequired(z);
        return (T) castThis();
    }

    public T collectionFormat(String str) {
        setCollectionFormat(str);
        return (T) castThis();
    }

    @JsonIgnore
    protected String getDefaultCollectionFormat() {
        return "csv";
    }

    public T items(Property property) {
        this.items = property;
        return (T) castThis();
    }

    public T _enum(List<String> list) {
        this._enum = list;
        return (T) castThis();
    }

    @Override // io.swagger.models.parameters.SerializableParameter
    public List<String> getEnum() {
        return this._enum;
    }

    @Override // io.swagger.models.parameters.SerializableParameter
    public void setEnum(List<String> _enum) {
        this._enum = _enum;
    }

    @Override // io.swagger.models.parameters.SerializableParameter
    public Property getItems() {
        return this.items;
    }

    @Override // io.swagger.models.parameters.SerializableParameter
    public void setItems(Property items) {
        this.items = items;
    }

    @Override // io.swagger.models.parameters.SerializableParameter
    public String getFormat() {
        return this.format;
    }

    @Override // io.swagger.models.parameters.SerializableParameter
    public void setFormat(String format) {
        this.format = format;
    }

    @Override // io.swagger.models.parameters.SerializableParameter
    public String getType() {
        return this.type;
    }

    @Override // io.swagger.models.parameters.SerializableParameter
    public void setType(String type) {
        this.type = type;
        setCollectionFormat(ArrayProperty.isType(type) ? getDefaultCollectionFormat() : null);
    }

    @Override // io.swagger.models.parameters.SerializableParameter
    public String getCollectionFormat() {
        return this.collectionFormat;
    }

    @Override // io.swagger.models.parameters.SerializableParameter
    public void setCollectionFormat(String collectionFormat) {
        this.collectionFormat = collectionFormat;
    }

    public void setProperty(Property property) {
        setType(property.getType());
        this.format = property.getFormat();
        if (property instanceof StringProperty) {
            StringProperty string = (StringProperty) property;
            setEnum(string.getEnum());
        } else if (property instanceof ArrayProperty) {
            ArrayProperty array = (ArrayProperty) property;
            setItems(array.getItems());
        }
    }

    public String getDefaultValue() {
        return this.defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public Boolean isExclusiveMaximum() {
        return this.exclusiveMaximum;
    }

    public void setExclusiveMaximum(Boolean exclusiveMaximum) {
        this.exclusiveMaximum = exclusiveMaximum;
    }

    public Double getMaximum() {
        return this.maximum;
    }

    public void setMaximum(Double maximum) {
        this.maximum = maximum;
    }

    public Boolean isExclusiveMinimum() {
        return this.exclusiveMinimum;
    }

    public void setExclusiveMinimum(Boolean exclusiveMinimum) {
        this.exclusiveMinimum = exclusiveMinimum;
    }

    public Double getMinimum() {
        return this.minimum;
    }

    public void setMinimum(Double minimum) {
        this.minimum = minimum;
    }

    @JsonIgnore
    private T castThis() {
        return this;
    }

    @Override // io.swagger.models.parameters.AbstractParameter
    public int hashCode() {
        int result = super.hashCode();
        return (31 * ((31 * ((31 * ((31 * ((31 * ((31 * ((31 * ((31 * ((31 * ((31 * result) + (this._enum == null ? 0 : this._enum.hashCode()))) + (this.collectionFormat == null ? 0 : this.collectionFormat.hashCode()))) + (this.defaultValue == null ? 0 : this.defaultValue.hashCode()))) + (this.exclusiveMaximum == null ? 0 : this.exclusiveMaximum.hashCode()))) + (this.exclusiveMinimum == null ? 0 : this.exclusiveMinimum.hashCode()))) + (this.format == null ? 0 : this.format.hashCode()))) + (this.items == null ? 0 : this.items.hashCode()))) + (this.maximum == null ? 0 : this.maximum.hashCode()))) + (this.minimum == null ? 0 : this.minimum.hashCode()))) + (this.type == null ? 0 : this.type.hashCode());
    }

    @Override // io.swagger.models.parameters.AbstractParameter
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj) || getClass() != obj.getClass()) {
            return false;
        }
        AbstractSerializableParameter<?> other = (AbstractSerializableParameter) obj;
        if (this._enum == null) {
            if (other._enum != null) {
                return false;
            }
        } else if (!this._enum.equals(other._enum)) {
            return false;
        }
        if (this.collectionFormat == null) {
            if (other.collectionFormat != null) {
                return false;
            }
        } else if (!this.collectionFormat.equals(other.collectionFormat)) {
            return false;
        }
        if (this.defaultValue == null) {
            if (other.defaultValue != null) {
                return false;
            }
        } else if (!this.defaultValue.equals(other.defaultValue)) {
            return false;
        }
        if (this.exclusiveMaximum == null) {
            if (other.exclusiveMaximum != null) {
                return false;
            }
        } else if (!this.exclusiveMaximum.equals(other.exclusiveMaximum)) {
            return false;
        }
        if (this.exclusiveMinimum == null) {
            if (other.exclusiveMinimum != null) {
                return false;
            }
        } else if (!this.exclusiveMinimum.equals(other.exclusiveMinimum)) {
            return false;
        }
        if (this.format == null) {
            if (other.format != null) {
                return false;
            }
        } else if (!this.format.equals(other.format)) {
            return false;
        }
        if (this.items == null) {
            if (other.items != null) {
                return false;
            }
        } else if (!this.items.equals(other.items)) {
            return false;
        }
        if (this.maximum == null) {
            if (other.maximum != null) {
                return false;
            }
        } else if (!this.maximum.equals(other.maximum)) {
            return false;
        }
        if (this.minimum == null) {
            if (other.minimum != null) {
                return false;
            }
        } else if (!this.minimum.equals(other.minimum)) {
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
