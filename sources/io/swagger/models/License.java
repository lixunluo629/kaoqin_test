package io.swagger.models;

/* loaded from: swagger-models-1.5.3.jar:io/swagger/models/License.class */
public class License {
    private String name;
    private String url;

    public License name(String name) {
        setName(name);
        return this;
    }

    public License url(String url) {
        setUrl(url);
        return this;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int hashCode() {
        int result = (31 * 1) + (this.name == null ? 0 : this.name.hashCode());
        return (31 * result) + (this.url == null ? 0 : this.url.hashCode());
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        License other = (License) obj;
        if (this.name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!this.name.equals(other.name)) {
            return false;
        }
        if (this.url == null) {
            if (other.url != null) {
                return false;
            }
            return true;
        }
        if (!this.url.equals(other.url)) {
            return false;
        }
        return true;
    }
}
