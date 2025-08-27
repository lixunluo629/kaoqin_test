package javax.servlet;

import java.util.Collection;
import java.util.Set;
import javax.servlet.Registration;

/* loaded from: tomcat-embed-core-8.5.43.jar:javax/servlet/ServletRegistration.class */
public interface ServletRegistration extends Registration {

    /* loaded from: tomcat-embed-core-8.5.43.jar:javax/servlet/ServletRegistration$Dynamic.class */
    public interface Dynamic extends ServletRegistration, Registration.Dynamic {
        void setLoadOnStartup(int i);

        void setMultipartConfig(MultipartConfigElement multipartConfigElement);

        void setRunAsRole(String str);

        Set<String> setServletSecurity(ServletSecurityElement servletSecurityElement);
    }

    Set<String> addMapping(String... strArr);

    Collection<String> getMappings();

    String getRunAsRole();
}
