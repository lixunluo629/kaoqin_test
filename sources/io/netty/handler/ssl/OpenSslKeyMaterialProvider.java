package io.netty.handler.ssl;

import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.UnpooledByteBufAllocator;
import io.netty.internal.tcnative.SSL;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import javax.net.ssl.SSLException;
import javax.net.ssl.X509KeyManager;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/ssl/OpenSslKeyMaterialProvider.class */
class OpenSslKeyMaterialProvider {
    private final X509KeyManager keyManager;
    private final String password;

    OpenSslKeyMaterialProvider(X509KeyManager keyManager, String password) {
        this.keyManager = keyManager;
        this.password = password;
    }

    static void validateKeyMaterialSupported(X509Certificate[] keyCertChain, PrivateKey key, String keyPassword) throws SSLException {
        validateSupported(keyCertChain);
        validateSupported(key, keyPassword);
    }

    private static void validateSupported(PrivateKey key, String password) throws SSLException {
        if (key == null) {
            return;
        }
        long pkeyBio = 0;
        long pkey = 0;
        try {
            try {
                pkeyBio = ReferenceCountedOpenSslContext.toBIO(UnpooledByteBufAllocator.DEFAULT, key);
                pkey = SSL.parsePrivateKey(pkeyBio, password);
                SSL.freeBIO(pkeyBio);
                if (pkey != 0) {
                    SSL.freePrivateKey(pkey);
                }
            } catch (Exception e) {
                throw new SSLException("PrivateKey type not supported " + key.getFormat(), e);
            }
        } catch (Throwable th) {
            SSL.freeBIO(pkeyBio);
            if (pkey != 0) {
                SSL.freePrivateKey(pkey);
            }
            throw th;
        }
    }

    private static void validateSupported(X509Certificate[] certificates) throws SSLException {
        if (certificates == null || certificates.length == 0) {
            return;
        }
        long chainBio = 0;
        long chain = 0;
        PemEncoded encoded = null;
        try {
            try {
                encoded = PemX509Certificate.toPEM(UnpooledByteBufAllocator.DEFAULT, true, certificates);
                chainBio = ReferenceCountedOpenSslContext.toBIO(UnpooledByteBufAllocator.DEFAULT, encoded.retain());
                chain = SSL.parseX509Chain(chainBio);
                SSL.freeBIO(chainBio);
                if (chain != 0) {
                    SSL.freeX509Chain(chain);
                }
                if (encoded != null) {
                    encoded.release();
                }
            } catch (Exception e) {
                throw new SSLException("Certificate type not supported", e);
            }
        } catch (Throwable th) {
            SSL.freeBIO(chainBio);
            if (chain != 0) {
                SSL.freeX509Chain(chain);
            }
            if (encoded != null) {
                encoded.release();
            }
            throw th;
        }
    }

    X509KeyManager keyManager() {
        return this.keyManager;
    }

    OpenSslKeyMaterial chooseKeyMaterial(ByteBufAllocator allocator, String alias) throws Exception {
        OpenSslKeyMaterial keyMaterial;
        X509Certificate[] certificates = this.keyManager.getCertificateChain(alias);
        if (certificates == null || certificates.length == 0) {
            return null;
        }
        PrivateKey key = this.keyManager.getPrivateKey(alias);
        PemEncoded encoded = PemX509Certificate.toPEM(allocator, true, certificates);
        long pkeyBio = 0;
        try {
            long chainBio = ReferenceCountedOpenSslContext.toBIO(allocator, encoded.retain());
            long chain = SSL.parseX509Chain(chainBio);
            if (key instanceof OpenSslPrivateKey) {
                keyMaterial = ((OpenSslPrivateKey) key).newKeyMaterial(chain, certificates);
            } else {
                pkeyBio = ReferenceCountedOpenSslContext.toBIO(allocator, key);
                long pkey = key == null ? 0L : SSL.parsePrivateKey(pkeyBio, this.password);
                keyMaterial = new DefaultOpenSslKeyMaterial(chain, pkey, certificates);
            }
            OpenSslKeyMaterial openSslKeyMaterial = keyMaterial;
            SSL.freeBIO(chainBio);
            SSL.freeBIO(pkeyBio);
            if (0 != 0) {
                SSL.freeX509Chain(0L);
            }
            if (0 != 0) {
                SSL.freePrivateKey(0L);
            }
            encoded.release();
            return openSslKeyMaterial;
        } catch (Throwable th) {
            SSL.freeBIO(0L);
            SSL.freeBIO(0L);
            if (0 != 0) {
                SSL.freeX509Chain(0L);
            }
            if (0 != 0) {
                SSL.freePrivateKey(0L);
            }
            encoded.release();
            throw th;
        }
    }

    void destroy() {
    }
}
