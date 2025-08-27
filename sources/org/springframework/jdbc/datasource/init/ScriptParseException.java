package org.springframework.jdbc.datasource.init;

import net.coobird.thumbnailator.tasks.UnsupportedFormatException;
import org.springframework.core.io.support.EncodedResource;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/datasource/init/ScriptParseException.class */
public class ScriptParseException extends ScriptException {
    public ScriptParseException(String message, EncodedResource resource) {
        super(buildMessage(message, resource));
    }

    public ScriptParseException(String message, EncodedResource resource, Throwable cause) {
        super(buildMessage(message, resource), cause);
    }

    private static String buildMessage(String message, EncodedResource resource) {
        Object[] objArr = new Object[2];
        objArr[0] = resource == null ? UnsupportedFormatException.UNKNOWN : resource;
        objArr[1] = message;
        return String.format("Failed to parse SQL script from resource [%s]: %s", objArr);
    }
}
