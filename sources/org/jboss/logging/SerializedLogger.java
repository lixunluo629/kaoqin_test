package org.jboss.logging;

import java.io.Serializable;

/* loaded from: jboss-logging-3.3.2.Final.jar:org/jboss/logging/SerializedLogger.class */
final class SerializedLogger implements Serializable {
    private static final long serialVersionUID = 508779982439435831L;
    private final String name;

    SerializedLogger(String name) {
        this.name = name;
    }

    protected Object readResolve() {
        return Logger.getLogger(this.name);
    }
}
