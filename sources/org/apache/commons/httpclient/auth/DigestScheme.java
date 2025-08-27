package org.apache.commons.httpclient.auth;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import org.apache.commons.codec.digest.MessageDigestAlgorithms;
import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpClientError;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.util.EncodingUtil;
import org.apache.commons.httpclient.util.ParameterFormatter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.naming.ResourceRef;

/* loaded from: commons-httpclient-3.1.jar:org/apache/commons/httpclient/auth/DigestScheme.class */
public class DigestScheme extends RFC2617Scheme {
    private static final Log LOG;
    private static final char[] HEXADECIMAL;
    private boolean complete;
    private static final String NC = "00000001";
    private static final int QOP_MISSING = 0;
    private static final int QOP_AUTH_INT = 1;
    private static final int QOP_AUTH = 2;
    private int qopVariant;
    private String cnonce;
    private final ParameterFormatter formatter;
    static Class class$org$apache$commons$httpclient$auth$DigestScheme;

    static Class class$(String x0) {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError(x1.getMessage());
        }
    }

    static {
        Class clsClass$;
        if (class$org$apache$commons$httpclient$auth$DigestScheme == null) {
            clsClass$ = class$("org.apache.commons.httpclient.auth.DigestScheme");
            class$org$apache$commons$httpclient$auth$DigestScheme = clsClass$;
        } else {
            clsClass$ = class$org$apache$commons$httpclient$auth$DigestScheme;
        }
        LOG = LogFactory.getLog(clsClass$);
        HEXADECIMAL = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    }

    public DigestScheme() {
        this.qopVariant = 0;
        this.complete = false;
        this.formatter = new ParameterFormatter();
    }

    @Override // org.apache.commons.httpclient.auth.RFC2617Scheme, org.apache.commons.httpclient.auth.AuthScheme
    public String getID() {
        String id = getRealm();
        String nonce = getParameter("nonce");
        if (nonce != null) {
            id = new StringBuffer().append(id).append("-").append(nonce).toString();
        }
        return id;
    }

    public DigestScheme(String challenge) throws MalformedChallengeException {
        this();
        processChallenge(challenge);
    }

    @Override // org.apache.commons.httpclient.auth.RFC2617Scheme, org.apache.commons.httpclient.auth.AuthScheme
    public void processChallenge(String challenge) throws MalformedChallengeException {
        super.processChallenge(challenge);
        if (getParameter("realm") == null) {
            throw new MalformedChallengeException("missing realm in challange");
        }
        if (getParameter("nonce") == null) {
            throw new MalformedChallengeException("missing nonce in challange");
        }
        boolean unsupportedQop = false;
        String qop = getParameter("qop");
        if (qop != null) {
            StringTokenizer tok = new StringTokenizer(qop, ",");
            while (true) {
                if (!tok.hasMoreTokens()) {
                    break;
                }
                String variant = tok.nextToken().trim();
                if (variant.equals(ResourceRef.AUTH)) {
                    this.qopVariant = 2;
                    break;
                } else if (variant.equals("auth-int")) {
                    this.qopVariant = 1;
                } else {
                    unsupportedQop = true;
                    LOG.warn(new StringBuffer().append("Unsupported qop detected: ").append(variant).toString());
                }
            }
        }
        if (unsupportedQop && this.qopVariant == 0) {
            throw new MalformedChallengeException("None of the qop methods is supported");
        }
        this.cnonce = createCnonce();
        this.complete = true;
    }

    @Override // org.apache.commons.httpclient.auth.AuthScheme
    public boolean isComplete() {
        String s = getParameter("stale");
        if ("true".equalsIgnoreCase(s)) {
            return false;
        }
        return this.complete;
    }

    @Override // org.apache.commons.httpclient.auth.AuthScheme
    public String getSchemeName() {
        return "digest";
    }

    @Override // org.apache.commons.httpclient.auth.AuthScheme
    public boolean isConnectionBased() {
        return false;
    }

    @Override // org.apache.commons.httpclient.auth.AuthScheme
    public String authenticate(Credentials credentials, String method, String uri) throws NoSuchAlgorithmException, AuthenticationException {
        LOG.trace("enter DigestScheme.authenticate(Credentials, String, String)");
        try {
            UsernamePasswordCredentials usernamepassword = (UsernamePasswordCredentials) credentials;
            getParameters().put("methodname", method);
            getParameters().put("uri", uri);
            String digest = createDigest(usernamepassword.getUserName(), usernamepassword.getPassword());
            return new StringBuffer().append("Digest ").append(createDigestHeader(usernamepassword.getUserName(), digest)).toString();
        } catch (ClassCastException e) {
            throw new InvalidCredentialsException(new StringBuffer().append("Credentials cannot be used for digest authentication: ").append(credentials.getClass().getName()).toString());
        }
    }

    @Override // org.apache.commons.httpclient.auth.AuthScheme
    public String authenticate(Credentials credentials, HttpMethod method) throws NoSuchAlgorithmException, AuthenticationException {
        LOG.trace("enter DigestScheme.authenticate(Credentials, HttpMethod)");
        try {
            UsernamePasswordCredentials usernamepassword = (UsernamePasswordCredentials) credentials;
            getParameters().put("methodname", method.getName());
            StringBuffer buffer = new StringBuffer(method.getPath());
            String query = method.getQueryString();
            if (query != null) {
                if (query.indexOf("?") != 0) {
                    buffer.append("?");
                }
                buffer.append(method.getQueryString());
            }
            getParameters().put("uri", buffer.toString());
            String charset = getParameter("charset");
            if (charset == null) {
                getParameters().put("charset", method.getParams().getCredentialCharset());
            }
            String digest = createDigest(usernamepassword.getUserName(), usernamepassword.getPassword());
            return new StringBuffer().append("Digest ").append(createDigestHeader(usernamepassword.getUserName(), digest)).toString();
        } catch (ClassCastException e) {
            throw new InvalidCredentialsException(new StringBuffer().append("Credentials cannot be used for digest authentication: ").append(credentials.getClass().getName()).toString());
        }
    }

    private String createDigest(String uname, String pwd) throws NoSuchAlgorithmException, AuthenticationException {
        String serverDigestValue;
        LOG.trace("enter DigestScheme.createDigest(String, String, Map)");
        String uri = getParameter("uri");
        String realm = getParameter("realm");
        String nonce = getParameter("nonce");
        String qop = getParameter("qop");
        String method = getParameter("methodname");
        String algorithm = getParameter("algorithm");
        if (algorithm == null) {
            algorithm = MessageDigestAlgorithms.MD5;
        }
        String charset = getParameter("charset");
        if (charset == null) {
            charset = "ISO-8859-1";
        }
        if (this.qopVariant == 1) {
            LOG.warn("qop=auth-int is not supported");
            throw new AuthenticationException("Unsupported qop in HTTP Digest authentication");
        }
        try {
            MessageDigest md5Helper = MessageDigest.getInstance(MessageDigestAlgorithms.MD5);
            StringBuffer tmp = new StringBuffer(uname.length() + realm.length() + pwd.length() + 2);
            tmp.append(uname);
            tmp.append(':');
            tmp.append(realm);
            tmp.append(':');
            tmp.append(pwd);
            String a1 = tmp.toString();
            if (algorithm.equals("MD5-sess")) {
                String tmp2 = encode(md5Helper.digest(EncodingUtil.getBytes(a1, charset)));
                StringBuffer tmp3 = new StringBuffer(tmp2.length() + nonce.length() + this.cnonce.length() + 2);
                tmp3.append(tmp2);
                tmp3.append(':');
                tmp3.append(nonce);
                tmp3.append(':');
                tmp3.append(this.cnonce);
                a1 = tmp3.toString();
            } else if (!algorithm.equals(MessageDigestAlgorithms.MD5)) {
                LOG.warn(new StringBuffer().append("Unhandled algorithm ").append(algorithm).append(" requested").toString());
            }
            String md5a1 = encode(md5Helper.digest(EncodingUtil.getBytes(a1, charset)));
            String a2 = null;
            if (this.qopVariant == 1) {
                LOG.error("Unhandled qop auth-int");
            } else {
                a2 = new StringBuffer().append(method).append(":").append(uri).toString();
            }
            String md5a2 = encode(md5Helper.digest(EncodingUtil.getAsciiBytes(a2)));
            if (this.qopVariant == 0) {
                LOG.debug("Using null qop method");
                StringBuffer tmp22 = new StringBuffer(md5a1.length() + nonce.length() + md5a2.length());
                tmp22.append(md5a1);
                tmp22.append(':');
                tmp22.append(nonce);
                tmp22.append(':');
                tmp22.append(md5a2);
                serverDigestValue = tmp22.toString();
            } else {
                if (LOG.isDebugEnabled()) {
                    LOG.debug(new StringBuffer().append("Using qop method ").append(qop).toString());
                }
                String qopOption = getQopVariantString();
                StringBuffer tmp23 = new StringBuffer(md5a1.length() + nonce.length() + NC.length() + this.cnonce.length() + qopOption.length() + md5a2.length() + 5);
                tmp23.append(md5a1);
                tmp23.append(':');
                tmp23.append(nonce);
                tmp23.append(':');
                tmp23.append(NC);
                tmp23.append(':');
                tmp23.append(this.cnonce);
                tmp23.append(':');
                tmp23.append(qopOption);
                tmp23.append(':');
                tmp23.append(md5a2);
                serverDigestValue = tmp23.toString();
            }
            String serverDigest = encode(md5Helper.digest(EncodingUtil.getAsciiBytes(serverDigestValue)));
            return serverDigest;
        } catch (Exception e) {
            throw new AuthenticationException("Unsupported algorithm in HTTP Digest authentication: MD5");
        }
    }

    private String createDigestHeader(String uname, String digest) throws AuthenticationException {
        LOG.trace("enter DigestScheme.createDigestHeader(String, Map, String)");
        String uri = getParameter("uri");
        String realm = getParameter("realm");
        String nonce = getParameter("nonce");
        String opaque = getParameter("opaque");
        String algorithm = getParameter("algorithm");
        List params = new ArrayList(20);
        params.add(new NameValuePair("username", uname));
        params.add(new NameValuePair("realm", realm));
        params.add(new NameValuePair("nonce", nonce));
        params.add(new NameValuePair("uri", uri));
        params.add(new NameValuePair("response", digest));
        if (this.qopVariant != 0) {
            params.add(new NameValuePair("qop", getQopVariantString()));
            params.add(new NameValuePair("nc", NC));
            params.add(new NameValuePair("cnonce", this.cnonce));
        }
        if (algorithm != null) {
            params.add(new NameValuePair("algorithm", algorithm));
        }
        if (opaque != null) {
            params.add(new NameValuePair("opaque", opaque));
        }
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < params.size(); i++) {
            NameValuePair param = (NameValuePair) params.get(i);
            if (i > 0) {
                buffer.append(", ");
            }
            boolean noQuotes = "nc".equals(param.getName()) || "qop".equals(param.getName());
            this.formatter.setAlwaysUseQuotes(!noQuotes);
            this.formatter.format(buffer, param);
        }
        return buffer.toString();
    }

    private String getQopVariantString() {
        String qopOption;
        if (this.qopVariant == 1) {
            qopOption = "auth-int";
        } else {
            qopOption = ResourceRef.AUTH;
        }
        return qopOption;
    }

    private static String encode(byte[] binaryData) {
        LOG.trace("enter DigestScheme.encode(byte[])");
        if (binaryData.length != 16) {
            return null;
        }
        char[] buffer = new char[32];
        for (int i = 0; i < 16; i++) {
            int low = binaryData[i] & 15;
            int high = (binaryData[i] & 240) >> 4;
            buffer[i * 2] = HEXADECIMAL[high];
            buffer[(i * 2) + 1] = HEXADECIMAL[low];
        }
        return new String(buffer);
    }

    public static String createCnonce() throws NoSuchAlgorithmException {
        LOG.trace("enter DigestScheme.createCnonce()");
        try {
            MessageDigest md5Helper = MessageDigest.getInstance(MessageDigestAlgorithms.MD5);
            String cnonce = Long.toString(System.currentTimeMillis());
            return encode(md5Helper.digest(EncodingUtil.getAsciiBytes(cnonce)));
        } catch (NoSuchAlgorithmException e) {
            throw new HttpClientError("Unsupported algorithm in HTTP Digest authentication: MD5");
        }
    }
}
