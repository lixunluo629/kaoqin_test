package javax.security.auth.message;

import javax.security.auth.Subject;

/* loaded from: tomcat-embed-core-8.5.43.jar:javax/security/auth/message/ServerAuth.class */
public interface ServerAuth {
    AuthStatus validateRequest(MessageInfo messageInfo, Subject subject, Subject subject2) throws AuthException;

    AuthStatus secureResponse(MessageInfo messageInfo, Subject subject) throws AuthException;

    void cleanSubject(MessageInfo messageInfo, Subject subject) throws AuthException;
}
