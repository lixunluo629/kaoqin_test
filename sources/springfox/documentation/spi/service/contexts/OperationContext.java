package springfox.documentation.spi.service.contexts;

import com.fasterxml.classmate.ResolvedType;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import java.util.List;
import java.util.Set;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import springfox.documentation.builders.BuilderDefaults;
import springfox.documentation.builders.OperationBuilder;
import springfox.documentation.service.Parameter;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.schema.AlternateTypeProvider;

/* loaded from: springfox-spi-2.2.2.jar:springfox/documentation/spi/service/contexts/OperationContext.class */
public class OperationContext {
    private final OperationBuilder operationBuilder;
    private final RequestMethod requestMethod;
    private final HandlerMethod handlerMethod;
    private final int operationIndex;
    private final RequestMappingInfo requestMappingInfo;
    private final DocumentationContext documentationContext;
    private final String requestMappingPattern;

    public OperationContext(OperationBuilder operationBuilder, RequestMethod requestMethod, HandlerMethod handlerMethod, int operationIndex, RequestMappingInfo requestMappingInfo, DocumentationContext documentationContext, String requestMappingPattern) {
        this.operationBuilder = operationBuilder;
        this.requestMethod = requestMethod;
        this.handlerMethod = handlerMethod;
        this.operationIndex = operationIndex;
        this.requestMappingInfo = requestMappingInfo;
        this.documentationContext = documentationContext;
        this.requestMappingPattern = requestMappingPattern;
    }

    public OperationBuilder operationBuilder() {
        return this.operationBuilder;
    }

    public HttpMethod httpMethod() {
        return HttpMethod.valueOf(this.requestMethod.toString());
    }

    public HandlerMethod getHandlerMethod() {
        return this.handlerMethod;
    }

    public int operationIndex() {
        return this.operationIndex;
    }

    public List<ResponseMessage> getGlobalResponseMessages(String forHttpMethod) {
        if (this.documentationContext.getGlobalResponseMessages().containsKey(RequestMethod.valueOf(forHttpMethod))) {
            return this.documentationContext.getGlobalResponseMessages().get(RequestMethod.valueOf(forHttpMethod));
        }
        return Lists.newArrayList();
    }

    public List<Parameter> getGlobalOperationParameters() {
        return BuilderDefaults.nullToEmptyList(this.documentationContext.getGlobalRequestParameters());
    }

    public Optional<SecurityContext> securityContext() {
        return Iterables.tryFind(this.documentationContext.getSecurityContexts(), pathMatches());
    }

    private Predicate<SecurityContext> pathMatches() {
        return new Predicate<SecurityContext>() { // from class: springfox.documentation.spi.service.contexts.OperationContext.1
            @Override // com.google.common.base.Predicate
            public boolean apply(SecurityContext input) {
                return input.securityForPath(OperationContext.this.requestMappingPattern) != null;
            }
        };
    }

    public String requestMappingPattern() {
        return this.requestMappingPattern;
    }

    public RequestMappingInfo getRequestMappingInfo() {
        return this.requestMappingInfo;
    }

    public DocumentationContext getDocumentationContext() {
        return this.documentationContext;
    }

    public DocumentationType getDocumentationType() {
        return this.documentationContext.getDocumentationType();
    }

    public AlternateTypeProvider getAlternateTypeProvider() {
        return this.documentationContext.getAlternateTypeProvider();
    }

    public ResolvedType alternateFor(ResolvedType resolved) {
        return getAlternateTypeProvider().alternateFor(resolved);
    }

    public Set<MediaType> produces() {
        return this.requestMappingInfo.getProducesCondition().getProducibleMediaTypes();
    }

    public Set<MediaType> consumes() {
        return this.requestMappingInfo.getConsumesCondition().getConsumableMediaTypes();
    }
}
