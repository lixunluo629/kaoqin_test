package springfox.documentation.swagger.readers.operation;

import io.swagger.annotations.ApiOperation;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.OperationBuilderPlugin;
import springfox.documentation.spi.service.contexts.OperationContext;
import springfox.documentation.swagger.common.SwaggerPluginSupport;

@Component
@Order(SwaggerPluginSupport.SWAGGER_PLUGIN_ORDER)
/* loaded from: springfox-swagger-common-2.2.2.jar:springfox/documentation/swagger/readers/operation/OperationNotesReader.class */
public class OperationNotesReader implements OperationBuilderPlugin {
    @Override // springfox.documentation.spi.service.OperationBuilderPlugin
    public void apply(OperationContext context) {
        HandlerMethod handlerMethod = context.getHandlerMethod();
        ApiOperation methodAnnotation = (ApiOperation) handlerMethod.getMethodAnnotation(ApiOperation.class);
        if (null != methodAnnotation && StringUtils.hasText(methodAnnotation.notes())) {
            context.operationBuilder().notes(methodAnnotation.notes());
        }
    }

    @Override // org.springframework.plugin.core.Plugin
    public boolean supports(DocumentationType delimiter) {
        return SwaggerPluginSupport.pluginDoesApply(delimiter);
    }
}
