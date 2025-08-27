package org.aspectj.runtime.reflect;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.lang.reflect.Method;
import java.util.StringTokenizer;
import org.aspectj.lang.reflect.AdviceSignature;
import org.springframework.beans.factory.support.PropertiesBeanDefinitionReader;

/* JADX WARN: Classes with same name are omitted:
  aspectjrt-1.8.14.jar:org/aspectj/runtime/reflect/AdviceSignatureImpl.class
 */
/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/runtime/reflect/AdviceSignatureImpl.class */
class AdviceSignatureImpl extends CodeSignatureImpl implements AdviceSignature {
    Class returnType;
    private Method adviceMethod;

    AdviceSignatureImpl(int modifiers, String name, Class declaringType, Class[] parameterTypes, String[] parameterNames, Class[] exceptionTypes, Class returnType) {
        super(modifiers, name, declaringType, parameterTypes, parameterNames, exceptionTypes);
        this.adviceMethod = null;
        this.returnType = returnType;
    }

    AdviceSignatureImpl(String stringRep) {
        super(stringRep);
        this.adviceMethod = null;
    }

    @Override // org.aspectj.lang.reflect.AdviceSignature
    public Class getReturnType() {
        if (this.returnType == null) {
            this.returnType = extractType(6);
        }
        return this.returnType;
    }

    @Override // org.aspectj.runtime.reflect.SignatureImpl
    protected String createToString(StringMaker sm) {
        StringBuffer buf = new StringBuffer();
        if (sm.includeArgs) {
            buf.append(sm.makeTypeName(getReturnType()));
        }
        if (sm.includeArgs) {
            buf.append(SymbolConstants.SPACE_SYMBOL);
        }
        buf.append(sm.makePrimaryTypeName(getDeclaringType(), getDeclaringTypeName()));
        buf.append(".");
        buf.append(toAdviceName(getName()));
        sm.addSignature(buf, getParameterTypes());
        sm.addThrows(buf, getExceptionTypes());
        return buf.toString();
    }

    private String toAdviceName(String methodName) {
        if (methodName.indexOf(36) == -1) {
            return methodName;
        }
        StringTokenizer strTok = new StringTokenizer(methodName, PropertiesBeanDefinitionReader.CONSTRUCTOR_ARG_PREFIX);
        while (strTok.hasMoreTokens()) {
            String token = strTok.nextToken();
            if (token.startsWith("before") || token.startsWith("after") || token.startsWith("around")) {
                return token;
            }
        }
        return methodName;
    }

    @Override // org.aspectj.lang.reflect.AdviceSignature
    public Method getAdvice() {
        if (this.adviceMethod == null) {
            try {
                this.adviceMethod = getDeclaringType().getDeclaredMethod(getName(), getParameterTypes());
            } catch (Exception e) {
            }
        }
        return this.adviceMethod;
    }
}
