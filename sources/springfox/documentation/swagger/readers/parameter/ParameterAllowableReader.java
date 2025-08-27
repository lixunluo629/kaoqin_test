package springfox.documentation.swagger.readers.parameter;

import com.google.common.base.Strings;
import io.swagger.annotations.ApiParam;
import java.lang.annotation.Annotation;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import springfox.documentation.schema.Enums;
import springfox.documentation.service.AllowableValues;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.ParameterBuilderPlugin;
import springfox.documentation.spi.service.contexts.ParameterContext;
import springfox.documentation.swagger.common.SwaggerPluginSupport;
import springfox.documentation.swagger.schema.ApiModelProperties;

@Component("swaggerParameterAllowableReader")
@Order(SwaggerPluginSupport.SWAGGER_PLUGIN_ORDER)
/* loaded from: springfox-swagger-common-2.2.2.jar:springfox/documentation/swagger/readers/parameter/ParameterAllowableReader.class */
public class ParameterAllowableReader implements ParameterBuilderPlugin {
    @Override // springfox.documentation.spi.service.ParameterBuilderPlugin
    public void apply(ParameterContext context) {
        MethodParameter methodParameter = context.methodParameter();
        AllowableValues allowableValues = null;
        String allowableValueString = findAnnotatedAllowableValues(methodParameter);
        if (!Strings.isNullOrEmpty(allowableValueString)) {
            allowableValues = ApiModelProperties.allowableValueFromString(allowableValueString);
        } else {
            if (methodParameter.getParameterType().isEnum()) {
                allowableValues = Enums.allowableValues(methodParameter.getParameterType());
            }
            if (methodParameter.getParameterType().isArray()) {
                allowableValues = Enums.allowableValues(methodParameter.getParameterType().getComponentType());
            }
        }
        context.parameterBuilder().allowableValues(allowableValues);
    }

    @Override // org.springframework.plugin.core.Plugin
    public boolean supports(DocumentationType delimiter) {
        return SwaggerPluginSupport.pluginDoesApply(delimiter);
    }

    private String findAnnotatedAllowableValues(MethodParameter methodParameter) {
        Annotation[] methodAnnotations = methodParameter.getParameterAnnotations();
        if (null != methodAnnotations) {
            for (Annotation annotation : methodAnnotations) {
                if (annotation instanceof ApiParam) {
                    return ((ApiParam) annotation).allowableValues();
                }
            }
            return null;
        }
        return null;
    }
}
