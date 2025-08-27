package org.aspectj.weaver.patterns;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.aspectj.bridge.IMessage;
import org.aspectj.bridge.MessageUtil;
import org.aspectj.util.FuzzyBoolean;
import org.aspectj.weaver.Advice;
import org.aspectj.weaver.AjcMemberMaker;
import org.aspectj.weaver.BCException;
import org.aspectj.weaver.CompressingDataOutputStream;
import org.aspectj.weaver.ISourceContext;
import org.aspectj.weaver.IntMap;
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
import org.aspectj.weaver.ast.Expr;
import org.aspectj.weaver.ast.Literal;
import org.aspectj.weaver.ast.Test;
import org.aspectj.weaver.ast.Var;
import org.aspectj.weaver.patterns.Pointcut;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/patterns/IfPointcut.class */
public class IfPointcut extends Pointcut {
    public ResolvedMember testMethod;
    public int extraParameterFlags;
    private final String enclosingPointcutHint;
    public Pointcut residueSource;
    int baseArgsCount;
    private int ifLastMatchedShadowId;
    private Test ifLastMatchedShadowResidue;
    private boolean findingResidue = false;
    private IfPointcut partiallyConcretized = null;

    public IfPointcut(ResolvedMember testMethod, int extraParameterFlags) {
        this.testMethod = testMethod;
        this.extraParameterFlags = extraParameterFlags;
        this.pointcutKind = (byte) 9;
        this.enclosingPointcutHint = null;
    }

    public IfPointcut(String enclosingPointcutHint) {
        this.pointcutKind = (byte) 9;
        this.enclosingPointcutHint = enclosingPointcutHint;
        this.testMethod = null;
        this.extraParameterFlags = -1;
    }

    @Override // org.aspectj.weaver.patterns.Pointcut
    public int couldMatchKinds() {
        return Shadow.ALL_SHADOW_KINDS_BITS;
    }

    @Override // org.aspectj.weaver.patterns.Pointcut
    public FuzzyBoolean fastMatch(FastMatchInfo type) {
        return FuzzyBoolean.MAYBE;
    }

    @Override // org.aspectj.weaver.patterns.Pointcut
    protected FuzzyBoolean matchInternal(Shadow shadow) {
        if ((this.extraParameterFlags & 16) != 0) {
            if ((this.extraParameterFlags & 32) != 0) {
                return FuzzyBoolean.YES;
            }
            return FuzzyBoolean.NO;
        }
        return FuzzyBoolean.MAYBE;
    }

    public boolean alwaysFalse() {
        return false;
    }

    public boolean alwaysTrue() {
        return false;
    }

    public Pointcut getResidueSource() {
        return this.residueSource;
    }

    @Override // org.aspectj.weaver.patterns.PatternNode
    public void write(CompressingDataOutputStream s) throws IOException {
        s.writeByte(9);
        s.writeBoolean(this.testMethod != null);
        if (this.testMethod != null) {
            this.testMethod.write(s);
        }
        s.writeByte(this.extraParameterFlags);
        writeLocation(s);
    }

    public static Pointcut read(VersionedDataInputStream s, ISourceContext context) throws IOException {
        boolean hasTestMethod = s.readBoolean();
        ResolvedMember resolvedTestMethod = null;
        if (hasTestMethod) {
            resolvedTestMethod = ResolvedMemberImpl.readResolvedMember(s, context);
        }
        IfPointcut ret = new IfPointcut(resolvedTestMethod, s.readByte());
        ret.readLocation(context, s);
        return ret;
    }

    @Override // org.aspectj.weaver.patterns.Pointcut
    public void resolveBindings(IScope scope, Bindings bindings) {
    }

    public boolean equals(Object other) {
        if (!(other instanceof IfPointcut)) {
            return false;
        }
        IfPointcut o = (IfPointcut) other;
        if (o.testMethod == null) {
            return this.testMethod == null;
        }
        return o.testMethod.equals(this.testMethod);
    }

    public int hashCode() {
        int result = (37 * 17) + this.testMethod.hashCode();
        return result;
    }

    public String toString() {
        if (this.extraParameterFlags < 0) {
            return "if()";
        }
        return "if(" + this.testMethod + ")";
    }

    @Override // org.aspectj.weaver.patterns.Pointcut
    protected Test findResidueInternal(Shadow shadow, ExposedState state) {
        if (this.findingResidue) {
            return Literal.TRUE;
        }
        this.findingResidue = true;
        try {
            if (shadow.shadowId == this.ifLastMatchedShadowId) {
                Test test = this.ifLastMatchedShadowResidue;
                this.findingResidue = false;
                return test;
            }
            Test ret = Literal.TRUE;
            List<Var> args = new ArrayList<>();
            if (this.extraParameterFlags < 0) {
                int currentStateIndex = 0;
                for (int i = 0; i < this.testMethod.getParameterTypes().length; i++) {
                    String argSignature = this.testMethod.getParameterTypes()[i].getSignature();
                    if (AjcMemberMaker.TYPEX_JOINPOINT.getSignature().equals(argSignature)) {
                        args.add(shadow.getThisJoinPointVar());
                    } else if (AjcMemberMaker.TYPEX_PROCEEDINGJOINPOINT.getSignature().equals(argSignature)) {
                        args.add(shadow.getThisJoinPointVar());
                    } else if (AjcMemberMaker.TYPEX_STATICJOINPOINT.getSignature().equals(argSignature)) {
                        args.add(shadow.getThisJoinPointStaticPartVar());
                    } else if (AjcMemberMaker.TYPEX_ENCLOSINGSTATICJOINPOINT.getSignature().equals(argSignature)) {
                        args.add(shadow.getThisEnclosingJoinPointStaticPartVar());
                    } else {
                        if (state.size() == 0 || currentStateIndex > state.size()) {
                            String[] paramNames = this.testMethod.getParameterNames();
                            StringBuffer errorParameter = new StringBuffer();
                            if (paramNames != null) {
                                errorParameter.append(this.testMethod.getParameterTypes()[i].getName()).append(SymbolConstants.SPACE_SYMBOL);
                                errorParameter.append(paramNames[i]);
                                shadow.getIWorld().getMessageHandler().handleMessage(MessageUtil.error("Missing binding for if() pointcut method.  Parameter " + (i + 1) + "(" + errorParameter.toString() + ") must be bound - even in reference pointcuts  (compiler limitation)", this.testMethod.getSourceLocation()));
                            } else {
                                shadow.getIWorld().getMessageHandler().handleMessage(MessageUtil.error("Missing binding for if() pointcut method.  Parameter " + (i + 1) + " must be bound - even in reference pointcuts (compiler limitation)", this.testMethod.getSourceLocation()));
                            }
                            Literal literal = Literal.TRUE;
                            this.findingResidue = false;
                            return literal;
                        }
                        int i2 = currentStateIndex;
                        currentStateIndex++;
                        Var v = state.get(i2);
                        while (v == null && currentStateIndex < state.size()) {
                            int i3 = currentStateIndex;
                            currentStateIndex++;
                            v = state.get(i3);
                        }
                        args.add(v);
                        ret = Test.makeAnd(ret, Test.makeInstanceof(v, this.testMethod.getParameterTypes()[i].resolve(shadow.getIWorld())));
                    }
                }
            } else {
                if ((this.extraParameterFlags & 16) != 0) {
                    if ((this.extraParameterFlags & 32) != 0) {
                        Test ret2 = Literal.TRUE;
                        this.ifLastMatchedShadowId = shadow.shadowId;
                        this.ifLastMatchedShadowResidue = ret2;
                        this.findingResidue = false;
                        return ret2;
                    }
                    Test ret3 = Literal.FALSE;
                    this.ifLastMatchedShadowId = shadow.shadowId;
                    this.ifLastMatchedShadowResidue = ret3;
                    this.findingResidue = false;
                    return ret3;
                }
                if (this.baseArgsCount > 0) {
                    ExposedState myState = new ExposedState(this.baseArgsCount);
                    myState.setConcreteAspect(state.getConcreteAspect());
                    this.residueSource.findResidue(shadow, myState);
                    UnresolvedType[] pTypes = this.testMethod == null ? null : this.testMethod.getParameterTypes();
                    if (pTypes != null && this.baseArgsCount > pTypes.length) {
                        throw new BCException("Unexpected problem with testMethod " + this.testMethod + ": expecting " + this.baseArgsCount + " arguments");
                    }
                    for (int i4 = 0; i4 < this.baseArgsCount; i4++) {
                        Var v2 = myState.get(i4);
                        if (v2 != null) {
                            args.add(v2);
                            ret = Test.makeAnd(ret, Test.makeInstanceof(v2, pTypes[i4].resolve(shadow.getIWorld())));
                        }
                    }
                }
                if ((this.extraParameterFlags & 2) != 0) {
                    args.add(shadow.getThisJoinPointVar());
                }
                if ((this.extraParameterFlags & 4) != 0) {
                    args.add(shadow.getThisJoinPointStaticPartVar());
                }
                if ((this.extraParameterFlags & 8) != 0) {
                    args.add(shadow.getThisEnclosingJoinPointStaticPartVar());
                }
                if ((this.extraParameterFlags & 64) != 0) {
                    args.add(shadow.getThisAspectInstanceVar(state.getConcreteAspect()));
                }
            }
            Test ret4 = Test.makeAnd(ret, Test.makeCall(this.testMethod, (Expr[]) args.toArray(new Expr[args.size()])));
            this.ifLastMatchedShadowId = shadow.shadowId;
            this.ifLastMatchedShadowResidue = ret4;
            this.findingResidue = false;
            return ret4;
        } catch (Throwable th) {
            this.findingResidue = false;
            throw th;
        }
    }

    @Override // org.aspectj.weaver.patterns.Pointcut
    protected boolean shouldCopyLocationForConcretize() {
        return false;
    }

    @Override // org.aspectj.weaver.patterns.Pointcut
    public Pointcut concretize1(ResolvedType inAspect, ResolvedType declaringType, IntMap bindings) {
        IfPointcut ret;
        if (isDeclare(bindings.getEnclosingAdvice())) {
            inAspect.getWorld().showMessage(IMessage.ERROR, WeaverMessages.format(WeaverMessages.IF_IN_DECLARE), bindings.getEnclosingAdvice().getSourceLocation(), null);
            return Pointcut.makeMatchesNothing(Pointcut.CONCRETE);
        }
        if (this.partiallyConcretized != null) {
            return this.partiallyConcretized;
        }
        if (this.extraParameterFlags < 0 && this.testMethod == null) {
            ResolvedPointcutDefinition def = bindings.peekEnclosingDefinition();
            if (def != null) {
                ResolvedType aspect = inAspect.getWorld().resolve(def.getDeclaringType());
                Iterator memberIter = aspect.getMethods(true, true);
                while (true) {
                    if (!memberIter.hasNext()) {
                        break;
                    }
                    ResolvedMember method = memberIter.next();
                    if (def.getName().equals(method.getName()) && def.getParameterTypes().length == method.getParameterTypes().length) {
                        boolean sameSig = true;
                        int j = 0;
                        while (true) {
                            if (j >= method.getParameterTypes().length) {
                                break;
                            }
                            UnresolvedType argJ = method.getParameterTypes()[j];
                            if (argJ.equals(def.getParameterTypes()[j])) {
                                j++;
                            } else {
                                sameSig = false;
                                break;
                            }
                        }
                        if (sameSig) {
                            this.testMethod = method;
                            break;
                        }
                    }
                }
                if (this.testMethod == null) {
                    inAspect.getWorld().showMessage(IMessage.ERROR, "Cannot find if() body from '" + def.toString() + "' for '" + this.enclosingPointcutHint + "'", getSourceLocation(), null);
                    return Pointcut.makeMatchesNothing(Pointcut.CONCRETE);
                }
            } else {
                this.testMethod = inAspect.getWorld().resolve(bindings.getAdviceSignature());
            }
            ret = new IfPointcut(this.enclosingPointcutHint);
            ret.testMethod = this.testMethod;
        } else {
            ret = new IfPointcut(this.testMethod, this.extraParameterFlags);
        }
        ret.copyLocationFrom(this);
        this.partiallyConcretized = ret;
        if (bindings.directlyInAdvice() && bindings.getEnclosingAdvice() == null) {
            inAspect.getWorld().showMessage(IMessage.ERROR, WeaverMessages.format(WeaverMessages.IF_IN_PERCLAUSE), getSourceLocation(), null);
            return Pointcut.makeMatchesNothing(Pointcut.CONCRETE);
        }
        if (bindings.directlyInAdvice()) {
            ShadowMunger advice = bindings.getEnclosingAdvice();
            if (advice instanceof Advice) {
                ret.baseArgsCount = ((Advice) advice).getBaseParameterCount();
            } else {
                ret.baseArgsCount = 0;
            }
            ret.residueSource = advice.getPointcut().concretize(inAspect, inAspect, ret.baseArgsCount, advice);
        } else {
            ResolvedPointcutDefinition def2 = bindings.peekEnclosingDefinition();
            if (def2 == CflowPointcut.CFLOW_MARKER) {
                inAspect.getWorld().showMessage(IMessage.ERROR, WeaverMessages.format(WeaverMessages.IF_LEXICALLY_IN_CFLOW), getSourceLocation(), null);
                return Pointcut.makeMatchesNothing(Pointcut.CONCRETE);
            }
            ret.baseArgsCount = def2.getParameterTypes().length;
            if (ret.extraParameterFlags < 0) {
                ret.baseArgsCount = 0;
                for (int i = 0; i < this.testMethod.getParameterTypes().length; i++) {
                    String argSignature = this.testMethod.getParameterTypes()[i].getSignature();
                    if (!AjcMemberMaker.TYPEX_JOINPOINT.getSignature().equals(argSignature) && !AjcMemberMaker.TYPEX_PROCEEDINGJOINPOINT.getSignature().equals(argSignature) && !AjcMemberMaker.TYPEX_STATICJOINPOINT.getSignature().equals(argSignature) && !AjcMemberMaker.TYPEX_ENCLOSINGSTATICJOINPOINT.getSignature().equals(argSignature)) {
                        ret.baseArgsCount++;
                    }
                }
            }
            IntMap newBindings = IntMap.idMap(ret.baseArgsCount);
            newBindings.copyContext(bindings);
            ret.residueSource = def2.getPointcut().concretize(inAspect, declaringType, newBindings);
        }
        return ret;
    }

    @Override // org.aspectj.weaver.patterns.Pointcut
    public Pointcut parameterizeWith(Map typeVariableMap, World w) {
        return this;
    }

    public static IfPointcut makeIfFalsePointcut(Pointcut.State state) {
        IfPointcut ret = new IfFalsePointcut();
        ret.state = state;
        return ret;
    }

    @Override // org.aspectj.weaver.patterns.PatternNode
    public Object accept(PatternNodeVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }

    /* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/patterns/IfPointcut$IfFalsePointcut.class */
    public static class IfFalsePointcut extends IfPointcut {
        public IfFalsePointcut() {
            super(null, 0);
            this.pointcutKind = (byte) 15;
        }

        @Override // org.aspectj.weaver.patterns.IfPointcut, org.aspectj.weaver.patterns.Pointcut
        public int couldMatchKinds() {
            return Shadow.NO_SHADOW_KINDS_BITS;
        }

        @Override // org.aspectj.weaver.patterns.IfPointcut
        public boolean alwaysFalse() {
            return true;
        }

        @Override // org.aspectj.weaver.patterns.IfPointcut, org.aspectj.weaver.patterns.Pointcut
        protected Test findResidueInternal(Shadow shadow, ExposedState state) {
            return Literal.FALSE;
        }

        @Override // org.aspectj.weaver.patterns.IfPointcut, org.aspectj.weaver.patterns.Pointcut
        public FuzzyBoolean fastMatch(FastMatchInfo type) {
            return FuzzyBoolean.NO;
        }

        @Override // org.aspectj.weaver.patterns.IfPointcut, org.aspectj.weaver.patterns.Pointcut
        protected FuzzyBoolean matchInternal(Shadow shadow) {
            return FuzzyBoolean.NO;
        }

        @Override // org.aspectj.weaver.patterns.IfPointcut, org.aspectj.weaver.patterns.Pointcut
        public void resolveBindings(IScope scope, Bindings bindings) {
        }

        @Override // org.aspectj.weaver.patterns.Pointcut
        public void postRead(ResolvedType enclosingType) {
        }

        @Override // org.aspectj.weaver.patterns.IfPointcut, org.aspectj.weaver.patterns.Pointcut
        public Pointcut concretize1(ResolvedType inAspect, ResolvedType declaringType, IntMap bindings) {
            if (isDeclare(bindings.getEnclosingAdvice())) {
                inAspect.getWorld().showMessage(IMessage.ERROR, WeaverMessages.format(WeaverMessages.IF_IN_DECLARE), bindings.getEnclosingAdvice().getSourceLocation(), null);
                return Pointcut.makeMatchesNothing(Pointcut.CONCRETE);
            }
            return makeIfFalsePointcut(this.state);
        }

        @Override // org.aspectj.weaver.patterns.IfPointcut, org.aspectj.weaver.patterns.PatternNode
        public void write(CompressingDataOutputStream s) throws IOException {
            s.writeByte(15);
        }

        @Override // org.aspectj.weaver.patterns.IfPointcut
        public int hashCode() {
            return 17;
        }

        @Override // org.aspectj.weaver.patterns.IfPointcut
        public String toString() {
            return "if(false)";
        }
    }

    public static IfPointcut makeIfTruePointcut(Pointcut.State state) {
        IfPointcut ret = new IfTruePointcut();
        ret.state = state;
        return ret;
    }

    /* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/patterns/IfPointcut$IfTruePointcut.class */
    public static class IfTruePointcut extends IfPointcut {
        public IfTruePointcut() {
            super(null, 0);
            this.pointcutKind = (byte) 14;
        }

        @Override // org.aspectj.weaver.patterns.IfPointcut
        public boolean alwaysTrue() {
            return true;
        }

        @Override // org.aspectj.weaver.patterns.IfPointcut, org.aspectj.weaver.patterns.Pointcut
        protected Test findResidueInternal(Shadow shadow, ExposedState state) {
            return Literal.TRUE;
        }

        @Override // org.aspectj.weaver.patterns.IfPointcut, org.aspectj.weaver.patterns.Pointcut
        public FuzzyBoolean fastMatch(FastMatchInfo type) {
            return FuzzyBoolean.YES;
        }

        @Override // org.aspectj.weaver.patterns.IfPointcut, org.aspectj.weaver.patterns.Pointcut
        protected FuzzyBoolean matchInternal(Shadow shadow) {
            return FuzzyBoolean.YES;
        }

        @Override // org.aspectj.weaver.patterns.IfPointcut, org.aspectj.weaver.patterns.Pointcut
        public void resolveBindings(IScope scope, Bindings bindings) {
        }

        @Override // org.aspectj.weaver.patterns.Pointcut
        public void postRead(ResolvedType enclosingType) {
        }

        @Override // org.aspectj.weaver.patterns.IfPointcut, org.aspectj.weaver.patterns.Pointcut
        public Pointcut concretize1(ResolvedType inAspect, ResolvedType declaringType, IntMap bindings) {
            if (isDeclare(bindings.getEnclosingAdvice())) {
                inAspect.getWorld().showMessage(IMessage.ERROR, WeaverMessages.format(WeaverMessages.IF_IN_DECLARE), bindings.getEnclosingAdvice().getSourceLocation(), null);
                return Pointcut.makeMatchesNothing(Pointcut.CONCRETE);
            }
            return makeIfTruePointcut(this.state);
        }

        @Override // org.aspectj.weaver.patterns.IfPointcut, org.aspectj.weaver.patterns.PatternNode
        public void write(CompressingDataOutputStream s) throws IOException {
            s.writeByte(14);
        }

        @Override // org.aspectj.weaver.patterns.IfPointcut
        public int hashCode() {
            return 37;
        }

        @Override // org.aspectj.weaver.patterns.IfPointcut
        public String toString() {
            return "if(true)";
        }
    }

    public void setAlways(boolean matches) {
        this.extraParameterFlags |= 16;
        if (matches) {
            this.extraParameterFlags |= 32;
        }
    }
}
