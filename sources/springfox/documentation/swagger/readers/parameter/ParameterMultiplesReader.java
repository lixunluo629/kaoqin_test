package springfox.documentation.swagger.readers.parameter;

import io.swagger.annotations.ApiParam;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.ParameterBuilderPlugin;
import springfox.documentation.spi.service.contexts.ParameterContext;
import springfox.documentation.swagger.common.SwaggerPluginSupport;

@Component("swaggerParameterMultiplesReader")
@Order(SwaggerPluginSupport.SWAGGER_PLUGIN_ORDER)
/* loaded from: springfox-swagger-common-2.2.2.jar:springfox/documentation/swagger/readers/parameter/ParameterMultiplesReader.class */
public class ParameterMultiplesReader implements ParameterBuilderPlugin {
    @Override // springfox.documentation.spi.service.ParameterBuilderPlugin
    public void apply(ParameterContext context) {
        MethodParameter methodParameter = context.methodParameter();
        ApiParam apiParam = (ApiParam) methodParameter.getParameterAnnotation(ApiParam.class);
        Class<?> parameterType = methodParameter.getParameterType();
        if (null != apiParam) {
            if (parameterType == null || !parameterType.isArray() || !parameterType.getComponentType().isEnum()) {
                Boolean allowMultiple = Boolean.valueOf(apiParam.allowMultiple());
                context.parameterBuilder().allowMultiple(allowMultiple.booleanValue());
            }
        }
    }

    @Override // org.springframework.plugin.core.Plugin
    public boolean supports(DocumentationType delimiter) {
        return SwaggerPluginSupport.pluginDoesApply(delimiter);
    }
}
