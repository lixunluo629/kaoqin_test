package org.aspectj.apache.bcel.classfile;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.aspectj.apache.bcel.Constants;
import org.aspectj.apache.bcel.generic.ArrayType;
import org.aspectj.apache.bcel.generic.ObjectType;
import org.springframework.jdbc.datasource.init.ScriptUtils;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/apache/bcel/classfile/ConstantPool.class */
public class ConstantPool implements Node {
    private Constant[] pool;
    private int poolSize;
    private Map<String, Integer> utf8Cache;
    private Map<String, Integer> methodCache;
    private Map<String, Integer> fieldCache;
    static final /* synthetic */ boolean $assertionsDisabled;

    public int getSize() {
        return this.poolSize;
    }

    public ConstantPool() {
        this.utf8Cache = new HashMap();
        this.methodCache = new HashMap();
        this.fieldCache = new HashMap();
        this.pool = new Constant[10];
        this.poolSize = 0;
    }

    public ConstantPool(Constant[] constantArr) {
        this.utf8Cache = new HashMap();
        this.methodCache = new HashMap();
        this.fieldCache = new HashMap();
        this.pool = constantArr;
        this.poolSize = constantArr == null ? 0 : constantArr.length;
    }

    ConstantPool(DataInputStream dataInputStream) throws IOException {
        this.utf8Cache = new HashMap();
        this.methodCache = new HashMap();
        this.fieldCache = new HashMap();
        this.poolSize = dataInputStream.readUnsignedShort();
        this.pool = new Constant[this.poolSize];
        int i = 1;
        while (i < this.poolSize) {
            this.pool[i] = Constant.readConstant(dataInputStream);
            byte tag = this.pool[i].getTag();
            if (tag == 6 || tag == 5) {
                i++;
            }
            i++;
        }
    }

    public Constant getConstant(int i, byte b) {
        Constant constant = getConstant(i);
        if (constant.tag == b) {
            return constant;
        }
        throw new ClassFormatException("Expected class '" + Constants.CONSTANT_NAMES[b] + "' at index " + i + " and found " + constant);
    }

    public Constant getConstant(int i) {
        try {
            return this.pool[i];
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new ClassFormatException("Index " + i + " into constant pool (size:" + this.poolSize + ") is invalid");
        }
    }

    public ConstantPool copy() {
        Constant[] constantArr = new Constant[this.poolSize];
        for (int i = 1; i < this.poolSize; i++) {
            if (this.pool[i] != null) {
                constantArr[i] = this.pool[i].copy();
            }
        }
        return new ConstantPool(constantArr);
    }

    public String getConstantString(int i, byte b) throws ClassFormatException {
        int stringIndex;
        Constant constant = getConstant(i, b);
        switch (b) {
            case 7:
                stringIndex = ((ConstantClass) constant).getNameIndex();
                break;
            case 8:
                stringIndex = ((ConstantString) constant).getStringIndex();
                break;
            default:
                throw new RuntimeException("getConstantString called with illegal tag " + ((int) b));
        }
        return ((ConstantUtf8) getConstant(stringIndex, (byte) 1)).getValue();
    }

    /* JADX WARN: Multi-variable type inference failed */
    public String constantToString(Constant constant) {
        String string;
        switch (constant.tag) {
            case 1:
            case 3:
            case 4:
            case 5:
            case 6:
                string = ((SimpleConstant) constant).getStringValue();
                break;
            case 2:
            case 13:
            case 14:
            case 15:
            case 16:
            case 17:
            default:
                throw new RuntimeException("Unknown constant type " + ((int) constant.tag));
            case 7:
                string = Utility.compactClassName(((ConstantUtf8) getConstant(((ConstantClass) constant).getNameIndex(), (byte) 1)).getValue(), false);
                break;
            case 8:
                string = SymbolConstants.QUOTES_SYMBOL + escape(((ConstantUtf8) getConstant(((ConstantString) constant).getStringIndex(), (byte) 1)).getValue()) + SymbolConstants.QUOTES_SYMBOL;
                break;
            case 9:
            case 10:
            case 11:
                string = constantToString(((ConstantCP) constant).getClassIndex(), (byte) 7) + "." + constantToString(((ConstantCP) constant).getNameAndTypeIndex(), (byte) 12);
                break;
            case 12:
                string = constantToString(((ConstantNameAndType) constant).getNameIndex(), (byte) 1) + SymbolConstants.SPACE_SYMBOL + constantToString(((ConstantNameAndType) constant).getSignatureIndex(), (byte) 1);
                break;
            case 18:
                string = ((ConstantInvokeDynamic) constant).toString();
                break;
        }
        return string;
    }

    private static final String escape(String str) {
        int length = str.length();
        StringBuffer stringBuffer = new StringBuffer(length + 5);
        char[] charArray = str.toCharArray();
        for (int i = 0; i < length; i++) {
            switch (charArray[i]) {
                case '\b':
                    stringBuffer.append("\\b");
                    break;
                case '\t':
                    stringBuffer.append("\\t");
                    break;
                case '\n':
                    stringBuffer.append("\\n");
                    break;
                case '\r':
                    stringBuffer.append("\\r");
                    break;
                case '\"':
                    stringBuffer.append("\\\"");
                    break;
                default:
                    stringBuffer.append(charArray[i]);
                    break;
            }
        }
        return stringBuffer.toString();
    }

    public String constantToString(int i, byte b) {
        return constantToString(getConstant(i, b));
    }

    public String constantToString(int i) {
        return constantToString(getConstant(i));
    }

    @Override // org.aspectj.apache.bcel.classfile.Node
    public void accept(ClassVisitor classVisitor) {
        classVisitor.visitConstantPool(this);
    }

    public Constant[] getConstantPool() {
        return this.pool;
    }

    public void dump(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeShort(this.poolSize);
        for (int i = 1; i < this.poolSize; i++) {
            if (this.pool[i] != null) {
                this.pool[i].dump(dataOutputStream);
            }
        }
    }

    public ConstantUtf8 getConstantUtf8(int i) {
        Constant constant = getConstant(i);
        if (!$assertionsDisabled && constant == null) {
            throw new AssertionError();
        }
        if ($assertionsDisabled || constant.tag == 1) {
            return (ConstantUtf8) constant;
        }
        throw new AssertionError();
    }

    public String getConstantString_CONSTANTClass(int i) {
        return ((ConstantUtf8) getConstant(((ConstantClass) getConstant(i, (byte) 7)).getNameIndex(), (byte) 1)).getValue();
    }

    public int getLength() {
        return this.poolSize;
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 1; i < this.poolSize; i++) {
            stringBuffer.append(i + ")" + this.pool[i] + ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
        }
        return stringBuffer.toString();
    }

    public int lookupInteger(int i) {
        for (int i2 = 1; i2 < this.poolSize; i2++) {
            if ((this.pool[i2] instanceof ConstantInteger) && ((ConstantInteger) this.pool[i2]).getValue().intValue() == i) {
                return i2;
            }
        }
        return -1;
    }

    public int lookupUtf8(String str) {
        Integer num = this.utf8Cache.get(str);
        if (num != null) {
            return num.intValue();
        }
        for (int i = 1; i < this.poolSize; i++) {
            Constant constant = this.pool[i];
            if (constant != null && constant.tag == 1 && ((ConstantUtf8) constant).getValue().equals(str)) {
                this.utf8Cache.put(str, Integer.valueOf(i));
                return i;
            }
        }
        return -1;
    }

    public int lookupClass(String str) {
        for (int i = 1; i < this.poolSize; i++) {
            Constant constant = this.pool[i];
            if (constant != null && constant.tag == 7) {
                if (((ConstantUtf8) this.pool[((ConstantClass) constant).getNameIndex()]).getValue().equals(str)) {
                    return i;
                }
            }
        }
        return -1;
    }

    public int addUtf8(String str) {
        int iLookupUtf8 = lookupUtf8(str);
        if (iLookupUtf8 != -1) {
            return iLookupUtf8;
        }
        adjustSize();
        int i = this.poolSize;
        Constant[] constantArr = this.pool;
        int i2 = this.poolSize;
        this.poolSize = i2 + 1;
        constantArr[i2] = new ConstantUtf8(str);
        return i;
    }

    public int addInteger(int i) {
        int iLookupInteger = lookupInteger(i);
        if (iLookupInteger != -1) {
            return iLookupInteger;
        }
        adjustSize();
        int i2 = this.poolSize;
        Constant[] constantArr = this.pool;
        int i3 = this.poolSize;
        this.poolSize = i3 + 1;
        constantArr[i3] = new ConstantInteger(i);
        return i2;
    }

    public int addArrayClass(ArrayType arrayType) {
        return addClass(arrayType.getSignature());
    }

    public int addClass(ObjectType objectType) {
        return addClass(objectType.getClassName());
    }

    public int addClass(String str) {
        String strReplace = str.replace('.', '/');
        int iLookupClass = lookupClass(strReplace);
        if (iLookupClass != -1) {
            return iLookupClass;
        }
        adjustSize();
        ConstantClass constantClass = new ConstantClass(addUtf8(strReplace));
        int i = this.poolSize;
        Constant[] constantArr = this.pool;
        int i2 = this.poolSize;
        this.poolSize = i2 + 1;
        constantArr[i2] = constantClass;
        return i;
    }

    private void adjustSize() {
        if (this.poolSize + 3 >= this.pool.length) {
            Constant[] constantArr = this.pool;
            this.pool = new Constant[constantArr.length + 8];
            System.arraycopy(constantArr, 0, this.pool, 0, constantArr.length);
        }
        if (this.poolSize == 0) {
            this.poolSize = 1;
        }
    }

    public int addFieldref(String str, String str2, String str3) {
        int iLookupFieldref = lookupFieldref(str, str2, str3);
        if (iLookupFieldref != -1) {
            return iLookupFieldref;
        }
        adjustSize();
        int iAddClass = addClass(str);
        int iAddNameAndType = addNameAndType(str2, str3);
        int i = this.poolSize;
        Constant[] constantArr = this.pool;
        int i2 = this.poolSize;
        this.poolSize = i2 + 1;
        constantArr[i2] = new ConstantFieldref(iAddClass, iAddNameAndType);
        return i;
    }

    public int lookupFieldref(String str, String str2, String str3) {
        String strReplace = str.replace('.', '/');
        String string = new StringBuffer().append(strReplace).append(str2).append(str3).toString();
        Integer num = this.fieldCache.get(string);
        if (num != null) {
            return num.intValue();
        }
        for (int i = 1; i < this.poolSize; i++) {
            Constant constant = this.pool[i];
            if (constant != null && constant.tag == 9) {
                ConstantFieldref constantFieldref = (ConstantFieldref) constant;
                ConstantNameAndType constantNameAndType = (ConstantNameAndType) this.pool[constantFieldref.getNameAndTypeIndex()];
                if (((ConstantUtf8) this.pool[((ConstantClass) this.pool[constantFieldref.getClassIndex()]).getNameIndex()]).getValue().equals(strReplace) && ((ConstantUtf8) this.pool[constantNameAndType.getNameIndex()]).getValue().equals(str2) && ((ConstantUtf8) this.pool[constantNameAndType.getSignatureIndex()]).getValue().equals(str3)) {
                    this.fieldCache.put(string, new Integer(i));
                    return i;
                }
            }
        }
        return -1;
    }

    public int addNameAndType(String str, String str2) {
        int iLookupNameAndType = lookupNameAndType(str, str2);
        if (iLookupNameAndType != -1) {
            return iLookupNameAndType;
        }
        adjustSize();
        int iAddUtf8 = addUtf8(str);
        int iAddUtf82 = addUtf8(str2);
        int i = this.poolSize;
        Constant[] constantArr = this.pool;
        int i2 = this.poolSize;
        this.poolSize = i2 + 1;
        constantArr[i2] = new ConstantNameAndType(iAddUtf8, iAddUtf82);
        return i;
    }

    public int lookupNameAndType(String str, String str2) {
        for (int i = 1; i < this.poolSize; i++) {
            Constant constant = this.pool[i];
            if (constant != null && constant.tag == 12) {
                ConstantNameAndType constantNameAndType = (ConstantNameAndType) constant;
                if (((ConstantUtf8) this.pool[constantNameAndType.getNameIndex()]).getValue().equals(str) && ((ConstantUtf8) this.pool[constantNameAndType.getSignatureIndex()]).getValue().equals(str2)) {
                    return i;
                }
            }
        }
        return -1;
    }

    public int addFloat(float f) {
        int iLookupFloat = lookupFloat(f);
        if (iLookupFloat != -1) {
            return iLookupFloat;
        }
        adjustSize();
        int i = this.poolSize;
        Constant[] constantArr = this.pool;
        int i2 = this.poolSize;
        this.poolSize = i2 + 1;
        constantArr[i2] = new ConstantFloat(f);
        return i;
    }

    public int lookupFloat(float f) {
        int iFloatToIntBits = Float.floatToIntBits(f);
        for (int i = 1; i < this.poolSize; i++) {
            Constant constant = this.pool[i];
            if (constant != null && constant.tag == 4 && Float.floatToIntBits(((ConstantFloat) constant).getValue().floatValue()) == iFloatToIntBits) {
                return i;
            }
        }
        return -1;
    }

    public int addDouble(double d) {
        int iLookupDouble = lookupDouble(d);
        if (iLookupDouble != -1) {
            return iLookupDouble;
        }
        adjustSize();
        int i = this.poolSize;
        this.pool[this.poolSize] = new ConstantDouble(d);
        this.poolSize += 2;
        return i;
    }

    public int lookupDouble(double d) {
        long jDoubleToLongBits = Double.doubleToLongBits(d);
        for (int i = 1; i < this.poolSize; i++) {
            Constant constant = this.pool[i];
            if (constant != null && constant.tag == 6 && Double.doubleToLongBits(((ConstantDouble) constant).getValue().doubleValue()) == jDoubleToLongBits) {
                return i;
            }
        }
        return -1;
    }

    public int addLong(long j) {
        int iLookupLong = lookupLong(j);
        if (iLookupLong != -1) {
            return iLookupLong;
        }
        adjustSize();
        int i = this.poolSize;
        this.pool[this.poolSize] = new ConstantLong(j);
        this.poolSize += 2;
        return i;
    }

    public int lookupString(String str) {
        for (int i = 1; i < this.poolSize; i++) {
            Constant constant = this.pool[i];
            if (constant != null && constant.tag == 8) {
                if (((ConstantUtf8) this.pool[((ConstantString) constant).getStringIndex()]).getValue().equals(str)) {
                    return i;
                }
            }
        }
        return -1;
    }

    public int addString(String str) {
        int iLookupString = lookupString(str);
        if (iLookupString != -1) {
            return iLookupString;
        }
        int iAddUtf8 = addUtf8(str);
        adjustSize();
        ConstantString constantString = new ConstantString(iAddUtf8);
        int i = this.poolSize;
        Constant[] constantArr = this.pool;
        int i2 = this.poolSize;
        this.poolSize = i2 + 1;
        constantArr[i2] = constantString;
        return i;
    }

    public int lookupLong(long j) {
        for (int i = 1; i < this.poolSize; i++) {
            Constant constant = this.pool[i];
            if (constant != null && constant.tag == 5 && ((ConstantLong) constant).getValue().longValue() == j) {
                return i;
            }
        }
        return -1;
    }

    public int addConstant(Constant constant, ConstantPool constantPool) {
        Constant[] constantPool2 = constantPool.getConstantPool();
        switch (constant.getTag()) {
            case 1:
                return addUtf8(((ConstantUtf8) constant).getValue());
            case 2:
            case 13:
            case 14:
            case 17:
            default:
                throw new RuntimeException("Unknown constant type " + constant);
            case 3:
                return addInteger(((ConstantInteger) constant).getValue().intValue());
            case 4:
                return addFloat(((ConstantFloat) constant).getValue().floatValue());
            case 5:
                return addLong(((ConstantLong) constant).getValue().longValue());
            case 6:
                return addDouble(((ConstantDouble) constant).getValue().doubleValue());
            case 7:
                return addClass(((ConstantUtf8) constantPool2[((ConstantClass) constant).getNameIndex()]).getValue());
            case 8:
                return addString(((ConstantUtf8) constantPool2[((ConstantString) constant).getStringIndex()]).getValue());
            case 9:
            case 10:
            case 11:
                ConstantCP constantCP = (ConstantCP) constant;
                ConstantClass constantClass = (ConstantClass) constantPool2[constantCP.getClassIndex()];
                ConstantNameAndType constantNameAndType = (ConstantNameAndType) constantPool2[constantCP.getNameAndTypeIndex()];
                String strReplace = ((ConstantUtf8) constantPool2[constantClass.getNameIndex()]).getValue().replace('/', '.');
                String value = ((ConstantUtf8) constantPool2[constantNameAndType.getNameIndex()]).getValue();
                String value2 = ((ConstantUtf8) constantPool2[constantNameAndType.getSignatureIndex()]).getValue();
                switch (constant.getTag()) {
                    case 9:
                        return addFieldref(strReplace, value, value2);
                    case 10:
                        return addMethodref(strReplace, value, value2);
                    case 11:
                        return addInterfaceMethodref(strReplace, value, value2);
                    default:
                        throw new RuntimeException("Unknown constant type " + constant);
                }
            case 12:
                ConstantNameAndType constantNameAndType2 = (ConstantNameAndType) constant;
                return addNameAndType(((ConstantUtf8) constantPool2[constantNameAndType2.getNameIndex()]).getValue(), ((ConstantUtf8) constantPool2[constantNameAndType2.getSignatureIndex()]).getValue());
            case 15:
                ConstantMethodHandle constantMethodHandle = (ConstantMethodHandle) constant;
                return addMethodHandle(constantMethodHandle.getReferenceKind(), addConstant(constantPool2[constantMethodHandle.getReferenceIndex()], constantPool));
            case 16:
                return addMethodType(addConstant(constantPool2[((ConstantMethodType) constant).getDescriptorIndex()], constantPool));
            case 18:
                ConstantInvokeDynamic constantInvokeDynamic = (ConstantInvokeDynamic) constant;
                int bootstrapMethodAttrIndex = constantInvokeDynamic.getBootstrapMethodAttrIndex();
                ConstantNameAndType constantNameAndType3 = (ConstantNameAndType) constantPool2[constantInvokeDynamic.getNameAndTypeIndex()];
                return addInvokeDynamic(bootstrapMethodAttrIndex, addNameAndType(((ConstantUtf8) constantPool2[constantNameAndType3.getNameIndex()]).getValue(), ((ConstantUtf8) constantPool2[constantNameAndType3.getSignatureIndex()]).getValue()));
        }
    }

    public int addMethodHandle(byte b, int i) {
        adjustSize();
        int i2 = this.poolSize;
        Constant[] constantArr = this.pool;
        int i3 = this.poolSize;
        this.poolSize = i3 + 1;
        constantArr[i3] = new ConstantMethodHandle(b, i);
        return i2;
    }

    public int addMethodType(int i) {
        adjustSize();
        int i2 = this.poolSize;
        Constant[] constantArr = this.pool;
        int i3 = this.poolSize;
        this.poolSize = i3 + 1;
        constantArr[i3] = new ConstantMethodType(i);
        return i2;
    }

    public int addMethodref(String str, String str2, String str3) {
        int iLookupMethodref = lookupMethodref(str, str2, str3);
        if (iLookupMethodref != -1) {
            return iLookupMethodref;
        }
        adjustSize();
        int iAddNameAndType = addNameAndType(str2, str3);
        int iAddClass = addClass(str);
        int i = this.poolSize;
        Constant[] constantArr = this.pool;
        int i2 = this.poolSize;
        this.poolSize = i2 + 1;
        constantArr[i2] = new ConstantMethodref(iAddClass, iAddNameAndType);
        return i;
    }

    public int addInvokeDynamic(int i, int i2) {
        adjustSize();
        int i3 = this.poolSize;
        Constant[] constantArr = this.pool;
        int i4 = this.poolSize;
        this.poolSize = i4 + 1;
        constantArr[i4] = new ConstantInvokeDynamic(i, i2);
        return i3;
    }

    public int addInterfaceMethodref(String str, String str2, String str3) {
        int iLookupInterfaceMethodref = lookupInterfaceMethodref(str, str2, str3);
        if (iLookupInterfaceMethodref != -1) {
            return iLookupInterfaceMethodref;
        }
        adjustSize();
        int iAddClass = addClass(str);
        int iAddNameAndType = addNameAndType(str2, str3);
        int i = this.poolSize;
        Constant[] constantArr = this.pool;
        int i2 = this.poolSize;
        this.poolSize = i2 + 1;
        constantArr[i2] = new ConstantInterfaceMethodref(iAddClass, iAddNameAndType);
        return i;
    }

    public int lookupInterfaceMethodref(String str, String str2, String str3) {
        String strReplace = str.replace('.', '/');
        for (int i = 1; i < this.poolSize; i++) {
            Constant constant = this.pool[i];
            if (constant != null && constant.tag == 11) {
                ConstantInterfaceMethodref constantInterfaceMethodref = (ConstantInterfaceMethodref) constant;
                if (((ConstantUtf8) this.pool[((ConstantClass) this.pool[constantInterfaceMethodref.getClassIndex()]).getNameIndex()]).getValue().equals(strReplace)) {
                    ConstantNameAndType constantNameAndType = (ConstantNameAndType) this.pool[constantInterfaceMethodref.getNameAndTypeIndex()];
                    if (((ConstantUtf8) this.pool[constantNameAndType.getNameIndex()]).getValue().equals(str2) && ((ConstantUtf8) this.pool[constantNameAndType.getSignatureIndex()]).getValue().equals(str3)) {
                        return i;
                    }
                } else {
                    continue;
                }
            }
        }
        return -1;
    }

    public int lookupMethodref(String str, String str2, String str3) {
        String string = new StringBuffer().append(str).append(str2).append(str3).toString();
        Integer num = this.methodCache.get(string);
        if (num != null) {
            return num.intValue();
        }
        String strReplace = str.replace('.', '/');
        for (int i = 1; i < this.poolSize; i++) {
            Constant constant = this.pool[i];
            if (constant != null && constant.tag == 10) {
                ConstantMethodref constantMethodref = (ConstantMethodref) constant;
                ConstantNameAndType constantNameAndType = (ConstantNameAndType) this.pool[constantMethodref.getNameAndTypeIndex()];
                if (((ConstantUtf8) this.pool[((ConstantClass) this.pool[constantMethodref.getClassIndex()]).getNameIndex()]).getValue().equals(strReplace) && ((ConstantUtf8) this.pool[constantNameAndType.getNameIndex()]).getValue().equals(str2) && ((ConstantUtf8) this.pool[constantNameAndType.getSignatureIndex()]).getValue().equals(str3)) {
                    this.methodCache.put(string, new Integer(i));
                    return i;
                }
            }
        }
        return -1;
    }

    public ConstantPool getFinalConstantPool() {
        Constant[] constantArr = new Constant[this.poolSize];
        System.arraycopy(this.pool, 0, constantArr, 0, this.poolSize);
        return new ConstantPool(constantArr);
    }

    static {
        $assertionsDisabled = !ConstantPool.class.desiredAssertionStatus();
    }
}
