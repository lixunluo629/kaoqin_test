package org.springframework.hateoas;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.springframework.hateoas.TemplateVariable;
import org.springframework.util.Assert;

/* loaded from: spring-hateoas-0.23.0.RELEASE.jar:org/springframework/hateoas/TemplateVariables.class */
public final class TemplateVariables implements Iterable<TemplateVariable>, Serializable {
    public static final TemplateVariables NONE = new TemplateVariables(new TemplateVariable[0]);
    private static final long serialVersionUID = -7736592281223783079L;
    private final List<TemplateVariable> variables;

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof TemplateVariables)) {
            return false;
        }
        TemplateVariables other = (TemplateVariables) o;
        Object this$variables = this.variables;
        Object other$variables = other.variables;
        return this$variables == null ? other$variables == null : this$variables.equals(other$variables);
    }

    public int hashCode() {
        Object $variables = this.variables;
        int result = (1 * 59) + ($variables == null ? 43 : $variables.hashCode());
        return result;
    }

    public TemplateVariables(TemplateVariable... variables) {
        this((List<TemplateVariable>) Arrays.asList(variables));
    }

    public TemplateVariables(List<TemplateVariable> variables) {
        Assert.notNull(variables, "Template variables must not be null!");
        this.variables = Collections.unmodifiableList(variables);
    }

    public TemplateVariables concat(TemplateVariable... variables) {
        return concat(Arrays.asList(variables));
    }

    public TemplateVariables concat(Collection<TemplateVariable> variables) {
        List<TemplateVariable> result = new ArrayList<>(this.variables.size() + variables.size());
        result.addAll(this.variables);
        for (TemplateVariable variable : variables) {
            if (!containsEquivalentFor(variable)) {
                result.add(variable);
            }
        }
        return new TemplateVariables(result);
    }

    public TemplateVariables concat(TemplateVariables variables) {
        return concat(variables.variables);
    }

    public List<TemplateVariable> asList() {
        return this.variables;
    }

    private boolean containsEquivalentFor(TemplateVariable candidate) {
        for (TemplateVariable variable : this.variables) {
            if (variable.isEquivalent(candidate)) {
                return true;
            }
        }
        return false;
    }

    @Override // java.lang.Iterable
    public Iterator<TemplateVariable> iterator() {
        return this.variables.iterator();
    }

    public String toString() {
        return toString(false);
    }

    String toString(boolean appended) {
        if (this.variables.isEmpty()) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        TemplateVariable previous = null;
        for (TemplateVariable variable : this.variables) {
            TemplateVariable.VariableType type = variable.getType();
            TemplateVariable.VariableType type2 = (appended && type.equals(TemplateVariable.VariableType.REQUEST_PARAM)) ? TemplateVariable.VariableType.REQUEST_PARAM_CONTINUED : type;
            if (previous == null) {
                builder.append("{").append(type2.toString());
            } else if (!previous.isCombinable(variable)) {
                builder.append("}{").append(type2.toString());
            } else {
                builder.append(",");
            }
            previous = variable;
            builder.append(variable.getName());
        }
        return builder.append("}").toString();
    }
}
