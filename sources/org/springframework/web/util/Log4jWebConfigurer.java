package org.springframework.web.util;

import java.io.FileNotFoundException;
import javax.servlet.ServletContext;
import javax.xml.parsers.FactoryConfigurationError;
import org.springframework.util.Log4jConfigurer;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;

@Deprecated
/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/web/util/Log4jWebConfigurer.class */
public abstract class Log4jWebConfigurer {
    public static final String CONFIG_LOCATION_PARAM = "log4jConfigLocation";
    public static final String REFRESH_INTERVAL_PARAM = "log4jRefreshInterval";
    public static final String EXPOSE_WEB_APP_ROOT_PARAM = "log4jExposeWebAppRoot";

    public static void initLogging(ServletContext servletContext) throws IllegalStateException, NumberFormatException, FactoryConfigurationError {
        if (exposeWebAppRoot(servletContext)) {
            WebUtils.setWebAppRootSystemProperty(servletContext);
        }
        String location = servletContext.getInitParameter(CONFIG_LOCATION_PARAM);
        if (location != null) {
            try {
                String location2 = ServletContextPropertyUtils.resolvePlaceholders(location, servletContext);
                if (!ResourceUtils.isUrl(location2)) {
                    location2 = WebUtils.getRealPath(servletContext, location2);
                }
                servletContext.log("Initializing log4j from [" + location2 + "]");
                String intervalString = servletContext.getInitParameter(REFRESH_INTERVAL_PARAM);
                if (StringUtils.hasText(intervalString)) {
                    try {
                        long refreshInterval = Long.parseLong(intervalString);
                        Log4jConfigurer.initLogging(location2, refreshInterval);
                    } catch (NumberFormatException ex) {
                        throw new IllegalArgumentException("Invalid 'log4jRefreshInterval' parameter: " + ex.getMessage());
                    }
                } else {
                    Log4jConfigurer.initLogging(location2);
                }
            } catch (FileNotFoundException ex2) {
                throw new IllegalArgumentException("Invalid 'log4jConfigLocation' parameter: " + ex2.getMessage());
            }
        }
    }

    public static void shutdownLogging(ServletContext servletContext) {
        servletContext.log("Shutting down log4j");
        try {
            Log4jConfigurer.shutdownLogging();
        } finally {
            if (exposeWebAppRoot(servletContext)) {
                WebUtils.removeWebAppRootSystemProperty(servletContext);
            }
        }
    }

    private static boolean exposeWebAppRoot(ServletContext servletContext) {
        String exposeWebAppRootParam = servletContext.getInitParameter(EXPOSE_WEB_APP_ROOT_PARAM);
        return exposeWebAppRootParam == null || Boolean.valueOf(exposeWebAppRootParam).booleanValue();
    }
}
