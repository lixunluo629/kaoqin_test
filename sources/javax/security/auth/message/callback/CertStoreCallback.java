package javax.security.auth.message.callback;

import java.security.cert.CertStore;
import javax.security.auth.callback.Callback;

/* loaded from: tomcat-embed-core-8.5.43.jar:javax/security/auth/message/callback/CertStoreCallback.class */
public class CertStoreCallback implements Callback {
    private CertStore certStore;

    public void setCertStore(CertStore certStore) {
        this.certStore = certStore;
    }

    public CertStore getCertStore() {
        return this.certStore;
    }
}
