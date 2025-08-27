package org.springframework.transaction.interceptor;

/* loaded from: spring-tx-4.3.25.RELEASE.jar:org/springframework/transaction/interceptor/NoRollbackRuleAttribute.class */
public class NoRollbackRuleAttribute extends RollbackRuleAttribute {
    public NoRollbackRuleAttribute(Class<?> clazz) {
        super(clazz);
    }

    public NoRollbackRuleAttribute(String exceptionName) {
        super(exceptionName);
    }

    @Override // org.springframework.transaction.interceptor.RollbackRuleAttribute
    public String toString() {
        return "No" + super.toString();
    }
}
