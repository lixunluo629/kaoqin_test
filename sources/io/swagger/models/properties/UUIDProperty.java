package io.swagger.models.properties;

import io.swagger.models.Xml;
import java.util.List;

/* loaded from: swagger-models-1.5.3.jar:io/swagger/models/properties/UUIDProperty.class */
public class UUIDProperty extends AbstractProperty implements Property {
    protected List<String> _enum;
    protected Integer minLength = null;
    protected Integer maxLength = null;
    protected String pattern = null;
    protected String _default;

    public UUIDProperty() {
        this.type = StringProperty.TYPE;
        this.format = "uuid";
    }

    public static boolean isType(String type, String format) {
        if (StringProperty.TYPE.equals(type) && "uuid".equals(format)) {
            return true;
        }
        return false;
    }

    public UUIDProperty xml(Xml xml) {
        setXml(xml);
        return this;
    }

    public UUIDProperty minLength(Integer minLength) {
        setMinLength(minLength);
        return this;
    }

    public UUIDProperty maxLength(Integer maxLength) {
        setMaxLength(maxLength);
        return this;
    }

    public UUIDProperty pattern(String pattern) {
        setPattern(pattern);
        return this;
    }

    public UUIDProperty _default(String _default) {
        this._default = _default;
        return this;
    }

    public Integer getMinLength() {
        return this.minLength;
    }

    public void setMinLength(Integer minLength) {
        this.minLength = minLength;
    }

    public Integer getMaxLength() {
        return this.maxLength;
    }

    public void setMaxLength(Integer maxLength) {
        this.maxLength = maxLength;
    }

    public String getPattern() {
        return this.pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public String getDefault() {
        return this._default;
    }

    @Override // io.swagger.models.properties.AbstractProperty, io.swagger.models.properties.Property
    public void setDefault(String _default) {
        this._default = _default;
    }

    @Override // io.swagger.models.properties.AbstractProperty
    public int hashCode() {
        int result = super.hashCode();
        return (31 * ((31 * ((31 * ((31 * ((31 * result) + (this._default == null ? 0 : this._default.hashCode()))) + (this._enum == null ? 0 : this._enum.hashCode()))) + (this.maxLength == null ? 0 : this.maxLength.hashCode()))) + (this.minLength == null ? 0 : this.minLength.hashCode()))) + (this.pattern == null ? 0 : this.pattern.hashCode());
    }

    @Override // io.swagger.models.properties.AbstractProperty
    public boolean equals(Object obj) {
        if (!super.equals(obj) || !(obj instanceof UUIDProperty)) {
            return false;
        }
        UUIDProperty other = (UUIDProperty) obj;
        if (this._default == null) {
            if (other._default != null) {
                return false;
            }
        } else if (!this._default.equals(other._default)) {
            return false;
        }
        if (this._enum == null) {
            if (other._enum != null) {
                return false;
            }
        } else if (!this._enum.equals(other._enum)) {
            return false;
        }
        if (this.maxLength == null) {
            if (other.maxLength != null) {
                return false;
            }
        } else if (!this.maxLength.equals(other.maxLength)) {
            return false;
        }
        if (this.minLength == null) {
            if (other.minLength != null) {
                return false;
            }
        } else if (!this.minLength.equals(other.minLength)) {
            return false;
        }
        if (this.pattern == null) {
            if (other.pattern != null) {
                return false;
            }
            return true;
        }
        if (!this.pattern.equals(other.pattern)) {
            return false;
        }
        return true;
    }
}
