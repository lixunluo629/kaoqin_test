package springfox.documentation.builders;

import springfox.documentation.service.AuthorizationScope;

/* loaded from: springfox-core-2.2.2.jar:springfox/documentation/builders/AuthorizationScopeBuilder.class */
public class AuthorizationScopeBuilder {
    private String scope;
    private String description;

    public AuthorizationScopeBuilder scope(String scope) {
        this.scope = (String) BuilderDefaults.defaultIfAbsent(scope, this.scope);
        return this;
    }

    public AuthorizationScopeBuilder description(String description) {
        this.description = (String) BuilderDefaults.defaultIfAbsent(description, this.description);
        return this;
    }

    public AuthorizationScope build() {
        return new AuthorizationScope(this.scope, this.description);
    }
}
