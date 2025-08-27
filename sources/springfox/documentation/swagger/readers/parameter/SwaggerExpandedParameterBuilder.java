package springfox.documentation.swagger.readers.parameter;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.AllowableListValues;
import springfox.documentation.service.AllowableValues;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.ExpandedParameterBuilderPlugin;
import springfox.documentation.spi.service.contexts.ParameterExpansionContext;
import springfox.documentation.swagger.annotations.Annotations;
import springfox.documentation.swagger.common.SwaggerPluginSupport;
import springfox.documentation.swagger.schema.ApiModelProperties;

@Component
@Order(SwaggerPluginSupport.SWAGGER_PLUGIN_ORDER)
/* loaded from: springfox-swagger-common-2.2.2.jar:springfox/documentation/swagger/readers/parameter/SwaggerExpandedParameterBuilder.class */
public class SwaggerExpandedParameterBuilder implements ExpandedParameterBuilderPlugin {
    @Override // springfox.documentation.spi.service.ExpandedParameterBuilderPlugin
    public void apply(ParameterExpansionContext context) {
        Optional<ApiModelProperty> apiModelPropertyOptional = ApiModelProperties.findApiModePropertyAnnotation(context.getField());
        if (apiModelPropertyOptional.isPresent()) {
            fromApiModelProperty(context, apiModelPropertyOptional.get());
        }
        Optional<ApiParam> apiParamOptional = Annotations.findApiParamAnnotation(context.getField());
        if (apiParamOptional.isPresent()) {
            fromApiParam(context, apiParamOptional.get());
        }
    }

    @Override // org.springframework.plugin.core.Plugin
    public boolean supports(DocumentationType delimiter) {
        return SwaggerPluginSupport.pluginDoesApply(delimiter);
    }

    private void fromApiParam(ParameterExpansionContext context, ApiParam apiParam) {
        String allowableProperty = Strings.emptyToNull(apiParam.allowableValues());
        AllowableValues allowable = allowableValues(Optional.fromNullable(allowableProperty), context.getField());
        String name = Strings.isNullOrEmpty(context.getParentName()) ? context.getField().getName() : String.format("%s.%s", context.getParentName(), context.getField().getName());
        context.getParameterBuilder().name(name).description(apiParam.value()).defaultValue(apiParam.defaultValue()).required(apiParam.required()).allowMultiple(apiParam.allowMultiple()).modelRef(new ModelRef(context.getDataTypeName())).allowableValues(allowable).parameterType("query").parameterAccess(apiParam.access()).build();
    }

    private void fromApiModelProperty(ParameterExpansionContext context, ApiModelProperty apiModelProperty) {
        String allowableProperty = Strings.emptyToNull(apiModelProperty.allowableValues());
        AllowableValues allowable = allowableValues(Optional.fromNullable(allowableProperty), context.getField());
        String name = Strings.isNullOrEmpty(context.getParentName()) ? context.getField().getName() : String.format("%s.%s", context.getParentName(), context.getField().getName());
        context.getParameterBuilder().name(name).description(apiModelProperty.value()).defaultValue(null).required(apiModelProperty.required()).allowMultiple(Boolean.FALSE.booleanValue()).modelRef(new ModelRef(context.getDataTypeName())).allowableValues(allowable).parameterType("query").parameterAccess(apiModelProperty.access()).build();
    }

    private AllowableValues allowableValues(Optional<String> optionalAllowable, Field field) {
        AllowableValues allowable = null;
        if (field.getType().isEnum()) {
            allowable = new AllowableListValues(getEnumValues(field.getType()), "LIST");
        } else if (optionalAllowable.isPresent()) {
            allowable = ApiModelProperties.allowableValueFromString(optionalAllowable.get());
        }
        return allowable;
    }

    private List<String> getEnumValues(Class<?> subject) {
        return Lists.transform(Arrays.asList(subject.getEnumConstants()), new Function<Object, String>() { // from class: springfox.documentation.swagger.readers.parameter.SwaggerExpandedParameterBuilder.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // com.google.common.base.Function
            public String apply(Object input) {
                return input.toString();
            }
        });
    }
}
