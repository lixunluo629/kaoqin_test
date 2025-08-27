package springfox.documentation.spring.web.scanners;

import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import springfox.documentation.service.ApiDescription;
import springfox.documentation.service.Operation;
import springfox.documentation.spi.service.contexts.ApiSelector;
import springfox.documentation.spi.service.contexts.PathContext;
import springfox.documentation.spi.service.contexts.RequestMappingContext;
import springfox.documentation.spring.web.plugins.DocumentationPluginsManager;
import springfox.documentation.spring.web.readers.operation.ApiOperationReader;

@Component
/* loaded from: springfox-spring-web-2.2.2.jar:springfox/documentation/spring/web/scanners/ApiDescriptionReader.class */
public class ApiDescriptionReader {
    private final ApiOperationReader operationReader;
    private final DocumentationPluginsManager pluginsManager;
    private final ApiDescriptionLookup lookup;

    @Autowired
    public ApiDescriptionReader(ApiOperationReader operationReader, DocumentationPluginsManager pluginsManager, ApiDescriptionLookup lookup) {
        this.operationReader = operationReader;
        this.pluginsManager = pluginsManager;
        this.lookup = lookup;
    }

    public List<ApiDescription> read(RequestMappingContext outerContext) {
        RequestMappingInfo requestMappingInfo = outerContext.getRequestMappingInfo();
        HandlerMethod handlerMethod = outerContext.getHandlerMethod();
        PatternsRequestCondition patternsCondition = requestMappingInfo.getPatternsCondition();
        ApiSelector selector = outerContext.getDocumentationContext().getApiSelector();
        List<ApiDescription> apiDescriptionList = Lists.newArrayList();
        for (String path : matchingPaths(selector, patternsCondition)) {
            String methodName = handlerMethod.getMethod().getName();
            RequestMappingContext operationContext = outerContext.copyPatternUsing(path);
            List<Operation> operations = this.operationReader.read(operationContext);
            operationContext.apiDescriptionBuilder().operations(operations).pathDecorator(this.pluginsManager.decorator(new PathContext(outerContext, FluentIterable.from(operations).first()))).path(path).description(methodName).hidden(false);
            ApiDescription apiDescription = operationContext.apiDescriptionBuilder().build();
            this.lookup.add(outerContext.getHandlerMethod().getMethod(), apiDescription);
            apiDescriptionList.add(apiDescription);
        }
        return apiDescriptionList;
    }

    private List<String> matchingPaths(ApiSelector selector, PatternsRequestCondition patternsCondition) {
        return Ordering.natural().sortedCopy(FluentIterable.from(patternsCondition.getPatterns()).filter(selector.getPathSelector()));
    }
}
