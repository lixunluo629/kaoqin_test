package io.swagger.models;

/* loaded from: swagger-models-1.5.3.jar:io/swagger/models/Xml.class */
public class Xml {
    private String name;
    private String namespace;
    private String prefix;
    private Boolean attribute;
    private Boolean wrapped;

    public Xml name(String name) {
        setName(name);
        return this;
    }

    public Xml namespace(String namespace) {
        setNamespace(namespace);
        return this;
    }

    public Xml prefix(String prefix) {
        setPrefix(prefix);
        return this;
    }

    public Xml attribute(Boolean attribute) {
        setAttribute(attribute);
        return this;
    }

    public Xml wrapped(Boolean wrapped) {
        setWrapped(wrapped);
        return this;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNamespace() {
        return this.namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getPrefix() {
        return this.prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public Boolean getAttribute() {
        return this.attribute;
    }

    public void setAttribute(Boolean attribute) {
        this.attribute = attribute;
    }

    public Boolean getWrapped() {
        return this.wrapped;
    }

    public void setWrapped(Boolean wrapped) {
        this.wrapped = wrapped;
    }

    public int hashCode() {
        int result = (31 * 1) + (this.attribute == null ? 0 : this.attribute.hashCode());
        return (31 * ((31 * ((31 * ((31 * result) + (this.name == null ? 0 : this.name.hashCode()))) + (this.namespace == null ? 0 : this.namespace.hashCode()))) + (this.prefix == null ? 0 : this.prefix.hashCode()))) + (this.wrapped == null ? 0 : this.wrapped.hashCode());
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Xml other = (Xml) obj;
        if (this.attribute == null) {
            if (other.attribute != null) {
                return false;
            }
        } else if (!this.attribute.equals(other.attribute)) {
            return false;
        }
        if (this.name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!this.name.equals(other.name)) {
            return false;
        }
        if (this.namespace == null) {
            if (other.namespace != null) {
                return false;
            }
        } else if (!this.namespace.equals(other.namespace)) {
            return false;
        }
        if (this.prefix == null) {
            if (other.prefix != null) {
                return false;
            }
        } else if (!this.prefix.equals(other.prefix)) {
            return false;
        }
        if (this.wrapped == null) {
            if (other.wrapped != null) {
                return false;
            }
            return true;
        }
        if (!this.wrapped.equals(other.wrapped)) {
            return false;
        }
        return true;
    }
}
