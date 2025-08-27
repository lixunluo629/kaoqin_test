package javax.servlet;

import java.util.Collection;
import java.util.EnumSet;
import javax.servlet.Registration;

/* loaded from: tomcat-embed-core-8.5.43.jar:javax/servlet/FilterRegistration.class */
public interface FilterRegistration extends Registration {

    /* loaded from: tomcat-embed-core-8.5.43.jar:javax/servlet/FilterRegistration$Dynamic.class */
    public interface Dynamic extends FilterRegistration, Registration.Dynamic {
    }

    void addMappingForServletNames(EnumSet<DispatcherType> enumSet, boolean z, String... strArr);

    Collection<String> getServletNameMappings();

    void addMappingForUrlPatterns(EnumSet<DispatcherType> enumSet, boolean z, String... strArr);

    Collection<String> getUrlPatternMappings();
}
