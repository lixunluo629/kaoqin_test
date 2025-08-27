package io.swagger.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.models.auth.SecuritySchemeDefinition;
import io.swagger.models.parameters.Parameter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/* loaded from: swagger-models-1.5.3.jar:io/swagger/models/Swagger.class */
public class Swagger {
    protected String swagger = "2.0";
    protected Info info;
    protected String host;
    protected String basePath;
    protected List<Tag> tags;
    protected List<Scheme> schemes;
    protected List<String> consumes;
    protected List<String> produces;
    protected List<SecurityRequirement> security;
    protected Map<String, Path> paths;
    protected Map<String, SecuritySchemeDefinition> securityDefinitions;
    protected Map<String, Model> definitions;
    protected Map<String, Parameter> parameters;
    protected ExternalDocs externalDocs;

    public Swagger info(Info info) {
        setInfo(info);
        return this;
    }

    public Swagger host(String host) {
        setHost(host);
        return this;
    }

    public Swagger basePath(String basePath) {
        setBasePath(basePath);
        return this;
    }

    public Swagger externalDocs(ExternalDocs value) {
        setExternalDocs(value);
        return this;
    }

    public Swagger tags(List<Tag> tags) {
        setTags(tags);
        return this;
    }

    public Swagger tag(Tag tag) {
        addTag(tag);
        return this;
    }

    public Swagger schemes(List<Scheme> schemes) {
        setSchemes(schemes);
        return this;
    }

    public Swagger scheme(Scheme scheme) {
        addScheme(scheme);
        return this;
    }

    public Swagger consumes(List<String> consumes) {
        setConsumes(consumes);
        return this;
    }

    public Swagger consumes(String consumes) {
        addConsumes(consumes);
        return this;
    }

    public Swagger produces(List<String> produces) {
        setProduces(produces);
        return this;
    }

    public Swagger produces(String produces) {
        addProduces(produces);
        return this;
    }

    public Swagger paths(Map<String, Path> paths) {
        setPaths(paths);
        return this;
    }

    public Swagger path(String key, Path path) {
        if (this.paths == null) {
            this.paths = new LinkedHashMap();
        }
        this.paths.put(key, path);
        return this;
    }

    public Swagger parameter(String key, Parameter parameter) {
        addParameter(key, parameter);
        return this;
    }

    public Swagger securityDefinition(String name, SecuritySchemeDefinition securityDefinition) {
        addSecurityDefinition(name, securityDefinition);
        return this;
    }

    public Swagger model(String name, Model model) {
        addDefinition(name, model);
        return this;
    }

    public Swagger security(SecurityRequirement securityRequirement) {
        addSecurity(securityRequirement);
        return this;
    }

    public String getSwagger() {
        return this.swagger;
    }

    public void setSwagger(String swagger) {
        this.swagger = swagger;
    }

    public Info getInfo() {
        return this.info;
    }

    public void setInfo(Info info) {
        this.info = info;
    }

    public String getHost() {
        return this.host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getBasePath() {
        return this.basePath;
    }

    public void setBasePath(String basePath) {
        this.basePath = basePath;
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

    public List<Tag> getTags() {
        return this.tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public void addTag(Tag tag) {
        if (this.tags == null) {
            this.tags = new ArrayList();
        }
        if (tag != null && tag.getName() != null) {
            boolean found = false;
            for (Tag existing : this.tags) {
                if (existing.getName().equals(tag.getName())) {
                    found = true;
                }
            }
            if (!found) {
                this.tags.add(tag);
            }
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
        if (!this.consumes.contains(consumes)) {
            this.consumes.add(consumes);
        }
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
        if (!this.produces.contains(produces)) {
            this.produces.add(produces);
        }
    }

    public Map<String, Path> getPaths() {
        if (this.paths == null) {
            return null;
        }
        Map<String, Path> sorted = new LinkedHashMap<>();
        List<String> keys = new ArrayList<>();
        keys.addAll(this.paths.keySet());
        Collections.sort(keys);
        for (String key : keys) {
            sorted.put(key, this.paths.get(key));
        }
        return sorted;
    }

    public void setPaths(Map<String, Path> paths) {
        this.paths = paths;
    }

    public Path getPath(String path) {
        if (this.paths == null) {
            return null;
        }
        return this.paths.get(path);
    }

    public Map<String, SecuritySchemeDefinition> getSecurityDefinitions() {
        return this.securityDefinitions;
    }

    public void setSecurityDefinitions(Map<String, SecuritySchemeDefinition> securityDefinitions) {
        this.securityDefinitions = securityDefinitions;
    }

    public void addSecurityDefinition(String name, SecuritySchemeDefinition securityDefinition) {
        if (this.securityDefinitions == null) {
            this.securityDefinitions = new HashMap();
        }
        this.securityDefinitions.put(name, securityDefinition);
    }

    @Deprecated
    public List<SecurityRequirement> getSecurityRequirement() {
        return this.security;
    }

    @Deprecated
    public void setSecurityRequirement(List<SecurityRequirement> securityRequirements) {
        this.security = securityRequirements;
    }

    @Deprecated
    public void addSecurityDefinition(SecurityRequirement securityRequirement) {
        addSecurity(securityRequirement);
    }

    @JsonIgnore
    public List<SecurityRequirement> getSecurity() {
        return this.security;
    }

    @JsonIgnore
    public void setSecurity(List<SecurityRequirement> securityRequirements) {
        this.security = securityRequirements;
    }

    public void addSecurity(SecurityRequirement securityRequirement) {
        if (this.security == null) {
            this.security = new ArrayList();
        }
        this.security.add(securityRequirement);
    }

    public Map<String, Model> getDefinitions() {
        return this.definitions;
    }

    public void setDefinitions(Map<String, Model> definitions) {
        this.definitions = definitions;
    }

    public void addDefinition(String key, Model model) {
        if (this.definitions == null) {
            this.definitions = new HashMap();
        }
        this.definitions.put(key, model);
    }

    public Map<String, Parameter> getParameters() {
        return this.parameters;
    }

    public void setParameters(Map<String, Parameter> parameters) {
        this.parameters = parameters;
    }

    public Parameter getParameter(String parameter) {
        if (this.parameters == null) {
            return null;
        }
        return this.parameters.get(parameter);
    }

    public void addParameter(String key, Parameter parameter) {
        if (this.parameters == null) {
            this.parameters = new HashMap();
        }
        this.parameters.put(key, parameter);
    }

    public ExternalDocs getExternalDocs() {
        return this.externalDocs;
    }

    public void setExternalDocs(ExternalDocs value) {
        this.externalDocs = value;
    }

    public int hashCode() {
        int result = (31 * 1) + (this.basePath == null ? 0 : this.basePath.hashCode());
        return (31 * ((31 * ((31 * ((31 * ((31 * ((31 * ((31 * ((31 * ((31 * ((31 * ((31 * ((31 * ((31 * result) + (this.consumes == null ? 0 : this.consumes.hashCode()))) + (this.definitions == null ? 0 : this.definitions.hashCode()))) + (this.externalDocs == null ? 0 : this.externalDocs.hashCode()))) + (this.host == null ? 0 : this.host.hashCode()))) + (this.info == null ? 0 : this.info.hashCode()))) + (this.parameters == null ? 0 : this.parameters.hashCode()))) + (this.paths == null ? 0 : this.paths.hashCode()))) + (this.produces == null ? 0 : this.produces.hashCode()))) + (this.schemes == null ? 0 : this.schemes.hashCode()))) + (this.securityDefinitions == null ? 0 : this.securityDefinitions.hashCode()))) + (this.security == null ? 0 : this.security.hashCode()))) + (this.swagger == null ? 0 : this.swagger.hashCode()))) + (this.tags == null ? 0 : this.tags.hashCode());
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Swagger other = (Swagger) obj;
        if (this.basePath == null) {
            if (other.basePath != null) {
                return false;
            }
        } else if (!this.basePath.equals(other.basePath)) {
            return false;
        }
        if (this.consumes == null) {
            if (other.consumes != null) {
                return false;
            }
        } else if (!this.consumes.equals(other.consumes)) {
            return false;
        }
        if (this.definitions == null) {
            if (other.definitions != null) {
                return false;
            }
        } else if (!this.definitions.equals(other.definitions)) {
            return false;
        }
        if (this.externalDocs == null) {
            if (other.externalDocs != null) {
                return false;
            }
        } else if (!this.externalDocs.equals(other.externalDocs)) {
            return false;
        }
        if (this.host == null) {
            if (other.host != null) {
                return false;
            }
        } else if (!this.host.equals(other.host)) {
            return false;
        }
        if (this.info == null) {
            if (other.info != null) {
                return false;
            }
        } else if (!this.info.equals(other.info)) {
            return false;
        }
        if (this.parameters == null) {
            if (other.parameters != null) {
                return false;
            }
        } else if (!this.parameters.equals(other.parameters)) {
            return false;
        }
        if (this.paths == null) {
            if (other.paths != null) {
                return false;
            }
        } else if (!this.paths.equals(other.paths)) {
            return false;
        }
        if (this.produces == null) {
            if (other.produces != null) {
                return false;
            }
        } else if (!this.produces.equals(other.produces)) {
            return false;
        }
        if (this.schemes == null) {
            if (other.schemes != null) {
                return false;
            }
        } else if (!this.schemes.equals(other.schemes)) {
            return false;
        }
        if (this.securityDefinitions == null) {
            if (other.securityDefinitions != null) {
                return false;
            }
        } else if (!this.securityDefinitions.equals(other.securityDefinitions)) {
            return false;
        }
        if (this.security == null) {
            if (other.security != null) {
                return false;
            }
        } else if (!this.security.equals(other.security)) {
            return false;
        }
        if (this.swagger == null) {
            if (other.swagger != null) {
                return false;
            }
        } else if (!this.swagger.equals(other.swagger)) {
            return false;
        }
        if (this.tags == null) {
            if (other.tags != null) {
                return false;
            }
            return true;
        }
        if (!this.tags.equals(other.tags)) {
            return false;
        }
        return true;
    }
}
