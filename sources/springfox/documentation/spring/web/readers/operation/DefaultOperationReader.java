package springfox.documentation.spring.web.readers.operation;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.OperationBuilderPlugin;
import springfox.documentation.spi.service.contexts.OperationContext;

@Component
@Order(Integer.MIN_VALUE)
/* loaded from: springfox-spring-web-2.2.2.jar:springfox/documentation/spring/web/readers/operation/DefaultOperationReader.class */
public class DefaultOperationReader implements OperationBuilderPlugin {
    @Override // springfox.documentation.spi.service.OperationBuilderPlugin
    public void apply(OperationContext context) {
        String operationName = context.getHandlerMethod().getMethod().getName();
        context.operationBuilder().uniqueId(operationName).method(context.httpMethod()).position(context.operationIndex()).summary(operationName);
    }

    @Override // org.springframework.plugin.core.Plugin
    public boolean supports(DocumentationType delimiter) {
        return true;
    }
}
