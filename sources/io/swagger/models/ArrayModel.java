package io.swagger.models;

import io.swagger.models.properties.Property;
import java.util.Map;

/* loaded from: swagger-models-1.5.3.jar:io/swagger/models/ArrayModel.class */
public class ArrayModel extends AbstractModel {
    private Map<String, Property> properties;
    private String type = "array";
    private String description;
    private Property items;
    private Object example;

    public ArrayModel description(String description) {
        setDescription(description);
        return this;
    }

    public ArrayModel items(Property items) {
        setItems(items);
        return this;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override // io.swagger.models.Model
    public String getDescription() {
        return this.description;
    }

    @Override // io.swagger.models.Model
    public void setDescription(String description) {
        this.description = description;
    }

    public Property getItems() {
        return this.items;
    }

    public void setItems(Property items) {
        this.items = items;
    }

    @Override // io.swagger.models.Model
    public Map<String, Property> getProperties() {
        return this.properties;
    }

    @Override // io.swagger.models.Model
    public void setProperties(Map<String, Property> properties) {
        this.properties = properties;
    }

    @Override // io.swagger.models.Model
    public Object getExample() {
        return this.example;
    }

    @Override // io.swagger.models.Model
    public void setExample(Object example) {
        this.example = example;
    }

    @Override // io.swagger.models.AbstractModel, io.swagger.models.Model
    public Object clone() {
        ArrayModel cloned = new ArrayModel();
        super.cloneTo(cloned);
        cloned.properties = this.properties;
        cloned.type = this.type;
        cloned.description = this.description;
        cloned.items = this.items;
        cloned.example = this.example;
        return cloned;
    }

    @Override // io.swagger.models.AbstractModel
    public int hashCode() {
        int result = super.hashCode();
        return (31 * ((31 * ((31 * ((31 * ((31 * result) + (this.description == null ? 0 : this.description.hashCode()))) + (this.example == null ? 0 : this.example.hashCode()))) + (this.items == null ? 0 : this.items.hashCode()))) + (this.properties == null ? 0 : this.properties.hashCode()))) + (this.type == null ? 0 : this.type.hashCode());
    }

    @Override // io.swagger.models.AbstractModel
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj) || getClass() != obj.getClass()) {
            return false;
        }
        ArrayModel other = (ArrayModel) obj;
        if (this.description == null) {
            if (other.description != null) {
                return false;
            }
        } else if (!this.description.equals(other.description)) {
            return false;
        }
        if (this.example == null) {
            if (other.example != null) {
                return false;
            }
        } else if (!this.example.equals(other.example)) {
            return false;
        }
        if (this.items == null) {
            if (other.items != null) {
                return false;
            }
        } else if (!this.items.equals(other.items)) {
            return false;
        }
        if (this.properties == null) {
            if (other.properties != null) {
                return false;
            }
        } else if (!this.properties.equals(other.properties)) {
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
