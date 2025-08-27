package org.apache.coyote.ajp;

import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.apache.tomcat.util.res.StringManager;

@Deprecated
/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/coyote/ajp/AjpProtocol.class */
public class AjpProtocol extends AjpNioProtocol {
    private static final Log log = LogFactory.getLog((Class<?>) AjpProtocol.class);
    private static final StringManager sm = StringManager.getManager((Class<?>) AjpProtocol.class);

    public AjpProtocol() {
        log.warn(sm.getString("ajpprotocol.noBio"));
    }
}
