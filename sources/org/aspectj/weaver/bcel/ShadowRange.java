package org.aspectj.weaver.bcel;

import org.apache.ibatis.ognl.OgnlContext;
import org.aspectj.apache.bcel.generic.Instruction;
import org.aspectj.apache.bcel.generic.InstructionBranch;
import org.aspectj.apache.bcel.generic.InstructionFactory;
import org.aspectj.apache.bcel.generic.InstructionHandle;
import org.aspectj.apache.bcel.generic.InstructionLV;
import org.aspectj.apache.bcel.generic.InstructionList;
import org.aspectj.apache.bcel.generic.InstructionSelect;
import org.aspectj.apache.bcel.generic.InstructionTargeter;
import org.aspectj.apache.bcel.generic.LocalVariableTag;
import org.aspectj.apache.bcel.generic.RET;
import org.aspectj.apache.bcel.generic.TargetLostException;
import org.aspectj.weaver.BCException;
import org.aspectj.weaver.IntMap;
import org.aspectj.weaver.Shadow;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/bcel/ShadowRange.class */
final class ShadowRange extends Range {
    private BcelShadow shadow;

    public ShadowRange(InstructionList body) {
        super(body);
    }

    protected void associateWithTargets(InstructionHandle start, InstructionHandle end) {
        this.start = start;
        this.end = end;
        start.addTargeter(this);
        end.addTargeter(this);
    }

    public void associateWithShadow(BcelShadow shadow) {
        this.shadow = shadow;
        shadow.setRange(this);
    }

    public Shadow.Kind getKind() {
        return this.shadow.getKind();
    }

    public String toString() {
        return this.shadow.toString();
    }

    void extractInstructionsInto(LazyMethodGen freshMethod, IntMap remap, boolean addReturn) {
        InstructionHandle freshIh;
        int freshIndex;
        LazyMethodGen.assertGoodBody(getBody(), toString());
        freshMethod.assertGoodBody();
        InstructionList freshBody = freshMethod.getBody();
        InstructionHandle next = this.start.getNext();
        while (true) {
            InstructionHandle oldIh = next;
            if (oldIh == this.end) {
                break;
            }
            Instruction oldI = oldIh.getInstruction();
            Instruction freshI = oldI == RANGEINSTRUCTION ? oldI : Utility.copyInstruction(oldI);
            if (freshI instanceof InstructionBranch) {
                InstructionBranch oldBranch = (InstructionBranch) oldI;
                InstructionBranch freshBranch = (InstructionBranch) freshI;
                InstructionHandle oldTarget = oldBranch.getTarget();
                oldTarget.removeTargeter(oldBranch);
                oldTarget.addTargeter(freshBranch);
                if (freshBranch instanceof InstructionSelect) {
                    InstructionSelect oldSelect = (InstructionSelect) oldI;
                    InstructionSelect freshSelect = (InstructionSelect) freshI;
                    InstructionHandle[] oldTargets = freshSelect.getTargets();
                    for (int k = oldTargets.length - 1; k >= 0; k--) {
                        oldTargets[k].removeTargeter(oldSelect);
                        oldTargets[k].addTargeter(freshSelect);
                    }
                }
                freshIh = freshBody.append(freshBranch);
            } else {
                freshIh = freshBody.append(freshI);
            }
            for (InstructionTargeter source : oldIh.getTargetersCopy()) {
                if (source instanceof LocalVariableTag) {
                    Shadow.Kind kind = getKind();
                    if (kind == Shadow.AdviceExecution || kind == Shadow.ConstructorExecution || kind == Shadow.MethodExecution || kind == Shadow.PreInitialization || kind == Shadow.Initialization || kind == Shadow.StaticInitialization) {
                        LocalVariableTag sourceLocalVariableTag = (LocalVariableTag) source;
                        if (sourceLocalVariableTag.getSlot() == 0 && sourceLocalVariableTag.getName().equals(OgnlContext.THIS_CONTEXT_KEY)) {
                            sourceLocalVariableTag.setName("ajc$this");
                        }
                        source.updateTarget(oldIh, freshIh);
                    } else {
                        source.updateTarget(oldIh, null);
                    }
                } else if (source instanceof Range) {
                    ((Range) source).updateTarget(oldIh, freshIh, freshBody);
                } else {
                    source.updateTarget(oldIh, freshIh);
                }
            }
            if (freshI.isLocalVariableInstruction() || (freshI instanceof RET)) {
                int oldIndex = freshI.getIndex();
                if (!remap.hasKey(oldIndex)) {
                    freshIndex = freshMethod.allocateLocal(2);
                    remap.put(oldIndex, freshIndex);
                } else {
                    freshIndex = remap.get(oldIndex);
                }
                if (freshI instanceof RET) {
                    freshI.setIndex(freshIndex);
                } else {
                    freshIh.setInstruction(((InstructionLV) freshI).setIndexAndCopyIfNecessary(freshIndex));
                }
            }
            next = oldIh.getNext();
        }
        InstructionHandle start = freshBody.getStart();
        while (true) {
            InstructionHandle newIh = start;
            if (newIh != freshBody.getEnd()) {
                for (InstructionTargeter source2 : newIh.getTargeters()) {
                    if (source2 instanceof LocalVariableTag) {
                        LocalVariableTag lvt = (LocalVariableTag) source2;
                        if (!lvt.isRemapped() && remap.hasKey(lvt.getSlot())) {
                            lvt.updateSlot(remap.get(lvt.getSlot()));
                        }
                    }
                }
                start = newIh.getNext();
            } else {
                try {
                    break;
                } catch (TargetLostException e) {
                    throw new BCException("shouldn't have gotten a target lost");
                }
            }
        }
        InstructionHandle oldIh2 = this.start.getNext();
        while (oldIh2 != this.end) {
            InstructionHandle next2 = oldIh2.getNext();
            this.body.delete(oldIh2);
            oldIh2 = next2;
        }
        InstructionHandle ret = null;
        if (addReturn) {
            ret = freshBody.append(InstructionFactory.createReturn(freshMethod.getReturnType()));
        }
        for (InstructionTargeter t : this.end.getTargetersCopy()) {
            if (t != this) {
                if (!addReturn) {
                    throw new BCException("range has target, but we aren't adding a return");
                }
                t.updateTarget(this.end, ret);
            }
        }
        LazyMethodGen.assertGoodBody(getBody(), toString());
        freshMethod.assertGoodBody();
    }

    public BcelShadow getShadow() {
        return this.shadow;
    }
}
