package springfox.documentation.spring.web.paths;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import java.util.Set;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import springfox.documentation.service.Parameter;
import springfox.documentation.service.PathDecorator;
import springfox.documentation.spi.service.contexts.DocumentationContext;
import springfox.documentation.spi.service.contexts.PathContext;

@Component
@Order(-2147483588)
/* loaded from: springfox-spring-web-2.2.2.jar:springfox/documentation/spring/web/paths/QueryStringUriTemplateDecorator.class */
class QueryStringUriTemplateDecorator implements PathDecorator {
    QueryStringUriTemplateDecorator() {
    }

    @Override // springfox.documentation.service.PathDecorator
    public Function<String, String> decorator(final PathContext context) {
        return new Function<String, String>() { // from class: springfox.documentation.spring.web.paths.QueryStringUriTemplateDecorator.1
            @Override // com.google.common.base.Function
            public String apply(String input) {
                Set<String> expressions = QueryStringUriTemplateDecorator.this.queryParamNames(context);
                String prefix = QueryStringUriTemplateDecorator.this.requiresContinuation(input) ? "{&" : "{?";
                String queryTemplate = Joiner.on(',').join(expressions);
                if (queryTemplate.length() == 0) {
                    return input;
                }
                return String.format("%s%s%s}", input, prefix, queryTemplate);
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean requiresContinuation(String url) {
        return url.contains("?");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Set<String> queryParamNames(PathContext context) {
        return FluentIterable.from(context.getParameters()).filter(queryStringParams()).transform(paramName()).toSet();
    }

    private Predicate<Parameter> queryStringParams() {
        return new Predicate<Parameter>() { // from class: springfox.documentation.spring.web.paths.QueryStringUriTemplateDecorator.2
            @Override // com.google.common.base.Predicate
            public boolean apply(Parameter input) {
                return "query".equals(input.getParamType());
            }
        };
    }

    private Function<Parameter, String> paramName() {
        return new Function<Parameter, String>() { // from class: springfox.documentation.spring.web.paths.QueryStringUriTemplateDecorator.3
            @Override // com.google.common.base.Function
            public String apply(Parameter input) {
                return input.getName();
            }
        };
    }

    @Override // org.springframework.plugin.core.Plugin
    public boolean supports(DocumentationContext delimiter) {
        return delimiter.isUriTemplatesEnabled();
    }
}
