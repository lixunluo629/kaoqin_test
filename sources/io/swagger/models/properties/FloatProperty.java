package io.swagger.models.properties;

import io.swagger.models.Xml;

/* loaded from: swagger-models-1.5.3.jar:io/swagger/models/properties/FloatProperty.class */
public class FloatProperty extends DecimalProperty {
    private static final String FORMAT = "float";
    protected Float _default;

    public FloatProperty() {
        super("float");
    }

    public static boolean isType(String type, String format) {
        return DecimalProperty.TYPE.equals(type) && "float".equals(format);
    }

    @Override // io.swagger.models.properties.DecimalProperty
    public FloatProperty xml(Xml xml) {
        setXml(xml);
        return this;
    }

    public FloatProperty example(Float example) {
        setExample(String.valueOf(example));
        return this;
    }

    public FloatProperty _default(String _default) {
        if (_default != null) {
            try {
                this._default = Float.valueOf(Float.parseFloat(_default));
            } catch (NumberFormatException e) {
            }
        }
        return this;
    }

    public FloatProperty _default(Float _default) {
        setDefault(_default);
        return this;
    }

    public Float getDefault() {
        return this._default;
    }

    @Override // io.swagger.models.properties.AbstractProperty, io.swagger.models.properties.Property
    public void setDefault(String _default) {
        _default(_default);
    }

    public void setDefault(Float _default) {
        this._default = _default;
    }

    @Override // io.swagger.models.properties.AbstractNumericProperty, io.swagger.models.properties.AbstractProperty
    public int hashCode() {
        int result = super.hashCode();
        return (31 * result) + (this._default == null ? 0 : this._default.hashCode());
    }

    @Override // io.swagger.models.properties.AbstractNumericProperty, io.swagger.models.properties.AbstractProperty
    public boolean equals(Object obj) {
        if (!super.equals(obj) || !(obj instanceof FloatProperty)) {
            return false;
        }
        FloatProperty other = (FloatProperty) obj;
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
