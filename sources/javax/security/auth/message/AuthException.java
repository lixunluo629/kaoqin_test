package javax.security.auth.message;

import javax.security.auth.login.LoginException;

/* loaded from: tomcat-embed-core-8.5.43.jar:javax/security/auth/message/AuthException.class */
public class AuthException extends LoginException {
    private static final long serialVersionUID = -1156951780670243758L;

    public AuthException() {
    }

    public AuthException(String msg) {
        super(msg);
    }
}
