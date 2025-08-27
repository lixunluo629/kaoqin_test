package springfox.documentation.swagger.readers.operation;

import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.OperationBuilderPlugin;
import springfox.documentation.spi.service.contexts.OperationContext;
import springfox.documentation.swagger.common.SwaggerPluginSupport;

@Component
@Order(SwaggerPluginSupport.SWAGGER_PLUGIN_ORDER)
/* loaded from: springfox-swagger-common-2.2.2.jar:springfox/documentation/swagger/readers/operation/OperationHttpMethodReader.class */
public class OperationHttpMethodReader implements OperationBuilderPlugin {
    private static final Logger log = LoggerFactory.getLogger((Class<?>) OperationHttpMethodReader.class);

    @Override // springfox.documentation.spi.service.OperationBuilderPlugin
    public void apply(OperationContext context) {
        HandlerMethod handlerMethod = context.getHandlerMethod();
        ApiOperation apiOperationAnnotation = (ApiOperation) handlerMethod.getMethodAnnotation(ApiOperation.class);
        if (apiOperationAnnotation != null && StringUtils.hasText(apiOperationAnnotation.httpMethod())) {
            String apiMethod = apiOperationAnnotation.httpMethod();
            try {
                RequestMethod.valueOf(apiMethod);
                context.operationBuilder().method(HttpMethod.valueOf(apiMethod));
            } catch (IllegalArgumentException e) {
                log.error("Invalid http method: " + apiMethod + "Valid ones are [" + RequestMethod.values() + "]", (Throwable) e);
            }
        }
    }

    @Override // org.springframework.plugin.core.Plugin
    public boolean supports(DocumentationType delimiter) {
        return SwaggerPluginSupport.pluginDoesApply(delimiter);
    }
}
