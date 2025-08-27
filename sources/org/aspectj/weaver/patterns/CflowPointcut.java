package org.aspectj.weaver.patterns;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.aspectj.bridge.IMessage;
import org.aspectj.util.FileUtil;
import org.aspectj.util.FuzzyBoolean;
import org.aspectj.weaver.Advice;
import org.aspectj.weaver.CompressingDataOutputStream;
import org.aspectj.weaver.CrosscuttingMembers;
import org.aspectj.weaver.ISourceContext;
import org.aspectj.weaver.IntMap;
import org.aspectj.weaver.Member;
import org.aspectj.weaver.NameMangler;
import org.aspectj.weaver.ResolvedMember;
import org.aspectj.weaver.ResolvedMemberImpl;
import org.aspectj.weaver.ResolvedPointcutDefinition;
import org.aspectj.weaver.ResolvedType;
import org.aspectj.weaver.Shadow;
import org.aspectj.weaver.ShadowMunger;
import org.aspectj.weaver.UnresolvedType;
import org.aspectj.weaver.VersionedDataInputStream;
import org.aspectj.weaver.WeaverMessages;
import org.aspectj.weaver.World;
import org.aspectj.weaver.ast.Test;
import org.aspectj.weaver.patterns.ConcreteCflowPointcut;
import org.hyperic.sigar.NetFlags;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/patterns/CflowPointcut.class */
public class CflowPointcut extends Pointcut {
    private final Pointcut entry;
    boolean isBelow;
    private int[] freeVars;
    public static final ResolvedPointcutDefinition CFLOW_MARKER = new ResolvedPointcutDefinition(null, 0, null, UnresolvedType.NONE, Pointcut.makeMatchesNothing(Pointcut.RESOLVED));

    public CflowPointcut(Pointcut entry, boolean isBelow, int[] freeVars) {
        this.entry = entry;
        this.isBelow = isBelow;
        this.freeVars = freeVars;
        this.pointcutKind = (byte) 10;
    }

    public boolean isCflowBelow() {
        return this.isBelow;
    }

    @Override // org.aspectj.weaver.patterns.Pointcut
    public int couldMatchKinds() {
        return Shadow.ALL_SHADOW_KINDS_BITS;
    }

    public Pointcut getEntry() {
        return this.entry;
    }

    @Override // org.aspectj.weaver.patterns.Pointcut
    public FuzzyBoolean fastMatch(FastMatchInfo type) {
        return FuzzyBoolean.MAYBE;
    }

    @Override // org.aspectj.weaver.patterns.Pointcut
    protected FuzzyBoolean matchInternal(Shadow shadow) {
        return FuzzyBoolean.MAYBE;
    }

    @Override // org.aspectj.weaver.patterns.PatternNode
    public void write(CompressingDataOutputStream s) throws IOException {
        s.writeByte(10);
        this.entry.write(s);
        s.writeBoolean(this.isBelow);
        FileUtil.writeIntArray(this.freeVars, s);
        writeLocation(s);
    }

    public static Pointcut read(VersionedDataInputStream s, ISourceContext context) throws IOException {
        CflowPointcut ret = new CflowPointcut(Pointcut.read(s, context), s.readBoolean(), FileUtil.readIntArray(s));
        ret.readLocation(context, s);
        return ret;
    }

    @Override // org.aspectj.weaver.patterns.Pointcut
    public Pointcut parameterizeWith(Map<String, UnresolvedType> typeVariableMap, World w) {
        CflowPointcut ret = new CflowPointcut(this.entry.parameterizeWith(typeVariableMap, w), this.isBelow, this.freeVars);
        ret.copyLocationFrom(this);
        return ret;
    }

    @Override // org.aspectj.weaver.patterns.Pointcut
    public void resolveBindings(IScope scope, Bindings bindings) {
        if (bindings == null) {
            this.entry.resolveBindings(scope, null);
            this.entry.state = RESOLVED;
            this.freeVars = new int[0];
            return;
        }
        Bindings entryBindings = new Bindings(bindings.size());
        this.entry.resolveBindings(scope, entryBindings);
        this.entry.state = RESOLVED;
        this.freeVars = entryBindings.getUsedFormals();
        bindings.mergeIn(entryBindings, scope);
    }

    public boolean equals(Object other) {
        if (!(other instanceof CflowPointcut)) {
            return false;
        }
        CflowPointcut o = (CflowPointcut) other;
        return o.entry.equals(this.entry) && o.isBelow == this.isBelow;
    }

    public int hashCode() {
        int result = (37 * 17) + this.entry.hashCode();
        return (37 * result) + (this.isBelow ? 0 : 1);
    }

    public String toString() {
        return "cflow" + (this.isBelow ? "below" : "") + "(" + this.entry + ")";
    }

    @Override // org.aspectj.weaver.patterns.Pointcut
    protected Test findResidueInternal(Shadow shadow, ExposedState state) {
        throw new RuntimeException("unimplemented - did concretization fail?");
    }

    @Override // org.aspectj.weaver.patterns.Pointcut
    public Pointcut concretize1(ResolvedType inAspect, ResolvedType declaringType, IntMap bindings) {
        ResolvedMember localCflowField;
        ResolvedMember localCflowField2;
        ResolvedType formalType;
        if (isDeclare(bindings.getEnclosingAdvice())) {
            inAspect.getWorld().showMessage(IMessage.ERROR, WeaverMessages.format(WeaverMessages.CFLOW_IN_DECLARE, this.isBelow ? "below" : ""), bindings.getEnclosingAdvice().getSourceLocation(), null);
            return Pointcut.makeMatchesNothing(Pointcut.CONCRETE);
        }
        IntMap entryBindings = new IntMap();
        if (this.freeVars != null) {
            int len = this.freeVars.length;
            for (int i = 0; i < len; i++) {
                entryBindings.put(this.freeVars[i], i);
            }
        }
        entryBindings.copyContext(bindings);
        World world = inAspect.getWorld();
        ResolvedType concreteAspect = bindings.getConcreteAspect();
        CrosscuttingMembers xcut = concreteAspect.crosscuttingMembers;
        Collection<?> previousCflowEntries = xcut.getCflowEntries();
        entryBindings.pushEnclosingDefinition(CFLOW_MARKER);
        try {
            Pointcut concreteEntry = this.entry.concretize(inAspect, declaringType, entryBindings);
            entryBindings.popEnclosingDefinitition();
            List<ShadowMunger> innerCflowEntries = new ArrayList<>(xcut.getCflowEntries());
            innerCflowEntries.removeAll(previousCflowEntries);
            if (this.freeVars == null || this.freeVars.length == 0) {
                Object field = getCflowfield(xcut, concreteEntry, concreteAspect, "counter");
                if (field != null) {
                    localCflowField = (ResolvedMember) field;
                } else {
                    localCflowField = new ResolvedMemberImpl(Member.FIELD, concreteAspect, 25, NameMangler.cflowCounter(xcut), UnresolvedType.forName(NameMangler.CFLOW_COUNTER_TYPE).getSignature());
                    concreteAspect.crosscuttingMembers.addTypeMunger(world.getWeavingSupport().makeCflowCounterFieldAdder(localCflowField));
                    concreteAspect.crosscuttingMembers.addConcreteShadowMunger(Advice.makeCflowEntry(world, concreteEntry, this.isBelow, localCflowField, this.freeVars == null ? 0 : this.freeVars.length, innerCflowEntries, inAspect));
                    putCflowfield(xcut, concreteEntry, concreteAspect, localCflowField, "counter");
                }
                Pointcut ret = new ConcreteCflowPointcut(concreteAspect, localCflowField, null, true);
                ret.copyLocationFrom(this);
                return ret;
            }
            List<ConcreteCflowPointcut.Slot> slots = new ArrayList<>();
            int len2 = this.freeVars.length;
            for (int i2 = 0; i2 < len2; i2++) {
                int freeVar = this.freeVars[i2];
                if (bindings.hasKey(freeVar)) {
                    int formalIndex = bindings.get(freeVar);
                    ResolvedPointcutDefinition enclosingDef = bindings.peekEnclosingDefinition();
                    if (enclosingDef != null && enclosingDef.getParameterTypes().length > 0) {
                        formalType = enclosingDef.getParameterTypes()[freeVar].resolve(world);
                    } else {
                        formalType = bindings.getAdviceSignature().getParameterTypes()[formalIndex].resolve(world);
                    }
                    ConcreteCflowPointcut.Slot slot = new ConcreteCflowPointcut.Slot(formalIndex, formalType, i2);
                    slots.add(slot);
                }
            }
            Object field2 = getCflowfield(xcut, concreteEntry, concreteAspect, "stack");
            if (field2 != null) {
                localCflowField2 = (ResolvedMember) field2;
            } else {
                localCflowField2 = new ResolvedMemberImpl(Member.FIELD, concreteAspect, 25, NameMangler.cflowStack(xcut), UnresolvedType.forName(NameMangler.CFLOW_STACK_TYPE).getSignature());
                concreteAspect.crosscuttingMembers.addConcreteShadowMunger(Advice.makeCflowEntry(world, concreteEntry, this.isBelow, localCflowField2, this.freeVars.length, innerCflowEntries, inAspect));
                concreteAspect.crosscuttingMembers.addTypeMunger(world.getWeavingSupport().makeCflowStackFieldAdder(localCflowField2));
                putCflowfield(xcut, concreteEntry, concreteAspect, localCflowField2, "stack");
            }
            Pointcut ret2 = new ConcreteCflowPointcut(concreteAspect, localCflowField2, slots, false);
            ret2.copyLocationFrom(this);
            return ret2;
        } catch (Throwable th) {
            entryBindings.popEnclosingDefinitition();
            throw th;
        }
    }

    private String getKey(Pointcut p, ResolvedType a, String stackOrCounter) {
        StringBuffer sb = new StringBuffer();
        sb.append(a.getName());
        sb.append(NetFlags.ANY_ADDR_V6);
        sb.append(p.toString());
        sb.append(NetFlags.ANY_ADDR_V6);
        sb.append(stackOrCounter);
        return sb.toString();
    }

    private Object getCflowfield(CrosscuttingMembers xcut, Pointcut pcutkey, ResolvedType concreteAspect, String stackOrCounter) {
        Object o;
        String key = getKey(pcutkey, concreteAspect, stackOrCounter);
        if (this.isBelow) {
            o = xcut.getCflowBelowFields().get(key);
        } else {
            o = xcut.getCflowFields().get(key);
        }
        return o;
    }

    private void putCflowfield(CrosscuttingMembers xcut, Pointcut pcutkey, ResolvedType concreteAspect, Object o, String stackOrCounter) {
        String key = getKey(pcutkey, concreteAspect, stackOrCounter);
        if (this.isBelow) {
            xcut.getCflowBelowFields().put(key, o);
        } else {
            xcut.getCflowFields().put(key, o);
        }
    }

    @Override // org.aspectj.weaver.patterns.PatternNode
    public Object accept(PatternNodeVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }
}
