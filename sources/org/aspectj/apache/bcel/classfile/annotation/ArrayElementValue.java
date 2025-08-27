package org.aspectj.apache.bcel.classfile.annotation;

import java.io.DataOutputStream;
import java.io.IOException;
import org.aspectj.apache.bcel.classfile.ConstantPool;
import org.springframework.beans.PropertyAccessor;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/apache/bcel/classfile/annotation/ArrayElementValue.class */
public class ArrayElementValue extends ElementValue {
    private static final ElementValue[] NO_VALUES = new ElementValue[0];
    private ElementValue[] evalues;

    public ElementValue[] getElementValuesArray() {
        return this.evalues;
    }

    public int getElementValuesArraySize() {
        return this.evalues.length;
    }

    public ArrayElementValue(ConstantPool constantPool) {
        super(91, constantPool);
        this.evalues = NO_VALUES;
    }

    public ArrayElementValue(int i, ElementValue[] elementValueArr, ConstantPool constantPool) {
        super(i, constantPool);
        this.evalues = NO_VALUES;
        if (i != 91) {
            throw new RuntimeException("Only element values of type array can be built with this ctor");
        }
        this.evalues = elementValueArr;
    }

    public ArrayElementValue(ArrayElementValue arrayElementValue, ConstantPool constantPool, boolean z) {
        super(91, constantPool);
        this.evalues = NO_VALUES;
        this.evalues = new ElementValue[arrayElementValue.getElementValuesArraySize()];
        ElementValue[] elementValuesArray = arrayElementValue.getElementValuesArray();
        for (int i = 0; i < elementValuesArray.length; i++) {
            this.evalues[i] = ElementValue.copy(elementValuesArray[i], constantPool, z);
        }
    }

    @Override // org.aspectj.apache.bcel.classfile.annotation.ElementValue
    public void dump(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeByte(this.type);
        dataOutputStream.writeShort(this.evalues.length);
        for (int i = 0; i < this.evalues.length; i++) {
            this.evalues[i].dump(dataOutputStream);
        }
    }

    @Override // org.aspectj.apache.bcel.classfile.annotation.ElementValue
    public String stringifyValue() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(PropertyAccessor.PROPERTY_KEY_PREFIX);
        for (int i = 0; i < this.evalues.length; i++) {
            stringBuffer.append(this.evalues[i].stringifyValue());
            if (i + 1 < this.evalues.length) {
                stringBuffer.append(",");
            }
        }
        stringBuffer.append("]");
        return stringBuffer.toString();
    }

    public void addElement(ElementValue elementValue) {
        ElementValue[] elementValueArr = this.evalues;
        this.evalues = new ElementValue[this.evalues.length + 1];
        System.arraycopy(elementValueArr, 0, this.evalues, 0, elementValueArr.length);
        this.evalues[elementValueArr.length] = elementValue;
    }
}
