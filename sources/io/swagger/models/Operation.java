package io.swagger.models;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import io.swagger.models.parameters.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* loaded from: swagger-models-1.5.3.jar:io/swagger/models/Operation.class */
public class Operation {
    private List<String> tags;
    private String summary;
    private String description;
    private String operationId;
    private List<Scheme> schemes;
    private List<String> consumes;
    private List<String> produces;
    private Map<String, Response> responses;
    private List<Map<String, List<String>>> security;
    private ExternalDocs externalDocs;
    private Boolean deprecated;
    private final Map<String, Object> vendorExtensions = new HashMap();
    private List<Parameter> parameters = new ArrayList();

    public Operation summary(String summary) {
        setSummary(summary);
        return this;
    }

    public Operation description(String description) {
        setDescription(description);
        return this;
    }

    public Operation operationId(String operationId) {
        setOperationId(operationId);
        return this;
    }

    public Operation schemes(List<Scheme> schemes) {
        setSchemes(schemes);
        return this;
    }

    public Operation scheme(Scheme scheme) {
        addScheme(scheme);
        return this;
    }

    public Operation consumes(List<String> consumes) {
        setConsumes(consumes);
        return this;
    }

    public Operation consumes(String consumes) {
        addConsumes(consumes);
        return this;
    }

    public Operation produces(List<String> produces) {
        setProduces(produces);
        return this;
    }

    public Operation produces(String produces) {
        addProduces(produces);
        return this;
    }

    public Operation security(SecurityRequirement security) {
        addSecurity(security.getName(), security.getScopes());
        return this;
    }

    public Operation parameter(Parameter parameter) {
        addParameter(parameter);
        return this;
    }

    public Operation response(int key, Response response) {
        addResponse(String.valueOf(key), response);
        return this;
    }

    public Operation defaultResponse(Response response) {
        addResponse("default", response);
        return this;
    }

    public Operation tags(List<String> tags) {
        setTags(tags);
        return this;
    }

    public Operation tag(String tag) {
        addTag(tag);
        return this;
    }

    public Operation externalDocs(ExternalDocs externalDocs) {
        setExternalDocs(externalDocs);
        return this;
    }

    public Operation deprecated(Boolean deprecated) {
        setDeprecated(deprecated);
        return this;
    }

    public List<String> getTags() {
        return this.tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public void addTag(String tag) {
        if (this.tags == null) {
            this.tags = new ArrayList();
        }
        this.tags.add(tag);
    }

    public String getSummary() {
        return this.summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOperationId() {
        return this.operationId;
    }

    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }

    public List<Scheme> getSchemes() {
        return this.schemes;
    }

    public void setSchemes(List<Scheme> schemes) {
        this.schemes = schemes;
    }

    public void addScheme(Scheme scheme) {
        if (this.schemes == null) {
            this.schemes = new ArrayList();
        }
        if (!this.schemes.contains(scheme)) {
            this.schemes.add(scheme);
        }
    }

    public List<String> getConsumes() {
        return this.consumes;
    }

    public void setConsumes(List<String> consumes) {
        this.consumes = consumes;
    }

    public void addConsumes(String consumes) {
        if (this.consumes == null) {
            this.consumes = new ArrayList();
        }
        this.consumes.add(consumes);
    }

    public List<String> getProduces() {
        return this.produces;
    }

    public void setProduces(List<String> produces) {
        this.produces = produces;
    }

    public void addProduces(String produces) {
        if (this.produces == null) {
            this.produces = new ArrayList();
        }
        this.produces.add(produces);
    }

    public List<Parameter> getParameters() {
        return this.parameters;
    }

    public void setParameters(List<Parameter> parameters) {
        this.parameters = parameters;
    }

    public void addParameter(Parameter parameter) {
        if (this.parameters == null) {
            this.parameters = new ArrayList();
        }
        this.parameters.add(parameter);
    }

    public Map<String, Response> getResponses() {
        return this.responses;
    }

    public void setResponses(Map<String, Response> responses) {
        this.responses = responses;
    }

    public void addResponse(String key, Response response) {
        if (this.responses == null) {
            this.responses = new HashMap();
        }
        this.responses.put(key, response);
    }

    public List<Map<String, List<String>>> getSecurity() {
        return this.security;
    }

    public void setSecurity(List<Map<String, List<String>>> security) {
        this.security = security;
    }

    public void addSecurity(String name, List<String> scopes) {
        if (this.security == null) {
            this.security = new ArrayList();
        }
        Map<String, List<String>> req = new HashMap<>();
        if (scopes == null) {
            scopes = new ArrayList();
        }
        req.put(name, scopes);
        this.security.add(req);
    }

    public ExternalDocs getExternalDocs() {
        return this.externalDocs;
    }

    public void setExternalDocs(ExternalDocs value) {
        this.externalDocs = value;
    }

    public Boolean isDeprecated() {
        return this.deprecated;
    }

    public void setDeprecated(Boolean value) {
        if (value == null || value.equals(Boolean.FALSE)) {
            this.deprecated = null;
        } else {
            this.deprecated = value;
        }
    }

    @JsonAnyGetter
    public Map<String, Object> getVendorExtensions() {
        return this.vendorExtensions;
    }

    @JsonAnySetter
    public void setVendorExtension(String name, Object value) {
        if (name.startsWith("x-")) {
            this.vendorExtensions.put(name, value);
        }
    }

    public int hashCode() {
        int result = (31 * 1) + (this.consumes == null ? 0 : this.consumes.hashCode());
        return (31 * ((31 * ((31 * ((31 * ((31 * ((31 * ((31 * ((31 * ((31 * ((31 * ((31 * ((31 * result) + (this.deprecated == null ? 0 : this.deprecated.hashCode()))) + (this.description == null ? 0 : this.description.hashCode()))) + (this.externalDocs == null ? 0 : this.externalDocs.hashCode()))) + (this.operationId == null ? 0 : this.operationId.hashCode()))) + (this.parameters == null ? 0 : this.parameters.hashCode()))) + (this.produces == null ? 0 : this.produces.hashCode()))) + (this.responses == null ? 0 : this.responses.hashCode()))) + (this.schemes == null ? 0 : this.schemes.hashCode()))) + (this.security == null ? 0 : this.security.hashCode()))) + (this.summary == null ? 0 : this.summary.hashCode()))) + (this.tags == null ? 0 : this.tags.hashCode()))) + (this.vendorExtensions == null ? 0 : this.vendorExtensions.hashCode());
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Operation other = (Operation) obj;
        if (this.consumes == null) {
            if (other.consumes != null) {
                return false;
            }
        } else if (!this.consumes.equals(other.consumes)) {
            return false;
        }
        if (this.deprecated == null) {
            if (other.deprecated != null) {
                return false;
            }
        } else if (!this.deprecated.equals(other.deprecated)) {
            return false;
        }
        if (this.description == null) {
            if (other.description != null) {
                return false;
            }
        } else if (!this.description.equals(other.description)) {
            return false;
        }
        if (this.externalDocs == null) {
            if (other.externalDocs != null) {
                return false;
            }
        } else if (!this.externalDocs.equals(other.externalDocs)) {
            return false;
        }
        if (this.operationId == null) {
            if (other.operationId != null) {
                return false;
            }
        } else if (!this.operationId.equals(other.operationId)) {
            return false;
        }
        if (this.parameters == null) {
            if (other.parameters != null) {
                return false;
            }
        } else if (!this.parameters.equals(other.parameters)) {
            return false;
        }
        if (this.produces == null) {
            if (other.produces != null) {
                return false;
            }
        } else if (!this.produces.equals(other.produces)) {
            return false;
        }
        if (this.responses == null) {
            if (other.responses != null) {
                return false;
            }
        } else if (!this.responses.equals(other.responses)) {
            return false;
        }
        if (this.schemes == null) {
            if (other.schemes != null) {
                return false;
            }
        } else if (!this.schemes.equals(other.schemes)) {
            return false;
        }
        if (this.security == null) {
            if (other.security != null) {
                return false;
            }
        } else if (!this.security.equals(other.security)) {
            return false;
        }
        if (this.summary == null) {
            if (other.summary != null) {
                return false;
            }
        } else if (!this.summary.equals(other.summary)) {
            return false;
        }
        if (this.tags == null) {
            if (other.tags != null) {
                return false;
            }
        } else if (!this.tags.equals(other.tags)) {
            return false;
        }
        if (this.vendorExtensions == null) {
            if (other.vendorExtensions != null) {
                return false;
            }
            return true;
        }
        if (!this.vendorExtensions.equals(other.vendorExtensions)) {
            return false;
        }
        return true;
    }
}
