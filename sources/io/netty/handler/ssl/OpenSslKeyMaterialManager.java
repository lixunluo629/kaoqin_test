package io.netty.handler.ssl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.net.ssl.SSLException;
import javax.net.ssl.X509ExtendedKeyManager;
import javax.net.ssl.X509KeyManager;
import javax.security.auth.x500.X500Principal;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/ssl/OpenSslKeyMaterialManager.class */
final class OpenSslKeyMaterialManager {
    static final String KEY_TYPE_RSA = "RSA";
    static final String KEY_TYPE_DH_RSA = "DH_RSA";
    static final String KEY_TYPE_EC = "EC";
    static final String KEY_TYPE_EC_EC = "EC_EC";
    static final String KEY_TYPE_EC_RSA = "EC_RSA";
    private static final Map<String, String> KEY_TYPES = new HashMap();
    private final OpenSslKeyMaterialProvider provider;

    static {
        KEY_TYPES.put("RSA", "RSA");
        KEY_TYPES.put("DHE_RSA", "RSA");
        KEY_TYPES.put("ECDHE_RSA", "RSA");
        KEY_TYPES.put("ECDHE_ECDSA", KEY_TYPE_EC);
        KEY_TYPES.put("ECDH_RSA", KEY_TYPE_EC_RSA);
        KEY_TYPES.put("ECDH_ECDSA", KEY_TYPE_EC_EC);
        KEY_TYPES.put(KEY_TYPE_DH_RSA, KEY_TYPE_DH_RSA);
    }

    OpenSslKeyMaterialManager(OpenSslKeyMaterialProvider provider) {
        this.provider = provider;
    }

    void setKeyMaterialServerSide(ReferenceCountedOpenSslEngine engine) throws SSLException {
        String alias;
        String[] authMethods = engine.authMethods();
        if (authMethods.length == 0) {
            return;
        }
        Set<String> aliases = new HashSet<>(authMethods.length);
        for (String authMethod : authMethods) {
            String type = KEY_TYPES.get(authMethod);
            if (type != null && (alias = chooseServerAlias(engine, type)) != null && aliases.add(alias) && !setKeyMaterial(engine, alias)) {
                return;
            }
        }
    }

    void setKeyMaterialClientSide(ReferenceCountedOpenSslEngine engine, String[] keyTypes, X500Principal[] issuer) throws SSLException {
        String alias = chooseClientAlias(engine, keyTypes, issuer);
        if (alias != null) {
            setKeyMaterial(engine, alias);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:7:0x001b  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private boolean setKeyMaterial(io.netty.handler.ssl.ReferenceCountedOpenSslEngine r5, java.lang.String r6) throws javax.net.ssl.SSLException {
        /*
            r4 = this;
            r0 = 0
            r7 = r0
            r0 = r4
            io.netty.handler.ssl.OpenSslKeyMaterialProvider r0 = r0.provider     // Catch: javax.net.ssl.SSLException -> L30 java.lang.Exception -> L35 java.lang.Throwable -> L41
            r1 = r5
            io.netty.buffer.ByteBufAllocator r1 = r1.alloc     // Catch: javax.net.ssl.SSLException -> L30 java.lang.Exception -> L35 java.lang.Throwable -> L41
            r2 = r6
            io.netty.handler.ssl.OpenSslKeyMaterial r0 = r0.chooseKeyMaterial(r1, r2)     // Catch: javax.net.ssl.SSLException -> L30 java.lang.Exception -> L35 java.lang.Throwable -> L41
            r7 = r0
            r0 = r7
            if (r0 == 0) goto L1b
            r0 = r5
            r1 = r7
            boolean r0 = r0.setKeyMaterial(r1)     // Catch: javax.net.ssl.SSLException -> L30 java.lang.Exception -> L35 java.lang.Throwable -> L41
            if (r0 == 0) goto L1f
        L1b:
            r0 = 1
            goto L20
        L1f:
            r0 = 0
        L20:
            r8 = r0
            r0 = r7
            if (r0 == 0) goto L2d
            r0 = r7
            boolean r0 = r0.release()
        L2d:
            r0 = r8
            return r0
        L30:
            r8 = move-exception
            r0 = r8
            throw r0     // Catch: java.lang.Throwable -> L41
        L35:
            r8 = move-exception
            javax.net.ssl.SSLException r0 = new javax.net.ssl.SSLException     // Catch: java.lang.Throwable -> L41
            r1 = r0
            r2 = r8
            r1.<init>(r2)     // Catch: java.lang.Throwable -> L41
            throw r0     // Catch: java.lang.Throwable -> L41
        L41:
            r9 = move-exception
            r0 = r7
            if (r0 == 0) goto L4e
            r0 = r7
            boolean r0 = r0.release()
        L4e:
            r0 = r9
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: io.netty.handler.ssl.OpenSslKeyMaterialManager.setKeyMaterial(io.netty.handler.ssl.ReferenceCountedOpenSslEngine, java.lang.String):boolean");
    }

    private String chooseClientAlias(ReferenceCountedOpenSslEngine engine, String[] keyTypes, X500Principal[] issuer) {
        X509KeyManager manager = this.provider.keyManager();
        if (manager instanceof X509ExtendedKeyManager) {
            return ((X509ExtendedKeyManager) manager).chooseEngineClientAlias(keyTypes, issuer, engine);
        }
        return manager.chooseClientAlias(keyTypes, issuer, null);
    }

    private String chooseServerAlias(ReferenceCountedOpenSslEngine engine, String type) {
        X509KeyManager manager = this.provider.keyManager();
        if (manager instanceof X509ExtendedKeyManager) {
            return ((X509ExtendedKeyManager) manager).chooseEngineServerAlias(type, null, engine);
        }
        return manager.chooseServerAlias(type, null, null);
    }
}
