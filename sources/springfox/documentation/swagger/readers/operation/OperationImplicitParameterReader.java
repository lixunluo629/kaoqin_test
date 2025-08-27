package springfox.documentation.swagger.readers.operation;

import com.google.common.collect.Lists;
import io.swagger.annotations.ApiImplicitParam;
import java.lang.reflect.Method;
import java.util.List;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.OperationBuilderPlugin;
import springfox.documentation.spi.service.contexts.OperationContext;
import springfox.documentation.swagger.common.SwaggerPluginSupport;
import springfox.documentation.swagger.schema.ApiModelProperties;

@Component
@Order(SwaggerPluginSupport.SWAGGER_PLUGIN_ORDER)
/* loaded from: springfox-swagger-common-2.2.2.jar:springfox/documentation/swagger/readers/operation/OperationImplicitParameterReader.class */
public class OperationImplicitParameterReader implements OperationBuilderPlugin {
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
        ApiImplicitParam annotation = (ApiImplicitParam) AnnotationUtils.findAnnotation(method, ApiImplicitParam.class);
        List<Parameter> parameters = Lists.newArrayList();
        if (null != annotation) {
            parameters.add(getImplicitParameter(annotation));
        }
        return parameters;
    }

    public static Parameter getImplicitParameter(ApiImplicitParam param) {
        return new ParameterBuilder().name(param.name()).description(param.value()).defaultValue(param.defaultValue()).required(param.required()).allowMultiple(param.allowMultiple()).modelRef(new ModelRef(param.dataType())).allowableValues(ApiModelProperties.allowableValueFromString(param.allowableValues())).parameterType(param.paramType()).parameterAccess(param.access()).build();
    }
}
