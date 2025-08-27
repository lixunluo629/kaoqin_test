package io.swagger.models;

/* loaded from: swagger-models-1.5.3.jar:io/swagger/models/Contact.class */
public class Contact {
    private String name;
    private String url;
    private String email;

    public Contact name(String name) {
        setName(name);
        return this;
    }

    public Contact url(String url) {
        setUrl(url);
        return this;
    }

    public Contact email(String email) {
        setEmail(email);
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

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int hashCode() {
        int result = (31 * 1) + (this.email == null ? 0 : this.email.hashCode());
        return (31 * ((31 * result) + (this.name == null ? 0 : this.name.hashCode()))) + (this.url == null ? 0 : this.url.hashCode());
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Contact other = (Contact) obj;
        if (this.email == null) {
            if (other.email != null) {
                return false;
            }
        } else if (!this.email.equals(other.email)) {
            return false;
        }
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
