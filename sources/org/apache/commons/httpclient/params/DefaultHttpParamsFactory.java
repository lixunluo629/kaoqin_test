package org.apache.commons.httpclient.params;

import java.util.ArrayList;
import java.util.Arrays;
import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpState;
import org.apache.commons.httpclient.HttpVersion;
import org.apache.commons.httpclient.cookie.CookiePolicy;

/* loaded from: commons-httpclient-3.1.jar:org/apache/commons/httpclient/params/DefaultHttpParamsFactory.class */
public class DefaultHttpParamsFactory implements HttpParamsFactory {
    private HttpParams httpParams;
    static Class class$org$apache$commons$httpclient$SimpleHttpConnectionManager;

    @Override // org.apache.commons.httpclient.params.HttpParamsFactory
    public synchronized HttpParams getDefaultParams() {
        if (this.httpParams == null) {
            this.httpParams = createParams();
        }
        return this.httpParams;
    }

    protected HttpParams createParams() {
        Class clsClass$;
        HttpClientParams params = new HttpClientParams(null);
        params.setParameter(HttpMethodParams.USER_AGENT, "Jakarta Commons-HttpClient/3.1");
        params.setVersion(HttpVersion.HTTP_1_1);
        if (class$org$apache$commons$httpclient$SimpleHttpConnectionManager == null) {
            clsClass$ = class$("org.apache.commons.httpclient.SimpleHttpConnectionManager");
            class$org$apache$commons$httpclient$SimpleHttpConnectionManager = clsClass$;
        } else {
            clsClass$ = class$org$apache$commons$httpclient$SimpleHttpConnectionManager;
        }
        params.setConnectionManagerClass(clsClass$);
        params.setCookiePolicy("default");
        params.setHttpElementCharset("US-ASCII");
        params.setContentCharset("ISO-8859-1");
        params.setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
        ArrayList datePatterns = new ArrayList();
        datePatterns.addAll(Arrays.asList("EEE, dd MMM yyyy HH:mm:ss zzz", "EEEE, dd-MMM-yy HH:mm:ss zzz", "EEE MMM d HH:mm:ss yyyy", "EEE, dd-MMM-yyyy HH:mm:ss z", "EEE, dd-MMM-yyyy HH-mm-ss z", "EEE, dd MMM yy HH:mm:ss z", "EEE dd-MMM-yyyy HH:mm:ss z", "EEE dd MMM yyyy HH:mm:ss z", "EEE dd-MMM-yyyy HH-mm-ss z", "EEE dd-MMM-yy HH:mm:ss z", "EEE dd MMM yy HH:mm:ss z", "EEE,dd-MMM-yy HH:mm:ss z", "EEE,dd-MMM-yyyy HH:mm:ss z", "EEE, dd-MM-yyyy HH:mm:ss z"));
        params.setParameter(HttpMethodParams.DATE_PATTERNS, datePatterns);
        String agent = null;
        try {
            agent = System.getProperty("httpclient.useragent");
        } catch (SecurityException e) {
        }
        if (agent != null) {
            params.setParameter(HttpMethodParams.USER_AGENT, agent);
        }
        String preemptiveDefault = null;
        try {
            preemptiveDefault = System.getProperty(HttpState.PREEMPTIVE_PROPERTY);
        } catch (SecurityException e2) {
        }
        if (preemptiveDefault != null) {
            String preemptiveDefault2 = preemptiveDefault.trim().toLowerCase();
            if (preemptiveDefault2.equals("true")) {
                params.setParameter(HttpClientParams.PREEMPTIVE_AUTHENTICATION, Boolean.TRUE);
            } else if (preemptiveDefault2.equals("false")) {
                params.setParameter(HttpClientParams.PREEMPTIVE_AUTHENTICATION, Boolean.FALSE);
            }
        }
        String defaultCookiePolicy = null;
        try {
            defaultCookiePolicy = System.getProperty("apache.commons.httpclient.cookiespec");
        } catch (SecurityException e3) {
        }
        if (defaultCookiePolicy != null) {
            if ("COMPATIBILITY".equalsIgnoreCase(defaultCookiePolicy)) {
                params.setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
            } else if ("NETSCAPE_DRAFT".equalsIgnoreCase(defaultCookiePolicy)) {
                params.setCookiePolicy(CookiePolicy.NETSCAPE);
            } else if ("RFC2109".equalsIgnoreCase(defaultCookiePolicy)) {
                params.setCookiePolicy(CookiePolicy.RFC_2109);
            }
        }
        return params;
    }

    static Class class$(String x0) {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError(x1.getMessage());
        }
    }
}
