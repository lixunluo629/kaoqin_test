package org.apache.catalina.authenticator;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.Realm;
import org.apache.catalina.connector.Request;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.apache.tomcat.util.http.parser.Authorization;
import org.apache.tomcat.util.security.ConcurrentMessageDigest;
import org.apache.tomcat.util.security.MD5Encoder;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/catalina/authenticator/DigestAuthenticator.class */
public class DigestAuthenticator extends AuthenticatorBase {
    protected static final String QOP = "auth";
    protected Map<String, NonceInfo> nonces;
    protected String opaque;
    private final Log log = LogFactory.getLog((Class<?>) DigestAuthenticator.class);
    protected long lastTimestamp = 0;
    protected final Object lastTimestampLock = new Object();
    protected int nonceCacheSize = 1000;
    protected int nonceCountWindowSize = 100;
    protected String key = null;
    protected long nonceValidity = 300000;
    protected boolean validateUri = true;

    /*  JADX ERROR: Failed to decode insn: 0x0024: MOVE_MULTI
        java.lang.ArrayIndexOutOfBoundsException: arraycopy: source index -1 out of bounds for object array[6]
        	at java.base/java.lang.System.arraycopy(Native Method)
        	at jadx.plugins.input.java.data.code.StackState.insert(StackState.java:52)
        	at jadx.plugins.input.java.data.code.CodeDecodeState.insert(CodeDecodeState.java:137)
        	at jadx.plugins.input.java.data.code.JavaInsnsRegister.dup2x1(JavaInsnsRegister.java:313)
        	at jadx.plugins.input.java.data.code.JavaInsnData.decode(JavaInsnData.java:46)
        	at jadx.core.dex.instructions.InsnDecoder.lambda$process$0(InsnDecoder.java:50)
        	at jadx.plugins.input.java.data.code.JavaCodeReader.visitInstructions(JavaCodeReader.java:85)
        	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:46)
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:158)
        	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:458)
        	at jadx.core.ProcessClass.process(ProcessClass.java:69)
        	at jadx.core.ProcessClass.generateCode(ProcessClass.java:117)
        	at jadx.core.dex.nodes.ClassNode.generateClassCode(ClassNode.java:401)
        	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:389)
        	at jadx.core.dex.nodes.ClassNode.getCode(ClassNode.java:339)
        */
    protected java.lang.String generateNonce(org.apache.catalina.connector.Request r7) {
        /*
            r6 = this;
            long r0 = java.lang.System.currentTimeMillis()
            r8 = r0
            r0 = r6
            java.lang.Object r0 = r0.lastTimestampLock
            r1 = r0
            r10 = r1
            monitor-enter(r0)
            r0 = r8
            r1 = r6
            long r1 = r1.lastTimestamp
            int r0 = (r0 > r1 ? 1 : (r0 == r1 ? 0 : -1))
            if (r0 <= 0) goto L1d
            r0 = r6
            r1 = r8
            r0.lastTimestamp = r1
            goto L29
            r0 = r6
            r1 = r0
            long r1 = r1.lastTimestamp
            r2 = 1
            long r1 = r1 + r2
            // decode failed: arraycopy: source index -1 out of bounds for object array[6]
            r0.lastTimestamp = r1
            r8 = r-1
            r0 = r10
            monitor-exit(r0)
            goto L37
            r11 = move-exception
            r0 = r10
            monitor-exit(r0)
            r0 = r11
            throw r0
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r1 = r0
            r1.<init>()
            r1 = r7
            java.lang.String r1 = r1.getRemoteAddr()
            java.lang.StringBuilder r0 = r0.append(r1)
            java.lang.String r1 = ":"
            java.lang.StringBuilder r0 = r0.append(r1)
            r1 = r8
            java.lang.StringBuilder r0 = r0.append(r1)
            java.lang.String r1 = ":"
            java.lang.StringBuilder r0 = r0.append(r1)
            r1 = r6
            java.lang.String r1 = r1.getKey()
            java.lang.StringBuilder r0 = r0.append(r1)
            java.lang.String r0 = r0.toString()
            r10 = r0
            r0 = 1
            byte[] r0 = new byte[r0]
            r1 = r0
            r2 = 0
            r3 = r10
            java.nio.charset.Charset r4 = java.nio.charset.StandardCharsets.ISO_8859_1
            byte[] r3 = r3.getBytes(r4)
            r1[r2] = r3
            byte[] r0 = org.apache.tomcat.util.security.ConcurrentMessageDigest.digestMD5(r0)
            r11 = r0
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r1 = r0
            r1.<init>()
            r1 = r8
            java.lang.StringBuilder r0 = r0.append(r1)
            java.lang.String r1 = ":"
            java.lang.StringBuilder r0 = r0.append(r1)
            r1 = r11
            java.lang.String r1 = org.apache.tomcat.util.security.MD5Encoder.encode(r1)
            java.lang.StringBuilder r0 = r0.append(r1)
            java.lang.String r0 = r0.toString()
            r12 = r0
            org.apache.catalina.authenticator.DigestAuthenticator$NonceInfo r0 = new org.apache.catalina.authenticator.DigestAuthenticator$NonceInfo
            r1 = r0
            r2 = r8
            r3 = r6
            int r3 = r3.getNonceCountWindowSize()
            r1.<init>(r2, r3)
            r13 = r0
            r0 = r6
            java.util.Map<java.lang.String, org.apache.catalina.authenticator.DigestAuthenticator$NonceInfo> r0 = r0.nonces
            r1 = r0
            r14 = r1
            monitor-enter(r0)
            r0 = r6
            java.util.Map<java.lang.String, org.apache.catalina.authenticator.DigestAuthenticator$NonceInfo> r0 = r0.nonces
            r1 = r12
            r2 = r13
            java.lang.Object r0 = r0.put(r1, r2)
            r0 = r14
            monitor-exit(r0)
            goto Lc2
            r15 = move-exception
            r0 = r14
            monitor-exit(r0)
            r0 = r15
            throw r0
            r0 = r12
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.catalina.authenticator.DigestAuthenticator.generateNonce(org.apache.catalina.connector.Request):java.lang.String");
    }

    public DigestAuthenticator() {
        setCache(false);
    }

    public int getNonceCountWindowSize() {
        return this.nonceCountWindowSize;
    }

    public void setNonceCountWindowSize(int nonceCountWindowSize) {
        this.nonceCountWindowSize = nonceCountWindowSize;
    }

    public int getNonceCacheSize() {
        return this.nonceCacheSize;
    }

    public void setNonceCacheSize(int nonceCacheSize) {
        this.nonceCacheSize = nonceCacheSize;
    }

    public String getKey() {
        return this.key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public long getNonceValidity() {
        return this.nonceValidity;
    }

    public void setNonceValidity(long nonceValidity) {
        this.nonceValidity = nonceValidity;
    }

    public String getOpaque() {
        return this.opaque;
    }

    public void setOpaque(String opaque) {
        this.opaque = opaque;
    }

    public boolean isValidateUri() {
        return this.validateUri;
    }

    public void setValidateUri(boolean validateUri) {
        this.validateUri = validateUri;
    }

    @Override // org.apache.catalina.authenticator.AuthenticatorBase
    protected boolean doAuthenticate(Request request, HttpServletResponse response) throws IOException {
        if (checkForCachedAuthentication(request, response, false)) {
            return true;
        }
        Principal principal = null;
        String authorization = request.getHeader("authorization");
        DigestInfo digestInfo = new DigestInfo(getOpaque(), getNonceValidity(), getKey(), this.nonces, isValidateUri());
        if (authorization != null && digestInfo.parse(request, authorization)) {
            if (digestInfo.validate(request)) {
                principal = digestInfo.authenticate(this.context.getRealm());
            }
            if (principal != null && !digestInfo.isNonceStale()) {
                register(request, response, principal, HttpServletRequest.DIGEST_AUTH, digestInfo.getUsername(), null);
                return true;
            }
        }
        String nonce = generateNonce(request);
        setAuthenticateHeader(request, response, nonce, principal != null && digestInfo.isNonceStale());
        response.sendError(401);
        return false;
    }

    @Override // org.apache.catalina.authenticator.AuthenticatorBase
    protected String getAuthMethod() {
        return HttpServletRequest.DIGEST_AUTH;
    }

    protected static String removeQuotes(String quotedString, boolean quotesRequired) {
        if (quotedString.length() > 0 && quotedString.charAt(0) != '\"' && !quotesRequired) {
            return quotedString;
        }
        if (quotedString.length() > 2) {
            return quotedString.substring(1, quotedString.length() - 1);
        }
        return "";
    }

    protected static String removeQuotes(String quotedString) {
        return removeQuotes(quotedString, false);
    }

    protected void setAuthenticateHeader(HttpServletRequest request, HttpServletResponse response, String nonce, boolean isNonceStale) {
        String authenticateHeader;
        String realmName = getRealmName(this.context);
        if (isNonceStale) {
            authenticateHeader = "Digest realm=\"" + realmName + "\", qop=\"auth\", nonce=\"" + nonce + "\", opaque=\"" + getOpaque() + "\", stale=true";
        } else {
            authenticateHeader = "Digest realm=\"" + realmName + "\", qop=\"auth\", nonce=\"" + nonce + "\", opaque=\"" + getOpaque() + SymbolConstants.QUOTES_SYMBOL;
        }
        response.setHeader("WWW-Authenticate", authenticateHeader);
    }

    @Override // org.apache.catalina.authenticator.AuthenticatorBase, org.apache.catalina.valves.ValveBase, org.apache.catalina.util.LifecycleBase
    protected synchronized void startInternal() throws LifecycleException {
        super.startInternal();
        if (getKey() == null) {
            setKey(this.sessionIdGenerator.generateSessionId());
        }
        if (getOpaque() == null) {
            setOpaque(this.sessionIdGenerator.generateSessionId());
        }
        this.nonces = new LinkedHashMap<String, NonceInfo>() { // from class: org.apache.catalina.authenticator.DigestAuthenticator.1
            private static final long serialVersionUID = 1;
            private static final long LOG_SUPPRESS_TIME = 300000;
            private long lastLog = 0;

            @Override // java.util.LinkedHashMap
            protected boolean removeEldestEntry(Map.Entry<String, NonceInfo> eldest) {
                long currentTime = System.currentTimeMillis();
                if (size() <= DigestAuthenticator.this.getNonceCacheSize()) {
                    return false;
                }
                if (this.lastLog < currentTime && currentTime - eldest.getValue().getTimestamp() < DigestAuthenticator.this.getNonceValidity()) {
                    DigestAuthenticator.this.log.warn(AuthenticatorBase.sm.getString("digestAuthenticator.cacheRemove"));
                    this.lastLog = currentTime + LOG_SUPPRESS_TIME;
                    return true;
                }
                return true;
            }
        };
    }

    /* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/catalina/authenticator/DigestAuthenticator$DigestInfo.class */
    public static class DigestInfo {
        private final String opaque;
        private final long nonceValidity;
        private final String key;
        private final Map<String, NonceInfo> nonces;
        private boolean validateUri;
        private String userName = null;
        private String method = null;
        private String uri = null;
        private String response = null;
        private String nonce = null;
        private String nc = null;
        private String cnonce = null;
        private String realmName = null;
        private String qop = null;
        private String opaqueReceived = null;
        private boolean nonceStale = false;

        public DigestInfo(String opaque, long nonceValidity, String key, Map<String, NonceInfo> nonces, boolean validateUri) {
            this.validateUri = true;
            this.opaque = opaque;
            this.nonceValidity = nonceValidity;
            this.key = key;
            this.nonces = nonces;
            this.validateUri = validateUri;
        }

        public String getUsername() {
            return this.userName;
        }

        public boolean parse(Request request, String authorization) throws IllegalArgumentException {
            if (authorization == null) {
                return false;
            }
            try {
                Map<String, String> directives = Authorization.parseAuthorizationDigest(new StringReader(authorization));
                if (directives == null) {
                    return false;
                }
                this.method = request.getMethod();
                this.userName = directives.get("username");
                this.realmName = directives.get("realm");
                this.nonce = directives.get("nonce");
                this.nc = directives.get("nc");
                this.cnonce = directives.get("cnonce");
                this.qop = directives.get("qop");
                this.uri = directives.get("uri");
                this.response = directives.get("response");
                this.opaqueReceived = directives.get("opaque");
                return true;
            } catch (IOException e) {
                return false;
            }
        }

        /* JADX WARN: Type inference failed for: r0v53, types: [byte[], byte[][]] */
        public boolean validate(Request request) throws NumberFormatException {
            int i;
            NonceInfo info;
            String uriQuery;
            if (this.userName == null || this.realmName == null || this.nonce == null || this.uri == null || this.response == null) {
                return false;
            }
            if (this.validateUri) {
                String query = request.getQueryString();
                if (query == null) {
                    uriQuery = request.getRequestURI();
                } else {
                    uriQuery = request.getRequestURI() + "?" + query;
                }
                if (!this.uri.equals(uriQuery)) {
                    String host = request.getHeader("host");
                    String scheme = request.getScheme();
                    if (host != null && !uriQuery.startsWith(scheme)) {
                        if (!this.uri.equals(scheme + "://" + host + uriQuery)) {
                            return false;
                        }
                    } else {
                        return false;
                    }
                }
            }
            String lcRealm = AuthenticatorBase.getRealmName(request.getContext());
            if (!lcRealm.equals(this.realmName) || !this.opaque.equals(this.opaqueReceived) || (i = this.nonce.indexOf(58)) < 0 || i + 1 == this.nonce.length()) {
                return false;
            }
            try {
                long nonceTime = Long.parseLong(this.nonce.substring(0, i));
                String md5clientIpTimeKey = this.nonce.substring(i + 1);
                long currentTime = System.currentTimeMillis();
                if (currentTime - nonceTime > this.nonceValidity) {
                    this.nonceStale = true;
                    synchronized (this.nonces) {
                        this.nonces.remove(this.nonce);
                    }
                }
                String serverIpTimeKey = request.getRemoteAddr() + ":" + nonceTime + ":" + this.key;
                byte[] buffer = ConcurrentMessageDigest.digestMD5(new byte[]{serverIpTimeKey.getBytes(StandardCharsets.ISO_8859_1)});
                String md5ServerIpTimeKey = MD5Encoder.encode(buffer);
                if (!md5ServerIpTimeKey.equals(md5clientIpTimeKey)) {
                    return false;
                }
                if (this.qop != null && !"auth".equals(this.qop)) {
                    return false;
                }
                if (this.qop == null) {
                    if (this.cnonce != null || this.nc != null) {
                        return false;
                    }
                    return true;
                }
                if (this.cnonce == null || this.nc == null || this.nc.length() < 6 || this.nc.length() > 8) {
                    return false;
                }
                try {
                    long count = Long.parseLong(this.nc, 16);
                    synchronized (this.nonces) {
                        info = this.nonces.get(this.nonce);
                    }
                    if (info == null) {
                        this.nonceStale = true;
                        return true;
                    }
                    if (!info.nonceCountValid(count)) {
                        return false;
                    }
                    return true;
                } catch (NumberFormatException e) {
                    return false;
                }
            } catch (NumberFormatException e2) {
                return false;
            }
        }

        public boolean isNonceStale() {
            return this.nonceStale;
        }

        /* JADX WARN: Type inference failed for: r0v6, types: [byte[], byte[][]] */
        public Principal authenticate(Realm realm) {
            String a2 = this.method + ":" + this.uri;
            byte[] buffer = ConcurrentMessageDigest.digestMD5(new byte[]{a2.getBytes(StandardCharsets.ISO_8859_1)});
            String md5a2 = MD5Encoder.encode(buffer);
            return realm.authenticate(this.userName, this.response, this.nonce, this.nc, this.cnonce, this.qop, this.realmName, md5a2);
        }
    }

    /* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/catalina/authenticator/DigestAuthenticator$NonceInfo.class */
    public static class NonceInfo {
        private final long timestamp;
        private final boolean[] seen;
        private final int offset;
        private int count = 0;

        public NonceInfo(long currentTime, int seenWindowSize) {
            this.timestamp = currentTime;
            this.seen = new boolean[seenWindowSize];
            this.offset = seenWindowSize / 2;
        }

        public synchronized boolean nonceCountValid(long nonceCount) {
            if (this.count - this.offset >= nonceCount || nonceCount > (this.count - this.offset) + this.seen.length) {
                return false;
            }
            int checkIndex = (int) ((nonceCount + this.offset) % this.seen.length);
            if (this.seen[checkIndex]) {
                return false;
            }
            this.seen[checkIndex] = true;
            this.seen[this.count % this.seen.length] = false;
            this.count++;
            return true;
        }

        public long getTimestamp() {
            return this.timestamp;
        }
    }
}
