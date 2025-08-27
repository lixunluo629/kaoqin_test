package org.aspectj.apache.bcel.classfile.annotation;

import java.io.DataOutputStream;
import java.io.IOException;
import org.aspectj.apache.bcel.classfile.ConstantDouble;
import org.aspectj.apache.bcel.classfile.ConstantFloat;
import org.aspectj.apache.bcel.classfile.ConstantInteger;
import org.aspectj.apache.bcel.classfile.ConstantLong;
import org.aspectj.apache.bcel.classfile.ConstantPool;
import org.aspectj.apache.bcel.classfile.ConstantUtf8;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/apache/bcel/classfile/annotation/SimpleElementValue.class */
public class SimpleElementValue extends ElementValue {
    private int idx;

    protected SimpleElementValue(int i, int i2, ConstantPool constantPool) {
        super(i, constantPool);
        this.idx = i2;
    }

    public SimpleElementValue(int i, ConstantPool constantPool, int i2) {
        super(i, constantPool);
        this.idx = constantPool.addInteger(i2);
    }

    public SimpleElementValue(int i, ConstantPool constantPool, long j) {
        super(i, constantPool);
        this.idx = constantPool.addLong(j);
    }

    public SimpleElementValue(int i, ConstantPool constantPool, double d) {
        super(i, constantPool);
        this.idx = constantPool.addDouble(d);
    }

    public SimpleElementValue(int i, ConstantPool constantPool, float f) {
        super(i, constantPool);
        this.idx = constantPool.addFloat(f);
    }

    public SimpleElementValue(int i, ConstantPool constantPool, short s) {
        super(i, constantPool);
        this.idx = constantPool.addInteger(s);
    }

    public SimpleElementValue(int i, ConstantPool constantPool, byte b) {
        super(i, constantPool);
        this.idx = constantPool.addInteger(b);
    }

    public SimpleElementValue(int i, ConstantPool constantPool, char c) {
        super(i, constantPool);
        this.idx = constantPool.addInteger(c);
    }

    public SimpleElementValue(int i, ConstantPool constantPool, boolean z) {
        super(i, constantPool);
        if (z) {
            this.idx = constantPool.addInteger(1);
        } else {
            this.idx = constantPool.addInteger(0);
        }
    }

    public SimpleElementValue(int i, ConstantPool constantPool, String str) {
        super(i, constantPool);
        this.idx = constantPool.addUtf8(str);
    }

    public byte getValueByte() {
        if (this.type != 66) {
            throw new RuntimeException("Dont call getValueByte() on a non BYTE ElementValue");
        }
        return (byte) ((ConstantInteger) this.cpool.getConstant(this.idx, (byte) 3)).getIntValue();
    }

    public char getValueChar() {
        if (this.type != 67) {
            throw new RuntimeException("Dont call getValueChar() on a non CHAR ElementValue");
        }
        return (char) ((ConstantInteger) this.cpool.getConstant(this.idx, (byte) 3)).getIntValue();
    }

    public long getValueLong() {
        if (this.type != 74) {
            throw new RuntimeException("Dont call getValueLong() on a non LONG ElementValue");
        }
        return ((ConstantLong) this.cpool.getConstant(this.idx)).getValue().longValue();
    }

    public float getValueFloat() {
        if (this.type != 70) {
            throw new RuntimeException("Dont call getValueFloat() on a non FLOAT ElementValue");
        }
        return ((ConstantFloat) this.cpool.getConstant(this.idx)).getValue().floatValue();
    }

    public double getValueDouble() {
        if (this.type != 68) {
            throw new RuntimeException("Dont call getValueDouble() on a non DOUBLE ElementValue");
        }
        return ((ConstantDouble) this.cpool.getConstant(this.idx)).getValue().doubleValue();
    }

    public boolean getValueBoolean() {
        if (this.type != 90) {
            throw new RuntimeException("Dont call getValueBoolean() on a non BOOLEAN ElementValue");
        }
        return ((ConstantInteger) this.cpool.getConstant(this.idx)).getValue().intValue() != 0;
    }

    public short getValueShort() {
        if (this.type != 83) {
            throw new RuntimeException("Dont call getValueShort() on a non SHORT ElementValue");
        }
        return (short) ((ConstantInteger) this.cpool.getConstant(this.idx)).getIntValue();
    }

    public SimpleElementValue(SimpleElementValue simpleElementValue, ConstantPool constantPool, boolean z) {
        super(simpleElementValue.getElementValueType(), constantPool);
        if (!z) {
            this.idx = simpleElementValue.getIndex();
            return;
        }
        switch (simpleElementValue.getElementValueType()) {
            case 66:
                this.idx = constantPool.addInteger(simpleElementValue.getValueByte());
                return;
            case 67:
                this.idx = constantPool.addInteger(simpleElementValue.getValueChar());
                return;
            case 68:
                this.idx = constantPool.addDouble(simpleElementValue.getValueDouble());
                return;
            case 70:
                this.idx = constantPool.addFloat(simpleElementValue.getValueFloat());
                return;
            case 73:
                this.idx = constantPool.addInteger(simpleElementValue.getValueInt());
                return;
            case 74:
                this.idx = constantPool.addLong(simpleElementValue.getValueLong());
                return;
            case 83:
                this.idx = constantPool.addInteger(simpleElementValue.getValueShort());
                return;
            case 90:
                if (simpleElementValue.getValueBoolean()) {
                    this.idx = constantPool.addInteger(1);
                    return;
                } else {
                    this.idx = constantPool.addInteger(0);
                    return;
                }
            case 115:
                this.idx = constantPool.addUtf8(simpleElementValue.getValueString());
                return;
            default:
                throw new RuntimeException("SimpleElementValueGen class does not know how to copy this type " + this.type);
        }
    }

    public int getIndex() {
        return this.idx;
    }

    public String getValueString() {
        if (this.type != 115) {
            throw new RuntimeException("Dont call getValueString() on a non STRING ElementValue");
        }
        return ((ConstantUtf8) this.cpool.getConstant(this.idx)).getValue();
    }

    public int getValueInt() {
        if (this.type != 73) {
            throw new RuntimeException("Dont call getValueString() on a non STRING ElementValue");
        }
        return ((ConstantInteger) this.cpool.getConstant(this.idx)).getValue().intValue();
    }

    @Override // org.aspectj.apache.bcel.classfile.annotation.ElementValue
    public String stringifyValue() {
        switch (this.type) {
            case 66:
                return Integer.toString(((ConstantInteger) this.cpool.getConstant(this.idx)).getValue().intValue());
            case 67:
                return new Character((char) ((ConstantInteger) this.cpool.getConstant(this.idx)).getIntValue()).toString();
            case 68:
                return ((ConstantDouble) this.cpool.getConstant(this.idx)).getValue().toString();
            case 70:
                return Float.toString(((ConstantFloat) this.cpool.getConstant(this.idx)).getValue().floatValue());
            case 73:
                return Integer.toString(((ConstantInteger) this.cpool.getConstant(this.idx)).getValue().intValue());
            case 74:
                return Long.toString(((ConstantLong) this.cpool.getConstant(this.idx)).getValue().longValue());
            case 83:
                return Integer.toString(((ConstantInteger) this.cpool.getConstant(this.idx)).getValue().intValue());
            case 90:
                return ((ConstantInteger) this.cpool.getConstant(this.idx)).getValue().intValue() == 0 ? "false" : "true";
            case 115:
                return ((ConstantUtf8) this.cpool.getConstant(this.idx)).getValue();
            default:
                throw new RuntimeException("SimpleElementValueGen class does not know how to stringify type " + this.type);
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        switch (this.type) {
            case 66:
                sb.append("(byte)").append(Integer.toString(((ConstantInteger) this.cpool.getConstant(this.idx)).getValue().intValue()));
                break;
            case 67:
                sb.append("(char)").append(new Character((char) ((ConstantInteger) this.cpool.getConstant(this.idx)).getIntValue()).toString());
                break;
            case 68:
                sb.append("(double)").append(((ConstantDouble) this.cpool.getConstant(this.idx)).getValue().toString());
                break;
            case 70:
                sb.append("(float)").append(Float.toString(((ConstantFloat) this.cpool.getConstant(this.idx)).getValue().floatValue()));
                break;
            case 73:
                sb.append("(int)").append(Integer.toString(((ConstantInteger) this.cpool.getConstant(this.idx)).getValue().intValue()));
                break;
            case 74:
                sb.append("(long)").append(Long.toString(((ConstantLong) this.cpool.getConstant(this.idx)).getValue().longValue()));
                break;
            case 83:
                sb.append("(short)").append(Integer.toString(((ConstantInteger) this.cpool.getConstant(this.idx)).getValue().intValue()));
                break;
            case 90:
                ConstantInteger constantInteger = (ConstantInteger) this.cpool.getConstant(this.idx);
                sb.append("(boolean)");
                if (constantInteger.getValue().intValue() != 0) {
                    sb.append("true");
                    break;
                } else {
                    sb.append("false");
                    break;
                }
            case 115:
                sb.append("(string)").append(((ConstantUtf8) this.cpool.getConstant(this.idx)).getValue());
                break;
            default:
                throw new RuntimeException("SimpleElementValueGen class does not know how to stringify type " + this.type);
        }
        return sb.toString();
    }

    @Override // org.aspectj.apache.bcel.classfile.annotation.ElementValue
    public void dump(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeByte(this.type);
        switch (this.type) {
            case 66:
            case 67:
            case 68:
            case 70:
            case 73:
            case 74:
            case 83:
            case 90:
            case 115:
                dataOutputStream.writeShort(this.idx);
                return;
            default:
                throw new RuntimeException("SimpleElementValueGen doesnt know how to write out type " + this.type);
        }
    }
}
