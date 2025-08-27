package springfox.documentation.swagger2.mappers;

import io.swagger.models.auth.SecuritySchemeDefinition;
import springfox.documentation.service.SecurityScheme;

/* loaded from: springfox-swagger2-2.2.2.jar:springfox/documentation/swagger2/mappers/SecuritySchemeFactory.class */
interface SecuritySchemeFactory {
    SecuritySchemeDefinition create(SecurityScheme securityScheme);
}
