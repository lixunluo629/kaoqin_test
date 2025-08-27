package io.swagger.models.properties;

import io.swagger.models.Xml;

/* loaded from: swagger-models-1.5.3.jar:io/swagger/models/properties/DoubleProperty.class */
public class DoubleProperty extends DecimalProperty {
    private static final String FORMAT = "double";
    protected Double _default;

    public DoubleProperty() {
        super("double");
    }

    public static boolean isType(String type, String format) {
        return DecimalProperty.TYPE.equals(type) && "double".equals(format);
    }

    @Override // io.swagger.models.properties.DecimalProperty
    public DoubleProperty xml(Xml xml) {
        setXml(xml);
        return this;
    }

    public DoubleProperty example(Double example) {
        setExample(String.valueOf(example));
        return this;
    }

    public DoubleProperty _default(String _default) {
        if (_default != null) {
            try {
                this._default = Double.valueOf(Double.parseDouble(_default));
            } catch (NumberFormatException e) {
            }
        }
        return this;
    }

    public DoubleProperty _default(Double _default) {
        setDefault(_default);
        return this;
    }

    public Double getDefault() {
        return this._default;
    }

    @Override // io.swagger.models.properties.AbstractProperty, io.swagger.models.properties.Property
    public void setDefault(String _default) {
        _default(_default);
    }

    public void setDefault(Double _default) {
        this._default = _default;
    }

    @Override // io.swagger.models.properties.AbstractNumericProperty, io.swagger.models.properties.AbstractProperty
    public int hashCode() {
        int result = super.hashCode();
        return (31 * result) + (this._default == null ? 0 : this._default.hashCode());
    }

    @Override // io.swagger.models.properties.AbstractNumericProperty, io.swagger.models.properties.AbstractProperty
    public boolean equals(Object obj) {
        if (!super.equals(obj) || !(obj instanceof DoubleProperty)) {
            return false;
        }
        DoubleProperty other = (DoubleProperty) obj;
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
