package org.springframework.hateoas;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/* loaded from: spring-hateoas-0.23.0.RELEASE.jar:org/springframework/hateoas/TemplateVariable.class */
public final class TemplateVariable implements Serializable {
    private static final long serialVersionUID = -2731446749851863774L;
    private final String name;
    private final VariableType type;
    private final String description;

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof TemplateVariable)) {
            return false;
        }
        TemplateVariable other = (TemplateVariable) o;
        Object this$name = getName();
        Object other$name = other.getName();
        if (this$name == null) {
            if (other$name != null) {
                return false;
            }
        } else if (!this$name.equals(other$name)) {
            return false;
        }
        Object this$type = getType();
        Object other$type = other.getType();
        if (this$type == null) {
            if (other$type != null) {
                return false;
            }
        } else if (!this$type.equals(other$type)) {
            return false;
        }
        Object this$description = getDescription();
        Object other$description = other.getDescription();
        return this$description == null ? other$description == null : this$description.equals(other$description);
    }

    public int hashCode() {
        Object $name = getName();
        int result = (1 * 59) + ($name == null ? 43 : $name.hashCode());
        Object $type = getType();
        int result2 = (result * 59) + ($type == null ? 43 : $type.hashCode());
        Object $description = getDescription();
        return (result2 * 59) + ($description == null ? 43 : $description.hashCode());
    }

    public String getName() {
        return this.name;
    }

    public VariableType getType() {
        return this.type;
    }

    public String getDescription() {
        return this.description;
    }

    public TemplateVariable(String name, VariableType type) {
        this(name, type, "");
    }

    public TemplateVariable(String name, VariableType type, String description) {
        Assert.hasText(name, "Variable name must not be null or empty!");
        Assert.notNull(type, "Variable type must not be null!");
        Assert.notNull(description, "Description must not be null!");
        this.name = name;
        this.type = type;
        this.description = description;
    }

    public boolean hasDescription() {
        return StringUtils.hasText(this.description);
    }

    boolean isRequired() {
        return !this.type.isOptional();
    }

    boolean isCombinable(TemplateVariable variable) {
        return this.type.canBeCombinedWith(variable.type);
    }

    boolean isEquivalent(TemplateVariable variable) {
        return this.name.equals(variable.name) && isCombinable(variable);
    }

    boolean isRequestParameterVariable() {
        return this.type.equals(VariableType.REQUEST_PARAM) || this.type.equals(VariableType.REQUEST_PARAM_CONTINUED);
    }

    boolean isFragment() {
        return this.type.equals(VariableType.FRAGMENT);
    }

    public String toString() {
        String base = String.format("{%s%s}", this.type.toString(), this.name);
        return StringUtils.hasText(this.description) ? String.format("%s - %s", base, this.description) : base;
    }

    /* loaded from: spring-hateoas-0.23.0.RELEASE.jar:org/springframework/hateoas/TemplateVariable$VariableType.class */
    public enum VariableType {
        PATH_VARIABLE("", false),
        REQUEST_PARAM("?", true),
        REQUEST_PARAM_CONTINUED("&", true),
        SEGMENT("/", true),
        FRAGMENT("#", true);

        private final String key;
        private final boolean optional;
        private static final List<VariableType> COMBINABLE_TYPES = Arrays.asList(REQUEST_PARAM, REQUEST_PARAM_CONTINUED);

        VariableType(String key, boolean optional) {
            this.key = key;
            this.optional = optional;
        }

        public boolean isOptional() {
            return this.optional;
        }

        public boolean canBeCombinedWith(VariableType type) {
            return equals(type) || (COMBINABLE_TYPES.contains(this) && COMBINABLE_TYPES.contains(type));
        }

        public static VariableType from(String key) {
            for (VariableType type : values()) {
                if (type.key.equals(key)) {
                    return type;
                }
            }
            throw new IllegalArgumentException("Unsupported variable type " + key + "!");
        }

        @Override // java.lang.Enum
        public String toString() {
            return this.key;
        }
    }
}
