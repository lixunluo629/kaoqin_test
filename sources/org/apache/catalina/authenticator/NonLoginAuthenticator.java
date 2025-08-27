package org.apache.catalina.authenticator;

import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import org.apache.catalina.connector.Request;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/catalina/authenticator/NonLoginAuthenticator.class */
public final class NonLoginAuthenticator extends AuthenticatorBase {
    @Override // org.apache.catalina.authenticator.AuthenticatorBase
    protected boolean doAuthenticate(Request request, HttpServletResponse response) throws IOException {
        if (checkForCachedAuthentication(request, response, true)) {
            if (this.cache) {
                request.getSessionInternal(true).setPrincipal(request.getPrincipal());
                return true;
            }
            return true;
        }
        if (this.containerLog.isDebugEnabled()) {
            this.containerLog.debug("User authenticated without any roles");
            return true;
        }
        return true;
    }

    @Override // org.apache.catalina.authenticator.AuthenticatorBase
    protected String getAuthMethod() {
        return org.apache.catalina.realm.Constants.NONE_TRANSPORT;
    }
}
