package javax.security.auth.message.config;

import java.util.Map;
import javax.security.auth.Subject;
import javax.security.auth.message.AuthException;

/* loaded from: tomcat-embed-core-8.5.43.jar:javax/security/auth/message/config/ClientAuthConfig.class */
public interface ClientAuthConfig extends AuthConfig {
    ClientAuthContext getAuthContext(String str, Subject subject, Map map) throws AuthException;
}
