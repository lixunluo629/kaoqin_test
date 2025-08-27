package springfox.documentation.swagger2.mappers;

import io.swagger.models.auth.BasicAuthDefinition;
import io.swagger.models.auth.SecuritySchemeDefinition;
import springfox.documentation.service.SecurityScheme;

/* loaded from: springfox-swagger2-2.2.2.jar:springfox/documentation/swagger2/mappers/BasicAuthFactory.class */
class BasicAuthFactory implements SecuritySchemeFactory {
    BasicAuthFactory() {
    }

    @Override // springfox.documentation.swagger2.mappers.SecuritySchemeFactory
    public SecuritySchemeDefinition create(SecurityScheme input) {
        return new BasicAuthDefinition();
    }
}
