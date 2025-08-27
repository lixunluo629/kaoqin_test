package org.apache.tomcat.websocket;

import java.util.Iterator;
import java.util.ServiceLoader;

/* loaded from: tomcat-embed-websocket-8.5.43.jar:org/apache/tomcat/websocket/AuthenticatorFactory.class */
public class AuthenticatorFactory {
    public static Authenticator getAuthenticator(String authScheme) {
        Authenticator auth;
        switch (authScheme.toLowerCase()) {
            case "basic":
                auth = new BasicAuthenticator();
                break;
            case "digest":
                auth = new DigestAuthenticator();
                break;
            default:
                auth = loadAuthenticators(authScheme);
                break;
        }
        return auth;
    }

    private static Authenticator loadAuthenticators(String authScheme) {
        ServiceLoader<Authenticator> serviceLoader = ServiceLoader.load(Authenticator.class);
        Iterator<Authenticator> auths = serviceLoader.iterator();
        while (auths.hasNext()) {
            Authenticator auth = auths.next();
            if (auth.getSchemeName().equalsIgnoreCase(authScheme)) {
                return auth;
            }
        }
        return null;
    }
}
