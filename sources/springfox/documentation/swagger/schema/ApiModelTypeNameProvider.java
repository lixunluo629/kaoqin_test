package springfox.documentation.swagger.schema;

import com.google.common.base.Optional;
import com.google.common.base.Strings;
import io.swagger.annotations.ApiModel;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import springfox.documentation.schema.DefaultTypeNameProvider;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.swagger.common.SwaggerPluginSupport;

@Component
@Order(SwaggerPluginSupport.SWAGGER_PLUGIN_ORDER)
/* loaded from: springfox-swagger-common-2.2.2.jar:springfox/documentation/swagger/schema/ApiModelTypeNameProvider.class */
public class ApiModelTypeNameProvider extends DefaultTypeNameProvider {
    @Override // springfox.documentation.schema.DefaultTypeNameProvider, springfox.documentation.spi.schema.TypeNameProviderPlugin
    public String nameFor(Class<?> type) {
        ApiModel annotation = (ApiModel) AnnotationUtils.findAnnotation(type, ApiModel.class);
        String defaultTypeName = super.nameFor(type);
        if (annotation != null) {
            return (String) Optional.fromNullable(Strings.emptyToNull(annotation.value())).or((Optional) defaultTypeName);
        }
        return defaultTypeName;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // springfox.documentation.schema.DefaultTypeNameProvider, org.springframework.plugin.core.Plugin
    public boolean supports(DocumentationType delimiter) {
        return SwaggerPluginSupport.pluginDoesApply(delimiter);
    }
}
