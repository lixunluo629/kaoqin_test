package org.apache.ibatis.javassist.bytecode;

import java.io.DataInputStream;
import java.io.IOException;

/* compiled from: ConstPool.java */
/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/javassist/bytecode/FieldrefInfo.class */
class FieldrefInfo extends MemberrefInfo {
    static final int tag = 9;

    public FieldrefInfo(int cindex, int ntindex, int thisIndex) {
        super(cindex, ntindex, thisIndex);
    }

    public FieldrefInfo(DataInputStream in, int thisIndex) throws IOException {
        super(in, thisIndex);
    }

    @Override // org.apache.ibatis.javassist.bytecode.ConstInfo
    public int getTag() {
        return 9;
    }

    @Override // org.apache.ibatis.javassist.bytecode.MemberrefInfo
    public String getTagName() {
        return "Field";
    }

    @Override // org.apache.ibatis.javassist.bytecode.MemberrefInfo
    protected int copy2(ConstPool dest, int cindex, int ntindex) {
        return dest.addFieldrefInfo(cindex, ntindex);
    }
}
