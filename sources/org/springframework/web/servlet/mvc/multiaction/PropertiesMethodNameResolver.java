package org.springframework.web.servlet.mvc.multiaction;

import java.util.Enumeration;
import java.util.Properties;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.Assert;
import org.springframework.util.PathMatcher;

@Deprecated
/* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/mvc/multiaction/PropertiesMethodNameResolver.class */
public class PropertiesMethodNameResolver extends AbstractUrlMethodNameResolver implements InitializingBean {
    private Properties mappings;
    private PathMatcher pathMatcher = new AntPathMatcher();

    public void setMappings(Properties mappings) {
        this.mappings = mappings;
    }

    public void setPathMatcher(PathMatcher pathMatcher) {
        Assert.notNull(pathMatcher, "PathMatcher must not be null");
        this.pathMatcher = pathMatcher;
    }

    @Override // org.springframework.beans.factory.InitializingBean
    public void afterPropertiesSet() {
        if (this.mappings == null || this.mappings.isEmpty()) {
            throw new IllegalArgumentException("'mappings' property is required");
        }
    }

    @Override // org.springframework.web.servlet.mvc.multiaction.AbstractUrlMethodNameResolver
    protected String getHandlerMethodNameForUrlPath(String urlPath) {
        String methodName = this.mappings.getProperty(urlPath);
        if (methodName != null) {
            return methodName;
        }
        Enumeration<?> propNames = this.mappings.propertyNames();
        while (propNames.hasMoreElements()) {
            String registeredPath = (String) propNames.nextElement();
            if (this.pathMatcher.match(registeredPath, urlPath)) {
                return (String) this.mappings.get(registeredPath);
            }
        }
        return null;
    }
}
