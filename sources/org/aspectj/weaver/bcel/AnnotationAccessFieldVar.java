package org.aspectj.weaver.bcel;

import org.aspectj.apache.bcel.generic.InstructionFactory;
import org.aspectj.apache.bcel.generic.InstructionList;
import org.aspectj.weaver.ResolvedType;
import org.aspectj.weaver.Shadow;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/bcel/AnnotationAccessFieldVar.class */
class AnnotationAccessFieldVar extends BcelVar {
    private AnnotationAccessVar annoAccessor;
    private ResolvedType annoFieldOfInterest;
    private String name;
    private int elementValueType;

    public AnnotationAccessFieldVar(AnnotationAccessVar aav, ResolvedType annoFieldOfInterest, String name) {
        super(annoFieldOfInterest, 0);
        this.annoAccessor = aav;
        this.name = name;
        String sig = annoFieldOfInterest.getSignature();
        if (sig.length() == 1) {
            switch (sig.charAt(0)) {
                case 'I':
                    this.elementValueType = 73;
                    break;
                default:
                    throw new IllegalStateException(sig);
            }
        } else if (sig.equals("Ljava/lang/String;")) {
            this.elementValueType = 115;
        } else if (annoFieldOfInterest.isEnum()) {
            this.elementValueType = 101;
        } else {
            throw new IllegalStateException(sig);
        }
        this.annoFieldOfInterest = annoFieldOfInterest;
    }

    /* JADX WARN: Removed duplicated region for block: B:48:0x01d3  */
    @Override // org.aspectj.weaver.bcel.BcelVar
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void appendLoadAndConvert(org.aspectj.apache.bcel.generic.InstructionList r7, org.aspectj.apache.bcel.generic.InstructionFactory r8, org.aspectj.weaver.ResolvedType r9) throws java.lang.NumberFormatException {
        /*
            Method dump skipped, instructions count: 746
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: org.aspectj.weaver.bcel.AnnotationAccessFieldVar.appendLoadAndConvert(org.aspectj.apache.bcel.generic.InstructionList, org.aspectj.apache.bcel.generic.InstructionFactory, org.aspectj.weaver.ResolvedType):void");
    }

    @Override // org.aspectj.weaver.bcel.BcelVar
    public void insertLoad(InstructionList il, InstructionFactory fact) throws NumberFormatException {
        if (this.annoAccessor.getKind() != Shadow.MethodExecution) {
            return;
        }
        appendLoadAndConvert(il, fact, this.annoFieldOfInterest);
    }

    @Override // org.aspectj.weaver.bcel.BcelVar, org.aspectj.weaver.ast.Var
    public String toString() {
        return super.toString();
    }
}
