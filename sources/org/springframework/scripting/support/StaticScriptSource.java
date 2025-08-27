package org.springframework.scripting.support;

import org.springframework.scripting.ScriptSource;
import org.springframework.util.Assert;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/scripting/support/StaticScriptSource.class */
public class StaticScriptSource implements ScriptSource {
    private String script;
    private boolean modified;
    private String className;

    public StaticScriptSource(String script) {
        setScript(script);
    }

    public StaticScriptSource(String script, String className) {
        setScript(script);
        this.className = className;
    }

    public synchronized void setScript(String script) {
        Assert.hasText(script, "Script must not be empty");
        this.modified = !script.equals(this.script);
        this.script = script;
    }

    @Override // org.springframework.scripting.ScriptSource
    public synchronized String getScriptAsString() {
        this.modified = false;
        return this.script;
    }

    @Override // org.springframework.scripting.ScriptSource
    public synchronized boolean isModified() {
        return this.modified;
    }

    @Override // org.springframework.scripting.ScriptSource
    public String suggestedClassName() {
        return this.className;
    }

    public String toString() {
        return "static script" + (this.className != null ? " [" + this.className + "]" : "");
    }
}
