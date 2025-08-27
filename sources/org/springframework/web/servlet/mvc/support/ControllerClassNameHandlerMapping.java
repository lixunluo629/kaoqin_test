package org.springframework.web.servlet.mvc.support;

import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

@Deprecated
/* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/mvc/support/ControllerClassNameHandlerMapping.class */
public class ControllerClassNameHandlerMapping extends AbstractControllerUrlHandlerMapping {
    private static final String CONTROLLER_SUFFIX = "Controller";
    private boolean caseSensitive = false;
    private String pathPrefix;
    private String basePackage;

    public void setCaseSensitive(boolean caseSensitive) {
        this.caseSensitive = caseSensitive;
    }

    public void setPathPrefix(String prefixPath) {
        this.pathPrefix = prefixPath;
        if (StringUtils.hasLength(this.pathPrefix)) {
            if (!this.pathPrefix.startsWith("/")) {
                this.pathPrefix = "/" + this.pathPrefix;
            }
            if (this.pathPrefix.endsWith("/")) {
                this.pathPrefix = this.pathPrefix.substring(0, this.pathPrefix.length() - 1);
            }
        }
    }

    public void setBasePackage(String basePackage) {
        this.basePackage = basePackage;
        if (StringUtils.hasLength(this.basePackage) && !this.basePackage.endsWith(".")) {
            this.basePackage += ".";
        }
    }

    @Override // org.springframework.web.servlet.mvc.support.AbstractControllerUrlHandlerMapping
    protected String[] buildUrlsForHandler(String beanName, Class<?> beanClass) {
        return generatePathMappings(beanClass);
    }

    protected String[] generatePathMappings(Class<?> beanClass) {
        StringBuilder pathMapping = buildPathPrefix(beanClass);
        String className = ClassUtils.getShortName(beanClass);
        String path = className.endsWith(CONTROLLER_SUFFIX) ? className.substring(0, className.lastIndexOf(CONTROLLER_SUFFIX)) : className;
        if (path.length() > 0) {
            if (this.caseSensitive) {
                pathMapping.append(path.substring(0, 1).toLowerCase()).append(path.substring(1));
            } else {
                pathMapping.append(path.toLowerCase());
            }
        }
        if (isMultiActionControllerType(beanClass)) {
            return new String[]{pathMapping.toString(), pathMapping.toString() + ScriptUtils.DEFAULT_BLOCK_COMMENT_START_DELIMITER};
        }
        return new String[]{pathMapping.toString() + "*"};
    }

    private StringBuilder buildPathPrefix(Class<?> beanClass) {
        StringBuilder pathMapping = new StringBuilder();
        if (this.pathPrefix != null) {
            pathMapping.append(this.pathPrefix);
            pathMapping.append("/");
        } else {
            pathMapping.append("/");
        }
        if (this.basePackage != null) {
            String packageName = ClassUtils.getPackageName(beanClass);
            if (packageName.startsWith(this.basePackage)) {
                String subPackage = packageName.substring(this.basePackage.length()).replace('.', '/');
                pathMapping.append(this.caseSensitive ? subPackage : subPackage.toLowerCase());
                pathMapping.append("/");
            }
        }
        return pathMapping;
    }
}
