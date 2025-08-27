package io.swagger.models;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import io.swagger.models.properties.Property;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/* loaded from: swagger-models-1.5.3.jar:io/swagger/models/Response.class */
public class Response {
    private String description;
    private Property schema;
    private Map<String, Object> examples;
    private Map<String, Property> headers;
    private final Map<String, Object> vendorExtensions = new HashMap();

    public Response schema(Property property) {
        setSchema(property);
        return this;
    }

    public Response description(String description) {
        setDescription(description);
        return this;
    }

    public Response example(String type, Object example) {
        if (this.examples == null) {
            this.examples = new HashMap();
        }
        this.examples.put(type, example);
        return this;
    }

    public Response header(String name, Property property) {
        addHeader(name, property);
        return this;
    }

    public Response headers(Map<String, Property> headers) {
        this.headers = headers;
        return this;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Property getSchema() {
        return this.schema;
    }

    public void setSchema(Property schema) {
        this.schema = schema;
    }

    public Map<String, Object> getExamples() {
        return this.examples;
    }

    public void setExamples(Map<String, Object> examples) {
        this.examples = examples;
    }

    public Map<String, Property> getHeaders() {
        return this.headers;
    }

    public void setHeaders(Map<String, Property> headers) {
        this.headers = headers;
    }

    public void addHeader(String key, Property property) {
        if (this.headers == null) {
            this.headers = new LinkedHashMap();
        }
        this.headers.put(key, property);
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
        return (31 * ((31 * ((31 * ((31 * result) + (this.examples == null ? 0 : this.examples.hashCode()))) + (this.headers == null ? 0 : this.headers.hashCode()))) + (this.schema == null ? 0 : this.schema.hashCode()))) + (this.vendorExtensions == null ? 0 : this.vendorExtensions.hashCode());
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Response other = (Response) obj;
        if (this.description == null) {
            if (other.description != null) {
                return false;
            }
        } else if (!this.description.equals(other.description)) {
            return false;
        }
        if (this.examples == null) {
            if (other.examples != null) {
                return false;
            }
        } else if (!this.examples.equals(other.examples)) {
            return false;
        }
        if (this.headers == null) {
            if (other.headers != null) {
                return false;
            }
        } else if (!this.headers.equals(other.headers)) {
            return false;
        }
        if (this.schema == null) {
            if (other.schema != null) {
                return false;
            }
        } else if (!this.schema.equals(other.schema)) {
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
