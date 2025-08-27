package springfox.documentation.swagger.readers.operation;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import io.swagger.annotations.ApiOperation;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import springfox.documentation.service.Tags;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.OperationBuilderPlugin;
import springfox.documentation.spi.service.contexts.OperationContext;
import springfox.documentation.spring.web.readers.operation.DefaultTagsProvider;
import springfox.documentation.swagger.annotations.Annotations;
import springfox.documentation.swagger.common.SwaggerPluginSupport;

@Component
@Order(SwaggerPluginSupport.SWAGGER_PLUGIN_ORDER)
/* loaded from: springfox-swagger-common-2.2.2.jar:springfox/documentation/swagger/readers/operation/SwaggerOperationTagsReader.class */
public class SwaggerOperationTagsReader implements OperationBuilderPlugin {
    private final DefaultTagsProvider tagsProvider;

    @Autowired
    public SwaggerOperationTagsReader(DefaultTagsProvider tagsProvider) {
        this.tagsProvider = tagsProvider;
    }

    @Override // springfox.documentation.spi.service.OperationBuilderPlugin
    public void apply(OperationContext context) {
        Set<String> defaultTags = this.tagsProvider.tags(context);
        HandlerMethod handlerMethod = context.getHandlerMethod();
        Optional<ApiOperation> annotation = Annotations.findApiOperationAnnotation(handlerMethod.getMethod());
        Set<String> tags = (Set) annotation.transform(toTags()).or((Optional<V>) Sets.newHashSet());
        if (tags.isEmpty()) {
            context.operationBuilder().tags(defaultTags);
        } else {
            context.operationBuilder().tags(tags);
        }
    }

    private Function<ApiOperation, Set<String>> toTags() {
        return new Function<ApiOperation, Set<String>>() { // from class: springfox.documentation.swagger.readers.operation.SwaggerOperationTagsReader.1
            @Override // com.google.common.base.Function
            public Set<String> apply(ApiOperation input) {
                Set<String> tags = Sets.newTreeSet();
                tags.addAll(FluentIterable.from(Lists.newArrayList(input.tags())).filter(Tags.emptyTags()).toSet());
                return tags;
            }
        };
    }

    @Override // org.springframework.plugin.core.Plugin
    public boolean supports(DocumentationType delimiter) {
        return true;
    }
}
