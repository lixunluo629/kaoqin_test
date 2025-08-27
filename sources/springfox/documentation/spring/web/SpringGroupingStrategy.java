package springfox.documentation.spring.web;

import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.base.Strings;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Sets;
import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.util.Arrays;
import java.util.Set;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import springfox.documentation.service.ResourceGroup;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.ResourceGroupingStrategy;
import springfox.documentation.spring.web.paths.Paths;

/* loaded from: springfox-spring-web-2.2.2.jar:springfox/documentation/spring/web/SpringGroupingStrategy.class */
public class SpringGroupingStrategy implements ResourceGroupingStrategy {
    @Override // springfox.documentation.spi.service.ResourceGroupingStrategy
    public Set<ResourceGroup> getResourceGroups(RequestMappingInfo requestMappingInfo, HandlerMethod handlerMethod) {
        return groups(handlerMethod);
    }

    @Override // springfox.documentation.spi.service.ResourceGroupingStrategy
    public String getResourceDescription(RequestMappingInfo requestMappingInfo, HandlerMethod handlerMethod) {
        return getDescription(handlerMethod.getBeanType());
    }

    @Override // springfox.documentation.spi.service.ResourceGroupingStrategy
    public Integer getResourcePosition(RequestMappingInfo requestMappingInfo, HandlerMethod handlerMethod) {
        return 0;
    }

    @Override // org.springframework.plugin.core.Plugin
    public boolean supports(DocumentationType delimiter) {
        return true;
    }

    private Set<ResourceGroup> groups(HandlerMethod handlerMethod) {
        Class<?> controllerClass = handlerMethod.getBeanType();
        String defaultGroup = String.format("%s", Paths.splitCamelCase(controllerClass.getSimpleName(), "-"));
        Optional<RequestMapping> requestMapping = Optional.fromNullable(AnnotationUtils.findAnnotation(controllerClass, RequestMapping.class));
        if (requestMapping.isPresent()) {
            Set<ResourceGroup> groups = Sets.newHashSet();
            Iterable<String> groupNames = FluentIterable.from(Arrays.asList(requestMapping.get().value())).filter(notNullOrEmpty());
            for (String each : groupNames) {
                String groupName = Paths.maybeChompLeadingSlash(Paths.firstPathSegment(each));
                groups.add(new ResourceGroup(groupName, handlerMethod.getBeanType()));
            }
            if (groups.size() > 0) {
                return groups;
            }
        }
        return Sets.newHashSet(new ResourceGroup(Paths.maybeChompLeadingSlash(defaultGroup.toLowerCase()), handlerMethod.getBeanType()));
    }

    private Predicate<String> notNullOrEmpty() {
        return new Predicate<String>() { // from class: springfox.documentation.spring.web.SpringGroupingStrategy.1
            @Override // com.google.common.base.Predicate
            public boolean apply(String input) {
                return !Strings.isNullOrEmpty(input);
            }
        };
    }

    private String getDescription(Class<?> controllerClass) {
        return Paths.splitCamelCase(controllerClass.getSimpleName(), SymbolConstants.SPACE_SYMBOL);
    }
}
