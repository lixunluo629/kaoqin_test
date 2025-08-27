package springfox.documentation.swagger.readers.parameter;

import com.google.common.base.Optional;
import io.swagger.annotations.ApiParam;
import java.lang.annotation.Annotation;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.ParameterBuilderPlugin;
import springfox.documentation.spi.service.contexts.ParameterContext;
import springfox.documentation.swagger.common.SwaggerPluginSupport;

@Component("swaggerParameterRequiredReader")
@Order(SwaggerPluginSupport.SWAGGER_PLUGIN_ORDER)
/* loaded from: springfox-swagger-common-2.2.2.jar:springfox/documentation/swagger/readers/parameter/ParameterRequiredReader.class */
public class ParameterRequiredReader implements ParameterBuilderPlugin {
    @Override // springfox.documentation.spi.service.ParameterBuilderPlugin
    public void apply(ParameterContext context) {
        MethodParameter methodParameter = context.methodParameter();
        if (getAnnotatedRequired(methodParameter).isPresent()) {
            context.parameterBuilder().required(getAnnotatedRequired(methodParameter).get().booleanValue());
        }
    }

    @Override // org.springframework.plugin.core.Plugin
    public boolean supports(DocumentationType delimiter) {
        return SwaggerPluginSupport.pluginDoesApply(delimiter);
    }

    private Optional<Boolean> getAnnotatedRequired(MethodParameter methodParameter) {
        Annotation[] methodAnnotations = methodParameter.getParameterAnnotations();
        if (null != methodAnnotations) {
            for (Annotation annotation : methodAnnotations) {
                if (annotation instanceof ApiParam) {
                    return Optional.of(Boolean.valueOf(((ApiParam) annotation).required()));
                }
            }
        }
        return Optional.absent();
    }
}
