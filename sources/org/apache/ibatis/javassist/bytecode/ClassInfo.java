package org.apache.ibatis.javassist.bytecode;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/* compiled from: ConstPool.java */
/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/javassist/bytecode/ClassInfo.class */
class ClassInfo extends ConstInfo {
    static final int tag = 7;
    int name;

    public ClassInfo(int className, int index) {
        super(index);
        this.name = className;
    }

    public ClassInfo(DataInputStream in, int index) throws IOException {
        super(index);
        this.name = in.readUnsignedShort();
    }

    public int hashCode() {
        return this.name;
    }

    public boolean equals(Object obj) {
        return (obj instanceof ClassInfo) && ((ClassInfo) obj).name == this.name;
    }

    @Override // org.apache.ibatis.javassist.bytecode.ConstInfo
    public int getTag() {
        return 7;
    }

    @Override // org.apache.ibatis.javassist.bytecode.ConstInfo
    public String getClassName(ConstPool cp) {
        return cp.getUtf8Info(this.name);
    }

    @Override // org.apache.ibatis.javassist.bytecode.ConstInfo
    public void renameClass(ConstPool cp, String oldName, String newName, HashMap cache) {
        String s;
        String nameStr = cp.getUtf8Info(this.name);
        String newNameStr = null;
        if (nameStr.equals(oldName)) {
            newNameStr = newName;
        } else if (nameStr.charAt(0) == '[' && nameStr != (s = Descriptor.rename(nameStr, oldName, newName))) {
            newNameStr = s;
        }
        if (newNameStr != null) {
            if (cache == null) {
                this.name = cp.addUtf8Info(newNameStr);
                return;
            }
            cache.remove(this);
            this.name = cp.addUtf8Info(newNameStr);
            cache.put(this, this);
        }
    }

    @Override // org.apache.ibatis.javassist.bytecode.ConstInfo
    public void renameClass(ConstPool cp, Map map, HashMap cache) {
        String oldName = cp.getUtf8Info(this.name);
        String newName = null;
        if (oldName.charAt(0) == '[') {
            String s = Descriptor.rename(oldName, map);
            if (oldName != s) {
                newName = s;
            }
        } else {
            String s2 = (String) map.get(oldName);
            if (s2 != null && !s2.equals(oldName)) {
                newName = s2;
            }
        }
        if (newName != null) {
            if (cache == null) {
                this.name = cp.addUtf8Info(newName);
                return;
            }
            cache.remove(this);
            this.name = cp.addUtf8Info(newName);
            cache.put(this, this);
        }
    }

    @Override // org.apache.ibatis.javassist.bytecode.ConstInfo
    public int copy(ConstPool src, ConstPool dest, Map map) {
        String newname;
        String classname = src.getUtf8Info(this.name);
        if (map != null && (newname = (String) map.get(classname)) != null) {
            classname = newname;
        }
        return dest.addClassInfo(classname);
    }

    @Override // org.apache.ibatis.javassist.bytecode.ConstInfo
    public void write(DataOutputStream out) throws IOException {
        out.writeByte(7);
        out.writeShort(this.name);
    }

    @Override // org.apache.ibatis.javassist.bytecode.ConstInfo
    public void print(PrintWriter out) {
        out.print("Class #");
        out.println(this.name);
    }
}
