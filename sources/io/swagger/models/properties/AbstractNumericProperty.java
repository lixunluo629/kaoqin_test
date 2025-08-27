package io.swagger.models.properties;

/* loaded from: swagger-models-1.5.3.jar:io/swagger/models/properties/AbstractNumericProperty.class */
public abstract class AbstractNumericProperty extends AbstractProperty implements Property {
    protected Double minimum;
    protected Double maximum;
    protected Boolean exclusiveMinimum;
    protected Boolean exclusiveMaximum;

    public AbstractNumericProperty minimum(Double minimum) {
        setMinimum(minimum);
        return this;
    }

    public AbstractNumericProperty maximum(Double maximum) {
        setMaximum(maximum);
        return this;
    }

    public AbstractNumericProperty exclusiveMinimum(Boolean exclusiveMinimum) {
        setExclusiveMinimum(exclusiveMinimum);
        return this;
    }

    public AbstractNumericProperty exclusiveMaximum(Boolean exclusiveMaximum) {
        setExclusiveMaximum(exclusiveMaximum);
        return this;
    }

    public Double getMinimum() {
        return this.minimum;
    }

    public void setMinimum(Double minimum) {
        this.minimum = minimum;
    }

    public Double getMaximum() {
        return this.maximum;
    }

    public void setMaximum(Double maximum) {
        this.maximum = maximum;
    }

    public Boolean getExclusiveMinimum() {
        return this.exclusiveMinimum;
    }

    public void setExclusiveMinimum(Boolean exclusiveMinimum) {
        this.exclusiveMinimum = exclusiveMinimum;
    }

    public Boolean getExclusiveMaximum() {
        return this.exclusiveMaximum;
    }

    public void setExclusiveMaximum(Boolean exclusiveMaximum) {
        this.exclusiveMaximum = exclusiveMaximum;
    }

    @Override // io.swagger.models.properties.AbstractProperty
    public int hashCode() {
        int result = super.hashCode();
        return (31 * ((31 * ((31 * ((31 * result) + (this.exclusiveMaximum == null ? 0 : this.exclusiveMaximum.hashCode()))) + (this.exclusiveMinimum == null ? 0 : this.exclusiveMinimum.hashCode()))) + (this.maximum == null ? 0 : this.maximum.hashCode()))) + (this.minimum == null ? 0 : this.minimum.hashCode());
    }

    @Override // io.swagger.models.properties.AbstractProperty
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj) || getClass() != obj.getClass()) {
            return false;
        }
        AbstractNumericProperty other = (AbstractNumericProperty) obj;
        if (this.exclusiveMaximum == null) {
            if (other.exclusiveMaximum != null) {
                return false;
            }
        } else if (!this.exclusiveMaximum.equals(other.exclusiveMaximum)) {
            return false;
        }
        if (this.exclusiveMinimum == null) {
            if (other.exclusiveMinimum != null) {
                return false;
            }
        } else if (!this.exclusiveMinimum.equals(other.exclusiveMinimum)) {
            return false;
        }
        if (this.maximum == null) {
            if (other.maximum != null) {
                return false;
            }
        } else if (!this.maximum.equals(other.maximum)) {
            return false;
        }
        if (this.minimum == null) {
            if (other.minimum != null) {
                return false;
            }
            return true;
        }
        if (!this.minimum.equals(other.minimum)) {
            return false;
        }
        return true;
    }
}
