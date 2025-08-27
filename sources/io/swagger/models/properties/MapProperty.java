package io.swagger.models.properties;

import io.swagger.models.Xml;

/* loaded from: swagger-models-1.5.3.jar:io/swagger/models/properties/MapProperty.class */
public class MapProperty extends AbstractProperty implements Property {
    Property property;

    public MapProperty() {
        this.type = "object";
    }

    public MapProperty(Property property) {
        this.type = "object";
        this.property = property;
    }

    public static boolean isType(String type, String format) {
        if ("object".equals(type) && format == null) {
            return true;
        }
        return false;
    }

    public MapProperty xml(Xml xml) {
        setXml(xml);
        return this;
    }

    public MapProperty additionalProperties(Property property) {
        setAdditionalProperties(property);
        return this;
    }

    @Override // io.swagger.models.properties.AbstractProperty, io.swagger.models.properties.Property
    public MapProperty description(String description) {
        setDescription(description);
        return this;
    }

    public Property getAdditionalProperties() {
        return this.property;
    }

    public void setAdditionalProperties(Property property) {
        this.property = property;
    }

    @Override // io.swagger.models.properties.AbstractProperty
    public int hashCode() {
        int result = super.hashCode();
        return (31 * result) + (this.property == null ? 0 : this.property.hashCode());
    }

    @Override // io.swagger.models.properties.AbstractProperty
    public boolean equals(Object obj) {
        if (!super.equals(obj) || !(obj instanceof MapProperty)) {
            return false;
        }
        MapProperty other = (MapProperty) obj;
        if (this.property == null) {
            if (other.property != null) {
                return false;
            }
            return true;
        }
        if (!this.property.equals(other.property)) {
            return false;
        }
        return true;
    }
}
