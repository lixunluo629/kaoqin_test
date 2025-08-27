package io.swagger.models;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import java.util.HashMap;
import java.util.Map;

/* loaded from: swagger-models-1.5.3.jar:io/swagger/models/ExternalDocs.class */
public class ExternalDocs {
    private String description;
    private String url;
    private final Map<String, Object> vendorExtensions = new HashMap();

    public ExternalDocs() {
    }

    public ExternalDocs(String description, String url) {
        setDescription(description);
        setUrl(url);
    }

    public ExternalDocs description(String description) {
        setDescription(description);
        return this;
    }

    public ExternalDocs url(String url) {
        setUrl(url);
        return this;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
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
        int result = (31 * 1) + (this.description == null ? 0 : this.description.hashCode());
        return (31 * ((31 * result) + (this.url == null ? 0 : this.url.hashCode()))) + (this.vendorExtensions == null ? 0 : this.vendorExtensions.hashCode());
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        ExternalDocs other = (ExternalDocs) obj;
        if (this.description == null) {
            if (other.description != null) {
                return false;
            }
        } else if (!this.description.equals(other.description)) {
            return false;
        }
        if (this.url == null) {
            if (other.url != null) {
                return false;
            }
        } else if (!this.url.equals(other.url)) {
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
