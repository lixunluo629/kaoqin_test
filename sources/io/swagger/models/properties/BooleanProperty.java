package io.swagger.models.properties;

import io.swagger.models.Xml;

/* loaded from: swagger-models-1.5.3.jar:io/swagger/models/properties/BooleanProperty.class */
public class BooleanProperty extends AbstractProperty implements Property {
    public static final String TYPE = "boolean";
    protected Boolean _default;

    public BooleanProperty() {
        this.type = "boolean";
    }

    public static boolean isType(String type, String format) {
        return "boolean".equals(type);
    }

    public BooleanProperty xml(Xml xml) {
        setXml(xml);
        return this;
    }

    public BooleanProperty example(Boolean example) {
        setExample(String.valueOf(example));
        return this;
    }

    public BooleanProperty _default(String _default) {
        try {
            setDefault(Boolean.valueOf(Boolean.parseBoolean(_default)));
        } catch (Exception e) {
        }
        return this;
    }

    public BooleanProperty _default(boolean _default) {
        setDefault(Boolean.valueOf(_default));
        return this;
    }

    public Boolean getDefault() {
        return this._default;
    }

    @Override // io.swagger.models.properties.AbstractProperty, io.swagger.models.properties.Property
    public void setDefault(String _default) {
        _default(_default);
    }

    public void setDefault(Boolean _default) {
        this._default = _default;
    }

    @Override // io.swagger.models.properties.AbstractProperty
    public int hashCode() {
        int result = super.hashCode();
        return (31 * result) + (this._default == null ? 0 : this._default.hashCode());
    }

    @Override // io.swagger.models.properties.AbstractProperty
    public boolean equals(Object obj) {
        if (!super.equals(obj) || !(obj instanceof BooleanProperty)) {
            return false;
        }
        BooleanProperty other = (BooleanProperty) obj;
        if (this._default == null) {
            if (other._default != null) {
                return false;
            }
            return true;
        }
        if (!this._default.equals(other._default)) {
            return false;
        }
        return true;
    }
}
