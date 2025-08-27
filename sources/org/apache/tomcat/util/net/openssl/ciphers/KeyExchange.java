package org.apache.tomcat.util.net.openssl.ciphers;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/tomcat/util/net/openssl/ciphers/KeyExchange.class */
enum KeyExchange {
    EECDH,
    RSA,
    DHr,
    DHd,
    EDH,
    PSK,
    FZA,
    KRB5,
    ECDHr,
    ECDHe,
    GOST,
    SRP,
    RSAPSK,
    ECDHEPSK,
    DHEPSK,
    ANY
}
