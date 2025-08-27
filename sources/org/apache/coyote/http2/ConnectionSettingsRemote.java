package org.apache.coyote.http2;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/coyote/http2/ConnectionSettingsRemote.class */
public class ConnectionSettingsRemote extends ConnectionSettingsBase<ConnectionException> {
    public ConnectionSettingsRemote(String connectionId) {
        super(connectionId);
    }

    @Override // org.apache.coyote.http2.ConnectionSettingsBase
    void throwException(String msg, Http2Error error) throws ConnectionException {
        throw new ConnectionException(msg, error);
    }
}
