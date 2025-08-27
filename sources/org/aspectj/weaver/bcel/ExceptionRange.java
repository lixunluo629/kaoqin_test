package org.aspectj.weaver.bcel;

import org.aspectj.apache.bcel.generic.InstructionHandle;
import org.aspectj.apache.bcel.generic.InstructionList;
import org.aspectj.weaver.UnresolvedType;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/bcel/ExceptionRange.class */
public final class ExceptionRange extends Range {
    private InstructionHandle handler;
    private final UnresolvedType exceptionType;
    private final int priority;
    private volatile int hashCode;

    @Override // org.aspectj.weaver.bcel.Range, org.aspectj.apache.bcel.generic.InstructionTargeter
    public /* bridge */ /* synthetic */ boolean containsTarget(InstructionHandle instructionHandle) {
        return super.containsTarget(instructionHandle);
    }

    public ExceptionRange(InstructionList body, UnresolvedType exceptionType, int priority) {
        super(body);
        this.hashCode = 0;
        this.exceptionType = exceptionType;
        this.priority = priority;
    }

    public ExceptionRange(InstructionList body, UnresolvedType exceptionType, boolean insideExisting) {
        this(body, exceptionType, insideExisting ? Integer.MAX_VALUE : -1);
    }

    public void associateWithTargets(InstructionHandle start, InstructionHandle end, InstructionHandle handler) {
        this.start = start;
        this.end = end;
        this.handler = handler;
        start.addTargeter(this);
        end.addTargeter(this);
        handler.addTargeter(this);
    }

    public InstructionHandle getHandler() {
        return this.handler;
    }

    public UnresolvedType getCatchType() {
        return this.exceptionType;
    }

    public int getPriority() {
        return this.priority;
    }

    public String toString() {
        String str;
        if (this.exceptionType == null) {
            str = "finally";
        } else {
            str = "catch " + this.exceptionType;
        }
        return str;
    }

    public boolean equals(Object other) {
        if (!(other instanceof ExceptionRange)) {
            return false;
        }
        ExceptionRange o = (ExceptionRange) other;
        return o.getStart() == getStart() && o.getEnd() == getEnd() && o.handler == this.handler && (o.exceptionType != null ? o.exceptionType.equals(this.exceptionType) : this.exceptionType == null) && o.priority == this.priority;
    }

    public int hashCode() {
        if (this.hashCode == 0) {
            int ret = (37 * 17) + getStart().hashCode();
            this.hashCode = (37 * ((37 * ((37 * ((37 * ret) + getEnd().hashCode())) + this.handler.hashCode())) + (this.exceptionType == null ? 0 : this.exceptionType.hashCode()))) + this.priority;
        }
        return this.hashCode;
    }

    @Override // org.aspectj.weaver.bcel.Range
    public void updateTarget(InstructionHandle oldIh, InstructionHandle newIh, InstructionList newBody) {
        super.updateTarget(oldIh, newIh, newBody);
        if (oldIh == this.handler) {
            this.handler = newIh;
        }
    }

    public static boolean isExceptionStart(InstructionHandle ih) {
        if (!isRangeHandle(ih)) {
            return false;
        }
        Range r = getRange(ih);
        if (!(r instanceof ExceptionRange)) {
            return false;
        }
        ExceptionRange er = (ExceptionRange) r;
        return er.getStart() == ih;
    }

    public static boolean isExceptionEnd(InstructionHandle ih) {
        if (!isRangeHandle(ih)) {
            return false;
        }
        Range r = getRange(ih);
        if (!(r instanceof ExceptionRange)) {
            return false;
        }
        ExceptionRange er = (ExceptionRange) r;
        return er.getEnd() == ih;
    }
}
