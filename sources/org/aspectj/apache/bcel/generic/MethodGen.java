package org.aspectj.apache.bcel.generic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;
import org.apache.ibatis.javassist.bytecode.ExceptionsAttribute;
import org.apache.ibatis.javassist.bytecode.LineNumberAttribute;
import org.apache.ibatis.javassist.bytecode.LocalVariableAttribute;
import org.aspectj.apache.bcel.classfile.Attribute;
import org.aspectj.apache.bcel.classfile.ClassFormatException;
import org.aspectj.apache.bcel.classfile.Code;
import org.aspectj.apache.bcel.classfile.CodeException;
import org.aspectj.apache.bcel.classfile.ConstantPool;
import org.aspectj.apache.bcel.classfile.ExceptionTable;
import org.aspectj.apache.bcel.classfile.LineNumber;
import org.aspectj.apache.bcel.classfile.LineNumberTable;
import org.aspectj.apache.bcel.classfile.LocalVariable;
import org.aspectj.apache.bcel.classfile.LocalVariableTable;
import org.aspectj.apache.bcel.classfile.Method;
import org.aspectj.apache.bcel.classfile.Utility;
import org.aspectj.apache.bcel.classfile.annotation.AnnotationGen;
import org.aspectj.apache.bcel.classfile.annotation.RuntimeAnnos;
import org.aspectj.apache.bcel.classfile.annotation.RuntimeParamAnnos;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/apache/bcel/generic/MethodGen.class */
public class MethodGen extends FieldGenOrMethodGen {
    private String classname;
    private Type[] parameterTypes;
    private String[] parameterNames;
    private int maxLocals;
    private int maxStack;
    private InstructionList il;
    private boolean stripAttributes;
    private int highestLineNumber;
    private ArrayList<LocalVariableGen> localVariablesList;
    private ArrayList<LineNumberGen> lineNumbersList;
    private ArrayList<CodeExceptionGen> exceptionsList;
    private ArrayList<String> exceptionsThrown;
    private ArrayList<Attribute> codeAttributesList;
    private List<AnnotationGen>[] param_annotations;
    private boolean hasParameterAnnotations;
    private boolean haveUnpackedParameterAnnotations;

    /* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/apache/bcel/generic/MethodGen$BranchStack.class */
    static final class BranchStack {
        Stack<BranchTarget> branchTargets = new Stack<>();
        Hashtable<InstructionHandle, BranchTarget> visitedTargets = new Hashtable<>();

        BranchStack() {
        }

        public void push(InstructionHandle instructionHandle, int i) {
            if (visited(instructionHandle)) {
                return;
            }
            this.branchTargets.push(visit(instructionHandle, i));
        }

        public BranchTarget pop() {
            if (this.branchTargets.empty()) {
                return null;
            }
            return this.branchTargets.pop();
        }

        private final BranchTarget visit(InstructionHandle instructionHandle, int i) {
            BranchTarget branchTarget = new BranchTarget(instructionHandle, i);
            this.visitedTargets.put(instructionHandle, branchTarget);
            return branchTarget;
        }

        private final boolean visited(InstructionHandle instructionHandle) {
            return this.visitedTargets.get(instructionHandle) != null;
        }
    }

    /* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/apache/bcel/generic/MethodGen$BranchTarget.class */
    static final class BranchTarget {
        InstructionHandle target;
        int stackDepth;

        BranchTarget(InstructionHandle instructionHandle, int i) {
            this.target = instructionHandle;
            this.stackDepth = i;
        }
    }

    public MethodGen(int i, Type type, Type[] typeArr, String[] strArr, String str, String str2, InstructionList instructionList, ConstantPool constantPool) {
        this.highestLineNumber = 0;
        this.localVariablesList = new ArrayList<>();
        this.lineNumbersList = new ArrayList<>();
        this.exceptionsList = new ArrayList<>();
        this.exceptionsThrown = new ArrayList<>();
        this.codeAttributesList = new ArrayList<>();
        this.hasParameterAnnotations = false;
        this.haveUnpackedParameterAnnotations = false;
        this.modifiers = i;
        this.type = type;
        this.parameterTypes = typeArr;
        this.parameterNames = strArr;
        this.name = str;
        this.classname = str2;
        this.il = instructionList;
        this.cp = constantPool;
    }

    public int getHighestlinenumber() {
        return this.highestLineNumber;
    }

    public MethodGen(Method method, String str, ConstantPool constantPool) {
        this(method, str, constantPool, false);
    }

    public MethodGen(Method method, String str, ConstantPool constantPool, boolean z) throws ClassFormatException {
        this(method.getModifiers(), method.getReturnType(), method.getArgumentTypes(), null, method.getName(), str, (method.getModifiers() & 1280) == 0 ? new InstructionList(method.getCode().getCode()) : null, constantPool);
        for (Attribute attribute : method.getAttributes()) {
            if (attribute instanceof Code) {
                Code code = (Code) attribute;
                setMaxStack(code.getMaxStack());
                setMaxLocals(code.getMaxLocals());
                CodeException[] exceptionTable = code.getExceptionTable();
                InstructionHandle[] instructionsAsArray = this.il.getInstructionsAsArray();
                if (exceptionTable != null) {
                    for (CodeException codeException : exceptionTable) {
                        int catchType = codeException.getCatchType();
                        ObjectType objectType = catchType > 0 ? new ObjectType(method.getConstantPool().getConstantString_CONSTANTClass(catchType)) : null;
                        int endPC = codeException.getEndPC();
                        addExceptionHandler(this.il.findHandle(codeException.getStartPC(), instructionsAsArray), method.getCode().getCode().length == endPC ? this.il.getEnd() : this.il.findHandle(endPC, instructionsAsArray).getPrev(), this.il.findHandle(codeException.getHandlerPC(), instructionsAsArray), objectType);
                    }
                }
                for (Attribute attribute2 : code.getAttributes()) {
                    if (attribute2 instanceof LineNumberTable) {
                        LineNumber[] lineNumberTable = ((LineNumberTable) attribute2).getLineNumberTable();
                        if (z) {
                            for (LineNumber lineNumber : lineNumberTable) {
                                int lineNumber2 = lineNumber.getLineNumber();
                                if (lineNumber2 > this.highestLineNumber) {
                                    this.highestLineNumber = lineNumber2;
                                }
                                this.il.findHandle(lineNumber.getStartPC(), instructionsAsArray, true).addTargeter(new LineNumberTag(lineNumber2));
                            }
                        } else {
                            for (LineNumber lineNumber3 : lineNumberTable) {
                                addLineNumber(this.il.findHandle(lineNumber3.getStartPC(), instructionsAsArray, true), lineNumber3.getLineNumber());
                            }
                        }
                    } else if (!(attribute2 instanceof LocalVariableTable)) {
                        addCodeAttribute(attribute2);
                    } else if (z) {
                        for (LocalVariable localVariable : ((LocalVariableTable) attribute2).getLocalVariableTable()) {
                            Type type = Type.getType(localVariable.getSignature());
                            LocalVariableTag localVariableTag = new LocalVariableTag(type, localVariable.getSignature(), localVariable.getName(), localVariable.getIndex(), localVariable.getStartPC());
                            InstructionHandle instructionHandleFindHandle = this.il.findHandle(localVariable.getStartPC(), instructionsAsArray, true);
                            if (type.getType() != 16) {
                                int size = type.getSize();
                                if (localVariable.getIndex() + size > this.maxLocals) {
                                    this.maxLocals = localVariable.getIndex() + size;
                                }
                            }
                            int startPC = localVariable.getStartPC() + localVariable.getLength();
                            do {
                                instructionHandleFindHandle.addTargeter(localVariableTag);
                                instructionHandleFindHandle = instructionHandleFindHandle.getNext();
                                if (instructionHandleFindHandle != null) {
                                }
                            } while (instructionHandleFindHandle.getPosition() < startPC);
                        }
                    } else {
                        LocalVariable[] localVariableTable = ((LocalVariableTable) attribute2).getLocalVariableTable();
                        removeLocalVariables();
                        for (LocalVariable localVariable2 : localVariableTable) {
                            InstructionHandle instructionHandleFindHandle2 = this.il.findHandle(localVariable2.getStartPC(), instructionsAsArray);
                            InstructionHandle instructionHandleFindHandle3 = this.il.findHandle(localVariable2.getStartPC() + localVariable2.getLength(), instructionsAsArray);
                            instructionHandleFindHandle3 = instructionHandleFindHandle3 != null ? instructionHandleFindHandle3.getPrev() : instructionHandleFindHandle3;
                            instructionHandleFindHandle2 = null == instructionHandleFindHandle2 ? this.il.getStart() : instructionHandleFindHandle2;
                            if (null == instructionHandleFindHandle3) {
                                instructionHandleFindHandle3 = this.il.getEnd();
                            }
                            addLocalVariable(localVariable2.getName(), Type.getType(localVariable2.getSignature()), localVariable2.getIndex(), instructionHandleFindHandle2, instructionHandleFindHandle3);
                        }
                    }
                }
            } else if (attribute instanceof ExceptionTable) {
                for (String str2 : ((ExceptionTable) attribute).getExceptionNames()) {
                    addException(str2);
                }
            } else if (attribute instanceof RuntimeAnnos) {
                this.annotationList.addAll(((RuntimeAnnos) attribute).getAnnotations());
            } else {
                addAttribute(attribute);
            }
        }
    }

    public LocalVariableGen addLocalVariable(String str, Type type, int i, InstructionHandle instructionHandle, InstructionHandle instructionHandle2) {
        int size = type.getSize();
        if (i + size > this.maxLocals) {
            this.maxLocals = i + size;
        }
        LocalVariableGen localVariableGen = new LocalVariableGen(i, str, type, instructionHandle, instructionHandle2);
        int iIndexOf = this.localVariablesList.indexOf(localVariableGen);
        if (iIndexOf >= 0) {
            this.localVariablesList.set(iIndexOf, localVariableGen);
        } else {
            this.localVariablesList.add(localVariableGen);
        }
        return localVariableGen;
    }

    public LocalVariableGen addLocalVariable(String str, Type type, InstructionHandle instructionHandle, InstructionHandle instructionHandle2) {
        return addLocalVariable(str, type, this.maxLocals, instructionHandle, instructionHandle2);
    }

    public void removeLocalVariable(LocalVariableGen localVariableGen) {
        this.localVariablesList.remove(localVariableGen);
    }

    public void removeLocalVariables() {
        this.localVariablesList.clear();
    }

    private static final void sort(LocalVariableGen[] localVariableGenArr, int i, int i2) {
        int i3 = i;
        int i4 = i2;
        int index = localVariableGenArr[(i + i2) / 2].getIndex();
        while (true) {
            if (localVariableGenArr[i3].getIndex() < index) {
                i3++;
            } else {
                while (index < localVariableGenArr[i4].getIndex()) {
                    i4--;
                }
                if (i3 <= i4) {
                    LocalVariableGen localVariableGen = localVariableGenArr[i3];
                    localVariableGenArr[i3] = localVariableGenArr[i4];
                    localVariableGenArr[i4] = localVariableGen;
                    i3++;
                    i4--;
                }
                if (i3 > i4) {
                    break;
                }
            }
        }
        if (i < i4) {
            sort(localVariableGenArr, i, i4);
        }
        if (i3 < i2) {
            sort(localVariableGenArr, i3, i2);
        }
    }

    public LocalVariableGen[] getLocalVariables() {
        int size = this.localVariablesList.size();
        LocalVariableGen[] localVariableGenArr = new LocalVariableGen[size];
        this.localVariablesList.toArray(localVariableGenArr);
        for (int i = 0; i < size; i++) {
            if (localVariableGenArr[i].getStart() == null) {
                localVariableGenArr[i].setStart(this.il.getStart());
            }
            if (localVariableGenArr[i].getEnd() == null) {
                localVariableGenArr[i].setEnd(this.il.getEnd());
            }
        }
        if (size > 1) {
            sort(localVariableGenArr, 0, size - 1);
        }
        return localVariableGenArr;
    }

    public LocalVariableTable getLocalVariableTable(ConstantPool constantPool) {
        LocalVariableGen[] localVariables = getLocalVariables();
        int length = localVariables.length;
        LocalVariable[] localVariableArr = new LocalVariable[length];
        for (int i = 0; i < length; i++) {
            localVariableArr[i] = localVariables[i].getLocalVariable(constantPool);
        }
        return new LocalVariableTable(constantPool.addUtf8(LocalVariableAttribute.tag), 2 + (localVariableArr.length * 10), localVariableArr, constantPool);
    }

    public LineNumberGen addLineNumber(InstructionHandle instructionHandle, int i) {
        LineNumberGen lineNumberGen = new LineNumberGen(instructionHandle, i);
        this.lineNumbersList.add(lineNumberGen);
        return lineNumberGen;
    }

    public void removeLineNumber(LineNumberGen lineNumberGen) {
        this.lineNumbersList.remove(lineNumberGen);
    }

    public void removeLineNumbers() {
        this.lineNumbersList.clear();
    }

    public LineNumberGen[] getLineNumbers() {
        LineNumberGen[] lineNumberGenArr = new LineNumberGen[this.lineNumbersList.size()];
        this.lineNumbersList.toArray(lineNumberGenArr);
        return lineNumberGenArr;
    }

    public LineNumberTable getLineNumberTable(ConstantPool constantPool) {
        int size = this.lineNumbersList.size();
        LineNumber[] lineNumberArr = new LineNumber[size];
        for (int i = 0; i < size; i++) {
            lineNumberArr[i] = this.lineNumbersList.get(i).getLineNumber();
        }
        return new LineNumberTable(constantPool.addUtf8(LineNumberAttribute.tag), 2 + (lineNumberArr.length * 4), lineNumberArr, constantPool);
    }

    public CodeExceptionGen addExceptionHandler(InstructionHandle instructionHandle, InstructionHandle instructionHandle2, InstructionHandle instructionHandle3, ObjectType objectType) {
        if (instructionHandle == null || instructionHandle2 == null || instructionHandle3 == null) {
            throw new ClassGenException("Exception handler target is null instruction");
        }
        CodeExceptionGen codeExceptionGen = new CodeExceptionGen(instructionHandle, instructionHandle2, instructionHandle3, objectType);
        this.exceptionsList.add(codeExceptionGen);
        return codeExceptionGen;
    }

    public void removeExceptionHandler(CodeExceptionGen codeExceptionGen) {
        this.exceptionsList.remove(codeExceptionGen);
    }

    public void removeExceptionHandlers() {
        this.exceptionsList.clear();
    }

    public CodeExceptionGen[] getExceptionHandlers() {
        CodeExceptionGen[] codeExceptionGenArr = new CodeExceptionGen[this.exceptionsList.size()];
        this.exceptionsList.toArray(codeExceptionGenArr);
        return codeExceptionGenArr;
    }

    private CodeException[] getCodeExceptions() {
        int size = this.exceptionsList.size();
        CodeException[] codeExceptionArr = new CodeException[size];
        for (int i = 0; i < size; i++) {
            try {
                codeExceptionArr[i] = this.exceptionsList.get(i).getCodeException(this.cp);
            } catch (ArrayIndexOutOfBoundsException e) {
            }
        }
        return codeExceptionArr;
    }

    public void addException(String str) {
        this.exceptionsThrown.add(str);
    }

    public void removeException(String str) {
        this.exceptionsThrown.remove(str);
    }

    public void removeExceptions() {
        this.exceptionsThrown.clear();
    }

    public String[] getExceptions() {
        String[] strArr = new String[this.exceptionsThrown.size()];
        this.exceptionsThrown.toArray(strArr);
        return strArr;
    }

    private ExceptionTable getExceptionTable(ConstantPool constantPool) {
        int size = this.exceptionsThrown.size();
        int[] iArr = new int[size];
        for (int i = 0; i < size; i++) {
            try {
                iArr[i] = constantPool.addClass(this.exceptionsThrown.get(i));
            } catch (ArrayIndexOutOfBoundsException e) {
            }
        }
        return new ExceptionTable(constantPool.addUtf8(ExceptionsAttribute.tag), 2 + (2 * size), iArr, constantPool);
    }

    public void addCodeAttribute(Attribute attribute) {
        this.codeAttributesList.add(attribute);
    }

    public void addParameterAnnotationsAsAttribute(ConstantPool constantPool) {
        Attribute[] parameterAnnotationAttributes;
        if (this.hasParameterAnnotations && (parameterAnnotationAttributes = Utility.getParameterAnnotationAttributes(constantPool, this.param_annotations)) != null) {
            for (Attribute attribute : parameterAnnotationAttributes) {
                addAttribute(attribute);
            }
        }
    }

    public void removeCodeAttribute(Attribute attribute) {
        this.codeAttributesList.remove(attribute);
    }

    public void removeCodeAttributes() {
        this.codeAttributesList.clear();
    }

    public Attribute[] getCodeAttributes() {
        Attribute[] attributeArr = new Attribute[this.codeAttributesList.size()];
        this.codeAttributesList.toArray(attributeArr);
        return attributeArr;
    }

    public Method getMethod() {
        String signature = getSignature();
        int iAddUtf8 = this.cp.addUtf8(this.name);
        int iAddUtf82 = this.cp.addUtf8(signature);
        byte[] byteCode = null;
        if (this.il != null) {
            try {
                byteCode = this.il.getByteCode();
            } catch (Exception e) {
                throw new IllegalStateException("Unexpected problem whilst preparing bytecode for " + getClassName() + "." + getName() + getSignature(), e);
            }
        }
        LineNumberTable lineNumberTable = null;
        LocalVariableTable localVariableTable = null;
        if (this.localVariablesList.size() > 0 && !this.stripAttributes) {
            LocalVariableTable localVariableTable2 = getLocalVariableTable(this.cp);
            localVariableTable = localVariableTable2;
            addCodeAttribute(localVariableTable2);
        }
        if (this.lineNumbersList.size() > 0 && !this.stripAttributes) {
            LineNumberTable lineNumberTable2 = getLineNumberTable(this.cp);
            lineNumberTable = lineNumberTable2;
            addCodeAttribute(lineNumberTable2);
        }
        Attribute[] codeAttributes = getCodeAttributes();
        int length = 0;
        for (Attribute attribute : codeAttributes) {
            length += attribute.getLength() + 6;
        }
        CodeException[] codeExceptions = getCodeExceptions();
        int length2 = codeExceptions.length * 8;
        Code code = null;
        if (this.il != null && !isAbstract()) {
            List<Attribute> attributes = getAttributes();
            for (int i = 0; i < attributes.size(); i++) {
                Attribute attribute2 = attributes.get(i);
                if (attribute2 instanceof Code) {
                    removeAttribute(attribute2);
                }
            }
            code = new Code(this.cp.addUtf8("Code"), 8 + byteCode.length + 2 + length2 + 2 + length, this.maxStack, this.maxLocals, byteCode, codeExceptions, codeAttributes, this.cp);
            addAttribute(code);
        }
        addAnnotationsAsAttribute(this.cp);
        addParameterAnnotationsAsAttribute(this.cp);
        ExceptionTable exceptionTable = null;
        if (this.exceptionsThrown.size() > 0) {
            ExceptionTable exceptionTable2 = getExceptionTable(this.cp);
            exceptionTable = exceptionTable2;
            addAttribute(exceptionTable2);
        }
        Method method = new Method(this.modifiers, iAddUtf8, iAddUtf82, getAttributesImmutable(), this.cp);
        if (localVariableTable != null) {
            removeCodeAttribute(localVariableTable);
        }
        if (lineNumberTable != null) {
            removeCodeAttribute(lineNumberTable);
        }
        if (code != null) {
            removeAttribute(code);
        }
        if (exceptionTable != null) {
            removeAttribute(exceptionTable);
        }
        return method;
    }

    public void setMaxLocals(int i) {
        this.maxLocals = i;
    }

    public int getMaxLocals() {
        return this.maxLocals;
    }

    public void setMaxStack(int i) {
        this.maxStack = i;
    }

    public int getMaxStack() {
        return this.maxStack;
    }

    public String getClassName() {
        return this.classname;
    }

    public void setClassName(String str) {
        this.classname = str;
    }

    public void setReturnType(Type type) {
        setType(type);
    }

    public Type getReturnType() {
        return getType();
    }

    public void setArgumentTypes(Type[] typeArr) {
        this.parameterTypes = typeArr;
    }

    public Type[] getArgumentTypes() {
        return this.parameterTypes;
    }

    public void setArgumentType(int i, Type type) {
        this.parameterTypes[i] = type;
    }

    public Type getArgumentType(int i) {
        return this.parameterTypes[i];
    }

    public void setArgumentNames(String[] strArr) {
        this.parameterNames = strArr;
    }

    public String[] getArgumentNames() {
        return this.parameterNames != null ? (String[]) this.parameterNames.clone() : new String[0];
    }

    public void setArgumentName(int i, String str) {
        this.parameterNames[i] = str;
    }

    public String getArgumentName(int i) {
        return this.parameterNames[i];
    }

    public InstructionList getInstructionList() {
        return this.il;
    }

    public void setInstructionList(InstructionList instructionList) {
        this.il = instructionList;
    }

    @Override // org.aspectj.apache.bcel.generic.FieldGenOrMethodGen
    public String getSignature() {
        return Utility.toMethodSignature(this.type, this.parameterTypes);
    }

    public void setMaxStack() {
        if (this.il != null) {
            this.maxStack = getMaxStack(this.cp, this.il, getExceptionHandlers());
        } else {
            this.maxStack = 0;
        }
    }

    public void setMaxLocals() {
        setMaxLocals(false);
    }

    public void setMaxLocals(boolean z) {
        int index;
        if (this.il == null) {
            if (z) {
                return;
            }
            this.maxLocals = 0;
            return;
        }
        int size = isStatic() ? 0 : 1;
        if (this.parameterTypes != null) {
            for (int i = 0; i < this.parameterTypes.length; i++) {
                size += this.parameterTypes[i].getSize();
            }
        }
        InstructionHandle start = this.il.getStart();
        while (true) {
            InstructionHandle instructionHandle = start;
            if (instructionHandle == null) {
                break;
            }
            Instruction instruction = instructionHandle.getInstruction();
            if (((instruction instanceof InstructionLV) || (instruction instanceof RET)) && (index = instruction.getIndex() + instruction.getType(this.cp).getSize()) > size) {
                size = index;
            }
            start = instructionHandle.getNext();
        }
        if (!z || size > this.maxLocals) {
            this.maxLocals = size;
        }
    }

    public void stripAttributes(boolean z) {
        this.stripAttributes = z;
    }

    public static int getMaxStack(ConstantPool constantPool, InstructionList instructionList, CodeExceptionGen[] codeExceptionGenArr) {
        BranchTarget branchTargetPop;
        BranchStack branchStack = new BranchStack();
        int iProduceStack = 0;
        int i = 0;
        for (CodeExceptionGen codeExceptionGen : codeExceptionGenArr) {
            InstructionHandle handlerPC = codeExceptionGen.getHandlerPC();
            if (handlerPC != null) {
                i = 1;
                branchStack.push(handlerPC, 1);
            }
        }
        InstructionHandle start = instructionList.getStart();
        while (start != null) {
            Instruction instruction = start.getInstruction();
            short s = instruction.opcode;
            iProduceStack += instruction.produceStack(constantPool) - instruction.consumeStack(constantPool);
            if (iProduceStack > i) {
                i = iProduceStack;
            }
            if (instruction instanceof InstructionBranch) {
                InstructionBranch instructionBranch = (InstructionBranch) instruction;
                if (instruction instanceof InstructionSelect) {
                    for (InstructionHandle instructionHandle : ((InstructionSelect) instructionBranch).getTargets()) {
                        branchStack.push(instructionHandle, iProduceStack);
                    }
                    start = null;
                } else if (!instructionBranch.isIfInstruction()) {
                    if (s == 168 || s == 201) {
                        branchStack.push(start.getNext(), iProduceStack - 1);
                    }
                    start = null;
                }
                branchStack.push(instructionBranch.getTarget(), iProduceStack);
            } else if (s == 191 || s == 169 || (s >= 172 && s <= 177)) {
                start = null;
            }
            if (start != null) {
                start = start.getNext();
            }
            if (start == null && (branchTargetPop = branchStack.pop()) != null) {
                start = branchTargetPop.target;
                iProduceStack = branchTargetPop.stackDepth;
            }
        }
        return i;
    }

    public final String toString() {
        StringBuffer stringBuffer = new StringBuffer(Utility.methodSignatureToString(Utility.toMethodSignature(this.type, this.parameterTypes), this.name, Utility.accessToString(this.modifiers), true, getLocalVariableTable(this.cp)));
        if (this.exceptionsThrown.size() > 0) {
            Iterator<String> it = this.exceptionsThrown.iterator();
            while (it.hasNext()) {
                stringBuffer.append("\n\t\tthrows " + it.next());
            }
        }
        return stringBuffer.toString();
    }

    public List<AnnotationGen> getAnnotationsOnParameter(int i) throws IOException {
        ensureExistingParameterAnnotationsUnpacked();
        if (!this.hasParameterAnnotations || i > this.parameterTypes.length) {
            return null;
        }
        return this.param_annotations[i];
    }

    private void ensureExistingParameterAnnotationsUnpacked() throws IOException {
        if (this.haveUnpackedParameterAnnotations) {
            return;
        }
        RuntimeParamAnnos runtimeParamAnnos = null;
        RuntimeParamAnnos runtimeParamAnnos2 = null;
        for (Attribute attribute : getAttributes()) {
            if (attribute instanceof RuntimeParamAnnos) {
                if (!this.hasParameterAnnotations) {
                    this.param_annotations = new List[this.parameterTypes.length];
                    for (int i = 0; i < this.parameterTypes.length; i++) {
                        this.param_annotations[i] = new ArrayList();
                    }
                }
                this.hasParameterAnnotations = true;
                RuntimeParamAnnos runtimeParamAnnos3 = (RuntimeParamAnnos) attribute;
                if (runtimeParamAnnos3.areVisible()) {
                    runtimeParamAnnos = runtimeParamAnnos3;
                } else {
                    runtimeParamAnnos2 = runtimeParamAnnos3;
                }
                for (int i2 = 0; i2 < this.parameterTypes.length; i2++) {
                    for (AnnotationGen annotationGen : runtimeParamAnnos3.getAnnotationsOnParameter(i2)) {
                        this.param_annotations[i2].add(annotationGen);
                    }
                }
            }
        }
        if (runtimeParamAnnos != null) {
            removeAttribute(runtimeParamAnnos);
        }
        if (runtimeParamAnnos2 != null) {
            removeAttribute(runtimeParamAnnos2);
        }
        this.haveUnpackedParameterAnnotations = true;
    }

    private List<AnnotationGen> makeMutableVersion(AnnotationGen[] annotationGenArr) {
        ArrayList arrayList = new ArrayList();
        for (AnnotationGen annotationGen : annotationGenArr) {
            arrayList.add(new AnnotationGen(annotationGen, getConstantPool(), false));
        }
        return arrayList;
    }

    public void addParameterAnnotation(int i, AnnotationGen annotationGen) {
        ensureExistingParameterAnnotationsUnpacked();
        if (!this.hasParameterAnnotations) {
            this.param_annotations = new List[this.parameterTypes.length];
            this.hasParameterAnnotations = true;
        }
        List<AnnotationGen> list = this.param_annotations[i];
        if (list != null) {
            list.add(annotationGen);
            return;
        }
        ArrayList arrayList = new ArrayList();
        arrayList.add(annotationGen);
        this.param_annotations[i] = arrayList;
    }
}
