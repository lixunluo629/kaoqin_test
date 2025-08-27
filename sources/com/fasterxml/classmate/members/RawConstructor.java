package com.fasterxml.classmate.members;

import com.fasterxml.classmate.ResolvedType;
import com.fasterxml.classmate.util.MethodKey;
import java.lang.reflect.Constructor;

/* loaded from: classmate-1.3.4.jar:com/fasterxml/classmate/members/RawConstructor.class */
public final class RawConstructor extends RawMember {
    protected final Constructor<?> _constructor;
    protected final int _hashCode;

    public RawConstructor(ResolvedType context, Constructor<?> constructor) {
        super(context);
        this._constructor = constructor;
        this._hashCode = this._constructor == null ? 0 : this._constructor.hashCode();
    }

    public MethodKey createKey() {
        Class<?>[] argTypes = this._constructor.getParameterTypes();
        return new MethodKey("<init>", argTypes);
    }

    @Override // com.fasterxml.classmate.members.RawMember
    public Constructor<?> getRawMember() {
        return this._constructor;
    }

    @Override // com.fasterxml.classmate.members.RawMember
    public int hashCode() {
        return this._hashCode;
    }

    @Override // com.fasterxml.classmate.members.RawMember
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o == null || o.getClass() != getClass()) {
            return false;
        }
        RawConstructor other = (RawConstructor) o;
        return other._constructor == this._constructor;
    }
}
