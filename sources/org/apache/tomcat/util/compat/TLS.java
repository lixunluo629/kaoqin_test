package org.apache.tomcat.util.compat;

import java.security.NoSuchAlgorithmException;
import javax.net.ssl.SSLContext;
import org.apache.tomcat.util.net.Constants;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/tomcat/util/compat/TLS.class */
public class TLS {
    private static final boolean tlsv13Available;

    static {
        boolean ok = false;
        try {
            SSLContext.getInstance(Constants.SSL_PROTO_TLSv1_3);
            ok = true;
        } catch (NoSuchAlgorithmException e) {
        }
        tlsv13Available = ok;
    }

    public static boolean isTlsv13Available() {
        return tlsv13Available;
    }
}
