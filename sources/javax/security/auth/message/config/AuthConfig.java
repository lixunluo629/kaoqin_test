package javax.security.auth.message.config;

import javax.security.auth.message.MessageInfo;

/* loaded from: tomcat-embed-core-8.5.43.jar:javax/security/auth/message/config/AuthConfig.class */
public interface AuthConfig {
    String getMessageLayer();

    String getAppContext();

    String getAuthContextID(MessageInfo messageInfo);

    void refresh();

    boolean isProtected();
}
