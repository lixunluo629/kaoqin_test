package springfox.documentation.swagger.readers.operation;

import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.OperationBuilderPlugin;
import springfox.documentation.spi.service.contexts.OperationContext;
import springfox.documentation.swagger.common.SwaggerPluginSupport;

@Component
@Order(SwaggerPluginSupport.SWAGGER_PLUGIN_ORDER)
/* loaded from: springfox-swagger-common-2.2.2.jar:springfox/documentation/swagger/readers/operation/OperationPositionReader.class */
public class OperationPositionReader implements OperationBuilderPlugin {
    private static final Logger log = LoggerFactory.getLogger((Class<?>) OperationPositionReader.class);

    @Override // springfox.documentation.spi.service.OperationBuilderPlugin
    public void apply(OperationContext context) {
        ApiOperation apiOperation = (ApiOperation) context.getHandlerMethod().getMethodAnnotation(ApiOperation.class);
        if (null != apiOperation && apiOperation.position() > 0) {
            context.operationBuilder().position(apiOperation.position());
            log.debug("Added operation at position: {}", Integer.valueOf(apiOperation.position()));
        }
    }

    @Override // org.springframework.plugin.core.Plugin
    public boolean supports(DocumentationType delimiter) {
        return SwaggerPluginSupport.pluginDoesApply(delimiter);
    }
}
