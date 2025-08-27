package org.springframework.beans.factory.xml;

import java.io.FileNotFoundException;
import java.io.IOException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;

/* loaded from: spring-beans-4.3.25.RELEASE.jar:org/springframework/beans/factory/xml/BeansDtdResolver.class */
public class BeansDtdResolver implements EntityResolver {
    private static final String DTD_EXTENSION = ".dtd";
    private static final String DTD_FILENAME = "spring-beans-2.0";
    private static final String DTD_NAME = "spring-beans";
    private static final Log logger = LogFactory.getLog(BeansDtdResolver.class);

    @Override // org.xml.sax.EntityResolver
    public InputSource resolveEntity(String publicId, String systemId) throws IOException {
        if (logger.isTraceEnabled()) {
            logger.trace("Trying to resolve XML entity with public ID [" + publicId + "] and system ID [" + systemId + "]");
        }
        if (systemId != null && systemId.endsWith(".dtd")) {
            int lastPathSeparator = systemId.lastIndexOf(47);
            int dtdNameStart = systemId.indexOf(DTD_NAME, lastPathSeparator);
            if (dtdNameStart != -1) {
                if (logger.isTraceEnabled()) {
                    logger.trace("Trying to locate [spring-beans-2.0.dtd] in Spring jar on classpath");
                }
                try {
                    Resource resource = new ClassPathResource("spring-beans-2.0.dtd", getClass());
                    InputSource source = new InputSource(resource.getInputStream());
                    source.setPublicId(publicId);
                    source.setSystemId(systemId);
                    if (logger.isDebugEnabled()) {
                        logger.debug("Found beans DTD [" + systemId + "] in classpath: spring-beans-2.0.dtd");
                    }
                    return source;
                } catch (FileNotFoundException ex) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Could not resolve beans DTD [" + systemId + "]: not found in classpath", ex);
                        return null;
                    }
                    return null;
                }
            }
            return null;
        }
        return null;
    }

    public String toString() {
        return "EntityResolver for spring-beans DTD";
    }
}
