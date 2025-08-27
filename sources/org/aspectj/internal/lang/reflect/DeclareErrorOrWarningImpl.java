package org.aspectj.internal.lang.reflect;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import org.aspectj.lang.reflect.AjType;
import org.aspectj.lang.reflect.DeclareErrorOrWarning;
import org.aspectj.lang.reflect.PointcutExpression;

/* JADX WARN: Classes with same name are omitted:
  aspectjrt-1.8.14.jar:org/aspectj/internal/lang/reflect/DeclareErrorOrWarningImpl.class
 */
/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/internal/lang/reflect/DeclareErrorOrWarningImpl.class */
public class DeclareErrorOrWarningImpl implements DeclareErrorOrWarning {
    private PointcutExpression pc;
    private String msg;
    private boolean isError;
    private AjType declaringType;

    public DeclareErrorOrWarningImpl(String pointcut, String message, boolean isError, AjType decType) {
        this.pc = new PointcutExpressionImpl(pointcut);
        this.msg = message;
        this.isError = isError;
        this.declaringType = decType;
    }

    @Override // org.aspectj.lang.reflect.DeclareErrorOrWarning
    public AjType getDeclaringType() {
        return this.declaringType;
    }

    @Override // org.aspectj.lang.reflect.DeclareErrorOrWarning
    public PointcutExpression getPointcutExpression() {
        return this.pc;
    }

    @Override // org.aspectj.lang.reflect.DeclareErrorOrWarning
    public String getMessage() {
        return this.msg;
    }

    @Override // org.aspectj.lang.reflect.DeclareErrorOrWarning
    public boolean isError() {
        return this.isError;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("declare ");
        sb.append(isError() ? "error : " : "warning : ");
        sb.append(getPointcutExpression().asString());
        sb.append(" : ");
        sb.append(SymbolConstants.QUOTES_SYMBOL);
        sb.append(getMessage());
        sb.append(SymbolConstants.QUOTES_SYMBOL);
        return sb.toString();
    }
}
