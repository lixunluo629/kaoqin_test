package org.springframework.web.servlet.view.tiles2;

import java.io.IOException;
import java.net.URL;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.servlet.ServletContext;
import org.apache.tiles.servlet.context.ServletTilesApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.context.support.ServletContextResourcePatternResolver;

@Deprecated
/* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/view/tiles2/SpringWildcardServletTilesApplicationContext.class */
public class SpringWildcardServletTilesApplicationContext extends ServletTilesApplicationContext {
    private final ResourcePatternResolver resolver;

    public SpringWildcardServletTilesApplicationContext(ServletContext servletContext) {
        super(servletContext);
        this.resolver = new ServletContextResourcePatternResolver(servletContext);
    }

    public URL getResource(String path) throws IOException {
        Set<URL> urlSet = getResources(path);
        if (!CollectionUtils.isEmpty(urlSet)) {
            return urlSet.iterator().next();
        }
        return null;
    }

    public Set<URL> getResources(String path) throws IOException {
        Set<URL> urlSet = null;
        Resource[] resources = this.resolver.getResources(path);
        if (!ObjectUtils.isEmpty((Object[]) resources)) {
            urlSet = new LinkedHashSet<>(resources.length);
            for (Resource resource : resources) {
                urlSet.add(resource.getURL());
            }
        }
        return urlSet;
    }
}
