package springfox.documentation.swagger.readers.operation;

import com.google.common.collect.Lists;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import java.lang.reflect.Method;
import java.util.List;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.OperationBuilderPlugin;
import springfox.documentation.spi.service.contexts.OperationContext;
import springfox.documentation.swagger.common.SwaggerPluginSupport;

@Component
@Order(SwaggerPluginSupport.SWAGGER_PLUGIN_ORDER)
/* loaded from: springfox-swagger-common-2.2.2.jar:springfox/documentation/swagger/readers/operation/OperationImplicitParametersReader.class */
public class OperationImplicitParametersReader implements OperationBuilderPlugin {
    @Override // springfox.documentation.spi.service.OperationBuilderPlugin
    public void apply(OperationContext context) {
        context.operationBuilder().parameters(readParameters(context));
    }

    @Override // org.springframework.plugin.core.Plugin
    public boolean supports(DocumentationType delimiter) {
        return SwaggerPluginSupport.pluginDoesApply(delimiter);
    }

    protected List<Parameter> readParameters(OperationContext context) {
        HandlerMethod handlerMethod = context.getHandlerMethod();
        Method method = handlerMethod.getMethod();
        ApiImplicitParams annotation = (ApiImplicitParams) AnnotationUtils.findAnnotation(method, ApiImplicitParams.class);
        List<Parameter> parameters = Lists.newArrayList();
        if (null != annotation) {
            for (ApiImplicitParam param : annotation.value()) {
                parameters.add(OperationImplicitParameterReader.getImplicitParameter(param));
            }
        }
        return parameters;
    }
}
