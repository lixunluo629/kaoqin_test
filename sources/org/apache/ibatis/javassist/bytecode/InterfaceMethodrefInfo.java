package org.apache.ibatis.javassist.bytecode;

import java.io.DataInputStream;
import java.io.IOException;

/* compiled from: ConstPool.java */
/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/javassist/bytecode/InterfaceMethodrefInfo.class */
class InterfaceMethodrefInfo extends MemberrefInfo {
    static final int tag = 11;

    public InterfaceMethodrefInfo(int cindex, int ntindex, int thisIndex) {
        super(cindex, ntindex, thisIndex);
    }

    public InterfaceMethodrefInfo(DataInputStream in, int thisIndex) throws IOException {
        super(in, thisIndex);
    }

    @Override // org.apache.ibatis.javassist.bytecode.ConstInfo
    public int getTag() {
        return 11;
    }

    @Override // org.apache.ibatis.javassist.bytecode.MemberrefInfo
    public String getTagName() {
        return "Interface";
    }

    @Override // org.apache.ibatis.javassist.bytecode.MemberrefInfo
    protected int copy2(ConstPool dest, int cindex, int ntindex) {
        return dest.addInterfaceMethodrefInfo(cindex, ntindex);
    }
}
