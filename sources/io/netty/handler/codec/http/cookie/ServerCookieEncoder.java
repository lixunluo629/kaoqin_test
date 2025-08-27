package io.netty.handler.codec.http.cookie;

import io.netty.handler.codec.DateFormatter;
import io.netty.util.internal.ObjectUtil;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/http/cookie/ServerCookieEncoder.class */
public final class ServerCookieEncoder extends CookieEncoder {
    public static final ServerCookieEncoder STRICT = new ServerCookieEncoder(true);
    public static final ServerCookieEncoder LAX = new ServerCookieEncoder(false);

    private ServerCookieEncoder(boolean strict) {
        super(strict);
    }

    public String encode(String name, String value) {
        return encode(new DefaultCookie(name, value));
    }

    public String encode(Cookie cookie) {
        String name = ((Cookie) ObjectUtil.checkNotNull(cookie, "cookie")).name();
        String value = cookie.value() != null ? cookie.value() : "";
        validateCookie(name, value);
        StringBuilder buf = CookieUtil.stringBuilder();
        if (cookie.wrap()) {
            CookieUtil.addQuoted(buf, name, value);
        } else {
            CookieUtil.add(buf, name, value);
        }
        if (cookie.maxAge() != Long.MIN_VALUE) {
            CookieUtil.add(buf, CookieHeaderNames.MAX_AGE, cookie.maxAge());
            Date expires = new Date((cookie.maxAge() * 1000) + System.currentTimeMillis());
            buf.append("Expires");
            buf.append('=');
            DateFormatter.append(expires, buf);
            buf.append(';');
            buf.append(' ');
        }
        if (cookie.path() != null) {
            CookieUtil.add(buf, CookieHeaderNames.PATH, cookie.path());
        }
        if (cookie.domain() != null) {
            CookieUtil.add(buf, CookieHeaderNames.DOMAIN, cookie.domain());
        }
        if (cookie.isSecure()) {
            CookieUtil.add(buf, CookieHeaderNames.SECURE);
        }
        if (cookie.isHttpOnly()) {
            CookieUtil.add(buf, CookieHeaderNames.HTTPONLY);
        }
        if (cookie instanceof DefaultCookie) {
            DefaultCookie c = (DefaultCookie) cookie;
            if (c.sameSite() != null) {
                CookieUtil.add(buf, CookieHeaderNames.SAMESITE, c.sameSite().name());
            }
        }
        return CookieUtil.stripTrailingSeparator(buf);
    }

    private static List<String> dedup(List<String> encoded, Map<String, Integer> nameToLastIndex) {
        boolean[] isLastInstance = new boolean[encoded.size()];
        Iterator<Integer> it = nameToLastIndex.values().iterator();
        while (it.hasNext()) {
            int idx = it.next().intValue();
            isLastInstance[idx] = true;
        }
        List<String> dedupd = new ArrayList<>(nameToLastIndex.size());
        int n = encoded.size();
        for (int i = 0; i < n; i++) {
            if (isLastInstance[i]) {
                dedupd.add(encoded.get(i));
            }
        }
        return dedupd;
    }

    public List<String> encode(Cookie... cookies) {
        if (((Cookie[]) ObjectUtil.checkNotNull(cookies, "cookies")).length == 0) {
            return Collections.emptyList();
        }
        List<String> encoded = new ArrayList<>(cookies.length);
        Map<String, Integer> nameToIndex = (!this.strict || cookies.length <= 1) ? null : new HashMap<>();
        boolean hasDupdName = false;
        for (int i = 0; i < cookies.length; i++) {
            Cookie c = cookies[i];
            encoded.add(encode(c));
            if (nameToIndex != null) {
                hasDupdName |= nameToIndex.put(c.name(), Integer.valueOf(i)) != null;
            }
        }
        return hasDupdName ? dedup(encoded, nameToIndex) : encoded;
    }

    public List<String> encode(Collection<? extends Cookie> cookies) {
        if (((Collection) ObjectUtil.checkNotNull(cookies, "cookies")).isEmpty()) {
            return Collections.emptyList();
        }
        List<String> encoded = new ArrayList<>(cookies.size());
        Map<String, Integer> nameToIndex = (!this.strict || cookies.size() <= 1) ? null : new HashMap<>();
        int i = 0;
        boolean hasDupdName = false;
        for (Cookie c : cookies) {
            encoded.add(encode(c));
            if (nameToIndex != null) {
                int i2 = i;
                i++;
                hasDupdName |= nameToIndex.put(c.name(), Integer.valueOf(i2)) != null;
            }
        }
        return hasDupdName ? dedup(encoded, nameToIndex) : encoded;
    }

    /* JADX WARN: Removed duplicated region for block: B:17:0x007e A[PHI: r11
  0x007e: PHI (r11v1 'i' int) = (r11v0 'i' int), (r11v6 'i' int) binds: [B:13:0x005e, B:15:0x0077] A[DONT_GENERATE, DONT_INLINE]] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public java.util.List<java.lang.String> encode(java.lang.Iterable<? extends io.netty.handler.codec.http.cookie.Cookie> r6) {
        /*
            r5 = this;
            r0 = r6
            java.lang.String r1 = "cookies"
            java.lang.Object r0 = io.netty.util.internal.ObjectUtil.checkNotNull(r0, r1)
            java.lang.Iterable r0 = (java.lang.Iterable) r0
            java.util.Iterator r0 = r0.iterator()
            r7 = r0
            r0 = r7
            boolean r0 = r0.hasNext()
            if (r0 != 0) goto L1c
            java.util.List r0 = java.util.Collections.emptyList()
            return r0
        L1c:
            java.util.ArrayList r0 = new java.util.ArrayList
            r1 = r0
            r1.<init>()
            r8 = r0
            r0 = r7
            java.lang.Object r0 = r0.next()
            io.netty.handler.codec.http.cookie.Cookie r0 = (io.netty.handler.codec.http.cookie.Cookie) r0
            r9 = r0
            r0 = r5
            boolean r0 = r0.strict
            if (r0 == 0) goto L49
            r0 = r7
            boolean r0 = r0.hasNext()
            if (r0 == 0) goto L49
            java.util.HashMap r0 = new java.util.HashMap
            r1 = r0
            r1.<init>()
            goto L4a
        L49:
            r0 = 0
        L4a:
            r10 = r0
            r0 = 0
            r11 = r0
            r0 = r8
            r1 = r5
            r2 = r9
            java.lang.String r1 = r1.encode(r2)
            boolean r0 = r0.add(r1)
            r0 = r10
            if (r0 == 0) goto L7e
            r0 = r10
            r1 = r9
            java.lang.String r1 = r1.name()
            r2 = r11
            int r11 = r11 + 1
            java.lang.Integer r2 = java.lang.Integer.valueOf(r2)
            java.lang.Object r0 = r0.put(r1, r2)
            if (r0 == 0) goto L7e
            r0 = 1
            goto L7f
        L7e:
            r0 = 0
        L7f:
            r12 = r0
        L81:
            r0 = r7
            boolean r0 = r0.hasNext()
            if (r0 == 0) goto Lcd
            r0 = r7
            java.lang.Object r0 = r0.next()
            io.netty.handler.codec.http.cookie.Cookie r0 = (io.netty.handler.codec.http.cookie.Cookie) r0
            r13 = r0
            r0 = r8
            r1 = r5
            r2 = r13
            java.lang.String r1 = r1.encode(r2)
            boolean r0 = r0.add(r1)
            r0 = r10
            if (r0 == 0) goto Lca
            r0 = r12
            r1 = r10
            r2 = r13
            java.lang.String r2 = r2.name()
            r3 = r11
            int r11 = r11 + 1
            java.lang.Integer r3 = java.lang.Integer.valueOf(r3)
            java.lang.Object r1 = r1.put(r2, r3)
            if (r1 == 0) goto Lc6
            r1 = 1
            goto Lc7
        Lc6:
            r1 = 0
        Lc7:
            r0 = r0 | r1
            r12 = r0
        Lca:
            goto L81
        Lcd:
            r0 = r12
            if (r0 == 0) goto Ldb
            r0 = r8
            r1 = r10
            java.util.List r0 = dedup(r0, r1)
            goto Ldc
        Ldb:
            r0 = r8
        Ldc:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: io.netty.handler.codec.http.cookie.ServerCookieEncoder.encode(java.lang.Iterable):java.util.List");
    }
}
