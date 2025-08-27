package springfox.documentation.spring.web.paths;

import com.google.common.base.Optional;
import org.springframework.web.util.UriComponentsBuilder;
import springfox.documentation.service.PathAdjuster;
import springfox.documentation.spi.service.contexts.DocumentationContext;

/* loaded from: springfox-spring-web-2.2.2.jar:springfox/documentation/spring/web/paths/PathMappingAdjuster.class */
public class PathMappingAdjuster implements PathAdjuster {
    private final DocumentationContext context;

    public PathMappingAdjuster(DocumentationContext context) {
        this.context = context;
    }

    @Override // springfox.documentation.service.PathAdjuster
    public String adjustedPath(String path) {
        return UriComponentsBuilder.fromPath(this.context.getPathMapping().or((Optional<String>) "/")).path(path).build().toUriString();
    }
}
