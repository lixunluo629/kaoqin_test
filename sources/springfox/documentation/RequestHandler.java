package springfox.documentation;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;

/* loaded from: springfox-core-2.2.2.jar:springfox/documentation/RequestHandler.class */
public class RequestHandler {
    private final RequestMappingInfo requestMapping;
    private final HandlerMethod handlerMethod;

    public RequestHandler(RequestMappingInfo requestMapping, HandlerMethod handlerMethod) {
        this.requestMapping = requestMapping;
        this.handlerMethod = handlerMethod;
    }

    public RequestMappingInfo getRequestMapping() {
        return this.requestMapping;
    }

    public HandlerMethod getHandlerMethod() {
        return this.handlerMethod;
    }
}
