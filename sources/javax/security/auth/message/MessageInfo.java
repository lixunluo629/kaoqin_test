package javax.security.auth.message;

import java.util.Map;

/* loaded from: tomcat-embed-core-8.5.43.jar:javax/security/auth/message/MessageInfo.class */
public interface MessageInfo {
    Object getRequestMessage();

    Object getResponseMessage();

    void setRequestMessage(Object obj);

    void setResponseMessage(Object obj);

    Map getMap();
}
