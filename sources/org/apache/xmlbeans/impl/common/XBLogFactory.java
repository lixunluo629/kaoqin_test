package org.apache.xmlbeans.impl.common;

import java.util.HashMap;
import java.util.Map;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/common/XBLogFactory.class */
public final class XBLogFactory {
    private static final Map<String, XBLogger> _loggers = new HashMap();
    private static final XBLogger _nullLogger = new NullLogger();
    static String _loggerClassName = null;

    private XBLogFactory() {
    }

    public static XBLogger getLogger(Class<?> theclass) {
        return getLogger(theclass.getName());
    }

    public static XBLogger getLogger(String cat) {
        if (_loggerClassName == null) {
            try {
                _loggerClassName = System.getProperty("org.apache.xmlbeans.impl.store.XBLogger");
            } catch (Exception e) {
            }
            if (_loggerClassName == null) {
                _loggerClassName = _nullLogger.getClass().getName();
            }
        }
        if (_loggerClassName.equals(_nullLogger.getClass().getName())) {
            return _nullLogger;
        }
        XBLogger logger = _loggers.get(cat);
        if (logger == null) {
            try {
                logger = (XBLogger) Class.forName(_loggerClassName).newInstance();
                logger.initialize(cat);
            } catch (Exception e2) {
                logger = _nullLogger;
                _loggerClassName = _nullLogger.getClass().getName();
            }
            _loggers.put(cat, logger);
        }
        return logger;
    }
}
