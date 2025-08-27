package org.springframework.scripting;

import java.util.Map;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/scripting/ScriptEvaluator.class */
public interface ScriptEvaluator {
    Object evaluate(ScriptSource scriptSource) throws ScriptCompilationException;

    Object evaluate(ScriptSource scriptSource, Map<String, Object> map) throws ScriptCompilationException;
}
