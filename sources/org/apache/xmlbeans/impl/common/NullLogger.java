package org.apache.xmlbeans.impl.common;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/common/NullLogger.class */
public class NullLogger extends XBLogger {
    @Override // org.apache.xmlbeans.impl.common.XBLogger
    public void initialize(String cat) {
    }

    @Override // org.apache.xmlbeans.impl.common.XBLogger
    protected void _log(int level, Object obj1) {
    }

    @Override // org.apache.xmlbeans.impl.common.XBLogger
    protected void _log(int level, Object obj1, Throwable exception) {
    }

    @Override // org.apache.xmlbeans.impl.common.XBLogger
    public void log(int level, Object... objs) {
    }

    @Override // org.apache.xmlbeans.impl.common.XBLogger
    public boolean check(int level) {
        return false;
    }
}
