package io.swagger.models.properties;

import io.swagger.models.Xml;

/* loaded from: swagger-models-1.5.3.jar:io/swagger/models/properties/ArrayProperty.class */
public class ArrayProperty extends AbstractProperty implements Property {
    public static final String TYPE = "array";
    protected Boolean uniqueItems;
    protected Property items;

    public ArrayProperty() {
        this.type = "array";
    }

    public ArrayProperty(Property items) {
        this.type = "array";
        setItems(items);
    }

    public static boolean isType(String type) {
        return "array".equals(type);
    }

    public ArrayProperty xml(Xml xml) {
        setXml(xml);
        return this;
    }

    public ArrayProperty uniqueItems() {
        setUniqueItems(true);
        return this;
    }

    @Override // io.swagger.models.properties.AbstractProperty, io.swagger.models.properties.Property
    public ArrayProperty description(String description) {
        setDescription(description);
        return this;
    }

    public ArrayProperty items(Property items) {
        setItems(items);
        return this;
    }

    public Property getItems() {
        return this.items;
    }

    public void setItems(Property items) {
        this.items = items;
    }

    public Boolean getUniqueItems() {
        return this.uniqueItems;
    }

    public void setUniqueItems(Boolean uniqueItems) {
        this.uniqueItems = uniqueItems.booleanValue() ? true : null;
    }

    @Override // io.swagger.models.properties.AbstractProperty
    public int hashCode() {
        int result = super.hashCode();
        return (31 * ((31 * result) + (this.items == null ? 0 : this.items.hashCode()))) + (this.uniqueItems == null ? 0 : this.uniqueItems.hashCode());
    }

    @Override // io.swagger.models.properties.AbstractProperty
    public boolean equals(Object obj) {
        if (!super.equals(obj) || !(obj instanceof ArrayProperty)) {
            return false;
        }
        ArrayProperty other = (ArrayProperty) obj;
        if (this.items == null) {
            if (other.items != null) {
                return false;
            }
        } else if (!this.items.equals(other.items)) {
            return false;
        }
        if (this.uniqueItems == null) {
            if (other.uniqueItems != null) {
                return false;
            }
            return true;
        }
        if (!this.uniqueItems.equals(other.uniqueItems)) {
            return false;
        }
        return true;
    }
}
