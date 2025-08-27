package org.aspectj.apache.bcel.generic;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.util.Iterator;
import org.apache.ibatis.javassist.bytecode.ConstantAttribute;
import org.aspectj.apache.bcel.classfile.Attribute;
import org.aspectj.apache.bcel.classfile.ConstantDouble;
import org.aspectj.apache.bcel.classfile.ConstantFloat;
import org.aspectj.apache.bcel.classfile.ConstantInteger;
import org.aspectj.apache.bcel.classfile.ConstantLong;
import org.aspectj.apache.bcel.classfile.ConstantObject;
import org.aspectj.apache.bcel.classfile.ConstantPool;
import org.aspectj.apache.bcel.classfile.ConstantString;
import org.aspectj.apache.bcel.classfile.ConstantValue;
import org.aspectj.apache.bcel.classfile.Field;
import org.aspectj.apache.bcel.classfile.Node;
import org.aspectj.apache.bcel.classfile.Utility;
import org.aspectj.apache.bcel.classfile.annotation.AnnotationGen;
import org.aspectj.apache.bcel.classfile.annotation.RuntimeAnnos;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/apache/bcel/generic/FieldGen.class */
public class FieldGen extends FieldGenOrMethodGen {
    private Object value;

    public FieldGen(int i, Type type, String str, ConstantPool constantPool) {
        this.value = null;
        setModifiers(i);
        setType(type);
        setName(str);
        setConstantPool(constantPool);
    }

    public FieldGen(Field field, ConstantPool constantPool) {
        this(field.getModifiers(), Type.getType(field.getSignature()), field.getName(), constantPool);
        Attribute[] attributes = field.getAttributes();
        for (int i = 0; i < attributes.length; i++) {
            if (attributes[i] instanceof ConstantValue) {
                setValue(((ConstantValue) attributes[i]).getConstantValueIndex());
            } else if (attributes[i] instanceof RuntimeAnnos) {
                Iterator<AnnotationGen> it = ((RuntimeAnnos) attributes[i]).getAnnotations().iterator();
                while (it.hasNext()) {
                    addAnnotation(new AnnotationGen(it.next(), constantPool, false));
                }
            } else {
                addAttribute(attributes[i]);
            }
        }
    }

    public void setValue(int i) {
        ConstantPool constantPool = this.cp;
        Node constant = constantPool.getConstant(i);
        if (constant instanceof ConstantInteger) {
            this.value = Integer.valueOf(((ConstantInteger) constant).getIntValue());
            return;
        }
        if (constant instanceof ConstantFloat) {
            this.value = ((ConstantFloat) constant).getValue();
            return;
        }
        if (constant instanceof ConstantDouble) {
            this.value = ((ConstantDouble) constant).getValue();
            return;
        }
        if (constant instanceof ConstantLong) {
            this.value = ((ConstantLong) constant).getValue();
        } else if (constant instanceof ConstantString) {
            this.value = ((ConstantString) constant).getString(constantPool);
        } else {
            this.value = ((ConstantObject) constant).getConstantValue(constantPool);
        }
    }

    public void setValue(String str) {
        this.value = str;
    }

    public void wipeValue() {
        this.value = null;
    }

    private void checkType(Type type) {
        if (this.type == null) {
            throw new ClassGenException("You haven't defined the type of the field yet");
        }
        if (!isFinal()) {
            throw new ClassGenException("Only final fields may have an initial value!");
        }
        if (!this.type.equals(type)) {
            throw new ClassGenException("Types are not compatible: " + this.type + " vs. " + type);
        }
    }

    public Field getField() {
        String signature = getSignature();
        int iAddUtf8 = this.cp.addUtf8(this.name);
        int iAddUtf82 = this.cp.addUtf8(signature);
        if (this.value != null) {
            checkType(this.type);
            addAttribute(new ConstantValue(this.cp.addUtf8(ConstantAttribute.tag), 2, addConstant(), this.cp));
        }
        addAnnotationsAsAttribute(this.cp);
        return new Field(this.modifiers, iAddUtf8, iAddUtf82, getAttributesImmutable(), this.cp);
    }

    private int addConstant() {
        switch (this.type.getType()) {
            case 4:
            case 5:
            case 8:
            case 9:
            case 10:
                return this.cp.addInteger(((Integer) this.value).intValue());
            case 6:
                return this.cp.addFloat(((Float) this.value).floatValue());
            case 7:
                return this.cp.addDouble(((Double) this.value).doubleValue());
            case 11:
                return this.cp.addLong(((Long) this.value).longValue());
            case 12:
            case 13:
            default:
                throw new RuntimeException("Oops: Unhandled : " + ((int) this.type.getType()));
            case 14:
                return this.cp.addString((String) this.value);
        }
    }

    @Override // org.aspectj.apache.bcel.generic.FieldGenOrMethodGen
    public String getSignature() {
        return this.type.getSignature();
    }

    public String getInitialValue() {
        if (this.value == null) {
            return null;
        }
        return this.value.toString();
    }

    public void setInitialStringValue(String str) {
        this.value = str;
    }

    public final String toString() {
        String strAccessToString = Utility.accessToString(this.modifiers);
        StringBuffer stringBufferAppend = new StringBuffer(strAccessToString.equals("") ? "" : strAccessToString + SymbolConstants.SPACE_SYMBOL).append(this.type.toString()).append(SymbolConstants.SPACE_SYMBOL).append(getName());
        String initialValue = getInitialValue();
        if (initialValue != null) {
            stringBufferAppend.append(" = ").append(initialValue);
        }
        return stringBufferAppend.toString();
    }
}
