package org.apache.commons.httpclient.auth;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import org.apache.commons.httpclient.params.HttpParams;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/* loaded from: commons-httpclient-3.1.jar:org/apache/commons/httpclient/auth/AuthChallengeProcessor.class */
public final class AuthChallengeProcessor {
    private static final Log LOG;
    private HttpParams params;
    static Class class$org$apache$commons$httpclient$auth$AuthChallengeProcessor;

    static Class class$(String x0) {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError(x1.getMessage());
        }
    }

    static {
        Class clsClass$;
        if (class$org$apache$commons$httpclient$auth$AuthChallengeProcessor == null) {
            clsClass$ = class$("org.apache.commons.httpclient.auth.AuthChallengeProcessor");
            class$org$apache$commons$httpclient$auth$AuthChallengeProcessor = clsClass$;
        } else {
            clsClass$ = class$org$apache$commons$httpclient$auth$AuthChallengeProcessor;
        }
        LOG = LogFactory.getLog(clsClass$);
    }

    public AuthChallengeProcessor(HttpParams params) {
        this.params = null;
        if (params == null) {
            throw new IllegalArgumentException("Parameter collection may not be null");
        }
        this.params = params;
    }

    public AuthScheme selectAuthScheme(Map challenges) throws AuthChallengeException {
        if (challenges == null) {
            throw new IllegalArgumentException("Challenge map may not be null");
        }
        Collection authPrefs = (Collection) this.params.getParameter(AuthPolicy.AUTH_SCHEME_PRIORITY);
        if (authPrefs == null || authPrefs.isEmpty()) {
            authPrefs = AuthPolicy.getDefaultAuthPrefs();
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug(new StringBuffer().append("Supported authentication schemes in the order of preference: ").append(authPrefs).toString());
        }
        AuthScheme authscheme = null;
        Iterator item = authPrefs.iterator();
        while (true) {
            if (!item.hasNext()) {
                break;
            }
            String id = (String) item.next();
            String challenge = (String) challenges.get(id.toLowerCase());
            if (challenge != null) {
                if (LOG.isInfoEnabled()) {
                    LOG.info(new StringBuffer().append(id).append(" authentication scheme selected").toString());
                }
                try {
                    authscheme = AuthPolicy.getAuthScheme(id);
                } catch (IllegalStateException e) {
                    throw new AuthChallengeException(e.getMessage());
                }
            } else if (LOG.isDebugEnabled()) {
                LOG.debug(new StringBuffer().append("Challenge for ").append(id).append(" authentication scheme not available").toString());
            }
        }
        if (authscheme == null) {
            throw new AuthChallengeException(new StringBuffer().append("Unable to respond to any of these challenges: ").append(challenges).toString());
        }
        return authscheme;
    }

    public AuthScheme processChallenge(AuthState state, Map challenges) throws MalformedChallengeException, AuthenticationException {
        if (state == null) {
            throw new IllegalArgumentException("Authentication state may not be null");
        }
        if (challenges == null) {
            throw new IllegalArgumentException("Challenge map may not be null");
        }
        if (state.isPreemptive() || state.getAuthScheme() == null) {
            state.setAuthScheme(selectAuthScheme(challenges));
        }
        AuthScheme authscheme = state.getAuthScheme();
        String id = authscheme.getSchemeName();
        if (LOG.isDebugEnabled()) {
            LOG.debug(new StringBuffer().append("Using authentication scheme: ").append(id).toString());
        }
        String challenge = (String) challenges.get(id.toLowerCase());
        if (challenge == null) {
            throw new AuthenticationException(new StringBuffer().append(id).append(" authorization challenge expected, but not found").toString());
        }
        authscheme.processChallenge(challenge);
        LOG.debug("Authorization challenge processed");
        return authscheme;
    }
}
