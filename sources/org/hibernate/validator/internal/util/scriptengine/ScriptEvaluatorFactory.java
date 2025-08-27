package org.hibernate.validator.internal.util.scriptengine;

import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import org.hibernate.validator.internal.util.logging.Messages;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/util/scriptengine/ScriptEvaluatorFactory.class */
public class ScriptEvaluatorFactory {
    private static Reference<ScriptEvaluatorFactory> INSTANCE = new SoftReference(new ScriptEvaluatorFactory());
    private final ConcurrentMap<String, ScriptEvaluator> scriptExecutorCache = new ConcurrentHashMap();

    private ScriptEvaluatorFactory() {
    }

    public static synchronized ScriptEvaluatorFactory getInstance() {
        ScriptEvaluatorFactory theValue = INSTANCE.get();
        if (theValue == null) {
            theValue = new ScriptEvaluatorFactory();
            INSTANCE = new SoftReference(theValue);
        }
        return theValue;
    }

    public ScriptEvaluator getScriptEvaluatorByLanguageName(String languageName) throws ScriptException {
        if (!this.scriptExecutorCache.containsKey(languageName)) {
            ScriptEvaluator scriptExecutor = createNewScriptEvaluator(languageName);
            this.scriptExecutorCache.putIfAbsent(languageName, scriptExecutor);
        }
        return this.scriptExecutorCache.get(languageName);
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: javax.script.ScriptException */
    private ScriptEvaluator createNewScriptEvaluator(String languageName) throws ScriptException {
        ScriptEngine engine = new ScriptEngineManager().getEngineByName(languageName);
        if (engine == null) {
            throw new ScriptException(Messages.MESSAGES.unableToFindScriptEngine(languageName));
        }
        return new ScriptEvaluator(engine);
    }
}
