package springfox.documentation.swagger2.readers.parameter;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Optional;
import com.google.common.base.Strings;
import io.swagger.annotations.ApiParam;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.ParameterBuilderPlugin;
import springfox.documentation.spi.service.contexts.ParameterContext;
import springfox.documentation.swagger.common.SwaggerPluginSupport;
import springfox.documentation.swagger.readers.parameter.ParameterAnnotationReader;

@Component("swagger2ParameterNameReader")
@Order(SwaggerPluginSupport.SWAGGER_PLUGIN_ORDER)
/* loaded from: springfox-swagger2-2.2.2.jar:springfox/documentation/swagger2/readers/parameter/ParameterNameReader.class */
public class ParameterNameReader implements ParameterBuilderPlugin {
    private ParameterAnnotationReader annotations = new ParameterAnnotationReader();

    @Override // springfox.documentation.spi.service.ParameterBuilderPlugin
    public void apply(ParameterContext context) {
        MethodParameter methodParameter = context.methodParameter();
        Optional<ApiParam> apiParam = apiParam(methodParameter);
        if (apiParam.isPresent()) {
            context.parameterBuilder().name(Strings.emptyToNull(apiParam.get().name()));
        }
    }

    @VisibleForTesting
    Optional<ApiParam> apiParam(MethodParameter methodParameter) {
        return Optional.fromNullable(methodParameter.getParameterAnnotation(ApiParam.class)).or(this.annotations.fromHierarchy(methodParameter, ApiParam.class));
    }

    @Override // org.springframework.plugin.core.Plugin
    public boolean supports(DocumentationType delimiter) {
        return DocumentationType.SWAGGER_2.equals(delimiter);
    }
}
