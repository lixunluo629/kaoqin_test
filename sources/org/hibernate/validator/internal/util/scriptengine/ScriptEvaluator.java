package org.hibernate.validator.internal.util.scriptengine;

import java.util.Map;
import javax.script.ScriptEngine;
import javax.script.ScriptException;
import javax.script.SimpleBindings;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/util/scriptengine/ScriptEvaluator.class */
public class ScriptEvaluator {
    private final ScriptEngine engine;

    public ScriptEvaluator(ScriptEngine engine) {
        this.engine = engine;
    }

    public Object evaluate(String script, Map<String, Object> bindings) throws ScriptException {
        Object objDoEvaluate;
        if (engineAllowsParallelAccessFromMultipleThreads()) {
            return doEvaluate(script, bindings);
        }
        synchronized (this.engine) {
            objDoEvaluate = doEvaluate(script, bindings);
        }
        return objDoEvaluate;
    }

    private Object doEvaluate(String script, Map<String, Object> bindings) throws ScriptException {
        return this.engine.eval(script, new SimpleBindings(bindings));
    }

    private boolean engineAllowsParallelAccessFromMultipleThreads() {
        String threadingType = (String) this.engine.getFactory().getParameter("THREADING");
        return "THREAD-ISOLATED".equals(threadingType) || "STATELESS".equals(threadingType);
    }
}
