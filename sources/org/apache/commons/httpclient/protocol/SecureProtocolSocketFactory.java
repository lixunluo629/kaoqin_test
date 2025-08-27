package org.apache.commons.httpclient.protocol;

import java.io.IOException;
import java.net.Socket;

/* loaded from: commons-httpclient-3.1.jar:org/apache/commons/httpclient/protocol/SecureProtocolSocketFactory.class */
public interface SecureProtocolSocketFactory extends ProtocolSocketFactory {
    Socket createSocket(Socket socket, String str, int i, boolean z) throws IOException;
}
