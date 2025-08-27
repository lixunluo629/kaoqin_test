package org.apache.tomcat.util.net.openssl.ciphers;

import org.apache.tomcat.util.net.Constants;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/tomcat/util/net/openssl/ciphers/Protocol.class */
enum Protocol {
    SSLv3(Constants.SSL_PROTO_SSLv3),
    SSLv2(Constants.SSL_PROTO_SSLv2),
    TLSv1(Constants.SSL_PROTO_TLSv1),
    TLSv1_2(Constants.SSL_PROTO_TLSv1_2),
    TLSv1_3(Constants.SSL_PROTO_TLSv1_3);

    private final String openSSLName;

    Protocol(String openSSLName) {
        this.openSSLName = openSSLName;
    }

    String getOpenSSLName() {
        return this.openSSLName;
    }
}
