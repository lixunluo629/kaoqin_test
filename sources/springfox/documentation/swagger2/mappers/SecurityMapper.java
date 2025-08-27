package springfox.documentation.swagger2.mappers;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import io.swagger.models.auth.SecuritySchemeDefinition;
import java.util.Map;
import org.mapstruct.Mapper;
import springfox.documentation.service.ResourceListing;
import springfox.documentation.service.SecurityScheme;

@Mapper
/* loaded from: springfox-swagger2-2.2.2.jar:springfox/documentation/swagger2/mappers/SecurityMapper.class */
public class SecurityMapper {
    private Map<String, SecuritySchemeFactory> factories = ImmutableMap.builder().put("oauth2", new OAuth2AuthFactory()).put("apiKey", new ApiKeyAuthFactory()).put("basicAuth", new BasicAuthFactory()).build();

    public Map<String, SecuritySchemeDefinition> toSecuritySchemeDefinitions(ResourceListing from) {
        if (from == null) {
            return Maps.newHashMap();
        }
        return Maps.transformValues(Maps.uniqueIndex(from.getSecuritySchemes(), schemeName()), toSecuritySchemeDefinition());
    }

    private Function<SecurityScheme, String> schemeName() {
        return new Function<SecurityScheme, String>() { // from class: springfox.documentation.swagger2.mappers.SecurityMapper.1
            @Override // com.google.common.base.Function
            public String apply(SecurityScheme input) {
                return input.getName();
            }
        };
    }

    private Function<SecurityScheme, SecuritySchemeDefinition> toSecuritySchemeDefinition() {
        return new Function<SecurityScheme, SecuritySchemeDefinition>() { // from class: springfox.documentation.swagger2.mappers.SecurityMapper.2
            @Override // com.google.common.base.Function
            public SecuritySchemeDefinition apply(SecurityScheme input) {
                return ((SecuritySchemeFactory) SecurityMapper.this.factories.get(input.getType())).create(input);
            }
        };
    }
}
