package springfox.documentation.swagger.schema;

import com.fasterxml.classmate.ResolvedType;
import com.google.common.base.Optional;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import springfox.documentation.schema.Annotations;
import springfox.documentation.service.AllowableValues;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.schema.ModelPropertyBuilderPlugin;
import springfox.documentation.spi.schema.contexts.ModelPropertyContext;
import springfox.documentation.swagger.common.SwaggerPluginSupport;

@Component
@Order(SwaggerPluginSupport.SWAGGER_PLUGIN_ORDER)
/* loaded from: springfox-swagger-common-2.2.2.jar:springfox/documentation/swagger/schema/ApiModelPropertyPropertyBuilder.class */
public class ApiModelPropertyPropertyBuilder implements ModelPropertyBuilderPlugin {
    @Override // springfox.documentation.spi.schema.ModelPropertyBuilderPlugin
    public void apply(ModelPropertyContext context) {
        Optional<ApiModelProperty> annotation = Optional.absent();
        if (context.getAnnotatedElement().isPresent()) {
            annotation = annotation.or(ApiModelProperties.findApiModePropertyAnnotation(context.getAnnotatedElement().get()));
        }
        if (context.getBeanPropertyDefinition().isPresent()) {
            annotation = annotation.or(Annotations.findPropertyAnnotation(context.getBeanPropertyDefinition().get(), ApiModelProperty.class));
        }
        if (annotation.isPresent()) {
            context.getBuilder().allowableValues((AllowableValues) annotation.transform(ApiModelProperties.toAllowableValues()).orNull()).required((Boolean) annotation.transform(ApiModelProperties.toIsRequired()).or((Optional) false)).readOnly((Boolean) annotation.transform(ApiModelProperties.toIsReadOnly()).or((Optional) false)).description((String) annotation.transform(ApiModelProperties.toDescription()).orNull()).isHidden((Boolean) annotation.transform(ApiModelProperties.toHidden()).or((Optional) false)).type((ResolvedType) annotation.transform(ApiModelProperties.toType(context.getResolver())).orNull());
        }
    }

    @Override // org.springframework.plugin.core.Plugin
    public boolean supports(DocumentationType delimiter) {
        return SwaggerPluginSupport.pluginDoesApply(delimiter);
    }
}
