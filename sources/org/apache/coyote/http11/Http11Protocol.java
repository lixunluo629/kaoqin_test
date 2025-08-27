package org.apache.coyote.http11;

import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.apache.tomcat.util.res.StringManager;

@Deprecated
/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/coyote/http11/Http11Protocol.class */
public class Http11Protocol extends Http11NioProtocol {
    private static final Log log = LogFactory.getLog((Class<?>) Http11Protocol.class);
    private static final StringManager sm = StringManager.getManager((Class<?>) Http11Protocol.class);

    public Http11Protocol() {
        log.warn(sm.getString("http11protocol.noBio"));
    }
}
