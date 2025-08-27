package org.aspectj.weaver.patterns;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import org.aspectj.bridge.AbortException;
import org.aspectj.bridge.IMessage;
import org.aspectj.bridge.MessageUtil;
import org.aspectj.util.FuzzyBoolean;
import org.aspectj.weaver.CompressingDataOutputStream;
import org.aspectj.weaver.ISourceContext;
import org.aspectj.weaver.IntMap;
import org.aspectj.weaver.ResolvedPointcutDefinition;
import org.aspectj.weaver.ResolvedType;
import org.aspectj.weaver.Shadow;
import org.aspectj.weaver.ShadowMunger;
import org.aspectj.weaver.TypeVariable;
import org.aspectj.weaver.TypeVariableReference;
import org.aspectj.weaver.UnresolvedType;
import org.aspectj.weaver.VersionedDataInputStream;
import org.aspectj.weaver.WeaverMessages;
import org.aspectj.weaver.World;
import org.aspectj.weaver.ast.Test;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/patterns/ReferencePointcut.class */
public class ReferencePointcut extends Pointcut {
    public UnresolvedType onType;
    public TypePattern onTypeSymbolic;
    public String name;
    public TypePatternList arguments;
    private Map<String, UnresolvedType> typeVariableMap;
    private boolean concretizing = false;

    public ReferencePointcut(TypePattern onTypeSymbolic, String name, TypePatternList arguments) {
        this.onTypeSymbolic = onTypeSymbolic;
        this.name = name;
        this.arguments = arguments;
        this.pointcutKind = (byte) 8;
    }

    public ReferencePointcut(UnresolvedType onType, String name, TypePatternList arguments) {
        this.onType = onType;
        this.name = name;
        this.arguments = arguments;
        this.pointcutKind = (byte) 8;
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
        return FuzzyBoolean.NO;
    }

    public String toString() {
        StringBuffer buf = new StringBuffer();
        if (this.onType != null) {
            buf.append(this.onType);
            buf.append(".");
        }
        buf.append(this.name);
        buf.append(this.arguments.toString());
        return buf.toString();
    }

    @Override // org.aspectj.weaver.patterns.PatternNode
    public void write(CompressingDataOutputStream s) throws IOException {
        s.writeByte(8);
        if (this.onType != null) {
            s.writeBoolean(true);
            this.onType.write(s);
        } else {
            s.writeBoolean(false);
        }
        s.writeUTF(this.name);
        this.arguments.write(s);
        writeLocation(s);
    }

    public static Pointcut read(VersionedDataInputStream s, ISourceContext context) throws IOException {
        UnresolvedType onType = null;
        if (s.readBoolean()) {
            onType = UnresolvedType.read(s);
        }
        ReferencePointcut ret = new ReferencePointcut(onType, s.readUTF(), TypePatternList.read(s, context));
        ret.readLocation(context, s);
        return ret;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // org.aspectj.weaver.patterns.Pointcut
    public void resolveBindings(IScope scope, Bindings bindings) {
        ResolvedType searchType;
        boolean reportProblem;
        if (this.onTypeSymbolic != null) {
            this.onType = this.onTypeSymbolic.resolveExactType(scope, bindings);
            if (ResolvedType.isMissing(this.onType)) {
                return;
            }
        }
        if (this.onType != null) {
            searchType = scope.getWorld().resolve(this.onType);
        } else {
            searchType = scope.getEnclosingType();
        }
        if (searchType.isTypeVariableReference()) {
            searchType = ((TypeVariableReference) searchType).getTypeVariable().getFirstBound().resolve(scope.getWorld());
        }
        this.arguments.resolveBindings(scope, bindings, true, true);
        ResolvedPointcutDefinition pointcutDef = searchType.findPointcut(this.name);
        if (pointcutDef == null && this.onType == null) {
            while (true) {
                UnresolvedType declaringType = searchType.getDeclaringType();
                if (declaringType == null) {
                    break;
                }
                searchType = declaringType.resolve(scope.getWorld());
                pointcutDef = searchType.findPointcut(this.name);
                if (pointcutDef != null) {
                    this.onType = searchType;
                    break;
                }
            }
        }
        if (pointcutDef == null) {
            scope.message(IMessage.ERROR, this, "can't find referenced pointcut " + this.name);
            return;
        }
        if (!pointcutDef.isVisible(scope.getEnclosingType())) {
            scope.message(IMessage.ERROR, this, "pointcut declaration " + pointcutDef + " is not accessible");
            return;
        }
        if (Modifier.isAbstract(pointcutDef.getModifiers())) {
            if (this.onType != null && !this.onType.isTypeVariableReference()) {
                scope.message(IMessage.ERROR, this, "can't make static reference to abstract pointcut");
                return;
            } else if (!searchType.isAbstract()) {
                scope.message(IMessage.ERROR, this, "can't use abstract pointcut in concrete context");
                return;
            }
        }
        ResolvedType[] resolvedTypeArrResolve = scope.getWorld().resolve(pointcutDef.getParameterTypes());
        if (resolvedTypeArrResolve.length != this.arguments.size()) {
            scope.message(IMessage.ERROR, this, "incompatible number of arguments to pointcut, expected " + resolvedTypeArrResolve.length + " found " + this.arguments.size());
            return;
        }
        if (this.onType != null) {
            if (this.onType.isParameterizedType()) {
                this.typeVariableMap = new HashMap();
                ResolvedType underlyingGenericType = ((ResolvedType) this.onType).getGenericType();
                TypeVariable[] tVars = underlyingGenericType.getTypeVariables();
                ResolvedType[] typeParams = ((ResolvedType) this.onType).getResolvedTypeParameters();
                for (int i = 0; i < tVars.length; i++) {
                    this.typeVariableMap.put(tVars[i].getName(), typeParams[i]);
                }
            } else if (this.onType.isGenericType()) {
                scope.message(MessageUtil.error(WeaverMessages.format(WeaverMessages.CANT_REFERENCE_POINTCUT_IN_RAW_TYPE), getSourceLocation()));
            }
        }
        int len = this.arguments.size();
        for (int i2 = 0; i2 < len; i2++) {
            TypePattern p = this.arguments.get(i2);
            if (this.typeVariableMap != null) {
                p = p.parameterizeWith(this.typeVariableMap, scope.getWorld());
            }
            if (p == TypePattern.NO) {
                scope.message(IMessage.ERROR, this, "bad parameter to pointcut reference");
                return;
            }
            if (resolvedTypeArrResolve[i2].isTypeVariableReference() && p.getExactType().isTypeVariableReference()) {
                UnresolvedType One = ((TypeVariableReference) resolvedTypeArrResolve[i2]).getTypeVariable().getFirstBound();
                UnresolvedType Two = ((TypeVariableReference) p.getExactType()).getTypeVariable().getFirstBound();
                reportProblem = !One.resolve(scope.getWorld()).isAssignableFrom(Two.resolve(scope.getWorld()));
            } else {
                reportProblem = (p.matchesSubtypes(resolvedTypeArrResolve[i2]) || p.getExactType().equals(UnresolvedType.OBJECT)) ? false : true;
            }
            if (reportProblem) {
                scope.message(IMessage.ERROR, this, "incompatible type, expected " + resolvedTypeArrResolve[i2].getName() + " found " + p + ".  Check the type specified in your pointcut");
                return;
            }
        }
    }

    @Override // org.aspectj.weaver.patterns.Pointcut
    public void postRead(ResolvedType enclosingType) {
        this.arguments.postRead(enclosingType);
    }

    @Override // org.aspectj.weaver.patterns.Pointcut
    protected Test findResidueInternal(Shadow shadow, ExposedState state) {
        throw new RuntimeException("shouldn't happen");
    }

    @Override // org.aspectj.weaver.patterns.Pointcut
    public Pointcut concretize1(ResolvedType searchStart, ResolvedType declaringType, IntMap bindings) throws AbortException {
        if (this.concretizing) {
            searchStart.getWorld().getMessageHandler().handleMessage(MessageUtil.error(WeaverMessages.format(WeaverMessages.CIRCULAR_POINTCUT, this), getSourceLocation()));
            Pointcut p = Pointcut.makeMatchesNothing(Pointcut.CONCRETE);
            p.sourceContext = this.sourceContext;
            return p;
        }
        try {
            this.concretizing = true;
            if (this.onType != null) {
                searchStart = this.onType.resolve(searchStart.getWorld());
                if (searchStart.isMissing()) {
                    Pointcut pointcutMakeMatchesNothing = Pointcut.makeMatchesNothing(Pointcut.CONCRETE);
                    this.concretizing = false;
                    return pointcutMakeMatchesNothing;
                }
                if (this.onType.isTypeVariableReference() && declaringType.isParameterizedType()) {
                    TypeVariable[] tvs = declaringType.getGenericType().getTypeVariables();
                    String typeVariableName = ((TypeVariableReference) this.onType).getTypeVariable().getName();
                    int i = 0;
                    while (true) {
                        if (i >= tvs.length) {
                            break;
                        }
                        if (tvs[i].getName().equals(typeVariableName)) {
                            ResolvedType realOnType = declaringType.getTypeParameters()[i].resolve(declaringType.getWorld());
                            this.onType = realOnType;
                            searchStart = realOnType;
                            break;
                        }
                        i++;
                    }
                }
            }
            if (declaringType == null) {
                declaringType = searchStart;
            }
            ResolvedPointcutDefinition pointcutDec = declaringType.findPointcut(this.name);
            boolean foundMatchingPointcut = pointcutDec != null && Modifier.isPrivate(pointcutDec.getModifiers());
            if (!foundMatchingPointcut) {
                pointcutDec = searchStart.findPointcut(this.name);
                if (pointcutDec == null) {
                    searchStart.getWorld().getMessageHandler().handleMessage(MessageUtil.error(WeaverMessages.format(WeaverMessages.CANT_FIND_POINTCUT, this.name, searchStart.getName()), getSourceLocation()));
                    Pointcut pointcutMakeMatchesNothing2 = Pointcut.makeMatchesNothing(Pointcut.CONCRETE);
                    this.concretizing = false;
                    return pointcutMakeMatchesNothing2;
                }
            }
            if (pointcutDec.isAbstract()) {
                ShadowMunger enclosingAdvice = bindings.getEnclosingAdvice();
                searchStart.getWorld().showMessage(IMessage.ERROR, WeaverMessages.format(WeaverMessages.ABSTRACT_POINTCUT, pointcutDec), getSourceLocation(), null == enclosingAdvice ? null : enclosingAdvice.getSourceLocation());
                Pointcut pointcutMakeMatchesNothing3 = Pointcut.makeMatchesNothing(Pointcut.CONCRETE);
                this.concretizing = false;
                return pointcutMakeMatchesNothing3;
            }
            TypePatternList arguments = this.arguments.resolveReferences(bindings);
            IntMap newBindings = new IntMap();
            int len = arguments.size();
            for (int i2 = 0; i2 < len; i2++) {
                TypePattern p2 = arguments.get(i2);
                if (p2 != TypePattern.NO && (p2 instanceof BindingTypePattern)) {
                    newBindings.put(i2, ((BindingTypePattern) p2).getFormalIndex());
                }
            }
            if (searchStart.isParameterizedType()) {
                this.typeVariableMap = new HashMap();
                ResolvedType underlyingGenericType = searchStart.getGenericType();
                TypeVariable[] tVars = underlyingGenericType.getTypeVariables();
                ResolvedType[] typeParams = searchStart.getResolvedTypeParameters();
                for (int i3 = 0; i3 < tVars.length; i3++) {
                    this.typeVariableMap.put(tVars[i3].getName(), typeParams[i3]);
                }
            }
            newBindings.copyContext(bindings);
            newBindings.pushEnclosingDefinition(pointcutDec);
            try {
                Pointcut ret = pointcutDec.getPointcut();
                if (this.typeVariableMap != null && !this.hasBeenParameterized) {
                    ret = ret.parameterizeWith(this.typeVariableMap, searchStart.getWorld());
                    ret.hasBeenParameterized = true;
                }
                Pointcut pointcutConcretize = ret.concretize(searchStart, declaringType, newBindings);
                newBindings.popEnclosingDefinitition();
                this.concretizing = false;
                return pointcutConcretize;
            } catch (Throwable th) {
                newBindings.popEnclosingDefinitition();
                throw th;
            }
        } catch (Throwable th2) {
            this.concretizing = false;
            throw th2;
        }
    }

    @Override // org.aspectj.weaver.patterns.Pointcut
    public Pointcut parameterizeWith(Map<String, UnresolvedType> typeVariableMap, World w) {
        ReferencePointcut ret = new ReferencePointcut(this.onType, this.name, this.arguments);
        ret.onTypeSymbolic = this.onTypeSymbolic;
        ret.typeVariableMap = typeVariableMap;
        return ret;
    }

    @Override // org.aspectj.weaver.patterns.Pointcut
    protected boolean shouldCopyLocationForConcretize() {
        return false;
    }

    public boolean equals(Object other) {
        if (!(other instanceof ReferencePointcut)) {
            return false;
        }
        if (this == other) {
            return true;
        }
        ReferencePointcut o = (ReferencePointcut) other;
        return o.name.equals(this.name) && o.arguments.equals(this.arguments) && (o.onType != null ? o.onType.equals(this.onType) : this.onType == null);
    }

    public int hashCode() {
        int result = (37 * 17) + (this.onType == null ? 0 : this.onType.hashCode());
        return (37 * ((37 * result) + this.arguments.hashCode())) + this.name.hashCode();
    }

    @Override // org.aspectj.weaver.patterns.PatternNode
    public Object accept(PatternNodeVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }
}
