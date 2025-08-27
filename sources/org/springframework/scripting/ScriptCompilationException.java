package org.springframework.scripting;

import org.springframework.core.NestedRuntimeException;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/scripting/ScriptCompilationException.class */
public class ScriptCompilationException extends NestedRuntimeException {
    private ScriptSource scriptSource;

    public ScriptCompilationException(String msg) {
        super(msg);
    }

    public ScriptCompilationException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public ScriptCompilationException(ScriptSource scriptSource, String msg) {
        super("Could not compile " + scriptSource + ": " + msg);
        this.scriptSource = scriptSource;
    }

    public ScriptCompilationException(ScriptSource scriptSource, Throwable cause) {
        super("Could not compile " + scriptSource, cause);
        this.scriptSource = scriptSource;
    }

    public ScriptCompilationException(ScriptSource scriptSource, String msg, Throwable cause) {
        super("Could not compile " + scriptSource + ": " + msg, cause);
        this.scriptSource = scriptSource;
    }

    public ScriptSource getScriptSource() {
        return this.scriptSource;
    }
}
