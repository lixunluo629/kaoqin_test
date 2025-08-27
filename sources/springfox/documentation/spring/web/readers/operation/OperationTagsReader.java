package springfox.documentation.spring.web.readers.operation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.OperationBuilderPlugin;
import springfox.documentation.spi.service.contexts.OperationContext;

@Component
@Order(Integer.MIN_VALUE)
/* loaded from: springfox-spring-web-2.2.2.jar:springfox/documentation/spring/web/readers/operation/OperationTagsReader.class */
public class OperationTagsReader implements OperationBuilderPlugin {
    private final DefaultTagsProvider tagsProvider;

    @Autowired
    public OperationTagsReader(DefaultTagsProvider tagsProvider) {
        this.tagsProvider = tagsProvider;
    }

    @Override // springfox.documentation.spi.service.OperationBuilderPlugin
    public void apply(OperationContext context) {
        context.operationBuilder().tags(this.tagsProvider.tags(context));
    }

    @Override // org.springframework.plugin.core.Plugin
    public boolean supports(DocumentationType delimiter) {
        return true;
    }
}
