package org.aspectj.apache.bcel.classfile.annotation;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.io.DataOutputStream;
import java.io.IOException;
import org.aspectj.apache.bcel.classfile.ConstantPool;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/apache/bcel/classfile/annotation/NameValuePair.class */
public class NameValuePair {
    private int nameIdx;
    private ElementValue value;
    private ConstantPool cpool;

    public NameValuePair(NameValuePair nameValuePair, ConstantPool constantPool, boolean z) {
        this.cpool = constantPool;
        if (z) {
            this.nameIdx = constantPool.addUtf8(nameValuePair.getNameString());
        } else {
            this.nameIdx = nameValuePair.getNameIndex();
        }
        this.value = ElementValue.copy(nameValuePair.getValue(), constantPool, z);
    }

    protected NameValuePair(int i, ElementValue elementValue, ConstantPool constantPool) {
        this.nameIdx = i;
        this.value = elementValue;
        this.cpool = constantPool;
    }

    public NameValuePair(String str, ElementValue elementValue, ConstantPool constantPool) {
        this.nameIdx = constantPool.addUtf8(str);
        this.value = elementValue;
        this.cpool = constantPool;
    }

    protected void dump(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeShort(this.nameIdx);
        this.value.dump(dataOutputStream);
    }

    public int getNameIndex() {
        return this.nameIdx;
    }

    public final String getNameString() {
        return this.cpool.getConstantUtf8(this.nameIdx).getValue();
    }

    public final ElementValue getValue() {
        return this.value;
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(getNameString()).append(SymbolConstants.EQUAL_SYMBOL).append(this.value.stringifyValue());
        return stringBuffer.toString();
    }
}
