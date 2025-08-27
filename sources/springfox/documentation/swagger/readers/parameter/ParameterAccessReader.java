package springfox.documentation.swagger.readers.parameter;

import com.google.common.base.Strings;
import io.swagger.annotations.ApiParam;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.ParameterBuilderPlugin;
import springfox.documentation.spi.service.contexts.ParameterContext;
import springfox.documentation.swagger.common.SwaggerPluginSupport;

@Component("swaggerParameterAccessReader")
@Order(SwaggerPluginSupport.SWAGGER_PLUGIN_ORDER)
/* loaded from: springfox-swagger-common-2.2.2.jar:springfox/documentation/swagger/readers/parameter/ParameterAccessReader.class */
public class ParameterAccessReader implements ParameterBuilderPlugin {
    @Override // springfox.documentation.spi.service.ParameterBuilderPlugin
    public void apply(ParameterContext context) {
        MethodParameter methodParameter = context.methodParameter();
        ApiParam apiParam = (ApiParam) methodParameter.getParameterAnnotation(ApiParam.class);
        if (apiParam != null && !Strings.isNullOrEmpty(apiParam.access())) {
            String access = apiParam.access();
            context.parameterBuilder().parameterAccess(access);
        }
    }

    @Override // org.springframework.plugin.core.Plugin
    public boolean supports(DocumentationType delimiter) {
        return SwaggerPluginSupport.pluginDoesApply(delimiter);
    }
}
