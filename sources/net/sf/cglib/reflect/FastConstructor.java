package net.sf.cglib.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/* loaded from: cglib-3.1.jar:net/sf/cglib/reflect/FastConstructor.class */
public class FastConstructor extends FastMember {
    FastConstructor(FastClass fc, Constructor constructor) {
        super(fc, constructor, fc.getIndex(constructor.getParameterTypes()));
    }

    @Override // net.sf.cglib.reflect.FastMember
    public Class[] getParameterTypes() {
        return ((Constructor) this.member).getParameterTypes();
    }

    @Override // net.sf.cglib.reflect.FastMember
    public Class[] getExceptionTypes() {
        return ((Constructor) this.member).getExceptionTypes();
    }

    public Object newInstance() throws InvocationTargetException {
        return this.fc.newInstance(this.index, (Object[]) null);
    }

    public Object newInstance(Object[] args) throws InvocationTargetException {
        return this.fc.newInstance(this.index, args);
    }

    public Constructor getJavaConstructor() {
        return (Constructor) this.member;
    }
}
