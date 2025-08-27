package org.apache.ibatis.javassist.bytecode;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

/* compiled from: ConstPool.java */
/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/javassist/bytecode/FloatInfo.class */
class FloatInfo extends ConstInfo {
    static final int tag = 4;
    float value;

    public FloatInfo(float f, int index) {
        super(index);
        this.value = f;
    }

    public FloatInfo(DataInputStream in, int index) throws IOException {
        super(index);
        this.value = in.readFloat();
    }

    public int hashCode() {
        return Float.floatToIntBits(this.value);
    }

    public boolean equals(Object obj) {
        return (obj instanceof FloatInfo) && ((FloatInfo) obj).value == this.value;
    }

    @Override // org.apache.ibatis.javassist.bytecode.ConstInfo
    public int getTag() {
        return 4;
    }

    @Override // org.apache.ibatis.javassist.bytecode.ConstInfo
    public int copy(ConstPool src, ConstPool dest, Map map) {
        return dest.addFloatInfo(this.value);
    }

    @Override // org.apache.ibatis.javassist.bytecode.ConstInfo
    public void write(DataOutputStream out) throws IOException {
        out.writeByte(4);
        out.writeFloat(this.value);
    }

    @Override // org.apache.ibatis.javassist.bytecode.ConstInfo
    public void print(PrintWriter out) {
        out.print("Float ");
        out.println(this.value);
    }
}
