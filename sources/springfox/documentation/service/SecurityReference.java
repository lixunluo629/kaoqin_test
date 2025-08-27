package springfox.documentation.service;

import com.google.common.collect.Lists;
import java.util.List;
import springfox.documentation.builders.BuilderDefaults;

/* loaded from: springfox-core-2.2.2.jar:springfox/documentation/service/SecurityReference.class */
public class SecurityReference {
    private final String reference;
    private final List<AuthorizationScope> scopes;

    public SecurityReference(String reference, AuthorizationScope[] scopes) {
        this.scopes = Lists.newArrayList(scopes);
        this.reference = reference;
    }

    public String getReference() {
        return this.reference;
    }

    public List<AuthorizationScope> getScopes() {
        return this.scopes;
    }

    public static SecurityReferenceBuilder builder() {
        return new SecurityReferenceBuilder();
    }

    /* loaded from: springfox-core-2.2.2.jar:springfox/documentation/service/SecurityReference$SecurityReferenceBuilder.class */
    public static class SecurityReferenceBuilder {
        private String reference;
        private AuthorizationScope[] scopes;

        SecurityReferenceBuilder() {
        }

        public SecurityReferenceBuilder reference(String reference) {
            this.reference = (String) BuilderDefaults.defaultIfAbsent(reference, this.reference);
            return this;
        }

        public SecurityReferenceBuilder scopes(AuthorizationScope[] scopes) {
            this.scopes = (AuthorizationScope[]) BuilderDefaults.defaultIfAbsent(scopes, this.scopes);
            return this;
        }

        public SecurityReference build() {
            return new SecurityReference(this.reference, this.scopes);
        }
    }
}
