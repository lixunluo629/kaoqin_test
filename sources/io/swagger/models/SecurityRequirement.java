package io.swagger.models;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* loaded from: swagger-models-1.5.3.jar:io/swagger/models/SecurityRequirement.class */
public class SecurityRequirement {
    private String name;
    private List<String> scopes;
    private Map<String, List<String>> requirements = new HashMap();

    public SecurityRequirement() {
    }

    @Deprecated
    public SecurityRequirement(String name) {
        setName(name);
    }

    public SecurityRequirement scope(String scope) {
        addScope(scope);
        return this;
    }

    public SecurityRequirement requirement(String name, List<String> scopes) {
        if (this.requirements == null) {
            this.requirements = new HashMap();
        }
        if (scopes == null) {
            scopes = new ArrayList();
        }
        this.requirements.put(name, scopes);
        return this;
    }

    public SecurityRequirement requirement(String name) {
        return requirement(name, null);
    }

    @JsonIgnore
    @Deprecated
    public String getName() {
        return this.name;
    }

    @Deprecated
    public void setName(String name) {
        this.name = name;
    }

    @Deprecated
    public List<String> getScopes() {
        return this.scopes;
    }

    @Deprecated
    public void setScopes(List<String> scopes) {
        this.scopes = scopes;
    }

    public void addScope(String scope) {
        if (this.scopes == null) {
            this.scopes = new ArrayList();
        }
        this.scopes.add(scope);
    }

    @JsonAnyGetter
    public Map<String, List<String>> getRequirements() {
        return this.requirements;
    }

    @JsonAnySetter
    public void setRequirements(String name, List<String> scopes) {
        this.requirements.put(name, scopes);
    }

    public int hashCode() {
        int result = (31 * 1) + (this.name == null ? 0 : this.name.hashCode());
        return (31 * ((31 * result) + (this.scopes == null ? 0 : this.scopes.hashCode()))) + (this.requirements == null ? 0 : this.requirements.hashCode());
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        SecurityRequirement other = (SecurityRequirement) obj;
        if (this.name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!this.name.equals(other.name)) {
            return false;
        }
        if (this.scopes == null) {
            if (other.scopes != null) {
                return false;
            }
        } else if (!this.scopes.equals(other.scopes)) {
            return false;
        }
        if (this.requirements == null) {
            if (other.requirements != null) {
                return false;
            }
            return true;
        }
        if (!this.requirements.equals(other.requirements)) {
            return false;
        }
        return true;
    }
}
