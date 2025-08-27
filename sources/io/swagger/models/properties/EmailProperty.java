package io.swagger.models.properties;

import io.swagger.models.Xml;
import java.util.List;

/* loaded from: swagger-models-1.5.3.jar:io/swagger/models/properties/EmailProperty.class */
public class EmailProperty extends StringProperty {
    @Override // io.swagger.models.properties.StringProperty
    public /* bridge */ /* synthetic */ StringProperty _enum(List x0) {
        return _enum((List<String>) x0);
    }

    public EmailProperty() {
        this.type = StringProperty.TYPE;
        this.format = "email";
    }

    public EmailProperty(StringProperty prop) {
        this();
        this._enum = prop.getEnum();
        this.minLength = prop.getMinLength();
        this.maxLength = prop.getMaxLength();
        this.pattern = prop.getPattern();
        this.name = prop.getName();
        this.type = prop.getType();
        this.example = prop.getExample();
        this._default = prop.getDefault();
        this.xml = prop.getXml();
        this.required = prop.getRequired();
        this.position = prop.getPosition();
        this.description = prop.getDescription();
        this.title = prop.getTitle();
        this.readOnly = prop.getReadOnly();
    }

    public static boolean isType(String type, String format) {
        if (StringProperty.TYPE.equals(type) && "email".equals(format)) {
            return true;
        }
        return false;
    }

    @Override // io.swagger.models.properties.StringProperty
    public EmailProperty xml(Xml xml) {
        super.xml(xml);
        return this;
    }

    @Override // io.swagger.models.properties.StringProperty
    public EmailProperty example(String example) {
        super.example(example);
        return this;
    }

    @Override // io.swagger.models.properties.StringProperty
    public EmailProperty minLength(Integer minLength) {
        super.minLength(minLength);
        return this;
    }

    @Override // io.swagger.models.properties.StringProperty
    public EmailProperty maxLength(Integer maxLength) {
        super.maxLength(maxLength);
        return this;
    }

    @Override // io.swagger.models.properties.StringProperty
    public EmailProperty pattern(String pattern) {
        super.pattern(pattern);
        return this;
    }

    @Override // io.swagger.models.properties.StringProperty
    public EmailProperty _enum(String value) {
        super._enum(value);
        return this;
    }

    @Override // io.swagger.models.properties.StringProperty
    public EmailProperty _enum(List<String> value) {
        super._enum(value);
        return this;
    }
}
