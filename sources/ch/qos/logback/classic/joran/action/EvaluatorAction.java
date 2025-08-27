package ch.qos.logback.classic.joran.action;

import ch.qos.logback.classic.boolex.JaninoEventEvaluator;
import ch.qos.logback.core.joran.action.AbstractEventEvaluatorAction;

/* JADX WARN: Classes with same name are omitted:
  logback-classic-1.2.3.jar:ch/qos/logback/classic/joran/action/EvaluatorAction.class
 */
/* loaded from: logback-classic-1.2.3.jar.bak:ch/qos/logback/classic/joran/action/EvaluatorAction.class */
public class EvaluatorAction extends AbstractEventEvaluatorAction {
    @Override // ch.qos.logback.core.joran.action.AbstractEventEvaluatorAction
    protected String defaultClassName() {
        return JaninoEventEvaluator.class.getName();
    }
}
