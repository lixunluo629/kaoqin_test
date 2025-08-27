package org.apache.coyote.http2;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.apache.tomcat.util.res.StringManager;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/coyote/http2/AbstractStream.class */
abstract class AbstractStream {
    private static final Log log = LogFactory.getLog((Class<?>) AbstractStream.class);
    private static final StringManager sm = StringManager.getManager((Class<?>) AbstractStream.class);
    private final Integer identifier;
    private volatile AbstractStream parentStream = null;
    private final Set<Stream> childStreams = Collections.newSetFromMap(new ConcurrentHashMap());
    private long windowSize = 65535;

    protected abstract String getConnectionId();

    protected abstract int getWeight();

    @Deprecated
    protected abstract void doNotifyAll();

    public AbstractStream(Integer identifier) {
        this.identifier = identifier;
    }

    public Integer getIdentifier() {
        return this.identifier;
    }

    public int getIdAsInt() {
        return this.identifier.intValue();
    }

    void detachFromParent() {
        if (this.parentStream != null) {
            this.parentStream.getChildStreams().remove(this);
            this.parentStream = null;
        }
    }

    final void addChild(Stream child) {
        child.setParentStream(this);
        this.childStreams.add(child);
    }

    boolean isDescendant(AbstractStream stream) {
        if (this.childStreams.contains(stream)) {
            return true;
        }
        for (Stream child : this.childStreams) {
            if (child.isDescendant(stream)) {
                return true;
            }
        }
        return false;
    }

    AbstractStream getParentStream() {
        return this.parentStream;
    }

    void setParentStream(AbstractStream parentStream) {
        this.parentStream = parentStream;
    }

    final Set<Stream> getChildStreams() {
        return this.childStreams;
    }

    protected synchronized void setWindowSize(long windowSize) {
        this.windowSize = windowSize;
    }

    protected synchronized long getWindowSize() {
        return this.windowSize;
    }

    protected synchronized void incrementWindowSize(int increment) throws Http2Exception {
        this.windowSize += increment;
        if (log.isDebugEnabled()) {
            log.debug(sm.getString("abstractStream.windowSizeInc", getConnectionId(), getIdentifier(), Integer.toString(increment), Long.toString(this.windowSize)));
        }
        if (this.windowSize > 2147483647L) {
            String msg = sm.getString("abstractStream.windowSizeTooBig", getConnectionId(), this.identifier, Integer.toString(increment), Long.toString(this.windowSize));
            if (this.identifier.intValue() == 0) {
                throw new ConnectionException(msg, Http2Error.FLOW_CONTROL_ERROR);
            }
            throw new StreamException(msg, Http2Error.FLOW_CONTROL_ERROR, this.identifier.intValue());
        }
    }

    protected synchronized void decrementWindowSize(int decrement) {
        this.windowSize -= decrement;
        if (log.isDebugEnabled()) {
            log.debug(sm.getString("abstractStream.windowSizeDec", getConnectionId(), getIdentifier(), Integer.toString(decrement), Long.toString(this.windowSize)));
        }
    }
}
