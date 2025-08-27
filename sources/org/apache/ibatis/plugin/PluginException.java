package org.apache.ibatis.plugin;

import org.apache.ibatis.exceptions.PersistenceException;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/plugin/PluginException.class */
public class PluginException extends PersistenceException {
    private static final long serialVersionUID = 8548771664564998595L;

    public PluginException() {
    }

    public PluginException(String message) {
        super(message);
    }

    public PluginException(String message, Throwable cause) {
        super(message, cause);
    }

    public PluginException(Throwable cause) {
        super(cause);
    }
}
