package springfox.documentation.swagger.web;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import io.swagger.annotations.Api;
import java.util.Set;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import springfox.documentation.service.Tags;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.ApiListingBuilderPlugin;
import springfox.documentation.spi.service.contexts.ApiListingContext;
import springfox.documentation.swagger.common.SwaggerPluginSupport;

@Component
/* loaded from: springfox-swagger-common-2.2.2.jar:springfox/documentation/swagger/web/ApiListingTagReader.class */
public class ApiListingTagReader implements ApiListingBuilderPlugin {
    @Override // springfox.documentation.spi.service.ApiListingBuilderPlugin
    public void apply(ApiListingContext apiListingContext) {
        Class<?> controllerClass = apiListingContext.getGroup().getControllerClass();
        Set<String> tagSet = (Set) Optional.fromNullable(AnnotationUtils.findAnnotation(controllerClass, Api.class)).transform(tags()).or((Optional) Sets.newTreeSet());
        if (tagSet.isEmpty()) {
            tagSet.add(apiListingContext.getGroup().getGroupName());
        }
        apiListingContext.apiListingBuilder().tags(tagSet);
    }

    private Function<Api, Set<String>> tags() {
        return new Function<Api, Set<String>>() { // from class: springfox.documentation.swagger.web.ApiListingTagReader.1
            @Override // com.google.common.base.Function
            public Set<String> apply(Api input) {
                return Sets.newTreeSet(FluentIterable.from(Lists.newArrayList(input.tags())).filter(Tags.emptyTags()).toSet());
            }
        };
    }

    @Override // org.springframework.plugin.core.Plugin
    public boolean supports(DocumentationType delimiter) {
        return SwaggerPluginSupport.pluginDoesApply(delimiter);
    }
}
