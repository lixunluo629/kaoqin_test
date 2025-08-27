package org.springframework.hateoas.client;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.springframework.util.Assert;

/* loaded from: spring-hateoas-0.23.0.RELEASE.jar:org/springframework/hateoas/client/Hop.class */
public final class Hop {
    private final String rel;
    private final Map<String, ? extends Object> parameters;

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Hop)) {
            return false;
        }
        Hop other = (Hop) o;
        Object this$rel = getRel();
        Object other$rel = other.getRel();
        if (this$rel == null) {
            if (other$rel != null) {
                return false;
            }
        } else if (!this$rel.equals(other$rel)) {
            return false;
        }
        Object this$parameters = getParameters();
        Object other$parameters = other.getParameters();
        return this$parameters == null ? other$parameters == null : this$parameters.equals(other$parameters);
    }

    public int hashCode() {
        Object $rel = getRel();
        int result = (1 * 59) + ($rel == null ? 43 : $rel.hashCode());
        Object $parameters = getParameters();
        return (result * 59) + ($parameters == null ? 43 : $parameters.hashCode());
    }

    public String toString() {
        return "Hop(rel=" + getRel() + ", parameters=" + getParameters() + ")";
    }

    private Hop(String rel, Map<String, ? extends Object> parameters) {
        this.rel = rel;
        this.parameters = parameters;
    }

    String getRel() {
        return this.rel;
    }

    Map<String, ? extends Object> getParameters() {
        return this.parameters;
    }

    public Hop withParameters(Map<String, ? extends Object> parameters) {
        return this.parameters == parameters ? this : new Hop(this.rel, parameters);
    }

    public static Hop rel(String rel) {
        Assert.hasText(rel, "Relation must not be null or empty!");
        return new Hop(rel, Collections.emptyMap());
    }

    public Hop withParameter(String name, Object value) {
        Assert.hasText(name, "Name must not be null or empty!");
        HashMap<String, Object> parameters = new HashMap<>(this.parameters);
        parameters.put(name, value);
        return new Hop(this.rel, parameters);
    }

    boolean hasParameters() {
        return !this.parameters.isEmpty();
    }

    Map<String, Object> getMergedParameters(Map<String, Object> globalParameters) {
        Assert.notNull(globalParameters, "Global parameters must not be null!");
        Map<String, Object> mergedParameters = new HashMap<>();
        mergedParameters.putAll(globalParameters);
        mergedParameters.putAll(this.parameters);
        return mergedParameters;
    }
}
