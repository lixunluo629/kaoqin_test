package org.apache.ibatis.javassist.bytecode;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

/* compiled from: ConstPool.java */
/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/javassist/bytecode/ConstInfoPadding.class */
class ConstInfoPadding extends ConstInfo {
    public ConstInfoPadding(int i) {
        super(i);
    }

    @Override // org.apache.ibatis.javassist.bytecode.ConstInfo
    public int getTag() {
        return 0;
    }

    @Override // org.apache.ibatis.javassist.bytecode.ConstInfo
    public int copy(ConstPool src, ConstPool dest, Map map) {
        return dest.addConstInfoPadding();
    }

    @Override // org.apache.ibatis.javassist.bytecode.ConstInfo
    public void write(DataOutputStream out) throws IOException {
    }

    @Override // org.apache.ibatis.javassist.bytecode.ConstInfo
    public void print(PrintWriter out) {
        out.println("padding");
    }
}
