package org.aspectj.apache.bcel.generic;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.io.DataOutputStream;
import java.io.IOException;
import org.aspectj.apache.bcel.classfile.ConstantPool;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/apache/bcel/generic/RET.class */
public class RET extends Instruction {
    private boolean wide;
    private int index;

    public RET(int i, boolean z) {
        super((short) 169);
        this.index = i;
        this.wide = z;
    }

    @Override // org.aspectj.apache.bcel.generic.Instruction
    public void dump(DataOutputStream dataOutputStream) throws IOException {
        if (this.wide) {
            dataOutputStream.writeByte(196);
        }
        dataOutputStream.writeByte(this.opcode);
        if (this.wide) {
            dataOutputStream.writeShort(this.index);
        } else {
            dataOutputStream.writeByte(this.index);
        }
    }

    @Override // org.aspectj.apache.bcel.generic.Instruction
    public int getLength() {
        return this.wide ? 4 : 2;
    }

    @Override // org.aspectj.apache.bcel.generic.Instruction
    public final int getIndex() {
        return this.index;
    }

    @Override // org.aspectj.apache.bcel.generic.Instruction
    public final void setIndex(int i) {
        this.index = i;
        this.wide = i > 255;
    }

    @Override // org.aspectj.apache.bcel.generic.Instruction
    public String toString(boolean z) {
        return super.toString(z) + SymbolConstants.SPACE_SYMBOL + this.index;
    }

    @Override // org.aspectj.apache.bcel.generic.Instruction
    public Type getType(ConstantPool constantPool) {
        return ReturnaddressType.NO_TARGET;
    }

    @Override // org.aspectj.apache.bcel.generic.Instruction
    public boolean equals(Object obj) {
        if (!(obj instanceof RET)) {
            return false;
        }
        RET ret = (RET) obj;
        return ret.opcode == this.opcode && ret.index == this.index;
    }

    @Override // org.aspectj.apache.bcel.generic.Instruction
    public int hashCode() {
        return (this.opcode * 37) + this.index;
    }
}
