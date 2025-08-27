package org.apache.poi.util;

@Internal
/* loaded from: poi-3.17.jar:org/apache/poi/util/NullLogger.class */
public class NullLogger extends POILogger {
    @Override // org.apache.poi.util.POILogger
    public void initialize(String cat) {
    }

    @Override // org.apache.poi.util.POILogger
    protected void _log(int level, Object obj1) {
    }

    @Override // org.apache.poi.util.POILogger
    protected void _log(int level, Object obj1, Throwable exception) {
    }

    @Override // org.apache.poi.util.POILogger
    public void log(int level, Object... objs) {
    }

    @Override // org.apache.poi.util.POILogger
    public boolean check(int level) {
        return false;
    }
}
