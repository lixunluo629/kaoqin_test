package org.aspectj.apache.bcel.generic;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.io.DataOutputStream;
import java.io.IOException;
import org.aspectj.apache.bcel.ExceptionConstants;
import org.aspectj.apache.bcel.classfile.ConstantPool;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/apache/bcel/generic/MULTIANEWARRAY.class */
public class MULTIANEWARRAY extends InstructionCP {
    private short dimensions;

    public MULTIANEWARRAY(int i, short s) {
        super((short) 197, i);
        this.dimensions = s;
    }

    @Override // org.aspectj.apache.bcel.generic.InstructionCP, org.aspectj.apache.bcel.generic.Instruction
    public void dump(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeByte(this.opcode);
        dataOutputStream.writeShort(this.index);
        dataOutputStream.writeByte(this.dimensions);
    }

    public final short getDimensions() {
        return this.dimensions;
    }

    @Override // org.aspectj.apache.bcel.generic.InstructionCP, org.aspectj.apache.bcel.generic.Instruction
    public String toString(boolean z) {
        return super.toString(z) + SymbolConstants.SPACE_SYMBOL + this.index + SymbolConstants.SPACE_SYMBOL + ((int) this.dimensions);
    }

    @Override // org.aspectj.apache.bcel.generic.InstructionCP
    public String toString(ConstantPool constantPool) {
        return super.toString(constantPool) + SymbolConstants.SPACE_SYMBOL + ((int) this.dimensions);
    }

    @Override // org.aspectj.apache.bcel.generic.Instruction
    public int consumeStack(ConstantPool constantPool) {
        return this.dimensions;
    }

    public Class[] getExceptions() {
        Class[] clsArr = new Class[2 + ExceptionConstants.EXCS_CLASS_AND_INTERFACE_RESOLUTION.length];
        System.arraycopy(ExceptionConstants.EXCS_CLASS_AND_INTERFACE_RESOLUTION, 0, clsArr, 0, ExceptionConstants.EXCS_CLASS_AND_INTERFACE_RESOLUTION.length);
        clsArr[ExceptionConstants.EXCS_CLASS_AND_INTERFACE_RESOLUTION.length + 1] = ExceptionConstants.NEGATIVE_ARRAY_SIZE_EXCEPTION;
        clsArr[ExceptionConstants.EXCS_CLASS_AND_INTERFACE_RESOLUTION.length] = ExceptionConstants.ILLEGAL_ACCESS_ERROR;
        return clsArr;
    }

    @Override // org.aspectj.apache.bcel.generic.Instruction
    public ObjectType getLoadClassType(ConstantPool constantPool) {
        Type type = getType(constantPool);
        if (type instanceof ArrayType) {
            type = ((ArrayType) type).getBasicType();
        }
        if (type instanceof ObjectType) {
            return (ObjectType) type;
        }
        return null;
    }

    @Override // org.aspectj.apache.bcel.generic.InstructionCP, org.aspectj.apache.bcel.generic.Instruction
    public boolean equals(Object obj) {
        if (!(obj instanceof MULTIANEWARRAY)) {
            return false;
        }
        MULTIANEWARRAY multianewarray = (MULTIANEWARRAY) obj;
        return multianewarray.opcode == this.opcode && multianewarray.index == this.index && multianewarray.dimensions == this.dimensions;
    }

    @Override // org.aspectj.apache.bcel.generic.InstructionCP, org.aspectj.apache.bcel.generic.Instruction
    public int hashCode() {
        return (this.opcode * 37) + (this.index * (this.dimensions + 17));
    }
}
