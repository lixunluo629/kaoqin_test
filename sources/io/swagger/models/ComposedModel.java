package io.swagger.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.models.properties.Property;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/* loaded from: swagger-models-1.5.3.jar:io/swagger/models/ComposedModel.class */
public class ComposedModel extends AbstractModel {
    private List<Model> allOf = new ArrayList();
    private Model parent;
    private Model child;
    private List<RefModel> interfaces;
    private String description;
    private Object example;

    public ComposedModel parent(Model model) {
        setParent(model);
        return this;
    }

    public ComposedModel child(Model model) {
        setChild(model);
        return this;
    }

    public ComposedModel interfaces(List<RefModel> interfaces) {
        setInterfaces(interfaces);
        return this;
    }

    @Override // io.swagger.models.Model
    public String getDescription() {
        return this.description;
    }

    @Override // io.swagger.models.Model
    public void setDescription(String description) {
        this.description = description;
    }

    @Override // io.swagger.models.Model
    public Map<String, Property> getProperties() {
        return null;
    }

    @Override // io.swagger.models.Model
    public void setProperties(Map<String, Property> properties) {
    }

    @Override // io.swagger.models.Model
    public Object getExample() {
        return this.example;
    }

    @Override // io.swagger.models.Model
    public void setExample(Object example) {
        this.example = example;
    }

    public List<Model> getAllOf() {
        return this.allOf;
    }

    public void setAllOf(List<Model> allOf) {
        this.allOf = allOf;
    }

    public Model getParent() {
        return this.parent;
    }

    @JsonIgnore
    public void setParent(Model model) {
        this.parent = model;
        if (!this.allOf.contains(model)) {
            this.allOf.add(model);
        }
    }

    public Model getChild() {
        return this.child;
    }

    @JsonIgnore
    public void setChild(Model model) {
        this.child = model;
        if (!this.allOf.contains(model)) {
            this.allOf.add(model);
        }
    }

    public List<RefModel> getInterfaces() {
        return this.interfaces;
    }

    @JsonIgnore
    public void setInterfaces(List<RefModel> interfaces) {
        this.interfaces = interfaces;
        for (RefModel model : interfaces) {
            if (!this.allOf.contains(model)) {
                this.allOf.add(model);
            }
        }
    }

    @Override // io.swagger.models.AbstractModel
    public int hashCode() {
        int result = super.hashCode();
        return (31 * ((31 * ((31 * ((31 * ((31 * ((31 * result) + (this.allOf == null ? 0 : this.allOf.hashCode()))) + (this.child == null ? 0 : this.child.hashCode()))) + (this.description == null ? 0 : this.description.hashCode()))) + (this.example == null ? 0 : this.example.hashCode()))) + (this.interfaces == null ? 0 : this.interfaces.hashCode()))) + (this.parent == null ? 0 : this.parent.hashCode());
    }

    @Override // io.swagger.models.AbstractModel
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj) || getClass() != obj.getClass()) {
            return false;
        }
        ComposedModel other = (ComposedModel) obj;
        if (this.allOf == null) {
            if (other.allOf != null) {
                return false;
            }
        } else if (!this.allOf.equals(other.allOf)) {
            return false;
        }
        if (this.child == null) {
            if (other.child != null) {
                return false;
            }
        } else if (!this.child.equals(other.child)) {
            return false;
        }
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
        if (this.interfaces == null) {
            if (other.interfaces != null) {
                return false;
            }
        } else if (!this.interfaces.equals(other.interfaces)) {
            return false;
        }
        if (this.parent == null) {
            if (other.parent != null) {
                return false;
            }
            return true;
        }
        if (!this.parent.equals(other.parent)) {
            return false;
        }
        return true;
    }
}
