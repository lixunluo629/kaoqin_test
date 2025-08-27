package net.sf.cglib.core;

import org.objectweb.asm.Type;

/* loaded from: cglib-3.1.jar:net/sf/cglib/core/Signature.class */
public class Signature {
    private String name;
    private String desc;

    public Signature(String name, String desc) {
        if (name.indexOf(40) >= 0) {
            throw new IllegalArgumentException(new StringBuffer().append("Name '").append(name).append("' is invalid").toString());
        }
        this.name = name;
        this.desc = desc;
    }

    public Signature(String name, Type returnType, Type[] argumentTypes) {
        this(name, Type.getMethodDescriptor(returnType, argumentTypes));
    }

    public String getName() {
        return this.name;
    }

    public String getDescriptor() {
        return this.desc;
    }

    public Type getReturnType() {
        return Type.getReturnType(this.desc);
    }

    public Type[] getArgumentTypes() {
        return Type.getArgumentTypes(this.desc);
    }

    public String toString() {
        return new StringBuffer().append(this.name).append(this.desc).toString();
    }

    public boolean equals(Object o) {
        if (o == null || !(o instanceof Signature)) {
            return false;
        }
        Signature other = (Signature) o;
        return this.name.equals(other.name) && this.desc.equals(other.desc);
    }

    public int hashCode() {
        return this.name.hashCode() ^ this.desc.hashCode();
    }
}
