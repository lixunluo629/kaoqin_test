package springfox.documentation.swagger.readers.parameter;

import com.google.common.base.Strings;
import io.swagger.annotations.ApiParam;
import java.lang.annotation.Annotation;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ValueConstants;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.ParameterBuilderPlugin;
import springfox.documentation.spi.service.contexts.ParameterContext;
import springfox.documentation.swagger.common.SwaggerPluginSupport;

@Component("swaggerParameterDefaultReader")
@Order(SwaggerPluginSupport.SWAGGER_PLUGIN_ORDER)
/* loaded from: springfox-swagger-common-2.2.2.jar:springfox/documentation/swagger/readers/parameter/ParameterDefaultReader.class */
public class ParameterDefaultReader implements ParameterBuilderPlugin {
    @Override // springfox.documentation.spi.service.ParameterBuilderPlugin
    public void apply(ParameterContext context) {
        MethodParameter methodParameter = context.methodParameter();
        String defaultValue = findAnnotatedDefaultValue(methodParameter);
        boolean isSkip = ValueConstants.DEFAULT_NONE.equals(defaultValue);
        if (!isSkip) {
            context.parameterBuilder().defaultValue(Strings.nullToEmpty(defaultValue));
        }
    }

    @Override // org.springframework.plugin.core.Plugin
    public boolean supports(DocumentationType delimiter) {
        return SwaggerPluginSupport.pluginDoesApply(delimiter);
    }

    private String findAnnotatedDefaultValue(MethodParameter methodParameter) {
        Annotation[] methodAnnotations = methodParameter.getParameterAnnotations();
        if (null != methodAnnotations) {
            for (Annotation annotation : methodAnnotations) {
                if ((annotation instanceof ApiParam) && StringUtils.hasText(((ApiParam) annotation).defaultValue())) {
                    return ((ApiParam) annotation).defaultValue();
                }
            }
            return null;
        }
        return null;
    }
}
