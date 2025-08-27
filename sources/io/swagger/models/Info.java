package io.swagger.models;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import java.util.HashMap;
import java.util.Map;

/* loaded from: swagger-models-1.5.3.jar:io/swagger/models/Info.class */
public class Info {
    private String description;
    private String version;
    private String title;
    private String termsOfService;
    private Contact contact;
    private License license;
    private Map<String, Object> vendorExtensions = new HashMap();

    public Info version(String version) {
        setVersion(version);
        return this;
    }

    public Info title(String title) {
        setTitle(title);
        return this;
    }

    public Info description(String description) {
        setDescription(description);
        return this;
    }

    public Info termsOfService(String termsOfService) {
        setTermsOfService(termsOfService);
        return this;
    }

    public Info contact(Contact contact) {
        setContact(contact);
        return this;
    }

    public Info license(License license) {
        setLicense(license);
        return this;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVersion() {
        return this.version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTermsOfService() {
        return this.termsOfService;
    }

    public void setTermsOfService(String termsOfService) {
        this.termsOfService = termsOfService;
    }

    public Contact getContact() {
        return this.contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public License getLicense() {
        return this.license;
    }

    public void setLicense(License license) {
        this.license = license;
    }

    public Info mergeWith(Info info) {
        if (info != null) {
            if (this.description == null) {
                this.description = info.description;
            }
            if (this.version == null) {
                this.version = info.version;
            }
            if (this.title == null) {
                this.title = info.title;
            }
            if (this.termsOfService == null) {
                this.termsOfService = info.termsOfService;
            }
            if (this.contact == null) {
                this.contact = info.contact;
            }
            if (this.license == null) {
                this.license = info.license;
            }
            if (this.vendorExtensions == null) {
                this.vendorExtensions = info.vendorExtensions;
            }
        }
        return this;
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
        int result = (31 * 1) + (this.contact == null ? 0 : this.contact.hashCode());
        return (31 * ((31 * ((31 * ((31 * ((31 * ((31 * result) + (this.description == null ? 0 : this.description.hashCode()))) + (this.license == null ? 0 : this.license.hashCode()))) + (this.termsOfService == null ? 0 : this.termsOfService.hashCode()))) + (this.title == null ? 0 : this.title.hashCode()))) + (this.vendorExtensions == null ? 0 : this.vendorExtensions.hashCode()))) + (this.version == null ? 0 : this.version.hashCode());
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Info other = (Info) obj;
        if (this.contact == null) {
            if (other.contact != null) {
                return false;
            }
        } else if (!this.contact.equals(other.contact)) {
            return false;
        }
        if (this.description == null) {
            if (other.description != null) {
                return false;
            }
        } else if (!this.description.equals(other.description)) {
            return false;
        }
        if (this.license == null) {
            if (other.license != null) {
                return false;
            }
        } else if (!this.license.equals(other.license)) {
            return false;
        }
        if (this.termsOfService == null) {
            if (other.termsOfService != null) {
                return false;
            }
        } else if (!this.termsOfService.equals(other.termsOfService)) {
            return false;
        }
        if (this.title == null) {
            if (other.title != null) {
                return false;
            }
        } else if (!this.title.equals(other.title)) {
            return false;
        }
        if (this.vendorExtensions == null) {
            if (other.vendorExtensions != null) {
                return false;
            }
        } else if (!this.vendorExtensions.equals(other.vendorExtensions)) {
            return false;
        }
        if (this.version == null) {
            if (other.version != null) {
                return false;
            }
            return true;
        }
        if (!this.version.equals(other.version)) {
            return false;
        }
        return true;
    }
}
