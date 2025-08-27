package org.springframework.expression;

/* loaded from: spring-expression-4.3.25.RELEASE.jar:org/springframework/expression/ExpressionException.class */
public class ExpressionException extends RuntimeException {
    protected String expressionString;
    protected int position;

    public ExpressionException(String message) {
        super(message);
    }

    public ExpressionException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExpressionException(String expressionString, String message) {
        super(message);
        this.expressionString = expressionString;
        this.position = -1;
    }

    public ExpressionException(String expressionString, int position, String message) {
        super(message);
        this.expressionString = expressionString;
        this.position = position;
    }

    public ExpressionException(int position, String message) {
        super(message);
        this.position = position;
    }

    public ExpressionException(int position, String message, Throwable cause) {
        super(message, cause);
        this.position = position;
    }

    public final String getExpressionString() {
        return this.expressionString;
    }

    public final int getPosition() {
        return this.position;
    }

    @Override // java.lang.Throwable
    public String getMessage() {
        return toDetailedString();
    }

    public String toDetailedString() {
        if (this.expressionString != null) {
            StringBuilder output = new StringBuilder();
            output.append("Expression [");
            output.append(this.expressionString);
            output.append("]");
            if (this.position >= 0) {
                output.append(" @");
                output.append(this.position);
            }
            output.append(": ");
            output.append(getSimpleMessage());
            return output.toString();
        }
        return getSimpleMessage();
    }

    public String getSimpleMessage() {
        return super.getMessage();
    }
}
