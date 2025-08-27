package org.springframework.web.util;

import javax.servlet.ServletContext;
import org.springframework.util.PropertyPlaceholderHelper;

/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/web/util/ServletContextPropertyUtils.class */
public abstract class ServletContextPropertyUtils {
    private static final PropertyPlaceholderHelper strictHelper = new PropertyPlaceholderHelper("${", "}", ":", false);
    private static final PropertyPlaceholderHelper nonStrictHelper = new PropertyPlaceholderHelper("${", "}", ":", true);

    public static String resolvePlaceholders(String text, ServletContext servletContext) {
        return resolvePlaceholders(text, servletContext, false);
    }

    public static String resolvePlaceholders(String text, ServletContext servletContext, boolean ignoreUnresolvablePlaceholders) {
        PropertyPlaceholderHelper helper = ignoreUnresolvablePlaceholders ? nonStrictHelper : strictHelper;
        return helper.replacePlaceholders(text, new ServletContextPlaceholderResolver(text, servletContext));
    }

    /* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/web/util/ServletContextPropertyUtils$ServletContextPlaceholderResolver.class */
    private static class ServletContextPlaceholderResolver implements PropertyPlaceholderHelper.PlaceholderResolver {
        private final String text;
        private final ServletContext servletContext;

        public ServletContextPlaceholderResolver(String text, ServletContext servletContext) {
            this.text = text;
            this.servletContext = servletContext;
        }

        @Override // org.springframework.util.PropertyPlaceholderHelper.PlaceholderResolver
        public String resolvePlaceholder(String placeholderName) {
            try {
                String propVal = this.servletContext.getInitParameter(placeholderName);
                if (propVal == null) {
                    propVal = System.getProperty(placeholderName);
                    if (propVal == null) {
                        propVal = System.getenv(placeholderName);
                    }
                }
                return propVal;
            } catch (Throwable ex) {
                System.err.println("Could not resolve placeholder '" + placeholderName + "' in [" + this.text + "] as ServletContext init-parameter or system property: " + ex);
                return null;
            }
        }
    }
}
