package org.aspectj.internal.lang.reflect;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import org.aspectj.lang.annotation.AdviceName;
import org.aspectj.lang.reflect.Advice;
import org.aspectj.lang.reflect.AdviceKind;
import org.aspectj.lang.reflect.AjType;
import org.aspectj.lang.reflect.AjTypeSystem;
import org.aspectj.lang.reflect.PointcutExpression;
import org.aspectj.weaver.NameMangler;

/* JADX WARN: Classes with same name are omitted:
  aspectjrt-1.8.14.jar:org/aspectj/internal/lang/reflect/AdviceImpl.class
 */
/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/internal/lang/reflect/AdviceImpl.class */
public class AdviceImpl implements Advice {
    private static final String AJC_INTERNAL = "org.aspectj.runtime.internal";
    private final AdviceKind kind;
    private final Method adviceMethod;
    private PointcutExpression pointcutExpression;
    private boolean hasExtraParam;
    private Type[] genericParameterTypes;
    private AjType[] parameterTypes;
    private AjType[] exceptionTypes;

    protected AdviceImpl(Method method, String pointcut, AdviceKind type) {
        this.hasExtraParam = false;
        this.kind = type;
        this.adviceMethod = method;
        this.pointcutExpression = new PointcutExpressionImpl(pointcut);
    }

    protected AdviceImpl(Method method, String pointcut, AdviceKind type, String extraParamName) {
        this(method, pointcut, type);
        this.hasExtraParam = true;
    }

    @Override // org.aspectj.lang.reflect.Advice
    public AjType getDeclaringType() {
        return AjTypeSystem.getAjType(this.adviceMethod.getDeclaringClass());
    }

    @Override // org.aspectj.lang.reflect.Advice
    public Type[] getGenericParameterTypes() {
        if (this.genericParameterTypes == null) {
            Type[] genTypes = this.adviceMethod.getGenericParameterTypes();
            int syntheticCount = 0;
            for (Type t : genTypes) {
                if ((t instanceof Class) && ((Class) t).getPackage().getName().equals(AJC_INTERNAL)) {
                    syntheticCount++;
                }
            }
            this.genericParameterTypes = new Type[genTypes.length - syntheticCount];
            for (int i = 0; i < this.genericParameterTypes.length; i++) {
                if (genTypes[i] instanceof Class) {
                    this.genericParameterTypes[i] = AjTypeSystem.getAjType((Class) genTypes[i]);
                } else {
                    this.genericParameterTypes[i] = genTypes[i];
                }
            }
        }
        return this.genericParameterTypes;
    }

    @Override // org.aspectj.lang.reflect.Advice
    public AjType<?>[] getParameterTypes() {
        if (this.parameterTypes == null) {
            Class<?>[] ptypes = this.adviceMethod.getParameterTypes();
            int syntheticCount = 0;
            for (Class<?> c : ptypes) {
                if (c.getPackage().getName().equals(AJC_INTERNAL)) {
                    syntheticCount++;
                }
            }
            this.parameterTypes = new AjType[ptypes.length - syntheticCount];
            for (int i = 0; i < this.parameterTypes.length; i++) {
                this.parameterTypes[i] = AjTypeSystem.getAjType(ptypes[i]);
            }
        }
        return this.parameterTypes;
    }

    @Override // org.aspectj.lang.reflect.Advice
    public AjType<?>[] getExceptionTypes() {
        if (this.exceptionTypes == null) {
            Class<?>[] exTypes = this.adviceMethod.getExceptionTypes();
            this.exceptionTypes = new AjType[exTypes.length];
            for (int i = 0; i < exTypes.length; i++) {
                this.exceptionTypes[i] = AjTypeSystem.getAjType(exTypes[i]);
            }
        }
        return this.exceptionTypes;
    }

    @Override // org.aspectj.lang.reflect.Advice
    public AdviceKind getKind() {
        return this.kind;
    }

    @Override // org.aspectj.lang.reflect.Advice
    public String getName() {
        String adviceName = this.adviceMethod.getName();
        if (adviceName.startsWith(NameMangler.PREFIX)) {
            adviceName = "";
            AdviceName name = (AdviceName) this.adviceMethod.getAnnotation(AdviceName.class);
            if (name != null) {
                adviceName = name.value();
            }
        }
        return adviceName;
    }

    @Override // org.aspectj.lang.reflect.Advice
    public PointcutExpression getPointcutExpression() {
        return this.pointcutExpression;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        if (getName().length() > 0) {
            sb.append("@AdviceName(\"");
            sb.append(getName());
            sb.append("\") ");
        }
        if (getKind() == AdviceKind.AROUND) {
            sb.append(this.adviceMethod.getGenericReturnType().toString());
            sb.append(SymbolConstants.SPACE_SYMBOL);
        }
        switch (getKind()) {
            case AFTER:
                sb.append("after(");
                break;
            case AFTER_RETURNING:
                sb.append("after(");
                break;
            case AFTER_THROWING:
                sb.append("after(");
                break;
            case AROUND:
                sb.append("around(");
                break;
            case BEFORE:
                sb.append("before(");
                break;
        }
        AjType<?>[] ptypes = getParameterTypes();
        int len = ptypes.length;
        if (this.hasExtraParam) {
            len--;
        }
        for (int i = 0; i < len; i++) {
            sb.append(ptypes[i].getName());
            if (i + 1 < len) {
                sb.append(",");
            }
        }
        sb.append(") ");
        switch (getKind()) {
            case AFTER_RETURNING:
                sb.append("returning");
                if (this.hasExtraParam) {
                    sb.append("(");
                    sb.append(ptypes[len - 1].getName());
                    sb.append(") ");
                }
            case AFTER_THROWING:
                sb.append("throwing");
                if (this.hasExtraParam) {
                    sb.append("(");
                    sb.append(ptypes[len - 1].getName());
                    sb.append(") ");
                    break;
                }
                break;
        }
        AjType<?>[] exTypes = getExceptionTypes();
        if (exTypes.length > 0) {
            sb.append("throws ");
            for (int i2 = 0; i2 < exTypes.length; i2++) {
                sb.append(exTypes[i2].getName());
                if (i2 + 1 < exTypes.length) {
                    sb.append(",");
                }
            }
            sb.append(SymbolConstants.SPACE_SYMBOL);
        }
        sb.append(": ");
        sb.append(getPointcutExpression().asString());
        return sb.toString();
    }
}
