package org.springframework.web.servlet.resource;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.Resource;

/* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/resource/AbstractResourceResolver.class */
public abstract class AbstractResourceResolver implements ResourceResolver {
    protected final Log logger = LogFactory.getLog(getClass());

    protected abstract Resource resolveResourceInternal(HttpServletRequest httpServletRequest, String str, List<? extends Resource> list, ResourceResolverChain resourceResolverChain);

    protected abstract String resolveUrlPathInternal(String str, List<? extends Resource> list, ResourceResolverChain resourceResolverChain);

    @Override // org.springframework.web.servlet.resource.ResourceResolver
    public Resource resolveResource(HttpServletRequest request, String requestPath, List<? extends Resource> locations, ResourceResolverChain chain) {
        if (this.logger.isTraceEnabled()) {
            this.logger.trace("Resolving resource for request path \"" + requestPath + SymbolConstants.QUOTES_SYMBOL);
        }
        return resolveResourceInternal(request, requestPath, locations, chain);
    }

    @Override // org.springframework.web.servlet.resource.ResourceResolver
    public String resolveUrlPath(String resourceUrlPath, List<? extends Resource> locations, ResourceResolverChain chain) {
        if (this.logger.isTraceEnabled()) {
            this.logger.trace("Resolving public URL for resource path \"" + resourceUrlPath + SymbolConstants.QUOTES_SYMBOL);
        }
        return resolveUrlPathInternal(resourceUrlPath, locations, chain);
    }
}
