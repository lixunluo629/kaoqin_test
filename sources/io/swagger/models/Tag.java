package io.swagger.models;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import java.util.HashMap;
import java.util.Map;
import org.springframework.jdbc.datasource.init.ScriptUtils;

/* loaded from: swagger-models-1.5.3.jar:io/swagger/models/Tag.class */
public class Tag {
    private final Map<String, Object> vendorExtensions = new HashMap();
    private String name;
    private String description;
    private ExternalDocs externalDocs;

    public Tag name(String name) {
        setName(name);
        return this;
    }

    public Tag description(String description) {
        setDescription(description);
        return this;
    }

    public Tag externalDocs(ExternalDocs externalDocs) {
        setExternalDocs(externalDocs);
        return this;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ExternalDocs getExternalDocs() {
        return this.externalDocs;
    }

    public void setExternalDocs(ExternalDocs externalDocs) {
        this.externalDocs = externalDocs;
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

    public String toString() {
        StringBuilder b = new StringBuilder();
        b.append("Tag {\n");
        b.append("\tname: ").append(getName()).append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
        b.append("\tdescription: ").append(getDescription()).append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
        b.append("\texternalDocs: ").append(getExternalDocs()).append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
        b.append("\textensions:").append(this.vendorExtensions.toString());
        b.append("}");
        return b.toString();
    }

    public int hashCode() {
        int result = (31 * 1) + (this.description == null ? 0 : this.description.hashCode());
        return (31 * ((31 * result) + (this.externalDocs == null ? 0 : this.externalDocs.hashCode()))) + (this.name == null ? 0 : this.name.hashCode());
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Tag other = (Tag) obj;
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
        if (this.name == null) {
            if (other.name != null) {
                return false;
            }
            return true;
        }
        if (!this.name.equals(other.name)) {
            return false;
        }
        return true;
    }
}
