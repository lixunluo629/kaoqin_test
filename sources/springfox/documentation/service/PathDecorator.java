package springfox.documentation.service;

import com.google.common.base.Function;
import org.springframework.plugin.core.Plugin;
import springfox.documentation.annotations.Incubating;
import springfox.documentation.spi.service.contexts.DocumentationContext;
import springfox.documentation.spi.service.contexts.PathContext;

@Incubating("2.1.0")
/* loaded from: springfox-spi-2.2.2.jar:springfox/documentation/service/PathDecorator.class */
public interface PathDecorator extends Plugin<DocumentationContext> {
    Function<String, String> decorator(PathContext pathContext);
}
