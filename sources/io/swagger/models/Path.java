package io.swagger.models;

import ch.qos.logback.core.joran.util.beans.BeanUtil;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.models.parameters.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonPropertyOrder({BeanUtil.PREFIX_GETTER_GET, "head", "post", "put", "delete", "options", "patch"})
/* loaded from: swagger-models-1.5.3.jar:io/swagger/models/Path.class */
public class Path {
    private final Map<String, Object> vendorExtensions = new HashMap();
    private Operation get;
    private Operation put;
    private Operation post;
    private Operation head;
    private Operation delete;
    private Operation patch;
    private Operation options;
    private List<Parameter> parameters;

    public Path set(String method, Operation op) {
        if (BeanUtil.PREFIX_GETTER_GET.equals(method)) {
            return get(op);
        }
        if ("put".equals(method)) {
            return put(op);
        }
        if ("head".equals(method)) {
            return head(op);
        }
        if ("post".equals(method)) {
            return post(op);
        }
        if ("delete".equals(method)) {
            return delete(op);
        }
        if ("patch".equals(method)) {
            return patch(op);
        }
        if ("options".equals(method)) {
            return options(op);
        }
        return null;
    }

    public Path get(Operation get) {
        this.get = get;
        return this;
    }

    public Path head(Operation head) {
        this.head = head;
        return this;
    }

    public Path put(Operation put) {
        this.put = put;
        return this;
    }

    public Path post(Operation post) {
        this.post = post;
        return this;
    }

    public Path delete(Operation delete) {
        this.delete = delete;
        return this;
    }

    public Path patch(Operation patch) {
        this.patch = patch;
        return this;
    }

    public Path options(Operation options) {
        this.options = options;
        return this;
    }

    public Operation getGet() {
        return this.get;
    }

    public void setGet(Operation get) {
        this.get = get;
    }

    public Operation getHead() {
        return this.head;
    }

    public void setHead(Operation head) {
        this.head = head;
    }

    public Operation getPut() {
        return this.put;
    }

    public void setPut(Operation put) {
        this.put = put;
    }

    public Operation getPost() {
        return this.post;
    }

    public void setPost(Operation post) {
        this.post = post;
    }

    public Operation getDelete() {
        return this.delete;
    }

    public void setDelete(Operation delete) {
        this.delete = delete;
    }

    public Operation getPatch() {
        return this.patch;
    }

    public void setPatch(Operation patch) {
        this.patch = patch;
    }

    public Operation getOptions() {
        return this.options;
    }

    public void setOptions(Operation options) {
        this.options = options;
    }

    @JsonIgnore
    public List<Operation> getOperations() {
        List<Operation> allOperations = new ArrayList<>();
        if (this.get != null) {
            allOperations.add(this.get);
        }
        if (this.put != null) {
            allOperations.add(this.put);
        }
        if (this.head != null) {
            allOperations.add(this.head);
        }
        if (this.post != null) {
            allOperations.add(this.post);
        }
        if (this.delete != null) {
            allOperations.add(this.delete);
        }
        if (this.patch != null) {
            allOperations.add(this.patch);
        }
        if (this.options != null) {
            allOperations.add(this.options);
        }
        return allOperations;
    }

    @JsonIgnore
    public Map<HttpMethod, Operation> getOperationMap() {
        Map<HttpMethod, Operation> result = new HashMap<>();
        if (this.get != null) {
            result.put(HttpMethod.GET, this.get);
        }
        if (this.put != null) {
            result.put(HttpMethod.PUT, this.put);
        }
        if (this.post != null) {
            result.put(HttpMethod.POST, this.post);
        }
        if (this.delete != null) {
            result.put(HttpMethod.DELETE, this.delete);
        }
        if (this.patch != null) {
            result.put(HttpMethod.PATCH, this.patch);
        }
        if (this.options != null) {
            result.put(HttpMethod.OPTIONS, this.options);
            result.put(HttpMethod.OPTIONS, this.options);
        }
        return result;
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

    @JsonIgnore
    public boolean isEmpty() {
        if (this.get == null && this.put == null && this.head == null && this.post == null && this.delete == null && this.patch == null && this.options == null) {
            return true;
        }
        return false;
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
        int result = (31 * 1) + (this.delete == null ? 0 : this.delete.hashCode());
        return (31 * ((31 * ((31 * ((31 * ((31 * ((31 * ((31 * ((31 * result) + (this.get == null ? 0 : this.get.hashCode()))) + (this.head == null ? 0 : this.head.hashCode()))) + (this.options == null ? 0 : this.options.hashCode()))) + (this.parameters == null ? 0 : this.parameters.hashCode()))) + (this.patch == null ? 0 : this.patch.hashCode()))) + (this.post == null ? 0 : this.post.hashCode()))) + (this.put == null ? 0 : this.put.hashCode()))) + (this.vendorExtensions == null ? 0 : this.vendorExtensions.hashCode());
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Path other = (Path) obj;
        if (this.delete == null) {
            if (other.delete != null) {
                return false;
            }
        } else if (!this.delete.equals(other.delete)) {
            return false;
        }
        if (this.get == null) {
            if (other.get != null) {
                return false;
            }
        } else if (!this.get.equals(other.get)) {
            return false;
        }
        if (this.head == null) {
            if (other.head != null) {
                return false;
            }
        } else if (!this.head.equals(other.head)) {
            return false;
        }
        if (this.options == null) {
            if (other.options != null) {
                return false;
            }
        } else if (!this.options.equals(other.options)) {
            return false;
        }
        if (this.parameters == null) {
            if (other.parameters != null) {
                return false;
            }
        } else if (!this.parameters.equals(other.parameters)) {
            return false;
        }
        if (this.patch == null) {
            if (other.patch != null) {
                return false;
            }
        } else if (!this.patch.equals(other.patch)) {
            return false;
        }
        if (this.post == null) {
            if (other.post != null) {
                return false;
            }
        } else if (!this.post.equals(other.post)) {
            return false;
        }
        if (this.put == null) {
            if (other.put != null) {
                return false;
            }
        } else if (!this.put.equals(other.put)) {
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
