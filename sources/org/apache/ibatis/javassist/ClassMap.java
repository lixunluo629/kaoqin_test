package org.apache.ibatis.javassist;

import java.util.HashMap;
import org.apache.ibatis.javassist.bytecode.Descriptor;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/javassist/ClassMap.class */
public class ClassMap extends HashMap {
    private ClassMap parent;

    public ClassMap() {
        this.parent = null;
    }

    ClassMap(ClassMap map) {
        this.parent = map;
    }

    public void put(CtClass oldname, CtClass newname) {
        put(oldname.getName(), newname.getName());
    }

    public void put(String oldname, String newname) {
        if (oldname == newname) {
            return;
        }
        String oldname2 = toJvmName(oldname);
        String s = (String) get(oldname2);
        if (s == null || !s.equals(oldname2)) {
            super.put((ClassMap) oldname2, toJvmName(newname));
        }
    }

    public void putIfNone(String oldname, String newname) {
        if (oldname == newname) {
            return;
        }
        String oldname2 = toJvmName(oldname);
        String s = (String) get(oldname2);
        if (s == null) {
            super.put((ClassMap) oldname2, toJvmName(newname));
        }
    }

    protected final void put0(Object oldname, Object newname) {
        super.put((ClassMap) oldname, newname);
    }

    @Override // java.util.HashMap, java.util.AbstractMap, java.util.Map
    public Object get(Object jvmClassName) {
        Object found = super.get(jvmClassName);
        if (found == null && this.parent != null) {
            return this.parent.get(jvmClassName);
        }
        return found;
    }

    public void fix(CtClass clazz) {
        fix(clazz.getName());
    }

    public void fix(String name) {
        String name2 = toJvmName(name);
        super.put((ClassMap) name2, name2);
    }

    public static String toJvmName(String classname) {
        return Descriptor.toJvmName(classname);
    }

    public static String toJavaName(String classname) {
        return Descriptor.toJavaName(classname);
    }
}
