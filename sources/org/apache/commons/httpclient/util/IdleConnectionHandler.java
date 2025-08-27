package org.apache.commons.httpclient.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.apache.commons.httpclient.HttpConnection;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/* loaded from: commons-httpclient-3.1.jar:org/apache/commons/httpclient/util/IdleConnectionHandler.class */
public class IdleConnectionHandler {
    private static final Log LOG;
    private Map connectionToAdded = new HashMap();
    static Class class$org$apache$commons$httpclient$util$IdleConnectionHandler;

    static Class class$(String x0) {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError(x1.getMessage());
        }
    }

    static {
        Class clsClass$;
        if (class$org$apache$commons$httpclient$util$IdleConnectionHandler == null) {
            clsClass$ = class$("org.apache.commons.httpclient.util.IdleConnectionHandler");
            class$org$apache$commons$httpclient$util$IdleConnectionHandler = clsClass$;
        } else {
            clsClass$ = class$org$apache$commons$httpclient$util$IdleConnectionHandler;
        }
        LOG = LogFactory.getLog(clsClass$);
    }

    public void add(HttpConnection connection) {
        Long timeAdded = new Long(System.currentTimeMillis());
        if (LOG.isDebugEnabled()) {
            LOG.debug(new StringBuffer().append("Adding connection at: ").append(timeAdded).toString());
        }
        this.connectionToAdded.put(connection, timeAdded);
    }

    public void remove(HttpConnection connection) {
        this.connectionToAdded.remove(connection);
    }

    public void removeAll() {
        this.connectionToAdded.clear();
    }

    public void closeIdleConnections(long idleTime) throws IOException {
        long idleTimeout = System.currentTimeMillis() - idleTime;
        if (LOG.isDebugEnabled()) {
            LOG.debug(new StringBuffer().append("Checking for connections, idleTimeout: ").append(idleTimeout).toString());
        }
        Iterator connectionIter = this.connectionToAdded.keySet().iterator();
        while (connectionIter.hasNext()) {
            HttpConnection conn = (HttpConnection) connectionIter.next();
            Long connectionTime = (Long) this.connectionToAdded.get(conn);
            if (connectionTime.longValue() <= idleTimeout) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug(new StringBuffer().append("Closing connection, connection time: ").append(connectionTime).toString());
                }
                connectionIter.remove();
                conn.close();
            }
        }
    }
}
