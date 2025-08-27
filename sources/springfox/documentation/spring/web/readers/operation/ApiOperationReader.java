package springfox.documentation.spring.web.readers.operation;

import com.google.common.collect.Lists;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.condition.RequestMethodsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import springfox.documentation.OperationNameGenerator;
import springfox.documentation.annotations.Cacheable;
import springfox.documentation.builders.OperationBuilder;
import springfox.documentation.service.Operation;
import springfox.documentation.spi.service.contexts.OperationContext;
import springfox.documentation.spi.service.contexts.RequestMappingContext;
import springfox.documentation.spring.web.OperationsKeyGenerator;
import springfox.documentation.spring.web.plugins.DocumentationPluginsManager;

@Component
/* loaded from: springfox-spring-web-2.2.2.jar:springfox/documentation/spring/web/readers/operation/ApiOperationReader.class */
public class ApiOperationReader {
    private static final Set<RequestMethod> allRequestMethods = new LinkedHashSet(Arrays.asList(RequestMethod.values()));
    private final DocumentationPluginsManager pluginsManager;
    private final OperationNameGenerator nameGenerator;

    @Autowired
    public ApiOperationReader(DocumentationPluginsManager pluginsManager, OperationNameGenerator nameGenerator) {
        this.pluginsManager = pluginsManager;
        this.nameGenerator = nameGenerator;
    }

    @Cacheable(value = "operations", keyGenerator = OperationsKeyGenerator.class)
    public List<Operation> read(RequestMappingContext outerContext) {
        RequestMappingInfo requestMappingInfo = outerContext.getRequestMappingInfo();
        String requestMappingPattern = outerContext.getRequestMappingPattern();
        RequestMethodsRequestCondition requestMethodsRequestCondition = requestMappingInfo.getMethodsCondition();
        List<Operation> operations = Lists.newArrayList();
        Set<RequestMethod> requestMethods = requestMethodsRequestCondition.getMethods();
        Set<RequestMethod> supportedMethods = supportedMethods(requestMethods);
        Integer currentCount = 0;
        for (RequestMethod httpRequestMethod : supportedMethods) {
            OperationContext operationContext = new OperationContext(new OperationBuilder(this.nameGenerator), httpRequestMethod, outerContext.getHandlerMethod(), currentCount.intValue(), requestMappingInfo, outerContext.getDocumentationContext(), requestMappingPattern);
            Operation operation = this.pluginsManager.operation(operationContext);
            if (!operation.isHidden()) {
                operations.add(operation);
                currentCount = Integer.valueOf(currentCount.intValue() + 1);
            }
        }
        Collections.sort(operations, outerContext.operationOrdering());
        return operations;
    }

    private Set<RequestMethod> supportedMethods(Set<RequestMethod> requestMethods) {
        return (requestMethods == null || requestMethods.isEmpty()) ? allRequestMethods : requestMethods;
    }
}
