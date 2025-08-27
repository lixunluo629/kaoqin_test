package org.springframework.web.servlet.mvc.support;

import java.util.ArrayList;
import java.util.List;
import org.springframework.util.StringUtils;

@Deprecated
/* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/mvc/support/ControllerBeanNameHandlerMapping.class */
public class ControllerBeanNameHandlerMapping extends AbstractControllerUrlHandlerMapping {
    private String urlPrefix = "";
    private String urlSuffix = "";

    public void setUrlPrefix(String urlPrefix) {
        this.urlPrefix = urlPrefix != null ? urlPrefix : "";
    }

    public void setUrlSuffix(String urlSuffix) {
        this.urlSuffix = urlSuffix != null ? urlSuffix : "";
    }

    @Override // org.springframework.web.servlet.mvc.support.AbstractControllerUrlHandlerMapping
    protected String[] buildUrlsForHandler(String beanName, Class<?> beanClass) {
        List<String> urls = new ArrayList<>();
        urls.add(generatePathMapping(beanName));
        String[] aliases = getApplicationContext().getAliases(beanName);
        for (String alias : aliases) {
            urls.add(generatePathMapping(alias));
        }
        return StringUtils.toStringArray(urls);
    }

    protected String generatePathMapping(String beanName) {
        String name = beanName.startsWith("/") ? beanName : "/" + beanName;
        StringBuilder path = new StringBuilder();
        if (!name.startsWith(this.urlPrefix)) {
            path.append(this.urlPrefix);
        }
        path.append(name);
        if (!name.endsWith(this.urlSuffix)) {
            path.append(this.urlSuffix);
        }
        return path.toString();
    }
}
