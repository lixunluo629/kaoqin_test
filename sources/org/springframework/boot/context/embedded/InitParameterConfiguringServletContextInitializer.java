package org.springframework.boot.context.embedded;

import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import org.springframework.boot.web.servlet.ServletContextInitializer;

/* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/context/embedded/InitParameterConfiguringServletContextInitializer.class */
public class InitParameterConfiguringServletContextInitializer implements ServletContextInitializer {
    private final Map<String, String> parameters;

    public InitParameterConfiguringServletContextInitializer(Map<String, String> parameters) {
        this.parameters = parameters;
    }

    @Override // org.springframework.boot.web.servlet.ServletContextInitializer
    public void onStartup(ServletContext servletContext) throws ServletException {
        for (Map.Entry<String, String> entry : this.parameters.entrySet()) {
            servletContext.setInitParameter(entry.getKey(), entry.getValue());
        }
    }
}
