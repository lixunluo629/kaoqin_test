package springfox.documentation.spi.service.contexts;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import java.util.List;
import springfox.documentation.PathProvider;
import springfox.documentation.annotations.Incubating;
import springfox.documentation.service.Operation;
import springfox.documentation.service.Parameter;

@Incubating("2.1.0")
/* loaded from: springfox-spi-2.2.2.jar:springfox/documentation/spi/service/contexts/PathContext.class */
public class PathContext {
    private final RequestMappingContext parent;
    private final Optional<Operation> operation;

    public PathContext(RequestMappingContext parent, Optional<Operation> operation) {
        this.parent = parent;
        this.operation = operation;
    }

    public DocumentationContext documentationContext() {
        return this.parent.getDocumentationContext();
    }

    public PathProvider pathProvider() {
        return this.parent.getDocumentationContext().getPathProvider();
    }

    public List<Parameter> getParameters() {
        if (this.operation.isPresent()) {
            return this.operation.get().getParameters();
        }
        return Lists.newArrayList();
    }
}
