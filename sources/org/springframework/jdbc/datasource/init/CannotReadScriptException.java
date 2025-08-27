package org.springframework.jdbc.datasource.init;

import org.springframework.core.io.support.EncodedResource;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/datasource/init/CannotReadScriptException.class */
public class CannotReadScriptException extends ScriptException {
    public CannotReadScriptException(EncodedResource resource, Throwable cause) {
        super("Cannot read SQL script from " + resource, cause);
    }
}
