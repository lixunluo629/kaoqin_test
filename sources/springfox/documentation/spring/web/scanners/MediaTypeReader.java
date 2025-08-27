package springfox.documentation.spring.web.scanners;

import com.fasterxml.classmate.TypeResolver;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.service.ResolvedMethodParameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.ApiListingBuilderPlugin;
import springfox.documentation.spi.service.OperationBuilderPlugin;
import springfox.documentation.spi.service.contexts.ApiListingContext;
import springfox.documentation.spi.service.contexts.OperationContext;
import springfox.documentation.spring.web.readers.operation.HandlerMethodResolver;

@Component
@Order(Integer.MIN_VALUE)
/* loaded from: springfox-spring-web-2.2.2.jar:springfox/documentation/spring/web/scanners/MediaTypeReader.class */
public class MediaTypeReader implements OperationBuilderPlugin, ApiListingBuilderPlugin {
    private final TypeResolver typeResolver;

    @Autowired
    public MediaTypeReader(TypeResolver typeResolver) {
        this.typeResolver = typeResolver;
    }

    @Override // springfox.documentation.spi.service.OperationBuilderPlugin
    public void apply(OperationContext context) {
        Set<String> consumesList = toSet(context.consumes());
        Set<String> producesList = toSet(context.produces());
        if (handlerMethodHasFileParameter(context)) {
            consumesList = Sets.newHashSet("multipart/form-data");
        }
        if (producesList.isEmpty()) {
            producesList.add("*/*");
        }
        if (consumesList.isEmpty()) {
            consumesList.add("application/json");
        }
        context.operationBuilder().consumes(consumesList);
        context.operationBuilder().produces(producesList);
    }

    @Override // springfox.documentation.spi.service.ApiListingBuilderPlugin
    public void apply(ApiListingContext context) {
        RequestMapping annotation = (RequestMapping) AnnotationUtils.findAnnotation(context.getResourceGroup().getControllerClass(), RequestMapping.class);
        if (annotation != null) {
            context.apiListingBuilder().appendProduces(Lists.newArrayList(annotation.produces())).appendConsumes(Lists.newArrayList(annotation.consumes()));
        }
    }

    @Override // org.springframework.plugin.core.Plugin
    public boolean supports(DocumentationType delimiter) {
        return true;
    }

    private boolean handlerMethodHasFileParameter(OperationContext context) throws NegativeArraySizeException {
        HandlerMethodResolver handlerMethodResolver = new HandlerMethodResolver(this.typeResolver);
        List<ResolvedMethodParameter> methodParameters = handlerMethodResolver.methodParameters(context.getHandlerMethod());
        for (ResolvedMethodParameter resolvedMethodParameter : methodParameters) {
            if (MultipartFile.class.isAssignableFrom(resolvedMethodParameter.getResolvedParameterType().getErasedType())) {
                return true;
            }
        }
        return false;
    }

    private Set<String> toSet(Set<MediaType> mediaTypeSet) {
        Set<String> mediaTypes = Sets.newHashSet();
        for (MediaType mediaType : mediaTypeSet) {
            mediaTypes.add(mediaType.toString());
        }
        return mediaTypes;
    }
}
