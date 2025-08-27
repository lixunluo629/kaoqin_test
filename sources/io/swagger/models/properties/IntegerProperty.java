package io.swagger.models.properties;

import io.swagger.models.Xml;

/* loaded from: swagger-models-1.5.3.jar:io/swagger/models/properties/IntegerProperty.class */
public class IntegerProperty extends BaseIntegerProperty {
    private static final String FORMAT = "int32";
    protected Integer _default;

    public IntegerProperty() {
        super(FORMAT);
    }

    public static boolean isType(String type, String format) {
        return "integer".equals(type) && FORMAT.equals(format);
    }

    public IntegerProperty xml(Xml xml) {
        setXml(xml);
        return this;
    }

    public IntegerProperty example(Integer example) {
        setExample(String.valueOf(example));
        return this;
    }

    public IntegerProperty _default(String _default) {
        if (_default != null) {
            try {
                this._default = Integer.valueOf(Integer.parseInt(_default));
            } catch (NumberFormatException e) {
            }
        }
        return this;
    }

    public IntegerProperty _default(Integer _default) {
        setDefault(_default);
        return this;
    }

    public Integer getDefault() {
        return this._default;
    }

    @Override // io.swagger.models.properties.AbstractProperty, io.swagger.models.properties.Property
    public void setDefault(String _default) {
        _default(_default);
    }

    public void setDefault(Integer _default) {
        this._default = _default;
    }

    @Override // io.swagger.models.properties.AbstractNumericProperty, io.swagger.models.properties.AbstractProperty
    public int hashCode() {
        int result = super.hashCode();
        return (31 * result) + (this._default == null ? 0 : this._default.hashCode());
    }

    @Override // io.swagger.models.properties.AbstractNumericProperty, io.swagger.models.properties.AbstractProperty
    public boolean equals(Object obj) {
        if (!super.equals(obj) || !(obj instanceof IntegerProperty)) {
            return false;
        }
        IntegerProperty other = (IntegerProperty) obj;
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
