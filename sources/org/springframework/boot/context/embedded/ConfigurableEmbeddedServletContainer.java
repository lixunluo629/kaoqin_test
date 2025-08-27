package org.springframework.boot.context.embedded;

import java.io.File;
import java.net.InetAddress;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.springframework.boot.web.servlet.ErrorPage;
import org.springframework.boot.web.servlet.ErrorPageRegistry;
import org.springframework.boot.web.servlet.ServletContextInitializer;

/* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/context/embedded/ConfigurableEmbeddedServletContainer.class */
public interface ConfigurableEmbeddedServletContainer extends ErrorPageRegistry {
    void setContextPath(String str);

    void setDisplayName(String str);

    void setPort(int i);

    void setSessionTimeout(int i);

    void setSessionTimeout(int i, TimeUnit timeUnit);

    void setPersistSession(boolean z);

    void setSessionStoreDir(File file);

    void setAddress(InetAddress inetAddress);

    void setRegisterDefaultServlet(boolean z);

    void setErrorPages(Set<? extends ErrorPage> set);

    void setMimeMappings(MimeMappings mimeMappings);

    void setDocumentRoot(File file);

    void setInitializers(List<? extends ServletContextInitializer> list);

    void addInitializers(ServletContextInitializer... servletContextInitializerArr);

    void setSsl(Ssl ssl);

    void setSslStoreProvider(SslStoreProvider sslStoreProvider);

    void setJspServlet(JspServlet jspServlet);

    void setCompression(Compression compression);

    void setServerHeader(String str);

    void setLocaleCharsetMappings(Map<Locale, Charset> map);
}
