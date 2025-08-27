package org.apache.tomcat.websocket;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import org.apache.tomcat.util.codec.binary.Base64;

/* loaded from: tomcat-embed-websocket-8.5.43.jar:org/apache/tomcat/websocket/BasicAuthenticator.class */
public class BasicAuthenticator extends Authenticator {
    public static final String schemeName = "basic";
    public static final String charsetparam = "charset";

    @Override // org.apache.tomcat.websocket.Authenticator
    public String getAuthorization(String requestUri, String WWWAuthenticate, Map<String, Object> userProperties) throws AuthenticationException {
        Charset charset;
        String userName = (String) userProperties.get(Constants.WS_AUTHENTICATION_USER_NAME);
        String password = (String) userProperties.get(Constants.WS_AUTHENTICATION_PASSWORD);
        if (userName == null || password == null) {
            throw new AuthenticationException("Failed to perform Basic authentication due to  missing user/password");
        }
        Map<String, String> wwwAuthenticate = parseWWWAuthenticateHeader(WWWAuthenticate);
        String userPass = userName + ":" + password;
        if (wwwAuthenticate.get("charset") != null && wwwAuthenticate.get("charset").equalsIgnoreCase("UTF-8")) {
            charset = StandardCharsets.UTF_8;
        } else {
            charset = StandardCharsets.ISO_8859_1;
        }
        String base64 = Base64.encodeBase64String(userPass.getBytes(charset));
        return " Basic " + base64;
    }

    @Override // org.apache.tomcat.websocket.Authenticator
    public String getSchemeName() {
        return "basic";
    }
}
