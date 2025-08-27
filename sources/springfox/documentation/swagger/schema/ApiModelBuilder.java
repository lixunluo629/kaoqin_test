package springfox.documentation.swagger.schema;

import com.fasterxml.classmate.TypeResolver;
import io.swagger.annotations.ApiModel;
import java.lang.reflect.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.schema.ModelBuilderPlugin;
import springfox.documentation.spi.schema.contexts.ModelContext;
import springfox.documentation.swagger.common.SwaggerPluginSupport;

@Component
@Order(SwaggerPluginSupport.SWAGGER_PLUGIN_ORDER)
/* loaded from: springfox-swagger-common-2.2.2.jar:springfox/documentation/swagger/schema/ApiModelBuilder.class */
public class ApiModelBuilder implements ModelBuilderPlugin {
    private final TypeResolver typeResolver;

    @Autowired
    public ApiModelBuilder(TypeResolver typeResolver) {
        this.typeResolver = typeResolver;
    }

    @Override // springfox.documentation.spi.schema.ModelBuilderPlugin
    public void apply(ModelContext context) {
        ApiModel annotation = (ApiModel) AnnotationUtils.findAnnotation(forClass(context), ApiModel.class);
        if (annotation != null) {
            context.getBuilder().description(annotation.description());
        }
    }

    private Class<?> forClass(ModelContext context) {
        return this.typeResolver.resolve(context.getType(), new Type[0]).getErasedType();
    }

    @Override // org.springframework.plugin.core.Plugin
    public boolean supports(DocumentationType delimiter) {
        return SwaggerPluginSupport.pluginDoesApply(delimiter);
    }
}
