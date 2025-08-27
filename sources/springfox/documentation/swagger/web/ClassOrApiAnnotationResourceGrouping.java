package springfox.documentation.swagger.web;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Strings;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Sets;
import com.moredian.onpremise.core.common.constants.SymbolConstants;
import io.swagger.annotations.Api;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import springfox.documentation.service.ResourceGroup;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.ResourceGroupingStrategy;
import springfox.documentation.spring.web.paths.Paths;
import springfox.documentation.swagger.common.SwaggerPluginSupport;

@Component
/* loaded from: springfox-swagger-common-2.2.2.jar:springfox/documentation/swagger/web/ClassOrApiAnnotationResourceGrouping.class */
public class ClassOrApiAnnotationResourceGrouping implements ResourceGroupingStrategy {
    private static final Logger LOG = LoggerFactory.getLogger((Class<?>) ClassOrApiAnnotationResourceGrouping.class);

    @Override // springfox.documentation.spi.service.ResourceGroupingStrategy
    public String getResourceDescription(RequestMappingInfo requestMappingInfo, HandlerMethod handlerMethod) {
        Class<?> controllerClass = handlerMethod.getBeanType();
        String className = Paths.splitCamelCase(controllerClass.getSimpleName(), SymbolConstants.SPACE_SYMBOL);
        return (String) Optional.fromNullable(Strings.emptyToNull(stripSlashes((String) ((Optional) extractAnnotation(controllerClass, descriptionOrValueExtractor())).or((Optional) "")))).or((Optional) className);
    }

    private String stripSlashes(String stringWithSlashes) {
        return stringWithSlashes.replace("/", "").replace("\\", "");
    }

    @Override // springfox.documentation.spi.service.ResourceGroupingStrategy
    public Integer getResourcePosition(RequestMappingInfo requestMappingInfo, HandlerMethod handlerMethod) {
        Class<?> controllerClass = handlerMethod.getBeanType();
        Api apiAnnotation = (Api) AnnotationUtils.findAnnotation(controllerClass, Api.class);
        if (null != apiAnnotation && StringUtils.hasText(apiAnnotation.value())) {
            return Integer.valueOf(apiAnnotation.position());
        }
        return 0;
    }

    @Override // springfox.documentation.spi.service.ResourceGroupingStrategy
    public Set<ResourceGroup> getResourceGroups(RequestMappingInfo requestMappingInfo, HandlerMethod handlerMethod) {
        return FluentIterable.from(groups(handlerMethod)).transform(toResourceGroup(requestMappingInfo, handlerMethod)).toSet();
    }

    private Set<String> groups(HandlerMethod handlerMethod) {
        Class<?> controllerClass = handlerMethod.getBeanType();
        String group = Paths.splitCamelCase(controllerClass.getSimpleName(), SymbolConstants.SPACE_SYMBOL);
        String apiValue = (String) Optional.fromNullable(AnnotationUtils.findAnnotation(controllerClass, Api.class)).transform(toApiValue()).or((Optional) "");
        return Strings.isNullOrEmpty(apiValue) ? Sets.newHashSet(normalize(group)) : Sets.newHashSet(normalize(apiValue));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String normalize(String tag) {
        return tag.toLowerCase().replaceAll(SymbolConstants.SPACE_SYMBOL, "-").replaceAll("/", "");
    }

    private Function<String, ResourceGroup> toResourceGroup(final RequestMappingInfo requestMappingInfo, final HandlerMethod handlerMethod) {
        return new Function<String, ResourceGroup>() { // from class: springfox.documentation.swagger.web.ClassOrApiAnnotationResourceGrouping.1
            @Override // com.google.common.base.Function
            public ResourceGroup apply(String group) {
                ClassOrApiAnnotationResourceGrouping.LOG.info("Group for method {} was {}", handlerMethod.getMethod().getName(), group);
                Integer position = ClassOrApiAnnotationResourceGrouping.this.getResourcePosition(requestMappingInfo, handlerMethod);
                return new ResourceGroup(group, handlerMethod.getBeanType(), position);
            }
        };
    }

    @Override // org.springframework.plugin.core.Plugin
    public boolean supports(DocumentationType delimiter) {
        return SwaggerPluginSupport.pluginDoesApply(delimiter);
    }

    private <T> T extractAnnotation(Class<?> controllerClass, Function<Api, T> annotationExtractor) {
        Api apiAnnotation = (Api) AnnotationUtils.findAnnotation(controllerClass, Api.class);
        return annotationExtractor.apply(apiAnnotation);
    }

    private Function<Api, Optional<String>> descriptionOrValueExtractor() {
        return new Function<Api, Optional<String>>() { // from class: springfox.documentation.swagger.web.ClassOrApiAnnotationResourceGrouping.2
            @Override // com.google.common.base.Function
            public Optional<String> apply(Api input) {
                return ((Optional) ClassOrApiAnnotationResourceGrouping.this.descriptionExtractor().apply(input)).or((Optional) ClassOrApiAnnotationResourceGrouping.this.valueExtractor().apply(input));
            }
        };
    }

    private Function<Api, String> toApiValue() {
        return new Function<Api, String>() { // from class: springfox.documentation.swagger.web.ClassOrApiAnnotationResourceGrouping.3
            @Override // com.google.common.base.Function
            public String apply(Api input) {
                return ClassOrApiAnnotationResourceGrouping.this.normalize(input.value());
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Function<Api, Optional<String>> descriptionExtractor() {
        return new Function<Api, Optional<String>>() { // from class: springfox.documentation.swagger.web.ClassOrApiAnnotationResourceGrouping.4
            @Override // com.google.common.base.Function
            public Optional<String> apply(Api input) {
                if (null != input) {
                    return Optional.fromNullable(Strings.emptyToNull(input.description()));
                }
                return Optional.absent();
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Function<Api, Optional<String>> valueExtractor() {
        return new Function<Api, Optional<String>>() { // from class: springfox.documentation.swagger.web.ClassOrApiAnnotationResourceGrouping.5
            @Override // com.google.common.base.Function
            public Optional<String> apply(Api input) {
                if (null != input) {
                    return Optional.fromNullable(Strings.emptyToNull(input.value()));
                }
                return Optional.absent();
            }
        };
    }
}
