package org.springframework.scripting;

import java.io.IOException;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/scripting/ScriptSource.class */
public interface ScriptSource {
    String getScriptAsString() throws IOException;

    boolean isModified();

    String suggestedClassName();
}
