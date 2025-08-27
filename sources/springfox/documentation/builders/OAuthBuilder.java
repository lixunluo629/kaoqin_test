package springfox.documentation.builders;

import java.util.ArrayList;
import java.util.List;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.GrantType;
import springfox.documentation.service.OAuth;

/* loaded from: springfox-core-2.2.2.jar:springfox/documentation/builders/OAuthBuilder.class */
public class OAuthBuilder {
    private List<AuthorizationScope> scopes = new ArrayList();
    private List<GrantType> grantTypes = new ArrayList();
    private String name;

    public OAuthBuilder scopes(List<AuthorizationScope> scopes) {
        this.scopes.addAll(BuilderDefaults.nullToEmptyList(scopes));
        return this;
    }

    public OAuthBuilder grantTypes(List<GrantType> grantTypes) {
        this.grantTypes.addAll(BuilderDefaults.nullToEmptyList(grantTypes));
        return this;
    }

    public OAuthBuilder name(String name) {
        this.name = (String) BuilderDefaults.defaultIfAbsent(name, this.name);
        return this;
    }

    public OAuth build() {
        return new OAuth(this.name, this.scopes, this.grantTypes);
    }
}
