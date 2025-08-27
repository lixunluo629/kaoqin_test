package org.apache.ibatis.logging.jdk14;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.ibatis.logging.Log;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/logging/jdk14/Jdk14LoggingImpl.class */
public class Jdk14LoggingImpl implements Log {
    private final Logger log;

    public Jdk14LoggingImpl(String clazz) {
        this.log = Logger.getLogger(clazz);
    }

    @Override // org.apache.ibatis.logging.Log
    public boolean isDebugEnabled() {
        return this.log.isLoggable(Level.FINE);
    }

    @Override // org.apache.ibatis.logging.Log
    public boolean isTraceEnabled() {
        return this.log.isLoggable(Level.FINER);
    }

    @Override // org.apache.ibatis.logging.Log
    public void error(String s, Throwable e) {
        this.log.log(Level.SEVERE, s, e);
    }

    @Override // org.apache.ibatis.logging.Log
    public void error(String s) {
        this.log.log(Level.SEVERE, s);
    }

    @Override // org.apache.ibatis.logging.Log
    public void debug(String s) {
        this.log.log(Level.FINE, s);
    }

    @Override // org.apache.ibatis.logging.Log
    public void trace(String s) {
        this.log.log(Level.FINER, s);
    }

    @Override // org.apache.ibatis.logging.Log
    public void warn(String s) {
        this.log.log(Level.WARNING, s);
    }
}
