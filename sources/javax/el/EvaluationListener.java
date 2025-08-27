package javax.el;

/* loaded from: tomcat-embed-el-8.5.43.jar:javax/el/EvaluationListener.class */
public abstract class EvaluationListener {
    public void beforeEvaluation(ELContext context, String expression) {
    }

    public void afterEvaluation(ELContext context, String expression) {
    }

    public void propertyResolved(ELContext context, Object base, Object property) {
    }
}
