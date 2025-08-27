package org.aspectj.weaver.bcel;

import com.itextpdf.kernel.pdf.tagging.StandardRoles;
import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import org.apache.ibatis.javassist.bytecode.SyntheticAttribute;
import org.apache.ibatis.ognl.OgnlContext;
import org.aspectj.apache.bcel.Constants;
import org.aspectj.apache.bcel.classfile.Attribute;
import org.aspectj.apache.bcel.classfile.ConstantPool;
import org.aspectj.apache.bcel.classfile.Method;
import org.aspectj.apache.bcel.classfile.Synthetic;
import org.aspectj.apache.bcel.classfile.annotation.AnnotationGen;
import org.aspectj.apache.bcel.generic.BranchHandle;
import org.aspectj.apache.bcel.generic.ClassGenException;
import org.aspectj.apache.bcel.generic.CodeExceptionGen;
import org.aspectj.apache.bcel.generic.Instruction;
import org.aspectj.apache.bcel.generic.InstructionBranch;
import org.aspectj.apache.bcel.generic.InstructionHandle;
import org.aspectj.apache.bcel.generic.InstructionList;
import org.aspectj.apache.bcel.generic.InstructionSelect;
import org.aspectj.apache.bcel.generic.InstructionTargeter;
import org.aspectj.apache.bcel.generic.LineNumberTag;
import org.aspectj.apache.bcel.generic.LocalVariableTag;
import org.aspectj.apache.bcel.generic.MethodGen;
import org.aspectj.apache.bcel.generic.ObjectType;
import org.aspectj.apache.bcel.generic.Tag;
import org.aspectj.apache.bcel.generic.TargetLostException;
import org.aspectj.apache.bcel.generic.Type;
import org.aspectj.bridge.AbortException;
import org.aspectj.bridge.IMessage;
import org.aspectj.bridge.ISourceLocation;
import org.aspectj.weaver.AjAttribute;
import org.aspectj.weaver.AnnotationAJ;
import org.aspectj.weaver.BCException;
import org.aspectj.weaver.ISourceContext;
import org.aspectj.weaver.MemberImpl;
import org.aspectj.weaver.NameMangler;
import org.aspectj.weaver.ResolvedMember;
import org.aspectj.weaver.ResolvedType;
import org.aspectj.weaver.Shadow;
import org.aspectj.weaver.UnresolvedType;
import org.aspectj.weaver.WeaverMessages;
import org.aspectj.weaver.World;
import org.aspectj.weaver.tools.Traceable;
import org.springframework.jdbc.datasource.init.ScriptUtils;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/bcel/LazyMethodGen.class */
public final class LazyMethodGen implements Traceable {
    private static final AnnotationAJ[] NO_ANNOTATIONAJ = new AnnotationAJ[0];
    private int modifiers;
    private Type returnType;
    private final String name;
    private Type[] argumentTypes;
    private String[] declaredExceptions;
    private InstructionList body;
    private List<Attribute> attributes;
    private List<AnnotationAJ> newAnnotations;
    private List<ResolvedType> annotationsForRemoval;
    private AnnotationAJ[][] newParameterAnnotations;
    private final LazyClassGen enclosingClass;
    private BcelMethod memberView;
    private AjAttribute.EffectiveSignatureAttribute effectiveSignature;
    int highestLineNumber;
    boolean wasPackedOptimally;
    private Method savedMethod;
    private final boolean originalMethodHasLocalVariableTable;
    String fromFilename;
    private int maxLocals;
    private boolean canInline;
    private boolean isSynthetic;
    List<BcelShadow> matchedShadows;
    public ResolvedType definingType;

    /* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/bcel/LazyMethodGen$LightweightBcelMethod.class */
    static class LightweightBcelMethod extends BcelMethod {
        LightweightBcelMethod(BcelObjectType declaringType, Method method) {
            super(declaringType, method);
        }
    }

    public LazyMethodGen(int modifiers, Type returnType, String name, Type[] paramTypes, String[] declaredExceptions, LazyClassGen enclosingClass) {
        this.highestLineNumber = 0;
        this.wasPackedOptimally = false;
        this.savedMethod = null;
        this.fromFilename = null;
        this.canInline = true;
        this.isSynthetic = false;
        this.definingType = null;
        this.memberView = null;
        this.modifiers = modifiers;
        this.returnType = returnType;
        this.name = name;
        this.argumentTypes = paramTypes;
        this.declaredExceptions = declaredExceptions;
        if (!Modifier.isAbstract(modifiers)) {
            this.body = new InstructionList();
            setMaxLocals(calculateMaxLocals());
        } else {
            this.body = null;
        }
        this.attributes = new ArrayList();
        this.enclosingClass = enclosingClass;
        assertGoodBody();
        this.originalMethodHasLocalVariableTable = true;
        if (this.memberView != null && isAdviceMethod() && enclosingClass.getType().isAnnotationStyleAspect()) {
            this.canInline = false;
        }
    }

    private int calculateMaxLocals() {
        int ret = Modifier.isStatic(this.modifiers) ? 0 : 1;
        for (Type type : this.argumentTypes) {
            ret += type.getSize();
        }
        return ret;
    }

    public LazyMethodGen(Method m, LazyClassGen enclosingClass) {
        this.highestLineNumber = 0;
        this.wasPackedOptimally = false;
        this.savedMethod = null;
        this.fromFilename = null;
        this.canInline = true;
        this.isSynthetic = false;
        this.definingType = null;
        this.savedMethod = m;
        this.enclosingClass = enclosingClass;
        if (!m.isAbstract() && !m.isNative() && m.getCode() == null) {
            throw new RuntimeException("bad non-abstract method with no code: " + m + " on " + enclosingClass);
        }
        if ((m.isAbstract() || m.isNative()) && m.getCode() != null) {
            throw new RuntimeException("bad abstract method with code: " + m + " on " + enclosingClass);
        }
        this.memberView = new BcelMethod(enclosingClass.getBcelObjectType(), m);
        this.originalMethodHasLocalVariableTable = this.savedMethod.getLocalVariableTable() != null;
        this.modifiers = m.getModifiers();
        this.name = m.getName();
        if (this.memberView != null && isAdviceMethod() && enclosingClass.getType().isAnnotationStyleAspect()) {
            this.canInline = false;
        }
    }

    private boolean isAbstractOrNative(int modifiers) {
        return Modifier.isAbstract(modifiers) || Modifier.isNative(modifiers);
    }

    public LazyMethodGen(BcelMethod m, LazyClassGen enclosingClass) {
        this.highestLineNumber = 0;
        this.wasPackedOptimally = false;
        this.savedMethod = null;
        this.fromFilename = null;
        this.canInline = true;
        this.isSynthetic = false;
        this.definingType = null;
        this.savedMethod = m.getMethod();
        this.enclosingClass = enclosingClass;
        if (!isAbstractOrNative(m.getModifiers()) && this.savedMethod.getCode() == null) {
            throw new RuntimeException("bad non-abstract method with no code: " + m + " on " + enclosingClass);
        }
        if (isAbstractOrNative(m.getModifiers()) && this.savedMethod.getCode() != null) {
            throw new RuntimeException("bad abstract method with code: " + m + " on " + enclosingClass);
        }
        this.memberView = m;
        this.modifiers = this.savedMethod.getModifiers();
        this.name = m.getName();
        this.originalMethodHasLocalVariableTable = this.savedMethod.getLocalVariableTable() != null;
        if (this.memberView != null && isAdviceMethod() && enclosingClass.getType().isAnnotationStyleAspect()) {
            this.canInline = false;
        }
    }

    public boolean hasDeclaredLineNumberInfo() {
        return this.memberView != null && this.memberView.hasDeclarationLineNumberInfo();
    }

    public int getDeclarationLineNumber() {
        if (hasDeclaredLineNumberInfo()) {
            return this.memberView.getDeclarationLineNumber();
        }
        return -1;
    }

    public int getDeclarationOffset() {
        if (hasDeclaredLineNumberInfo()) {
            return this.memberView.getDeclarationOffset();
        }
        return 0;
    }

    public void addAnnotation(AnnotationAJ ax) {
        initialize();
        if (this.memberView == null) {
            if (this.newAnnotations == null) {
                this.newAnnotations = new ArrayList();
            }
            this.newAnnotations.add(ax);
            return;
        }
        this.memberView.addAnnotation(ax);
    }

    public void removeAnnotation(ResolvedType annotationType) {
        initialize();
        if (this.memberView == null) {
            if (this.annotationsForRemoval == null) {
                this.annotationsForRemoval = new ArrayList();
            }
            this.annotationsForRemoval.add(annotationType);
            return;
        }
        this.memberView.removeAnnotation(annotationType);
    }

    /* JADX WARN: Type inference failed for: r1v8, types: [org.aspectj.weaver.AnnotationAJ[], org.aspectj.weaver.AnnotationAJ[][]] */
    public void addParameterAnnotation(int parameterNumber, AnnotationAJ anno) {
        initialize();
        if (this.memberView == null) {
            if (this.newParameterAnnotations == null) {
                int pcount = getArgumentTypes().length;
                this.newParameterAnnotations = new AnnotationAJ[pcount];
                for (int i = 0; i < pcount; i++) {
                    if (i == parameterNumber) {
                        this.newParameterAnnotations[i] = new AnnotationAJ[1];
                        this.newParameterAnnotations[i][0] = anno;
                    } else {
                        this.newParameterAnnotations[i] = NO_ANNOTATIONAJ;
                    }
                }
                return;
            }
            AnnotationAJ[] currentAnnoArray = this.newParameterAnnotations[parameterNumber];
            AnnotationAJ[] newAnnoArray = new AnnotationAJ[currentAnnoArray.length + 1];
            System.arraycopy(currentAnnoArray, 0, newAnnoArray, 0, currentAnnoArray.length);
            newAnnoArray[currentAnnoArray.length] = anno;
            this.newParameterAnnotations[parameterNumber] = newAnnoArray;
            return;
        }
        this.memberView.addParameterAnnotation(parameterNumber, anno);
    }

    public ResolvedType[] getAnnotationTypes() {
        initialize();
        if (this.memberView == null && this.newAnnotations != null && this.newAnnotations.size() != 0) {
            ResolvedType[] annotationTypes = new ResolvedType[this.newAnnotations.size()];
            int len = this.newAnnotations.size();
            for (int a = 0; a < len; a++) {
                annotationTypes[a] = this.newAnnotations.get(a).getType();
            }
            return annotationTypes;
        }
        return null;
    }

    public AnnotationAJ[] getAnnotations() {
        initialize();
        if (this.memberView == null && this.newAnnotations != null && this.newAnnotations.size() != 0) {
            return (AnnotationAJ[]) this.newAnnotations.toArray(new AnnotationAJ[this.newAnnotations.size()]);
        }
        return null;
    }

    public boolean hasAnnotation(UnresolvedType annotationType) {
        initialize();
        if (this.memberView == null) {
            if (this.annotationsForRemoval != null) {
                for (ResolvedType at : this.annotationsForRemoval) {
                    if (at.equals(annotationType)) {
                        return false;
                    }
                }
            }
            if (this.newAnnotations != null) {
                for (AnnotationAJ annotation : this.newAnnotations) {
                    if (annotation.getTypeSignature().equals(annotationType.getSignature())) {
                        return true;
                    }
                }
            }
            this.memberView = new BcelMethod(getEnclosingClass().getBcelObjectType(), getMethod());
            return this.memberView.hasAnnotation(annotationType);
        }
        return this.memberView.hasAnnotation(annotationType);
    }

    private void initialize() {
        if (this.returnType != null) {
            return;
        }
        MethodGen gen = new MethodGen(this.savedMethod, this.enclosingClass.getName(), this.enclosingClass.getConstantPool(), true);
        this.returnType = gen.getReturnType();
        this.argumentTypes = gen.getArgumentTypes();
        this.declaredExceptions = gen.getExceptions();
        this.attributes = gen.getAttributes();
        this.maxLocals = gen.getMaxLocals();
        if (gen.isAbstract() || gen.isNative()) {
            this.body = null;
        } else {
            this.body = gen.getInstructionList();
            unpackHandlers(gen);
            ensureAllLineNumberSetup();
            this.highestLineNumber = gen.getHighestlinenumber();
        }
        assertGoodBody();
    }

    private void unpackHandlers(MethodGen gen) {
        CodeExceptionGen[] exns = gen.getExceptionHandlers();
        if (exns != null) {
            int len = exns.length;
            int priority = len - 1;
            int i = 0;
            while (i < len) {
                CodeExceptionGen exn = exns[i];
                InstructionHandle start = Range.genStart(this.body, getOutermostExceptionStart(exn.getStartPC()));
                InstructionHandle end = Range.genEnd(this.body, getOutermostExceptionEnd(exn.getEndPC()));
                ExceptionRange er = new ExceptionRange(this.body, exn.getCatchType() == null ? null : BcelWorld.fromBcel(exn.getCatchType()), priority);
                er.associateWithTargets(start, end, exn.getHandlerPC());
                exn.setStartPC(null);
                exn.setEndPC(null);
                exn.setHandlerPC(null);
                i++;
                priority--;
            }
            gen.removeExceptionHandlers();
        }
    }

    private InstructionHandle getOutermostExceptionStart(InstructionHandle ih) {
        while (ExceptionRange.isExceptionStart(ih.getPrev())) {
            ih = ih.getPrev();
        }
        return ih;
    }

    private InstructionHandle getOutermostExceptionEnd(InstructionHandle ih) {
        while (ExceptionRange.isExceptionEnd(ih.getNext())) {
            ih = ih.getNext();
        }
        return ih;
    }

    public void ensureAllLineNumberSetup() {
        LineNumberTag lastKnownLineNumberTag = null;
        InstructionHandle start = this.body.getStart();
        while (true) {
            InstructionHandle ih = start;
            if (ih != null) {
                boolean skip = false;
                for (InstructionTargeter targeter : ih.getTargeters()) {
                    if (targeter instanceof LineNumberTag) {
                        lastKnownLineNumberTag = (LineNumberTag) targeter;
                        skip = true;
                    }
                }
                if (lastKnownLineNumberTag != null && !skip) {
                    ih.addTargeter(lastKnownLineNumberTag);
                }
                start = ih.getNext();
            } else {
                return;
            }
        }
    }

    public int allocateLocal(Type type) {
        return allocateLocal(type.getSize());
    }

    public int allocateLocal(int slots) {
        int max = getMaxLocals();
        setMaxLocals(max + slots);
        return max;
    }

    public Method getMethod() {
        if (this.savedMethod != null) {
            return this.savedMethod;
        }
        try {
            MethodGen gen = pack();
            this.savedMethod = gen.getMethod();
            return this.savedMethod;
        } catch (ClassGenException e) {
            this.enclosingClass.getBcelObjectType().getResolvedTypeX().getWorld().showMessage(IMessage.ERROR, WeaverMessages.format(WeaverMessages.PROBLEM_GENERATING_METHOD, getClassName(), getName(), e.getMessage()), getMemberView() == null ? null : getMemberView().getSourceLocation(), null);
            this.body = null;
            MethodGen gen2 = pack();
            return gen2.getMethod();
        } catch (RuntimeException re) {
            if (re.getCause() instanceof ClassGenException) {
                this.enclosingClass.getBcelObjectType().getResolvedTypeX().getWorld().showMessage(IMessage.ERROR, WeaverMessages.format(WeaverMessages.PROBLEM_GENERATING_METHOD, getClassName(), getName(), re.getCause().getMessage()), getMemberView() == null ? null : getMemberView().getSourceLocation(), null);
                this.body = null;
                MethodGen gen3 = pack();
                return gen3.getMethod();
            }
            throw re;
        }
    }

    public void markAsChanged() {
        if (this.wasPackedOptimally) {
            throw new RuntimeException("Already packed method is being re-modified: " + getClassName() + SymbolConstants.SPACE_SYMBOL + toShortString());
        }
        initialize();
        this.savedMethod = null;
    }

    public String toString() {
        BcelObjectType bot = this.enclosingClass.getBcelObjectType();
        AjAttribute.WeaverVersionInfo weaverVersion = bot == null ? AjAttribute.WeaverVersionInfo.CURRENT : bot.getWeaverVersionAttribute();
        return toLongString(weaverVersion);
    }

    public String toShortString() {
        String access = org.aspectj.apache.bcel.classfile.Utility.accessToString(getAccessFlags());
        StringBuffer buf = new StringBuffer();
        if (!access.equals("")) {
            buf.append(access);
            buf.append(SymbolConstants.SPACE_SYMBOL);
        }
        buf.append(org.aspectj.apache.bcel.classfile.Utility.signatureToString(getReturnType().getSignature(), true));
        buf.append(SymbolConstants.SPACE_SYMBOL);
        buf.append(getName());
        buf.append("(");
        int len = this.argumentTypes.length;
        if (len > 0) {
            buf.append(org.aspectj.apache.bcel.classfile.Utility.signatureToString(this.argumentTypes[0].getSignature(), true));
            for (int i = 1; i < this.argumentTypes.length; i++) {
                buf.append(", ");
                buf.append(org.aspectj.apache.bcel.classfile.Utility.signatureToString(this.argumentTypes[i].getSignature(), true));
            }
        }
        buf.append(")");
        int len2 = this.declaredExceptions != null ? this.declaredExceptions.length : 0;
        if (len2 > 0) {
            buf.append(" throws ");
            buf.append(this.declaredExceptions[0]);
            for (int i2 = 1; i2 < this.declaredExceptions.length; i2++) {
                buf.append(", ");
                buf.append(this.declaredExceptions[i2]);
            }
        }
        return buf.toString();
    }

    public String toLongString(AjAttribute.WeaverVersionInfo weaverVersion) {
        ByteArrayOutputStream s = new ByteArrayOutputStream();
        print(new PrintStream(s), weaverVersion);
        return new String(s.toByteArray());
    }

    public void print(AjAttribute.WeaverVersionInfo weaverVersion) {
        print(System.out, weaverVersion);
    }

    public void print(PrintStream out, AjAttribute.WeaverVersionInfo weaverVersion) {
        out.print("  " + toShortString());
        printAspectAttributes(out, weaverVersion);
        InstructionList body = getBody();
        if (body == null) {
            out.println(ScriptUtils.DEFAULT_STATEMENT_SEPARATOR);
            return;
        }
        out.println(":");
        new BodyPrinter(out).run();
        out.println("  end " + toShortString());
    }

    private void printAspectAttributes(PrintStream out, AjAttribute.WeaverVersionInfo weaverVersion) throws AbortException {
        ISourceContext context = null;
        if (this.enclosingClass != null && this.enclosingClass.getType() != null) {
            context = this.enclosingClass.getType().getSourceContext();
        }
        List<AjAttribute> as = Utility.readAjAttributes(getClassName(), (Attribute[]) this.attributes.toArray(new Attribute[0]), context, null, weaverVersion, new BcelConstantPoolReader(this.enclosingClass.getConstantPool()));
        if (!as.isEmpty()) {
            out.println("    " + as.get(0));
        }
    }

    /* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/bcel/LazyMethodGen$BodyPrinter.class */
    private class BodyPrinter {
        Map<InstructionHandle, String> labelMap = new HashMap();
        InstructionList body;
        PrintStream out;
        ConstantPool pool;
        static final int BODY_INDENT = 4;
        static final int CODE_INDENT = 16;

        BodyPrinter(PrintStream out) {
            this.pool = LazyMethodGen.this.enclosingClass.getConstantPool();
            this.body = LazyMethodGen.this.getBodyForPrint();
            this.out = out;
        }

        BodyPrinter(PrintStream out, InstructionList il) {
            this.pool = LazyMethodGen.this.enclosingClass.getConstantPool();
            this.body = il;
            this.out = out;
        }

        void run() {
            assignLabels();
            print();
        }

        void assignLabels() {
            LinkedList<ExceptionRange> exnTable = new LinkedList<>();
            String pendingLabel = null;
            int lcounter = 0;
            InstructionHandle start = this.body.getStart();
            while (true) {
                InstructionHandle ih = start;
                if (ih == null) {
                    break;
                }
                for (InstructionTargeter t : ih.getTargeters()) {
                    if (t instanceof ExceptionRange) {
                        ExceptionRange r = (ExceptionRange) t;
                        if (r.getStart() == ih) {
                            LazyMethodGen.insertHandler(r, exnTable);
                        }
                    } else if ((t instanceof InstructionBranch) && pendingLabel == null) {
                        int i = lcounter;
                        lcounter++;
                        pendingLabel = StandardRoles.L + i;
                    }
                }
                if (pendingLabel != null) {
                    this.labelMap.put(ih, pendingLabel);
                    if (!Range.isRangeHandle(ih)) {
                        pendingLabel = null;
                    }
                }
                start = ih.getNext();
            }
            int ecounter = 0;
            Iterator<ExceptionRange> it = exnTable.iterator();
            while (it.hasNext()) {
                ExceptionRange er = it.next();
                int i2 = ecounter;
                ecounter++;
                String exceptionLabel = "E" + i2;
                this.labelMap.put(Range.getRealStart(er.getHandler()), exceptionLabel);
                this.labelMap.put(er.getHandler(), exceptionLabel);
            }
        }

        void print() {
            int depth = 0;
            int currLine = -1;
            InstructionHandle start = this.body.getStart();
            while (true) {
                InstructionHandle ih = start;
                if (ih != null) {
                    if (Range.isRangeHandle(ih)) {
                        Range r = Range.getRange(ih);
                        InstructionHandle start2 = r.getStart();
                        while (true) {
                            InstructionHandle xx = start2;
                            if (Range.isRangeHandle(xx)) {
                                if (xx == r.getEnd()) {
                                    break;
                                } else {
                                    start2 = xx.getNext();
                                }
                            } else if (r.getStart() == ih) {
                                int i = depth;
                                depth++;
                                printRangeString(r, i);
                            } else {
                                if (r.getEnd() != ih) {
                                    throw new RuntimeException("bad");
                                }
                                depth--;
                                printRangeString(r, depth);
                            }
                        }
                    } else {
                        printInstruction(ih, depth);
                        int line = LazyMethodGen.getLineNumber(ih, currLine);
                        if (line != currLine) {
                            currLine = line;
                            this.out.println("   (line " + line + ")");
                        } else {
                            this.out.println();
                        }
                    }
                    start = ih.getNext();
                } else {
                    return;
                }
            }
        }

        void printRangeString(Range r, int depth) {
            printDepth(depth);
            this.out.println(getRangeString(r, this.labelMap));
        }

        String getRangeString(Range r, Map<InstructionHandle, String> labelMap) {
            if (r instanceof ExceptionRange) {
                ExceptionRange er = (ExceptionRange) r;
                return er.toString() + " -> " + labelMap.get(er.getHandler());
            }
            return r.toString();
        }

        void printDepth(int depth) {
            pad(4);
            while (depth > 0) {
                this.out.print("| ");
                depth--;
            }
        }

        void printLabel(String s, int depth) {
            int space = Math.max(16 - (depth * 2), 0);
            if (s == null) {
                pad(space);
                return;
            }
            pad(Math.max(space - (s.length() + 2), 0));
            this.out.print(s);
            this.out.print(": ");
        }

        void printInstruction(InstructionHandle h, int depth) {
            printDepth(depth);
            printLabel(this.labelMap.get(h), depth);
            Instruction inst = h.getInstruction();
            if (inst.isConstantPoolInstruction()) {
                this.out.print(Constants.OPCODE_NAMES[inst.opcode].toUpperCase());
                this.out.print(SymbolConstants.SPACE_SYMBOL);
                this.out.print(this.pool.constantToString(this.pool.getConstant(inst.getIndex())));
                return;
            }
            if (!(inst instanceof InstructionSelect)) {
                if (inst instanceof InstructionBranch) {
                    InstructionBranch brinst = (InstructionBranch) inst;
                    this.out.print(Constants.OPCODE_NAMES[brinst.getOpcode()].toUpperCase());
                    this.out.print(SymbolConstants.SPACE_SYMBOL);
                    this.out.print(this.labelMap.get(brinst.getTarget()));
                    return;
                }
                if (inst.isLocalVariableInstruction()) {
                    this.out.print(inst.toString(false).toUpperCase());
                    int index = inst.getIndex();
                    LocalVariableTag tag = LazyMethodGen.getLocalVariableTag(h, index);
                    if (tag != null) {
                        this.out.print("     // ");
                        this.out.print(tag.getType());
                        this.out.print(SymbolConstants.SPACE_SYMBOL);
                        this.out.print(tag.getName());
                        return;
                    }
                    return;
                }
                this.out.print(inst.toString(false).toUpperCase());
                return;
            }
            InstructionSelect sinst = (InstructionSelect) inst;
            this.out.println(Constants.OPCODE_NAMES[sinst.opcode].toUpperCase());
            int[] matches = sinst.getMatchs();
            InstructionHandle[] targets = sinst.getTargets();
            InstructionHandle defaultTarget = sinst.getTarget();
            int len = matches.length;
            for (int i = 0; i < len; i++) {
                printDepth(depth);
                printLabel(null, depth);
                this.out.print("  ");
                this.out.print(matches[i]);
                this.out.print(": \t");
                this.out.println(this.labelMap.get(targets[i]));
            }
            printDepth(depth);
            printLabel(null, depth);
            this.out.print("  ");
            this.out.print("default: \t");
            this.out.print(this.labelMap.get(defaultTarget));
        }

        void pad(int size) {
            for (int i = 0; i < size; i++) {
                this.out.print(SymbolConstants.SPACE_SYMBOL);
            }
        }
    }

    static LocalVariableTag getLocalVariableTag(InstructionHandle ih, int index) {
        for (InstructionTargeter t : ih.getTargeters()) {
            if (t instanceof LocalVariableTag) {
                LocalVariableTag lvt = (LocalVariableTag) t;
                if (lvt.getSlot() == index) {
                    return lvt;
                }
            }
        }
        return null;
    }

    static int getLineNumber(InstructionHandle ih, int prevLine) {
        for (InstructionTargeter t : ih.getTargeters()) {
            if (t instanceof LineNumberTag) {
                return ((LineNumberTag) t).getLineNumber();
            }
        }
        return prevLine;
    }

    public boolean isStatic() {
        return Modifier.isStatic(getAccessFlags());
    }

    public boolean isAbstract() {
        return Modifier.isAbstract(getAccessFlags());
    }

    public boolean isBridgeMethod() {
        return (getAccessFlags() & 64) != 0;
    }

    public void addExceptionHandler(InstructionHandle start, InstructionHandle end, InstructionHandle handlerStart, ObjectType catchType, boolean highPriority) {
        InstructionHandle start1 = Range.genStart(this.body, start);
        InstructionHandle end1 = Range.genEnd(this.body, end);
        ExceptionRange er = new ExceptionRange(this.body, catchType == null ? null : BcelWorld.fromBcel(catchType), highPriority);
        er.associateWithTargets(start1, end1, handlerStart);
    }

    public int getAccessFlags() {
        return this.modifiers;
    }

    public int getAccessFlagsWithoutSynchronized() {
        if (isSynchronized()) {
            return this.modifiers - 32;
        }
        return this.modifiers;
    }

    public boolean isSynchronized() {
        return (this.modifiers & 32) != 0;
    }

    public void setAccessFlags(int newFlags) {
        this.modifiers = newFlags;
    }

    public Type[] getArgumentTypes() {
        initialize();
        return this.argumentTypes;
    }

    public LazyClassGen getEnclosingClass() {
        return this.enclosingClass;
    }

    public int getMaxLocals() {
        return this.maxLocals;
    }

    public String getName() {
        return this.name;
    }

    public String getGenericReturnTypeSignature() {
        if (this.memberView == null) {
            return getReturnType().getSignature();
        }
        return this.memberView.getGenericReturnType().getSignature();
    }

    public Type getReturnType() {
        initialize();
        return this.returnType;
    }

    public void setMaxLocals(int maxLocals) {
        this.maxLocals = maxLocals;
    }

    public InstructionList getBody() {
        markAsChanged();
        return this.body;
    }

    public InstructionList getBodyForPrint() {
        return this.body;
    }

    public boolean hasBody() {
        return this.savedMethod != null ? this.savedMethod.getCode() != null : this.body != null;
    }

    public List<Attribute> getAttributes() {
        return this.attributes;
    }

    public String[] getDeclaredExceptions() {
        return this.declaredExceptions;
    }

    public String getClassName() {
        return this.enclosingClass.getName();
    }

    public MethodGen pack() {
        forceSyntheticForAjcMagicMembers();
        int flags = getAccessFlags();
        if (this.enclosingClass.getWorld().isJoinpointSynchronizationEnabled() && this.enclosingClass.getWorld().areSynchronizationPointcutsInUse()) {
            flags = getAccessFlagsWithoutSynchronized();
        }
        MethodGen gen = new MethodGen(flags, getReturnType(), getArgumentTypes(), null, getName(), getEnclosingClass().getName(), new InstructionList(), getEnclosingClass().getConstantPool());
        int len = this.declaredExceptions.length;
        for (int i = 0; i < len; i++) {
            gen.addException(this.declaredExceptions[i]);
        }
        for (Attribute attr : this.attributes) {
            gen.addAttribute(attr);
        }
        if (this.newAnnotations != null) {
            for (AnnotationAJ element : this.newAnnotations) {
                gen.addAnnotation(new AnnotationGen(((BcelAnnotation) element).getBcelAnnotation(), gen.getConstantPool(), true));
            }
        }
        if (this.newParameterAnnotations != null) {
            for (int i2 = 0; i2 < this.newParameterAnnotations.length; i2++) {
                AnnotationAJ[] annos = this.newParameterAnnotations[i2];
                for (AnnotationAJ annotationAJ : annos) {
                    gen.addParameterAnnotation(i2, new AnnotationGen(((BcelAnnotation) annotationAJ).getBcelAnnotation(), gen.getConstantPool(), true));
                }
            }
        }
        if (this.memberView != null && this.memberView.getAnnotations() != null && this.memberView.getAnnotations().length != 0) {
            AnnotationAJ[] ans = this.memberView.getAnnotations();
            for (AnnotationAJ annotationAJ2 : ans) {
                AnnotationGen a = ((BcelAnnotation) annotationAJ2).getBcelAnnotation();
                gen.addAnnotation(new AnnotationGen(a, gen.getConstantPool(), true));
            }
        }
        if (this.isSynthetic) {
            if (this.enclosingClass.getWorld().isInJava5Mode()) {
                gen.setModifiers(gen.getModifiers() | 4096);
            }
            if (!hasAttribute(SyntheticAttribute.tag)) {
                ConstantPool cpg = gen.getConstantPool();
                int index = cpg.addUtf8(SyntheticAttribute.tag);
                gen.addAttribute(new Synthetic(index, 0, new byte[0], cpg));
            }
        }
        if (hasBody()) {
            if (!this.enclosingClass.getWorld().shouldFastPackMethods() || isAdviceMethod() || getName().equals("<clinit>")) {
                packBody(gen);
            } else {
                optimizedPackBody(gen);
            }
            gen.setMaxLocals(true);
            gen.setMaxStack();
        } else {
            gen.setInstructionList(null);
        }
        return gen;
    }

    private boolean hasAttribute(String attributeName) {
        for (Attribute attr : this.attributes) {
            if (attr.getName().equals(attributeName)) {
                return true;
            }
        }
        return false;
    }

    private void forceSyntheticForAjcMagicMembers() {
        if (NameMangler.isSyntheticMethod(getName(), inAspect())) {
            makeSynthetic();
        }
    }

    private boolean inAspect() {
        BcelObjectType objectType = this.enclosingClass.getBcelObjectType();
        if (objectType == null) {
            return false;
        }
        return objectType.isAspect();
    }

    public void makeSynthetic() {
        this.isSynthetic = true;
    }

    /* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/bcel/LazyMethodGen$LVPosition.class */
    private static class LVPosition {
        InstructionHandle start;
        InstructionHandle end;

        private LVPosition() {
            this.start = null;
            this.end = null;
        }
    }

    public void packBody(MethodGen gen) {
        InstructionList fresh = gen.getInstructionList();
        Map<InstructionHandle, InstructionHandle> map = copyAllInstructionsExceptRangeInstructionsInto(fresh);
        InstructionHandle oldInstructionHandle = getBody().getStart();
        InstructionHandle newInstructionHandle = fresh.getStart();
        LinkedList<ExceptionRange> exceptionList = new LinkedList<>();
        Map<LocalVariableTag, LVPosition> localVariables = new HashMap<>();
        int currLine = -1;
        int lineNumberOffset = this.fromFilename == null ? 0 : getEnclosingClass().getSourceDebugExtensionOffset(this.fromFilename);
        while (oldInstructionHandle != null) {
            if (map.get(oldInstructionHandle) == null) {
                handleRangeInstruction(oldInstructionHandle, exceptionList);
                oldInstructionHandle = oldInstructionHandle.getNext();
            } else {
                Instruction oldInstruction = oldInstructionHandle.getInstruction();
                Instruction newInstruction = newInstructionHandle.getInstruction();
                if (oldInstruction instanceof InstructionBranch) {
                    handleBranchInstruction(map, oldInstruction, newInstruction);
                }
                for (InstructionTargeter targeter : oldInstructionHandle.getTargeters()) {
                    if (targeter instanceof LineNumberTag) {
                        int line = ((LineNumberTag) targeter).getLineNumber();
                        if (line != currLine) {
                            gen.addLineNumber(newInstructionHandle, line + lineNumberOffset);
                            currLine = line;
                        }
                    } else if (targeter instanceof LocalVariableTag) {
                        LocalVariableTag lvt = (LocalVariableTag) targeter;
                        LVPosition p = localVariables.get(lvt);
                        if (p == null) {
                            LVPosition newp = new LVPosition();
                            InstructionHandle instructionHandle = newInstructionHandle;
                            newp.end = instructionHandle;
                            newp.start = instructionHandle;
                            localVariables.put(lvt, newp);
                        } else {
                            p.end = newInstructionHandle;
                        }
                    }
                }
                oldInstructionHandle = oldInstructionHandle.getNext();
                newInstructionHandle = newInstructionHandle.getNext();
            }
        }
        addExceptionHandlers(gen, map, exceptionList);
        if (this.originalMethodHasLocalVariableTable || this.enclosingClass.getBcelObjectType().getResolvedTypeX().getWorld().generateNewLvts) {
            if (localVariables.size() == 0) {
                createNewLocalVariables(gen);
            } else {
                addLocalVariables(gen, localVariables);
            }
        }
        if (gen.getLineNumbers().length == 0) {
            gen.addLineNumber(gen.getInstructionList().getStart(), 1);
        }
    }

    private void createNewLocalVariables(MethodGen gen) {
        gen.removeLocalVariables();
        if (!getName().startsWith("<")) {
            int slot = 0;
            InstructionHandle start = gen.getInstructionList().getStart();
            InstructionHandle end = gen.getInstructionList().getEnd();
            if (!isStatic()) {
                String cname = this.enclosingClass.getClassName();
                if (cname == null) {
                    return;
                }
                Type enclosingType = BcelWorld.makeBcelType(UnresolvedType.forName(cname));
                slot = 0 + 1;
                gen.addLocalVariable(OgnlContext.THIS_CONTEXT_KEY, enclosingType, 0, start, end);
            }
            String[] paramNames = this.memberView == null ? null : this.memberView.getParameterNames();
            if (paramNames != null) {
                for (int i = 0; i < this.argumentTypes.length; i++) {
                    String pname = paramNames[i];
                    if (pname == null) {
                        pname = "arg" + i;
                    }
                    gen.addLocalVariable(pname, this.argumentTypes[i], slot, start, end);
                    slot += this.argumentTypes[i].getSize();
                }
            }
        }
    }

    private World getWorld() {
        return this.enclosingClass.getBcelObjectType().getResolvedTypeX().getWorld();
    }

    public void optimizedPackBody(MethodGen gen) {
        InstructionList theBody = getBody();
        int currLine = -1;
        int lineNumberOffset = this.fromFilename == null ? 0 : getEnclosingClass().getSourceDebugExtensionOffset(this.fromFilename);
        Map<LocalVariableTag, LVPosition> localVariables = new HashMap<>();
        LinkedList<ExceptionRange> exceptionList = new LinkedList<>();
        Set<InstructionHandle> forDeletion = new HashSet<>();
        Set<BranchHandle> branchInstructions = new HashSet<>();
        for (InstructionHandle iHandle = theBody.getStart(); iHandle != null; iHandle = iHandle.getNext()) {
            Instruction inst = iHandle.getInstruction();
            if (inst == Range.RANGEINSTRUCTION) {
                Range r = Range.getRange(iHandle);
                if (r instanceof ExceptionRange) {
                    ExceptionRange er = (ExceptionRange) r;
                    if (er.getStart() == iHandle && !er.isEmpty()) {
                        insertHandler(er, exceptionList);
                    }
                }
                forDeletion.add(iHandle);
            } else {
                if (inst instanceof InstructionBranch) {
                    branchInstructions.add((BranchHandle) iHandle);
                }
                for (InstructionTargeter targeter : iHandle.getTargetersCopy()) {
                    if (targeter instanceof LineNumberTag) {
                        int line = ((LineNumberTag) targeter).getLineNumber();
                        if (line != currLine) {
                            gen.addLineNumber(iHandle, line + lineNumberOffset);
                            currLine = line;
                        }
                    } else if (targeter instanceof LocalVariableTag) {
                        LocalVariableTag lvt = (LocalVariableTag) targeter;
                        LVPosition p = localVariables.get(lvt);
                        if (p == null) {
                            LVPosition newp = new LVPosition();
                            InstructionHandle instructionHandle = iHandle;
                            newp.end = instructionHandle;
                            newp.start = instructionHandle;
                            localVariables.put(lvt, newp);
                        } else {
                            p.end = iHandle;
                        }
                    }
                }
            }
        }
        for (BranchHandle branchHandle : branchInstructions) {
            handleBranchInstruction(branchHandle, forDeletion);
        }
        Iterator<ExceptionRange> it = exceptionList.iterator();
        while (it.hasNext()) {
            ExceptionRange r2 = it.next();
            if (!r2.isEmpty()) {
                gen.addExceptionHandler(jumpForward(r2.getRealStart(), forDeletion), jumpForward(r2.getRealEnd(), forDeletion), jumpForward(r2.getHandler(), forDeletion), r2.getCatchType() == null ? null : (ObjectType) BcelWorld.makeBcelType(r2.getCatchType()));
            }
        }
        for (InstructionHandle handle : forDeletion) {
            try {
                theBody.delete(handle);
            } catch (TargetLostException e) {
                e.printStackTrace();
            }
        }
        gen.setInstructionList(theBody);
        if (this.originalMethodHasLocalVariableTable || getWorld().generateNewLvts) {
            if (localVariables.size() == 0) {
                createNewLocalVariables(gen);
            } else {
                addLocalVariables(gen, localVariables);
            }
        }
        if (gen.getLineNumbers().length == 0) {
            gen.addLineNumber(gen.getInstructionList().getStart(), 1);
        }
        this.wasPackedOptimally = true;
    }

    private void addLocalVariables(MethodGen gen, Map<LocalVariableTag, LVPosition> localVariables) {
        gen.removeLocalVariables();
        InstructionHandle methodStart = gen.getInstructionList().getStart();
        InstructionHandle methodEnd = gen.getInstructionList().getEnd();
        int paramSlots = gen.isStatic() ? 0 : 1;
        Type[] argTypes = gen.getArgumentTypes();
        if (argTypes != null) {
            for (Type type : argTypes) {
                if (type.getSize() == 2) {
                    paramSlots += 2;
                } else {
                    paramSlots++;
                }
            }
        }
        if (!this.enclosingClass.getWorld().generateNewLvts) {
            paramSlots = -1;
        }
        Map<InstructionHandle, Set<Integer>> duplicatedLocalMap = new HashMap<>();
        for (LocalVariableTag tag : localVariables.keySet()) {
            LVPosition lvpos = localVariables.get(tag);
            InstructionHandle start = tag.getSlot() < paramSlots ? methodStart : lvpos.start;
            InstructionHandle end = tag.getSlot() < paramSlots ? methodEnd : lvpos.end;
            Set<Integer> slots = duplicatedLocalMap.get(start);
            if (slots == null) {
                slots = new HashSet<>();
                duplicatedLocalMap.put(start, slots);
            } else if (slots.contains(new Integer(tag.getSlot()))) {
            }
            slots.add(Integer.valueOf(tag.getSlot()));
            Type t = tag.getRealType();
            if (t == null) {
                t = BcelWorld.makeBcelType(UnresolvedType.forSignature(tag.getType()));
            }
            gen.addLocalVariable(tag.getName(), t, tag.getSlot(), start, end);
        }
    }

    private void addExceptionHandlers(MethodGen gen, Map<InstructionHandle, InstructionHandle> map, LinkedList<ExceptionRange> exnList) {
        Iterator<ExceptionRange> it = exnList.iterator();
        while (it.hasNext()) {
            ExceptionRange r = it.next();
            if (!r.isEmpty()) {
                InstructionHandle rMappedStart = remap(r.getRealStart(), map);
                InstructionHandle rMappedEnd = remap(r.getRealEnd(), map);
                InstructionHandle rMappedHandler = remap(r.getHandler(), map);
                gen.addExceptionHandler(rMappedStart, rMappedEnd, rMappedHandler, r.getCatchType() == null ? null : (ObjectType) BcelWorld.makeBcelType(r.getCatchType()));
            }
        }
    }

    private void handleBranchInstruction(Map<InstructionHandle, InstructionHandle> map, Instruction oldInstruction, Instruction newInstruction) {
        InstructionBranch oldBranchInstruction = (InstructionBranch) oldInstruction;
        InstructionBranch newBranchInstruction = (InstructionBranch) newInstruction;
        InstructionHandle oldTarget = oldBranchInstruction.getTarget();
        newBranchInstruction.setTarget(remap(oldTarget, map));
        if (oldBranchInstruction instanceof InstructionSelect) {
            InstructionHandle[] oldTargets = ((InstructionSelect) oldBranchInstruction).getTargets();
            InstructionHandle[] newTargets = ((InstructionSelect) newBranchInstruction).getTargets();
            for (int k = oldTargets.length - 1; k >= 0; k--) {
                newTargets[k] = remap(oldTargets[k], map);
                newTargets[k].addTargeter(newBranchInstruction);
            }
        }
    }

    private InstructionHandle jumpForward(InstructionHandle t, Set<InstructionHandle> handlesForDeletion) {
        InstructionHandle target = t;
        if (handlesForDeletion.contains(target)) {
            do {
                target = target.getNext();
            } while (handlesForDeletion.contains(target));
        }
        return target;
    }

    private void handleBranchInstruction(BranchHandle branchHandle, Set<InstructionHandle> handlesForDeletion) {
        InstructionBranch branchInstruction = (InstructionBranch) branchHandle.getInstruction();
        InstructionHandle target = branchInstruction.getTarget();
        if (handlesForDeletion.contains(target)) {
            do {
                target = target.getNext();
            } while (handlesForDeletion.contains(target));
            branchInstruction.setTarget(target);
        }
        if (branchInstruction instanceof InstructionSelect) {
            InstructionSelect iSelect = (InstructionSelect) branchInstruction;
            InstructionHandle[] targets = iSelect.getTargets();
            for (int k = targets.length - 1; k >= 0; k--) {
                InstructionHandle oneTarget = targets[k];
                if (handlesForDeletion.contains(oneTarget)) {
                    do {
                        oneTarget = oneTarget.getNext();
                    } while (handlesForDeletion.contains(oneTarget));
                    iSelect.setTarget(k, oneTarget);
                    oneTarget.addTargeter(branchInstruction);
                }
            }
        }
    }

    private void handleRangeInstruction(InstructionHandle ih, LinkedList<ExceptionRange> exnList) {
        Range r = Range.getRange(ih);
        if (r instanceof ExceptionRange) {
            ExceptionRange er = (ExceptionRange) r;
            if (er.getStart() == ih && !er.isEmpty()) {
                insertHandler(er, exnList);
            }
        }
    }

    private Map<InstructionHandle, InstructionHandle> copyAllInstructionsExceptRangeInstructionsInto(InstructionList intoList) {
        Map<InstructionHandle, InstructionHandle> map = new HashMap<>();
        InstructionHandle start = getBody().getStart();
        while (true) {
            InstructionHandle ih = start;
            if (ih != null) {
                if (!Range.isRangeHandle(ih)) {
                    Instruction inst = ih.getInstruction();
                    Instruction copy = Utility.copyInstruction(inst);
                    if (copy instanceof InstructionBranch) {
                        map.put(ih, intoList.append((InstructionBranch) copy));
                    } else {
                        map.put(ih, intoList.append(copy));
                    }
                }
                start = ih.getNext();
            } else {
                return map;
            }
        }
    }

    private static InstructionHandle remap(InstructionHandle handle, Map<InstructionHandle, InstructionHandle> map) {
        while (true) {
            InstructionHandle ret = map.get(handle);
            if (ret == null) {
                handle = handle.getNext();
            } else {
                return ret;
            }
        }
    }

    static void insertHandler(ExceptionRange fresh, LinkedList<ExceptionRange> l) {
        ListIterator<ExceptionRange> iter = l.listIterator();
        while (iter.hasNext()) {
            ExceptionRange r = iter.next();
            if (fresh.getPriority() >= r.getPriority()) {
                iter.previous();
                iter.add(fresh);
                return;
            }
        }
        l.add(fresh);
    }

    public boolean isPrivate() {
        return Modifier.isPrivate(getAccessFlags());
    }

    public boolean isProtected() {
        return Modifier.isProtected(getAccessFlags());
    }

    public boolean isDefault() {
        return (isProtected() || isPrivate() || isPublic()) ? false : true;
    }

    public boolean isPublic() {
        return Modifier.isPublic(getAccessFlags());
    }

    public void assertGoodBody() {
    }

    public static void assertGoodBody(InstructionList il, String from) {
    }

    private static void assertTargetedBy(InstructionHandle target, InstructionTargeter targeter, String from) {
        Iterator tIter = target.getTargeters().iterator();
        while (tIter.hasNext()) {
            if (tIter.next() == targeter) {
                return;
            }
        }
        throw new RuntimeException("bad targeting relationship in " + from);
    }

    private static void assertTargets(InstructionTargeter targeter, InstructionHandle target, String from) {
        if (targeter instanceof Range) {
            Range r = (Range) targeter;
            if (r.getStart() == target || r.getEnd() == target) {
                return;
            }
            if ((r instanceof ExceptionRange) && ((ExceptionRange) r).getHandler() == target) {
                return;
            }
        } else if (targeter instanceof InstructionBranch) {
            InstructionBranch bi = (InstructionBranch) targeter;
            if (bi.getTarget() == target) {
                return;
            }
            if (targeter instanceof InstructionSelect) {
                InstructionSelect sel = (InstructionSelect) targeter;
                InstructionHandle[] itargets = sel.getTargets();
                for (int k = itargets.length - 1; k >= 0; k--) {
                    if (itargets[k] == target) {
                        return;
                    }
                }
            }
        } else if (targeter instanceof Tag) {
            return;
        }
        throw new BCException(targeter + " doesn't target " + target + " in " + from);
    }

    private static Range getRangeAndAssertExactlyOne(InstructionHandle ih, String from) {
        Range ret = null;
        Iterator<InstructionTargeter> tIter = ih.getTargeters().iterator();
        if (!tIter.hasNext()) {
            throw new BCException("range handle with no range in " + from);
        }
        while (tIter.hasNext()) {
            InstructionTargeter ts = tIter.next();
            if (ts instanceof Range) {
                if (ret != null) {
                    throw new BCException("range handle with multiple ranges in " + from);
                }
                ret = (Range) ts;
            }
        }
        if (ret == null) {
            throw new BCException("range handle with no range in " + from);
        }
        return ret;
    }

    boolean isAdviceMethod() {
        return (this.memberView == null || this.memberView.getAssociatedShadowMunger() == null) ? false : true;
    }

    boolean isAjSynthetic() {
        if (this.memberView == null) {
            return true;
        }
        return this.memberView.isAjSynthetic();
    }

    boolean isSynthetic() {
        if (this.memberView == null) {
            return false;
        }
        return this.memberView.isSynthetic();
    }

    public ISourceLocation getSourceLocation() {
        if (this.memberView != null) {
            return this.memberView.getSourceLocation();
        }
        return null;
    }

    public AjAttribute.EffectiveSignatureAttribute getEffectiveSignature() {
        if (this.effectiveSignature != null) {
            return this.effectiveSignature;
        }
        return this.memberView.getEffectiveSignature();
    }

    public void setEffectiveSignature(ResolvedMember member, Shadow.Kind kind, boolean shouldWeave) {
        this.effectiveSignature = new AjAttribute.EffectiveSignatureAttribute(member, kind, shouldWeave);
    }

    public String getSignature() {
        if (this.memberView != null) {
            return this.memberView.getSignature();
        }
        return MemberImpl.typesToSignature(BcelWorld.fromBcel(getReturnType()), BcelWorld.fromBcel(getArgumentTypes()), false);
    }

    public String getParameterSignature() {
        if (this.memberView != null) {
            return this.memberView.getParameterSignature();
        }
        return MemberImpl.typesToSignature(BcelWorld.fromBcel(getArgumentTypes()));
    }

    public BcelMethod getMemberView() {
        return this.memberView;
    }

    public void forcePublic() {
        markAsChanged();
        this.modifiers = Utility.makePublic(this.modifiers);
    }

    public boolean getCanInline() {
        return this.canInline;
    }

    public void setCanInline(boolean canInline) {
        this.canInline = canInline;
    }

    public void addAttribute(Attribute attribute) {
        this.attributes.add(attribute);
    }

    @Override // org.aspectj.weaver.tools.Traceable
    public String toTraceString() {
        return toShortString();
    }

    public ConstantPool getConstantPool() {
        return this.enclosingClass.getConstantPool();
    }

    public static boolean isConstructor(LazyMethodGen aMethod) {
        return aMethod.getName().equals("<init>");
    }
}
