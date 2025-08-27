package org.aspectj.apache.bcel.generic;

import com.itextpdf.kernel.pdf.tagging.StandardRoles;
import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.io.DataOutputStream;
import java.io.IOException;
import org.aspectj.apache.bcel.Constants;
import org.aspectj.apache.bcel.classfile.Constant;
import org.aspectj.apache.bcel.classfile.ConstantClass;
import org.aspectj.apache.bcel.classfile.ConstantDouble;
import org.aspectj.apache.bcel.classfile.ConstantFloat;
import org.aspectj.apache.bcel.classfile.ConstantInteger;
import org.aspectj.apache.bcel.classfile.ConstantLong;
import org.aspectj.apache.bcel.classfile.ConstantPool;
import org.aspectj.apache.bcel.classfile.ConstantString;
import org.aspectj.apache.bcel.classfile.ConstantUtf8;
import org.springframework.beans.PropertyAccessor;
import org.springframework.jdbc.datasource.init.ScriptUtils;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/apache/bcel/generic/InstructionCP.class */
public class InstructionCP extends Instruction {
    protected int index;

    public InstructionCP(short s, int i) {
        super(s);
        this.index = i;
    }

    @Override // org.aspectj.apache.bcel.generic.Instruction
    public void dump(DataOutputStream dataOutputStream) throws IOException {
        if (this.opcode == 19 && this.index < 256) {
            dataOutputStream.writeByte(18);
            dataOutputStream.writeByte(this.index);
            return;
        }
        dataOutputStream.writeByte(this.opcode);
        if (Constants.iLen[this.opcode] != 2) {
            dataOutputStream.writeShort(this.index);
        } else {
            if (this.index > 255) {
                throw new IllegalStateException();
            }
            dataOutputStream.writeByte(this.index);
        }
    }

    @Override // org.aspectj.apache.bcel.generic.Instruction
    public int getLength() {
        if (this.opcode != 19 || this.index >= 256) {
            return super.getLength();
        }
        return 2;
    }

    @Override // org.aspectj.apache.bcel.generic.Instruction
    public String toString(boolean z) {
        return super.toString(z) + SymbolConstants.SPACE_SYMBOL + this.index;
    }

    public String toString(ConstantPool constantPool) {
        Constant constant = constantPool.getConstant(this.index);
        String strConstantToString = constantPool.constantToString(constant);
        if (constant instanceof ConstantClass) {
            strConstantToString = strConstantToString.replace('.', '/');
        }
        return Constants.OPCODE_NAMES[this.opcode] + SymbolConstants.SPACE_SYMBOL + strConstantToString;
    }

    @Override // org.aspectj.apache.bcel.generic.Instruction
    public final int getIndex() {
        return this.index;
    }

    @Override // org.aspectj.apache.bcel.generic.Instruction
    public void setIndex(int i) {
        this.index = i;
        if (this.index <= 255 || this.opcode != 18) {
            return;
        }
        this.opcode = (short) 19;
    }

    @Override // org.aspectj.apache.bcel.generic.Instruction
    public Type getType(ConstantPool constantPool) {
        switch (constantPool.getConstant(this.index).getTag()) {
            case 3:
                return Type.INT;
            case 4:
                return Type.FLOAT;
            case 5:
                return Type.LONG;
            case 6:
                return Type.DOUBLE;
            case 7:
                String constantString_CONSTANTClass = constantPool.getConstantString_CONSTANTClass(this.index);
                if (constantString_CONSTANTClass.startsWith(PropertyAccessor.PROPERTY_KEY_PREFIX)) {
                    return Type.getType(constantString_CONSTANTClass);
                }
                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append(StandardRoles.L).append(constantString_CONSTANTClass).append(ScriptUtils.DEFAULT_STATEMENT_SEPARATOR);
                return Type.getType(stringBuffer.toString());
            case 8:
                return Type.STRING;
            default:
                throw new RuntimeException("Unknown or invalid constant type at " + this.index);
        }
    }

    @Override // org.aspectj.apache.bcel.generic.Instruction
    public Object getValue(ConstantPool constantPool) {
        Constant constant = constantPool.getConstant(this.index);
        switch (constant.getTag()) {
            case 3:
                return ((ConstantInteger) constant).getValue();
            case 4:
                return ((ConstantFloat) constant).getValue();
            case 5:
                return ((ConstantLong) constant).getValue();
            case 6:
                return ((ConstantDouble) constant).getValue();
            case 7:
            default:
                throw new RuntimeException("Unknown or invalid constant type at " + this.index);
            case 8:
                return ((ConstantUtf8) constantPool.getConstant(((ConstantString) constant).getStringIndex())).getValue();
        }
    }

    @Override // org.aspectj.apache.bcel.generic.Instruction
    public boolean equals(Object obj) {
        if (!(obj instanceof InstructionCP)) {
            return false;
        }
        InstructionCP instructionCP = (InstructionCP) obj;
        return instructionCP.opcode == this.opcode && instructionCP.index == this.index;
    }

    @Override // org.aspectj.apache.bcel.generic.Instruction
    public int hashCode() {
        return (this.opcode * 37) + this.index;
    }
}
