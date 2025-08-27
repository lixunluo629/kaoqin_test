package org.apache.commons.httpclient.auth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/* loaded from: commons-httpclient-3.1.jar:org/apache/commons/httpclient/auth/AuthPolicy.class */
public abstract class AuthPolicy {
    private static final HashMap SCHEMES = new HashMap();
    private static final ArrayList SCHEME_LIST = new ArrayList();
    public static final String AUTH_SCHEME_PRIORITY = "http.auth.scheme-priority";
    public static final String NTLM = "NTLM";
    public static final String DIGEST = "Digest";
    public static final String BASIC = "Basic";
    protected static final Log LOG;
    static Class class$org$apache$commons$httpclient$auth$NTLMScheme;
    static Class class$org$apache$commons$httpclient$auth$DigestScheme;
    static Class class$org$apache$commons$httpclient$auth$BasicScheme;
    static Class class$org$apache$commons$httpclient$auth$AuthPolicy;

    static {
        Class clsClass$;
        Class clsClass$2;
        Class clsClass$3;
        Class clsClass$4;
        if (class$org$apache$commons$httpclient$auth$NTLMScheme == null) {
            clsClass$ = class$("org.apache.commons.httpclient.auth.NTLMScheme");
            class$org$apache$commons$httpclient$auth$NTLMScheme = clsClass$;
        } else {
            clsClass$ = class$org$apache$commons$httpclient$auth$NTLMScheme;
        }
        registerAuthScheme(NTLM, clsClass$);
        if (class$org$apache$commons$httpclient$auth$DigestScheme == null) {
            clsClass$2 = class$("org.apache.commons.httpclient.auth.DigestScheme");
            class$org$apache$commons$httpclient$auth$DigestScheme = clsClass$2;
        } else {
            clsClass$2 = class$org$apache$commons$httpclient$auth$DigestScheme;
        }
        registerAuthScheme(DIGEST, clsClass$2);
        if (class$org$apache$commons$httpclient$auth$BasicScheme == null) {
            clsClass$3 = class$("org.apache.commons.httpclient.auth.BasicScheme");
            class$org$apache$commons$httpclient$auth$BasicScheme = clsClass$3;
        } else {
            clsClass$3 = class$org$apache$commons$httpclient$auth$BasicScheme;
        }
        registerAuthScheme(BASIC, clsClass$3);
        if (class$org$apache$commons$httpclient$auth$AuthPolicy == null) {
            clsClass$4 = class$("org.apache.commons.httpclient.auth.AuthPolicy");
            class$org$apache$commons$httpclient$auth$AuthPolicy = clsClass$4;
        } else {
            clsClass$4 = class$org$apache$commons$httpclient$auth$AuthPolicy;
        }
        LOG = LogFactory.getLog(clsClass$4);
    }

    static Class class$(String x0) {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError(x1.getMessage());
        }
    }

    public static synchronized void registerAuthScheme(String id, Class clazz) {
        if (id == null) {
            throw new IllegalArgumentException("Id may not be null");
        }
        if (clazz == null) {
            throw new IllegalArgumentException("Authentication scheme class may not be null");
        }
        SCHEMES.put(id.toLowerCase(), clazz);
        SCHEME_LIST.add(id.toLowerCase());
    }

    public static synchronized void unregisterAuthScheme(String id) {
        if (id == null) {
            throw new IllegalArgumentException("Id may not be null");
        }
        SCHEMES.remove(id.toLowerCase());
        SCHEME_LIST.remove(id.toLowerCase());
    }

    public static synchronized AuthScheme getAuthScheme(String id) throws IllegalStateException {
        if (id == null) {
            throw new IllegalArgumentException("Id may not be null");
        }
        Class clazz = (Class) SCHEMES.get(id.toLowerCase());
        if (clazz != null) {
            try {
                return (AuthScheme) clazz.newInstance();
            } catch (Exception e) {
                LOG.error(new StringBuffer().append("Error initializing authentication scheme: ").append(id).toString(), e);
                throw new IllegalStateException(new StringBuffer().append(id).append(" authentication scheme implemented by ").append(clazz.getName()).append(" could not be initialized").toString());
            }
        }
        throw new IllegalStateException(new StringBuffer().append("Unsupported authentication scheme ").append(id).toString());
    }

    public static synchronized List getDefaultAuthPrefs() {
        return (List) SCHEME_LIST.clone();
    }
}
