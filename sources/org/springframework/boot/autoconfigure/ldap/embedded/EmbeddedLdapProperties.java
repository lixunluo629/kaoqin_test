package org.springframework.boot.autoconfigure.ldap.embedded;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;

@ConfigurationProperties(prefix = "spring.ldap.embedded")
/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/ldap/embedded/EmbeddedLdapProperties.class */
public class EmbeddedLdapProperties {
    private String baseDn;
    private int port = 0;
    private Credential credential = new Credential();
    private String ldif = "classpath:schema.ldif";
    private Validation validation = new Validation();

    public int getPort() {
        return this.port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public Credential getCredential() {
        return this.credential;
    }

    public void setCredential(Credential credential) {
        this.credential = credential;
    }

    public String getBaseDn() {
        return this.baseDn;
    }

    public void setBaseDn(String baseDn) {
        this.baseDn = baseDn;
    }

    public String getLdif() {
        return this.ldif;
    }

    public void setLdif(String ldif) {
        this.ldif = ldif;
    }

    public Validation getValidation() {
        return this.validation;
    }

    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/ldap/embedded/EmbeddedLdapProperties$Credential.class */
    public static class Credential {
        private String username;
        private String password;

        public String getUsername() {
            return this.username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return this.password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/ldap/embedded/EmbeddedLdapProperties$Validation.class */
    public static class Validation {
        private boolean enabled = true;
        private Resource schema;

        public boolean isEnabled() {
            return this.enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public Resource getSchema() {
            return this.schema;
        }

        public void setSchema(Resource schema) {
            this.schema = schema;
        }
    }
}
