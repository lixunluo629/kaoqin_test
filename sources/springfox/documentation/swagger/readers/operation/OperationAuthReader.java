package springfox.documentation.swagger.readers.operation;

import com.google.common.base.Optional;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import io.swagger.annotations.AuthorizationScope;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import springfox.documentation.builders.AuthorizationScopeBuilder;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.OperationBuilderPlugin;
import springfox.documentation.spi.service.contexts.OperationContext;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.swagger.common.SwaggerPluginSupport;

@Component
@Order(SwaggerPluginSupport.SWAGGER_PLUGIN_ORDER)
/* loaded from: springfox-swagger-common-2.2.2.jar:springfox/documentation/swagger/readers/operation/OperationAuthReader.class */
public class OperationAuthReader implements OperationBuilderPlugin {
    private static final Logger LOG = LoggerFactory.getLogger((Class<?>) OperationAuthReader.class);

    @Override // springfox.documentation.spi.service.OperationBuilderPlugin
    public void apply(OperationContext context) {
        Authorization[] authorizationAnnotations;
        Optional<SecurityContext> securityContext = context.securityContext();
        HandlerMethod handlerMethod = context.getHandlerMethod();
        String requestMappingPattern = context.requestMappingPattern();
        List<SecurityReference> securityReferences = Lists.newArrayList();
        if (securityContext.isPresent()) {
            securityReferences = securityContext.get().securityForPath(requestMappingPattern);
        }
        ApiOperation apiOperationAnnotation = (ApiOperation) handlerMethod.getMethodAnnotation(ApiOperation.class);
        if (null != apiOperationAnnotation && null != apiOperationAnnotation.authorizations() && (authorizationAnnotations = apiOperationAnnotation.authorizations()) != null && authorizationAnnotations.length > 0 && StringUtils.hasText(authorizationAnnotations[0].value())) {
            securityReferences = Lists.newArrayList();
            for (Authorization authorization : authorizationAnnotations) {
                String value = authorization.value();
                AuthorizationScope[] scopes = authorization.scopes();
                List<springfox.documentation.service.AuthorizationScope> authorizationScopeList = Lists.newArrayList();
                for (AuthorizationScope authorizationScope : scopes) {
                    String description = authorizationScope.description();
                    String scope = authorizationScope.scope();
                    if (!Strings.isNullOrEmpty(scope)) {
                        authorizationScopeList.add(new AuthorizationScopeBuilder().scope(scope).description(description).build());
                    }
                }
                springfox.documentation.service.AuthorizationScope[] authorizationScopes = (springfox.documentation.service.AuthorizationScope[]) authorizationScopeList.toArray(new springfox.documentation.service.AuthorizationScope[authorizationScopeList.size()]);
                SecurityReference securityReference = SecurityReference.builder().reference(value).scopes(authorizationScopes).build();
                securityReferences.add(securityReference);
            }
        }
        if (securityReferences != null) {
            LOG.debug("Authorization count {} for method {}", Integer.valueOf(securityReferences.size()), handlerMethod.getMethod().getName());
            context.operationBuilder().authorizations(securityReferences);
        }
    }

    @Override // org.springframework.plugin.core.Plugin
    public boolean supports(DocumentationType delimiter) {
        return SwaggerPluginSupport.pluginDoesApply(delimiter);
    }
}
