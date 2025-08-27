package org.springframework.aop.support;

import java.io.Serializable;

/* loaded from: spring-aop-4.3.25.RELEASE.jar:org/springframework/aop/support/AbstractExpressionPointcut.class */
public abstract class AbstractExpressionPointcut implements ExpressionPointcut, Serializable {
    private String location;
    private String expression;

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLocation() {
        return this.location;
    }

    public void setExpression(String expression) {
        this.expression = expression;
        try {
            onSetExpression(expression);
        } catch (IllegalArgumentException ex) {
            if (this.location != null) {
                throw new IllegalArgumentException("Invalid expression at location [" + this.location + "]: " + ex);
            }
            throw ex;
        }
    }

    protected void onSetExpression(String expression) throws IllegalArgumentException {
    }

    @Override // org.springframework.aop.support.ExpressionPointcut
    public String getExpression() {
        return this.expression;
    }
}
