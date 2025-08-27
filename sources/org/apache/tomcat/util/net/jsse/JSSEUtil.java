package org.apache.tomcat.util.net.jsse;

import ch.qos.logback.core.net.ssl.SSL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.apache.tomcat.util.compat.JreVendor;
import org.apache.tomcat.util.net.Constants;
import org.apache.tomcat.util.net.SSLContext;
import org.apache.tomcat.util.net.SSLHostConfigCertificate;
import org.apache.tomcat.util.net.SSLUtilBase;
import org.apache.tomcat.util.res.StringManager;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/tomcat/util/net/jsse/JSSEUtil.class */
public class JSSEUtil extends SSLUtilBase {
    private static final Log log = LogFactory.getLog((Class<?>) JSSEUtil.class);
    private static final StringManager sm = StringManager.getManager((Class<?>) JSSEUtil.class);
    private static final Set<String> implementedProtocols;
    private static final Set<String> implementedCiphers;

    static {
        try {
            SSLContext context = new JSSESSLContext(Constants.SSL_PROTO_TLS);
            context.init(null, null, null);
            String[] implementedProtocolsArray = context.getSupportedSSLParameters().getProtocols();
            implementedProtocols = new HashSet(implementedProtocolsArray.length);
            for (String protocol : implementedProtocolsArray) {
                String protocolUpper = protocol.toUpperCase(Locale.ENGLISH);
                if (!"SSLV2HELLO".equals(protocolUpper) && !"SSLV3".equals(protocolUpper) && protocolUpper.contains(SSL.DEFAULT_PROTOCOL)) {
                    log.debug(sm.getString("jsse.excludeProtocol", protocol));
                } else {
                    implementedProtocols.add(protocol);
                }
            }
            if (implementedProtocols.size() == 0) {
                log.warn(sm.getString("jsse.noDefaultProtocols"));
            }
            String[] implementedCipherSuiteArray = context.getSupportedSSLParameters().getCipherSuites();
            if (JreVendor.IS_IBM_JVM) {
                implementedCiphers = new HashSet(implementedCipherSuiteArray.length * 2);
                for (String name : implementedCipherSuiteArray) {
                    implementedCiphers.add(name);
                    if (name.startsWith(SSL.DEFAULT_PROTOCOL)) {
                        implementedCiphers.add(Constants.SSL_PROTO_TLS + name.substring(3));
                    }
                }
                return;
            }
            implementedCiphers = new HashSet(implementedCipherSuiteArray.length);
            implementedCiphers.addAll(Arrays.asList(implementedCipherSuiteArray));
        } catch (KeyManagementException | NoSuchAlgorithmException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public JSSEUtil(SSLHostConfigCertificate certificate) {
        this(certificate, true);
    }

    public JSSEUtil(SSLHostConfigCertificate certificate, boolean warnOnSkip) {
        super(certificate, warnOnSkip);
    }

    @Override // org.apache.tomcat.util.net.SSLUtilBase
    protected Log getLog() {
        return log;
    }

    @Override // org.apache.tomcat.util.net.SSLUtilBase
    protected Set<String> getImplementedProtocols() {
        return implementedProtocols;
    }

    @Override // org.apache.tomcat.util.net.SSLUtilBase
    protected Set<String> getImplementedCiphers() {
        return implementedCiphers;
    }

    @Override // org.apache.tomcat.util.net.SSLUtilBase
    protected boolean isTls13RenegAuthAvailable() {
        return false;
    }

    @Override // org.apache.tomcat.util.net.SSLUtilBase
    public SSLContext createSSLContextInternal(List<String> negotiableProtocols) throws NoSuchAlgorithmException {
        return new JSSESSLContext(this.sslHostConfig.getSslProtocol());
    }
}
