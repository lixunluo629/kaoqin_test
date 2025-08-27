package springfox.documentation.swagger.readers.operation;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Sets;
import io.swagger.annotations.ApiOperation;
import java.util.Set;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.OperationBuilderPlugin;
import springfox.documentation.spi.service.contexts.OperationContext;
import springfox.documentation.swagger.common.SwaggerPluginSupport;

@Component
@Order(SwaggerPluginSupport.SWAGGER_PLUGIN_ORDER)
/* loaded from: springfox-swagger-common-2.2.2.jar:springfox/documentation/swagger/readers/operation/SwaggerMediaTypeReader.class */
public class SwaggerMediaTypeReader implements OperationBuilderPlugin {
    @Override // springfox.documentation.spi.service.OperationBuilderPlugin
    public void apply(OperationContext context) {
        ApiOperation annotation = (ApiOperation) AnnotationUtils.findAnnotation(context.getHandlerMethod().getMethod(), ApiOperation.class);
        if (null != annotation) {
            context.operationBuilder().consumes(asSet(Strings.nullToEmpty(annotation.consumes())));
            context.operationBuilder().produces(asSet(Strings.nullToEmpty(annotation.produces())));
        }
    }

    @Override // org.springframework.plugin.core.Plugin
    public boolean supports(DocumentationType delimiter) {
        return SwaggerPluginSupport.pluginDoesApply(delimiter);
    }

    private Set<String> asSet(String mediaTypes) {
        return Sets.newHashSet(Splitter.on(',').trimResults().omitEmptyStrings().splitToList(mediaTypes));
    }
}
