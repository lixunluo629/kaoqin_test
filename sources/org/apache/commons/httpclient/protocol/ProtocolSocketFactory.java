package org.apache.commons.httpclient.protocol;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import org.apache.commons.httpclient.params.HttpConnectionParams;

/* loaded from: commons-httpclient-3.1.jar:org/apache/commons/httpclient/protocol/ProtocolSocketFactory.class */
public interface ProtocolSocketFactory {
    Socket createSocket(String str, int i, InetAddress inetAddress, int i2) throws IOException;

    Socket createSocket(String str, int i, InetAddress inetAddress, int i2, HttpConnectionParams httpConnectionParams) throws IOException;

    Socket createSocket(String str, int i) throws IOException;
}
