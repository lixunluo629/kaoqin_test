package springfox.documentation.swagger2.mappers;

import io.swagger.models.auth.ApiKeyAuthDefinition;
import io.swagger.models.auth.In;
import io.swagger.models.auth.SecuritySchemeDefinition;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.SecurityScheme;

/* loaded from: springfox-swagger2-2.2.2.jar:springfox/documentation/swagger2/mappers/ApiKeyAuthFactory.class */
class ApiKeyAuthFactory implements SecuritySchemeFactory {
    ApiKeyAuthFactory() {
    }

    @Override // springfox.documentation.swagger2.mappers.SecuritySchemeFactory
    public SecuritySchemeDefinition create(SecurityScheme input) {
        ApiKey apiKey = (ApiKey) input;
        ApiKeyAuthDefinition definition = new ApiKeyAuthDefinition();
        definition.name(apiKey.getName()).in(In.forValue(apiKey.getPassAs()));
        return definition;
    }
}
