package org.springframework.aop.aspectj;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;
import org.aspectj.lang.reflect.SourceLocation;
import org.aspectj.runtime.internal.AroundClosure;
import org.springframework.aop.ProxyMethodInvocation;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.util.Assert;

/* loaded from: spring-aop-4.3.25.RELEASE.jar:org/springframework/aop/aspectj/MethodInvocationProceedingJoinPoint.class */
public class MethodInvocationProceedingJoinPoint implements ProceedingJoinPoint, JoinPoint.StaticPart {
    private static final ParameterNameDiscoverer parameterNameDiscoverer = new DefaultParameterNameDiscoverer();
    private final ProxyMethodInvocation methodInvocation;
    private Object[] args;
    private Signature signature;
    private SourceLocation sourceLocation;

    public MethodInvocationProceedingJoinPoint(ProxyMethodInvocation methodInvocation) {
        Assert.notNull(methodInvocation, "MethodInvocation must not be null");
        this.methodInvocation = methodInvocation;
    }

    @Override // org.aspectj.lang.ProceedingJoinPoint
    public void set$AroundClosure(AroundClosure aroundClosure) {
        throw new UnsupportedOperationException();
    }

    @Override // org.aspectj.lang.ProceedingJoinPoint
    public Object proceed() throws Throwable {
        return this.methodInvocation.invocableClone().proceed();
    }

    @Override // org.aspectj.lang.ProceedingJoinPoint
    public Object proceed(Object[] arguments) throws Throwable {
        Assert.notNull(arguments, "Argument array passed to proceed cannot be null");
        if (arguments.length != this.methodInvocation.getArguments().length) {
            throw new IllegalArgumentException("Expecting " + this.methodInvocation.getArguments().length + " arguments to proceed, but was passed " + arguments.length + " arguments");
        }
        this.methodInvocation.setArguments(arguments);
        return this.methodInvocation.invocableClone(arguments).proceed();
    }

    @Override // org.aspectj.lang.JoinPoint
    public Object getThis() {
        return this.methodInvocation.getProxy();
    }

    @Override // org.aspectj.lang.JoinPoint
    public Object getTarget() {
        return this.methodInvocation.getThis();
    }

    @Override // org.aspectj.lang.JoinPoint
    public Object[] getArgs() {
        if (this.args == null) {
            this.args = (Object[]) this.methodInvocation.getArguments().clone();
        }
        return this.args;
    }

    @Override // org.aspectj.lang.JoinPoint
    public Signature getSignature() {
        if (this.signature == null) {
            this.signature = new MethodSignatureImpl();
        }
        return this.signature;
    }

    @Override // org.aspectj.lang.JoinPoint
    public SourceLocation getSourceLocation() {
        if (this.sourceLocation == null) {
            this.sourceLocation = new SourceLocationImpl();
        }
        return this.sourceLocation;
    }

    @Override // org.aspectj.lang.JoinPoint
    public String getKind() {
        return JoinPoint.METHOD_EXECUTION;
    }

    @Override // org.aspectj.lang.JoinPoint.StaticPart
    public int getId() {
        return 0;
    }

    @Override // org.aspectj.lang.JoinPoint
    public JoinPoint.StaticPart getStaticPart() {
        return this;
    }

    @Override // org.aspectj.lang.JoinPoint
    public String toShortString() {
        return "execution(" + getSignature().toShortString() + ")";
    }

    @Override // org.aspectj.lang.JoinPoint
    public String toLongString() {
        return "execution(" + getSignature().toLongString() + ")";
    }

    @Override // org.aspectj.lang.JoinPoint
    public String toString() {
        return "execution(" + getSignature().toString() + ")";
    }

    /* loaded from: spring-aop-4.3.25.RELEASE.jar:org/springframework/aop/aspectj/MethodInvocationProceedingJoinPoint$MethodSignatureImpl.class */
    private class MethodSignatureImpl implements MethodSignature {
        private volatile String[] parameterNames;

        private MethodSignatureImpl() {
        }

        @Override // org.aspectj.lang.Signature
        public String getName() {
            return MethodInvocationProceedingJoinPoint.this.methodInvocation.getMethod().getName();
        }

        @Override // org.aspectj.lang.Signature
        public int getModifiers() {
            return MethodInvocationProceedingJoinPoint.this.methodInvocation.getMethod().getModifiers();
        }

        @Override // org.aspectj.lang.Signature
        public Class<?> getDeclaringType() {
            return MethodInvocationProceedingJoinPoint.this.methodInvocation.getMethod().getDeclaringClass();
        }

        @Override // org.aspectj.lang.Signature
        public String getDeclaringTypeName() {
            return MethodInvocationProceedingJoinPoint.this.methodInvocation.getMethod().getDeclaringClass().getName();
        }

        @Override // org.aspectj.lang.reflect.MethodSignature
        public Class<?> getReturnType() {
            return MethodInvocationProceedingJoinPoint.this.methodInvocation.getMethod().getReturnType();
        }

        @Override // org.aspectj.lang.reflect.MethodSignature
        public Method getMethod() {
            return MethodInvocationProceedingJoinPoint.this.methodInvocation.getMethod();
        }

        @Override // org.aspectj.lang.reflect.CodeSignature
        public Class<?>[] getParameterTypes() {
            return MethodInvocationProceedingJoinPoint.this.methodInvocation.getMethod().getParameterTypes();
        }

        @Override // org.aspectj.lang.reflect.CodeSignature
        public String[] getParameterNames() {
            if (this.parameterNames == null) {
                this.parameterNames = MethodInvocationProceedingJoinPoint.parameterNameDiscoverer.getParameterNames(getMethod());
            }
            return this.parameterNames;
        }

        @Override // org.aspectj.lang.reflect.CodeSignature
        public Class<?>[] getExceptionTypes() {
            return MethodInvocationProceedingJoinPoint.this.methodInvocation.getMethod().getExceptionTypes();
        }

        @Override // org.aspectj.lang.Signature
        public String toShortString() {
            return toString(false, false, false, false);
        }

        @Override // org.aspectj.lang.Signature
        public String toLongString() {
            return toString(true, true, true, true);
        }

        @Override // org.aspectj.lang.Signature
        public String toString() {
            return toString(false, true, false, true);
        }

        private String toString(boolean includeModifier, boolean includeReturnTypeAndArgs, boolean useLongReturnAndArgumentTypeName, boolean useLongTypeName) {
            StringBuilder sb = new StringBuilder();
            if (includeModifier) {
                sb.append(Modifier.toString(getModifiers()));
                sb.append(SymbolConstants.SPACE_SYMBOL);
            }
            if (includeReturnTypeAndArgs) {
                appendType(sb, getReturnType(), useLongReturnAndArgumentTypeName);
                sb.append(SymbolConstants.SPACE_SYMBOL);
            }
            appendType(sb, getDeclaringType(), useLongTypeName);
            sb.append(".");
            sb.append(getMethod().getName());
            sb.append("(");
            Class<?>[] parametersTypes = getParameterTypes();
            appendTypes(sb, parametersTypes, includeReturnTypeAndArgs, useLongReturnAndArgumentTypeName);
            sb.append(")");
            return sb.toString();
        }

        private void appendTypes(StringBuilder sb, Class<?>[] types, boolean includeArgs, boolean useLongReturnAndArgumentTypeName) {
            if (includeArgs) {
                int size = types.length;
                for (int i = 0; i < size; i++) {
                    appendType(sb, types[i], useLongReturnAndArgumentTypeName);
                    if (i < size - 1) {
                        sb.append(",");
                    }
                }
                return;
            }
            if (types.length != 0) {
                sb.append("..");
            }
        }

        private void appendType(StringBuilder sb, Class<?> type, boolean useLongTypeName) {
            if (type.isArray()) {
                appendType(sb, type.getComponentType(), useLongTypeName);
                sb.append("[]");
            } else {
                sb.append(useLongTypeName ? type.getName() : type.getSimpleName());
            }
        }
    }

    /* loaded from: spring-aop-4.3.25.RELEASE.jar:org/springframework/aop/aspectj/MethodInvocationProceedingJoinPoint$SourceLocationImpl.class */
    private class SourceLocationImpl implements SourceLocation {
        private SourceLocationImpl() {
        }

        @Override // org.aspectj.lang.reflect.SourceLocation
        public Class<?> getWithinType() {
            if (MethodInvocationProceedingJoinPoint.this.methodInvocation.getThis() != null) {
                return MethodInvocationProceedingJoinPoint.this.methodInvocation.getThis().getClass();
            }
            throw new UnsupportedOperationException("No source location joinpoint available: target is null");
        }

        @Override // org.aspectj.lang.reflect.SourceLocation
        public String getFileName() {
            throw new UnsupportedOperationException();
        }

        @Override // org.aspectj.lang.reflect.SourceLocation
        public int getLine() {
            throw new UnsupportedOperationException();
        }

        @Override // org.aspectj.lang.reflect.SourceLocation
        @Deprecated
        public int getColumn() {
            throw new UnsupportedOperationException();
        }
    }
}
