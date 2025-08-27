package springfox.documentation.spi.service;

import java.util.Set;
import org.springframework.plugin.core.Plugin;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import springfox.documentation.service.ResourceGroup;
import springfox.documentation.spi.DocumentationType;

/* loaded from: springfox-spi-2.2.2.jar:springfox/documentation/spi/service/ResourceGroupingStrategy.class */
public interface ResourceGroupingStrategy extends Plugin<DocumentationType> {
    Set<ResourceGroup> getResourceGroups(RequestMappingInfo requestMappingInfo, HandlerMethod handlerMethod);

    @Deprecated
    String getResourceDescription(RequestMappingInfo requestMappingInfo, HandlerMethod handlerMethod);

    @Deprecated
    Integer getResourcePosition(RequestMappingInfo requestMappingInfo, HandlerMethod handlerMethod);
}
