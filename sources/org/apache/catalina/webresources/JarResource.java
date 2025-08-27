package org.apache.catalina.webresources;

import java.util.jar.JarEntry;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.springframework.util.ResourceUtils;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/catalina/webresources/JarResource.class */
public class JarResource extends AbstractSingleArchiveResource {
    private static final Log log = LogFactory.getLog((Class<?>) JarResource.class);

    public JarResource(AbstractArchiveResourceSet archiveResourceSet, String webAppPath, String baseUrl, JarEntry jarEntry) {
        super(archiveResourceSet, webAppPath, ResourceUtils.JAR_URL_PREFIX + baseUrl + ResourceUtils.JAR_URL_SEPARATOR, jarEntry, baseUrl);
    }

    @Override // org.apache.catalina.webresources.AbstractResource
    protected Log getLog() {
        return log;
    }
}
