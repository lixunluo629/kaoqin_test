package org.aspectj.internal.lang.reflect;

import org.aspectj.lang.reflect.AjType;
import org.aspectj.lang.reflect.AjTypeSystem;
import org.aspectj.lang.reflect.DeclareSoft;
import org.aspectj.lang.reflect.PointcutExpression;

/* JADX WARN: Classes with same name are omitted:
  aspectjrt-1.8.14.jar:org/aspectj/internal/lang/reflect/DeclareSoftImpl.class
 */
/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/internal/lang/reflect/DeclareSoftImpl.class */
public class DeclareSoftImpl implements DeclareSoft {
    private AjType<?> declaringType;
    private PointcutExpression pointcut;
    private AjType<?> exceptionType;
    private String missingTypeName;

    public DeclareSoftImpl(AjType<?> declaringType, String pcut, String exceptionTypeName) {
        this.declaringType = declaringType;
        this.pointcut = new PointcutExpressionImpl(pcut);
        try {
            ClassLoader cl = declaringType.getJavaClass().getClassLoader();
            this.exceptionType = AjTypeSystem.getAjType(Class.forName(exceptionTypeName, false, cl));
        } catch (ClassNotFoundException e) {
            this.missingTypeName = exceptionTypeName;
        }
    }

    @Override // org.aspectj.lang.reflect.DeclareSoft
    public AjType getDeclaringType() {
        return this.declaringType;
    }

    @Override // org.aspectj.lang.reflect.DeclareSoft
    public AjType getSoftenedExceptionType() throws ClassNotFoundException {
        if (this.missingTypeName != null) {
            throw new ClassNotFoundException(this.missingTypeName);
        }
        return this.exceptionType;
    }

    @Override // org.aspectj.lang.reflect.DeclareSoft
    public PointcutExpression getPointcutExpression() {
        return this.pointcut;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("declare soft : ");
        if (this.missingTypeName != null) {
            sb.append(this.exceptionType.getName());
        } else {
            sb.append(this.missingTypeName);
        }
        sb.append(" : ");
        sb.append(getPointcutExpression().asString());
        return sb.toString();
    }
}
