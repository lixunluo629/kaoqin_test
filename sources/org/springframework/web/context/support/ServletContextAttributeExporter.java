package org.springframework.web.context.support;

import java.util.Map;
import javax.servlet.ServletContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.context.ServletContextAware;

/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/web/context/support/ServletContextAttributeExporter.class */
public class ServletContextAttributeExporter implements ServletContextAware {
    protected final Log logger = LogFactory.getLog(getClass());
    private Map<String, Object> attributes;

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override // org.springframework.web.context.ServletContextAware
    public void setServletContext(ServletContext servletContext) {
        if (this.attributes != null) {
            for (Map.Entry<String, Object> entry : this.attributes.entrySet()) {
                String attributeName = entry.getKey();
                if (this.logger.isWarnEnabled() && servletContext.getAttribute(attributeName) != null) {
                    this.logger.warn("Replacing existing ServletContext attribute with name '" + attributeName + "'");
                }
                servletContext.setAttribute(attributeName, entry.getValue());
                if (this.logger.isInfoEnabled()) {
                    this.logger.info("Exported ServletContext attribute with name '" + attributeName + "'");
                }
            }
        }
    }
}
