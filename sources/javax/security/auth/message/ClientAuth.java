package javax.security.auth.message;

import javax.security.auth.Subject;

/* loaded from: tomcat-embed-core-8.5.43.jar:javax/security/auth/message/ClientAuth.class */
public interface ClientAuth {
    AuthStatus secureRequest(MessageInfo messageInfo, Subject subject) throws AuthException;

    AuthStatus validateResponse(MessageInfo messageInfo, Subject subject, Subject subject2) throws AuthException;

    void cleanSubject(MessageInfo messageInfo, Subject subject) throws AuthException;
}
