package net.sf.cglib.reflect;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/* loaded from: cglib-3.1.jar:net/sf/cglib/reflect/FastMethod.class */
public class FastMethod extends FastMember {
    FastMethod(FastClass fc, Method method) {
        super(fc, method, helper(fc, method));
    }

    private static int helper(FastClass fc, Method method) {
        int index = fc.getIndex(method.getName(), method.getParameterTypes());
        if (index < 0) {
            Class[] types = method.getParameterTypes();
            System.err.println(new StringBuffer().append("hash=").append(method.getName().hashCode()).append(" size=").append(types.length).toString());
            for (int i = 0; i < types.length; i++) {
                System.err.println(new StringBuffer().append("  types[").append(i).append("]=").append(types[i].getName()).toString());
            }
            throw new IllegalArgumentException(new StringBuffer().append("Cannot find method ").append(method).toString());
        }
        return index;
    }

    public Class getReturnType() {
        return ((Method) this.member).getReturnType();
    }

    @Override // net.sf.cglib.reflect.FastMember
    public Class[] getParameterTypes() {
        return ((Method) this.member).getParameterTypes();
    }

    @Override // net.sf.cglib.reflect.FastMember
    public Class[] getExceptionTypes() {
        return ((Method) this.member).getExceptionTypes();
    }

    public Object invoke(Object obj, Object[] args) throws InvocationTargetException {
        return this.fc.invoke(this.index, obj, args);
    }

    public Method getJavaMethod() {
        return (Method) this.member;
    }
}
