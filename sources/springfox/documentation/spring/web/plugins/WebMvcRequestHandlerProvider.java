package springfox.documentation.spring.web.plugins;

import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.RequestMappingInfoHandlerMapping;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.BuilderDefaults;
import springfox.documentation.spi.service.RequestHandlerProvider;
import springfox.documentation.spi.service.contexts.Orderings;

@Component
@Order(Integer.MIN_VALUE)
/* loaded from: springfox-spring-web-2.2.2.jar:springfox/documentation/spring/web/plugins/WebMvcRequestHandlerProvider.class */
public class WebMvcRequestHandlerProvider implements RequestHandlerProvider {
    private final List<RequestMappingInfoHandlerMapping> handlerMappings;

    @Autowired
    public WebMvcRequestHandlerProvider(List<RequestMappingInfoHandlerMapping> handlerMappings) {
        this.handlerMappings = handlerMappings;
    }

    @Override // springfox.documentation.spi.service.RequestHandlerProvider
    public List<RequestHandler> requestHandlers() {
        return Orderings.byPatternsCondition().sortedCopy(FluentIterable.from(BuilderDefaults.nullToEmptyList(this.handlerMappings)).transformAndConcat(toMappingEntries()).transform(toRequestHandler()));
    }

    private Function<? super RequestMappingInfoHandlerMapping, Iterable<Map.Entry<RequestMappingInfo, HandlerMethod>>> toMappingEntries() {
        return new Function<RequestMappingInfoHandlerMapping, Iterable<Map.Entry<RequestMappingInfo, HandlerMethod>>>() { // from class: springfox.documentation.spring.web.plugins.WebMvcRequestHandlerProvider.1
            @Override // com.google.common.base.Function
            public Iterable<Map.Entry<RequestMappingInfo, HandlerMethod>> apply(RequestMappingInfoHandlerMapping input) {
                return input.getHandlerMethods().entrySet();
            }
        };
    }

    private Function<Map.Entry<RequestMappingInfo, HandlerMethod>, RequestHandler> toRequestHandler() {
        return new Function<Map.Entry<RequestMappingInfo, HandlerMethod>, RequestHandler>() { // from class: springfox.documentation.spring.web.plugins.WebMvcRequestHandlerProvider.2
            @Override // com.google.common.base.Function
            public RequestHandler apply(Map.Entry<RequestMappingInfo, HandlerMethod> input) {
                return new RequestHandler(input.getKey(), input.getValue());
            }
        };
    }
}
