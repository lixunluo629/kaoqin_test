package springfox.documentation.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/* loaded from: springfox-core-2.2.2.jar:springfox/documentation/service/OAuth.class */
public class OAuth extends SecurityScheme {
    private final List<AuthorizationScope> scopes;
    private final LinkedHashMap<String, GrantType> grantTypes;

    public OAuth(String name, List<AuthorizationScope> scopes, List<GrantType> gTypes) {
        super(name, "oauth2");
        this.scopes = scopes;
        this.grantTypes = initializeGrantTypes(gTypes);
    }

    private LinkedHashMap<String, GrantType> initializeGrantTypes(List<GrantType> gTypes) {
        if (null != gTypes) {
            LinkedHashMap<String, GrantType> map = new LinkedHashMap<>();
            for (GrantType grantType : gTypes) {
                map.put(grantType.getType(), grantType);
            }
            return map;
        }
        return null;
    }

    public List<AuthorizationScope> getScopes() {
        return this.scopes;
    }

    public List<GrantType> getGrantTypes() {
        return new ArrayList(this.grantTypes.values());
    }
}
