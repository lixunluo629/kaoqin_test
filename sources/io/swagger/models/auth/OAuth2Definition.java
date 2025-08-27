package io.swagger.models.auth;

import com.mysql.jdbc.NonRegisteringDriver;
import java.util.HashMap;
import java.util.Map;

/* loaded from: swagger-models-1.5.3.jar:io/swagger/models/auth/OAuth2Definition.class */
public class OAuth2Definition extends AbstractSecuritySchemeDefinition {
    private String type = "oauth2";
    private String authorizationUrl;
    private String tokenUrl;
    private String flow;
    private Map<String, String> scopes;

    public OAuth2Definition implicit(String authorizationUrl) {
        setAuthorizationUrl(authorizationUrl);
        setFlow("implicit");
        return this;
    }

    public OAuth2Definition password(String tokenUrl) {
        setTokenUrl(tokenUrl);
        setFlow(NonRegisteringDriver.PASSWORD_PROPERTY_KEY);
        return this;
    }

    public OAuth2Definition application(String tokenUrl) {
        setTokenUrl(tokenUrl);
        setFlow("application");
        return this;
    }

    public OAuth2Definition accessCode(String authorizationUrl, String tokenUrl) {
        setTokenUrl(tokenUrl);
        setAuthorizationUrl(authorizationUrl);
        setFlow("accessCode");
        return this;
    }

    public OAuth2Definition scope(String name, String description) {
        addScope(name, description);
        return this;
    }

    public String getAuthorizationUrl() {
        return this.authorizationUrl;
    }

    public void setAuthorizationUrl(String authorizationUrl) {
        this.authorizationUrl = authorizationUrl;
    }

    public String getTokenUrl() {
        return this.tokenUrl;
    }

    public void setTokenUrl(String tokenUrl) {
        this.tokenUrl = tokenUrl;
    }

    public String getFlow() {
        return this.flow;
    }

    public void setFlow(String flow) {
        this.flow = flow;
    }

    public Map<String, String> getScopes() {
        return this.scopes;
    }

    public void setScopes(Map<String, String> scopes) {
        this.scopes = scopes;
    }

    public void addScope(String name, String description) {
        if (this.scopes == null) {
            this.scopes = new HashMap();
        }
        this.scopes.put(name, description);
    }

    @Override // io.swagger.models.auth.SecuritySchemeDefinition
    public String getType() {
        return this.type;
    }

    @Override // io.swagger.models.auth.SecuritySchemeDefinition
    public void setType(String type) {
        this.type = type;
    }

    @Override // io.swagger.models.auth.AbstractSecuritySchemeDefinition
    public int hashCode() {
        int result = super.hashCode();
        return (31 * ((31 * ((31 * ((31 * ((31 * result) + (this.authorizationUrl == null ? 0 : this.authorizationUrl.hashCode()))) + (this.flow == null ? 0 : this.flow.hashCode()))) + (this.scopes == null ? 0 : this.scopes.hashCode()))) + (this.tokenUrl == null ? 0 : this.tokenUrl.hashCode()))) + (this.type == null ? 0 : this.type.hashCode());
    }

    @Override // io.swagger.models.auth.AbstractSecuritySchemeDefinition
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || !super.equals(obj) || getClass() != obj.getClass()) {
            return false;
        }
        OAuth2Definition other = (OAuth2Definition) obj;
        if (this.authorizationUrl == null) {
            if (other.authorizationUrl != null) {
                return false;
            }
        } else if (!this.authorizationUrl.equals(other.authorizationUrl)) {
            return false;
        }
        if (this.flow == null) {
            if (other.flow != null) {
                return false;
            }
        } else if (!this.flow.equals(other.flow)) {
            return false;
        }
        if (this.scopes == null) {
            if (other.scopes != null) {
                return false;
            }
        } else if (!this.scopes.equals(other.scopes)) {
            return false;
        }
        if (this.tokenUrl == null) {
            if (other.tokenUrl != null) {
                return false;
            }
        } else if (!this.tokenUrl.equals(other.tokenUrl)) {
            return false;
        }
        if (this.type == null) {
            if (other.type != null) {
                return false;
            }
            return true;
        }
        if (!this.type.equals(other.type)) {
            return false;
        }
        return true;
    }
}
