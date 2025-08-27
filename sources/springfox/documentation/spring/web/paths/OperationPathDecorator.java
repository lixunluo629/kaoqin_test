package springfox.documentation.spring.web.paths;

import com.google.common.base.Function;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import springfox.documentation.service.PathDecorator;
import springfox.documentation.spi.service.contexts.DocumentationContext;
import springfox.documentation.spi.service.contexts.PathContext;

@Component
@Order
/* loaded from: springfox-spring-web-2.2.2.jar:springfox/documentation/spring/web/paths/OperationPathDecorator.class */
class OperationPathDecorator implements PathDecorator {
    OperationPathDecorator() {
    }

    @Override // springfox.documentation.service.PathDecorator
    public Function<String, String> decorator(final PathContext context) {
        return new Function<String, String>() { // from class: springfox.documentation.spring.web.paths.OperationPathDecorator.1
            @Override // com.google.common.base.Function
            public String apply(String input) {
                return context.pathProvider().getOperationPath(input);
            }
        };
    }

    @Override // org.springframework.plugin.core.Plugin
    public boolean supports(DocumentationContext delimiter) {
        return true;
    }
}
