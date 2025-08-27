package org.aspectj.apache.bcel.classfile.annotation;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;
import org.aspectj.apache.bcel.classfile.ConstantPool;
import org.aspectj.apache.bcel.classfile.ConstantUtf8;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/apache/bcel/classfile/annotation/AnnotationElementValue.class */
public class AnnotationElementValue extends ElementValue {
    private AnnotationGen a;
    static final /* synthetic */ boolean $assertionsDisabled;

    public AnnotationElementValue(AnnotationGen annotationGen, ConstantPool constantPool) {
        super(64, constantPool);
        this.a = annotationGen;
    }

    public AnnotationElementValue(int i, AnnotationGen annotationGen, ConstantPool constantPool) {
        super(i, constantPool);
        if (!$assertionsDisabled && i != 64) {
            throw new AssertionError();
        }
        this.a = annotationGen;
    }

    public AnnotationElementValue(AnnotationElementValue annotationElementValue, ConstantPool constantPool, boolean z) {
        super(64, constantPool);
        this.a = new AnnotationGen(annotationElementValue.getAnnotation(), constantPool, z);
    }

    @Override // org.aspectj.apache.bcel.classfile.annotation.ElementValue
    public void dump(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeByte(this.type);
        this.a.dump(dataOutputStream);
    }

    @Override // org.aspectj.apache.bcel.classfile.annotation.ElementValue
    public String stringifyValue() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(((ConstantUtf8) this.cpool.getConstant(this.a.getTypeIndex(), (byte) 1)).getValue());
        List<NameValuePair> values = this.a.getValues();
        if (values != null && values.size() > 0) {
            stringBuffer.append("(");
            for (int i = 0; i < values.size(); i++) {
                if (i > 0) {
                    stringBuffer.append(",");
                }
                stringBuffer.append(values.get(i).getNameString()).append(SymbolConstants.EQUAL_SYMBOL).append(values.get(i).getValue().stringifyValue());
            }
            stringBuffer.append(")");
        }
        return stringBuffer.toString();
    }

    public AnnotationGen getAnnotation() {
        return this.a;
    }

    static {
        $assertionsDisabled = !AnnotationElementValue.class.desiredAssertionStatus();
    }
}
