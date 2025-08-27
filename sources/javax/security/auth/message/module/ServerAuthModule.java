package javax.security.auth.message.module;

import java.util.Map;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.message.AuthException;
import javax.security.auth.message.MessagePolicy;
import javax.security.auth.message.ServerAuth;

/* loaded from: tomcat-embed-core-8.5.43.jar:javax/security/auth/message/module/ServerAuthModule.class */
public interface ServerAuthModule extends ServerAuth {
    void initialize(MessagePolicy messagePolicy, MessagePolicy messagePolicy2, CallbackHandler callbackHandler, Map map) throws AuthException;

    Class[] getSupportedMessageTypes();
}
