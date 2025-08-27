package org.apache.ibatis.javassist.bytecode;

import java.io.DataInputStream;
import java.io.IOException;

/* compiled from: ConstPool.java */
/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/javassist/bytecode/MethodrefInfo.class */
class MethodrefInfo extends MemberrefInfo {
    static final int tag = 10;

    public MethodrefInfo(int cindex, int ntindex, int thisIndex) {
        super(cindex, ntindex, thisIndex);
    }

    public MethodrefInfo(DataInputStream in, int thisIndex) throws IOException {
        super(in, thisIndex);
    }

    @Override // org.apache.ibatis.javassist.bytecode.ConstInfo
    public int getTag() {
        return 10;
    }

    @Override // org.apache.ibatis.javassist.bytecode.MemberrefInfo
    public String getTagName() {
        return "Method";
    }

    @Override // org.apache.ibatis.javassist.bytecode.MemberrefInfo
    protected int copy2(ConstPool dest, int cindex, int ntindex) {
        return dest.addMethodrefInfo(cindex, ntindex);
    }
}
