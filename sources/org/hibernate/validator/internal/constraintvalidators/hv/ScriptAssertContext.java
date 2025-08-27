package org.hibernate.validator.internal.constraintvalidators.hv;

import java.util.Map;
import javax.script.ScriptException;
import org.hibernate.validator.internal.util.CollectionHelper;
import org.hibernate.validator.internal.util.logging.Log;
import org.hibernate.validator.internal.util.logging.LoggerFactory;
import org.hibernate.validator.internal.util.scriptengine.ScriptEvaluator;
import org.hibernate.validator.internal.util.scriptengine.ScriptEvaluatorFactory;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/constraintvalidators/hv/ScriptAssertContext.class */
class ScriptAssertContext {
    private static final Log log = LoggerFactory.make();
    private final String script;
    private final ScriptEvaluator scriptEvaluator;

    public ScriptAssertContext(String languageName, String script) {
        this.script = script;
        this.scriptEvaluator = getScriptEvaluator(languageName);
    }

    public boolean evaluateScriptAssertExpression(Object object, String alias) {
        Map<String, Object> bindings = CollectionHelper.newHashMap();
        bindings.put(alias, object);
        return evaluateScriptAssertExpression(bindings);
    }

    public boolean evaluateScriptAssertExpression(Map<String, Object> bindings) {
        try {
            Object result = this.scriptEvaluator.evaluate(this.script, bindings);
            return handleResult(result);
        } catch (ScriptException e) {
            throw log.getErrorDuringScriptExecutionException(this.script, e);
        }
    }

    private ScriptEvaluator getScriptEvaluator(String languageName) {
        try {
            ScriptEvaluatorFactory evaluatorFactory = ScriptEvaluatorFactory.getInstance();
            return evaluatorFactory.getScriptEvaluatorByLanguageName(languageName);
        } catch (ScriptException e) {
            throw log.getCreationOfScriptExecutorFailedException(languageName, e);
        }
    }

    private boolean handleResult(Object evaluationResult) {
        if (evaluationResult == null) {
            throw log.getScriptMustReturnTrueOrFalseException(this.script);
        }
        if (!(evaluationResult instanceof Boolean)) {
            throw log.getScriptMustReturnTrueOrFalseException(this.script, evaluationResult, evaluationResult.getClass().getCanonicalName());
        }
        return Boolean.TRUE.equals(evaluationResult);
    }
}
