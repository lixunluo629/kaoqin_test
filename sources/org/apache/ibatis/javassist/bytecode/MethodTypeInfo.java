package org.apache.ibatis.javassist.bytecode;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/* compiled from: ConstPool.java */
/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/javassist/bytecode/MethodTypeInfo.class */
class MethodTypeInfo extends ConstInfo {
    static final int tag = 16;
    int descriptor;

    public MethodTypeInfo(int desc, int index) {
        super(index);
        this.descriptor = desc;
    }

    public MethodTypeInfo(DataInputStream in, int index) throws IOException {
        super(index);
        this.descriptor = in.readUnsignedShort();
    }

    public int hashCode() {
        return this.descriptor;
    }

    public boolean equals(Object obj) {
        return (obj instanceof MethodTypeInfo) && ((MethodTypeInfo) obj).descriptor == this.descriptor;
    }

    @Override // org.apache.ibatis.javassist.bytecode.ConstInfo
    public int getTag() {
        return 16;
    }

    @Override // org.apache.ibatis.javassist.bytecode.ConstInfo
    public void renameClass(ConstPool cp, String oldName, String newName, HashMap cache) {
        String desc = cp.getUtf8Info(this.descriptor);
        String desc2 = Descriptor.rename(desc, oldName, newName);
        if (desc != desc2) {
            if (cache == null) {
                this.descriptor = cp.addUtf8Info(desc2);
                return;
            }
            cache.remove(this);
            this.descriptor = cp.addUtf8Info(desc2);
            cache.put(this, this);
        }
    }

    @Override // org.apache.ibatis.javassist.bytecode.ConstInfo
    public void renameClass(ConstPool cp, Map map, HashMap cache) {
        String desc = cp.getUtf8Info(this.descriptor);
        String desc2 = Descriptor.rename(desc, map);
        if (desc != desc2) {
            if (cache == null) {
                this.descriptor = cp.addUtf8Info(desc2);
                return;
            }
            cache.remove(this);
            this.descriptor = cp.addUtf8Info(desc2);
            cache.put(this, this);
        }
    }

    @Override // org.apache.ibatis.javassist.bytecode.ConstInfo
    public int copy(ConstPool src, ConstPool dest, Map map) {
        String desc = src.getUtf8Info(this.descriptor);
        return dest.addMethodTypeInfo(dest.addUtf8Info(Descriptor.rename(desc, map)));
    }

    @Override // org.apache.ibatis.javassist.bytecode.ConstInfo
    public void write(DataOutputStream out) throws IOException {
        out.writeByte(16);
        out.writeShort(this.descriptor);
    }

    @Override // org.apache.ibatis.javassist.bytecode.ConstInfo
    public void print(PrintWriter out) {
        out.print("MethodType #");
        out.println(this.descriptor);
    }
}
