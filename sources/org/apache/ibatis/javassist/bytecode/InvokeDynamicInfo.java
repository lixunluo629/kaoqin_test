package org.apache.ibatis.javassist.bytecode;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

/* compiled from: ConstPool.java */
/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/javassist/bytecode/InvokeDynamicInfo.class */
class InvokeDynamicInfo extends ConstInfo {
    static final int tag = 18;
    int bootstrap;
    int nameAndType;

    public InvokeDynamicInfo(int bootstrapMethod, int ntIndex, int index) {
        super(index);
        this.bootstrap = bootstrapMethod;
        this.nameAndType = ntIndex;
    }

    public InvokeDynamicInfo(DataInputStream in, int index) throws IOException {
        super(index);
        this.bootstrap = in.readUnsignedShort();
        this.nameAndType = in.readUnsignedShort();
    }

    public int hashCode() {
        return (this.bootstrap << 16) ^ this.nameAndType;
    }

    public boolean equals(Object obj) {
        if (obj instanceof InvokeDynamicInfo) {
            InvokeDynamicInfo iv = (InvokeDynamicInfo) obj;
            return iv.bootstrap == this.bootstrap && iv.nameAndType == this.nameAndType;
        }
        return false;
    }

    @Override // org.apache.ibatis.javassist.bytecode.ConstInfo
    public int getTag() {
        return 18;
    }

    @Override // org.apache.ibatis.javassist.bytecode.ConstInfo
    public int copy(ConstPool src, ConstPool dest, Map map) {
        return dest.addInvokeDynamicInfo(this.bootstrap, src.getItem(this.nameAndType).copy(src, dest, map));
    }

    @Override // org.apache.ibatis.javassist.bytecode.ConstInfo
    public void write(DataOutputStream out) throws IOException {
        out.writeByte(18);
        out.writeShort(this.bootstrap);
        out.writeShort(this.nameAndType);
    }

    @Override // org.apache.ibatis.javassist.bytecode.ConstInfo
    public void print(PrintWriter out) {
        out.print("InvokeDynamic #");
        out.print(this.bootstrap);
        out.print(", name&type #");
        out.println(this.nameAndType);
    }
}
