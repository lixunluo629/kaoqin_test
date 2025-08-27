package javax.servlet.http;

/* loaded from: tomcat-embed-core-8.5.43.jar:javax/servlet/http/HttpUpgradeHandler.class */
public interface HttpUpgradeHandler {
    void init(WebConnection webConnection);

    void destroy();
}
