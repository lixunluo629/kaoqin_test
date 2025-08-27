package springfox.documentation.spring.web.paths;

import com.google.common.base.Function;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import springfox.documentation.service.PathDecorator;
import springfox.documentation.spi.service.contexts.DocumentationContext;
import springfox.documentation.spi.service.contexts.PathContext;

@Component
@Order(-2147483628)
/* loaded from: springfox-spring-web-2.2.2.jar:springfox/documentation/spring/web/paths/PathSanitizer.class */
class PathSanitizer implements PathDecorator {
    PathSanitizer() {
    }

    @Override // springfox.documentation.service.PathDecorator
    public Function<String, String> decorator(PathContext context) {
        return new Function<String, String>() { // from class: springfox.documentation.spring.web.paths.PathSanitizer.1
            @Override // com.google.common.base.Function
            public String apply(String input) {
                return Paths.removeAdjacentForwardSlashes(Paths.sanitizeRequestMappingPattern(input));
            }
        };
    }

    @Override // org.springframework.plugin.core.Plugin
    public boolean supports(DocumentationContext delimiter) {
        return true;
    }
}
