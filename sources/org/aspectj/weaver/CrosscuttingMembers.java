package org.aspectj.weaver;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.aspectj.weaver.patterns.Declare;
import org.aspectj.weaver.patterns.DeclareAnnotation;
import org.aspectj.weaver.patterns.DeclareErrorOrWarning;
import org.aspectj.weaver.patterns.DeclareParents;
import org.aspectj.weaver.patterns.DeclarePrecedence;
import org.aspectj.weaver.patterns.DeclareSoft;
import org.aspectj.weaver.patterns.DeclareTypeErrorOrWarning;
import org.aspectj.weaver.patterns.PerClause;
import org.aspectj.weaver.patterns.Pointcut;
import org.aspectj.weaver.patterns.PointcutRewriter;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/CrosscuttingMembers.class */
public class CrosscuttingMembers {
    private final ResolvedType inAspect;
    private final World world;
    private PerClause perClause;
    private boolean shouldConcretizeIfNeeded;
    private List<ShadowMunger> shadowMungers = new ArrayList(4);
    private List<ConcreteTypeMunger> typeMungers = new ArrayList(4);
    private List<ConcreteTypeMunger> lateTypeMungers = new ArrayList(0);
    private Set<DeclareParents> declareParents = new HashSet();
    private Set<DeclareSoft> declareSofts = new HashSet();
    private List<Declare> declareDominates = new ArrayList(4);
    private Set<DeclareAnnotation> declareAnnotationsOnType = new LinkedHashSet();
    private Set<DeclareAnnotation> declareAnnotationsOnField = new LinkedHashSet();
    private Set<DeclareAnnotation> declareAnnotationsOnMethods = new LinkedHashSet();
    private Set<DeclareTypeErrorOrWarning> declareTypeEow = new HashSet();
    private final Hashtable<String, Object> cflowFields = new Hashtable<>();
    private final Hashtable<String, Object> cflowBelowFields = new Hashtable<>();

    public CrosscuttingMembers(ResolvedType inAspect, boolean shouldConcretizeIfNeeded) {
        this.shouldConcretizeIfNeeded = true;
        this.inAspect = inAspect;
        this.world = inAspect.getWorld();
        this.shouldConcretizeIfNeeded = shouldConcretizeIfNeeded;
    }

    public void addConcreteShadowMunger(ShadowMunger m) {
        this.shadowMungers.add(m);
    }

    public void addShadowMungers(Collection<ShadowMunger> c) {
        for (ShadowMunger munger : c) {
            addShadowMunger(munger);
        }
    }

    private void addShadowMunger(ShadowMunger m) {
        if (this.inAspect.isAbstract()) {
            return;
        }
        addConcreteShadowMunger(m.concretize(this.inAspect, this.world, this.perClause));
    }

    public void addTypeMungers(Collection<ConcreteTypeMunger> c) {
        this.typeMungers.addAll(c);
    }

    public void addTypeMunger(ConcreteTypeMunger m) {
        if (m == null) {
            throw new Error("FIXME AV - should not happen or what ?");
        }
        this.typeMungers.add(m);
    }

    public void addLateTypeMungers(Collection<ConcreteTypeMunger> c) {
        this.lateTypeMungers.addAll(c);
    }

    public void addLateTypeMunger(ConcreteTypeMunger m) {
        this.lateTypeMungers.add(m);
    }

    public void addDeclares(Collection<Declare> declares) {
        for (Declare declare : declares) {
            addDeclare(declare);
        }
    }

    public void addDeclare(Declare declare) {
        if (declare instanceof DeclareErrorOrWarning) {
            ShadowMunger m = new Checker((DeclareErrorOrWarning) declare);
            m.setDeclaringType(declare.getDeclaringType());
            addShadowMunger(m);
            return;
        }
        if (declare instanceof DeclarePrecedence) {
            this.declareDominates.add(declare);
            return;
        }
        if (declare instanceof DeclareParents) {
            DeclareParents dp = (DeclareParents) declare;
            exposeTypes(dp.getParents().getExactTypes());
            this.declareParents.add(dp);
            return;
        }
        if (declare instanceof DeclareSoft) {
            DeclareSoft d = (DeclareSoft) declare;
            ShadowMunger m2 = Advice.makeSoftener(this.world, d.getPointcut(), d.getException(), this.inAspect, d);
            m2.setDeclaringType(d.getDeclaringType());
            Pointcut concretePointcut = d.getPointcut().concretize(this.inAspect, d.getDeclaringType(), 0, m2);
            m2.pointcut = concretePointcut;
            this.declareSofts.add(new DeclareSoft(d.getException(), concretePointcut));
            addConcreteShadowMunger(m2);
            return;
        }
        if (!(declare instanceof DeclareAnnotation)) {
            if (declare instanceof DeclareTypeErrorOrWarning) {
                this.declareTypeEow.add((DeclareTypeErrorOrWarning) declare);
                return;
            }
            throw new RuntimeException("unimplemented");
        }
        DeclareAnnotation da = (DeclareAnnotation) declare;
        if (da.getAspect() == null) {
            da.setAspect(this.inAspect);
        }
        if (da.isDeclareAtType()) {
            this.declareAnnotationsOnType.add(da);
            return;
        }
        if (da.isDeclareAtField()) {
            this.declareAnnotationsOnField.add(da);
        } else if (da.isDeclareAtMethod() || da.isDeclareAtConstuctor()) {
            this.declareAnnotationsOnMethods.add(da);
        }
    }

    public void exposeTypes(List<UnresolvedType> typesToExpose) {
        for (UnresolvedType typeToExpose : typesToExpose) {
            exposeType(typeToExpose);
        }
    }

    public void exposeType(UnresolvedType typeToExpose) {
        if (ResolvedType.isMissing(typeToExpose)) {
            return;
        }
        if (typeToExpose.isParameterizedType() || typeToExpose.isRawType()) {
            if (typeToExpose instanceof ResolvedType) {
                typeToExpose = ((ResolvedType) typeToExpose).getGenericType();
            } else {
                typeToExpose = UnresolvedType.forSignature(typeToExpose.getErasureSignature());
            }
        }
        String signatureToLookFor = typeToExpose.getSignature();
        for (ConcreteTypeMunger cTM : this.typeMungers) {
            ResolvedTypeMunger rTM = cTM.getMunger();
            if (rTM != null && (rTM instanceof ExposeTypeMunger)) {
                String exposedType = ((ExposeTypeMunger) rTM).getExposedTypeSignature();
                if (exposedType.equals(signatureToLookFor)) {
                    return;
                }
            }
        }
        addTypeMunger(this.world.getWeavingSupport().concreteTypeMunger(new ExposeTypeMunger(typeToExpose), this.inAspect));
    }

    public void addPrivilegedAccesses(Collection<ResolvedMember> accessedMembers) {
        int version = this.inAspect.getCompilerVersion();
        for (ResolvedMember member : accessedMembers) {
            ResolvedMember resolvedMember = this.world.resolve(member);
            if (resolvedMember == null) {
                resolvedMember = member;
                if (resolvedMember.hasBackingGenericMember()) {
                    resolvedMember = resolvedMember.getBackingGenericMember();
                }
            } else {
                UnresolvedType unresolvedDeclaringType = member.getDeclaringType().getRawType();
                UnresolvedType resolvedDeclaringType = resolvedMember.getDeclaringType().getRawType();
                if (!unresolvedDeclaringType.equals(resolvedDeclaringType)) {
                    resolvedMember = member;
                }
            }
            PrivilegedAccessMunger privilegedAccessMunger = new PrivilegedAccessMunger(resolvedMember, version >= 7);
            ConcreteTypeMunger concreteTypeMunger = this.world.getWeavingSupport().concreteTypeMunger(privilegedAccessMunger, this.inAspect);
            addTypeMunger(concreteTypeMunger);
        }
    }

    public Collection<ShadowMunger> getCflowEntries() {
        List<ShadowMunger> ret = new ArrayList<>();
        for (ShadowMunger m : this.shadowMungers) {
            if (m instanceof Advice) {
                Advice a = (Advice) m;
                if (a.getKind().isCflow()) {
                    ret.add(a);
                }
            }
        }
        return ret;
    }

    public boolean replaceWith(CrosscuttingMembers other, boolean careAboutShadowMungers) {
        boolean changed = false;
        if (careAboutShadowMungers && (this.perClause == null || !this.perClause.equals(other.perClause))) {
            changed = true;
            this.perClause = other.perClause;
        }
        if (careAboutShadowMungers) {
            Set<ShadowMunger> theseShadowMungers = new HashSet<>();
            Set<ShadowMunger> theseInlinedAroundMungers = new HashSet<>();
            for (ShadowMunger munger : this.shadowMungers) {
                if (munger instanceof Advice) {
                    Advice adviceMunger = (Advice) munger;
                    if (!this.world.isXnoInline() && adviceMunger.getKind().equals(AdviceKind.Around)) {
                        theseInlinedAroundMungers.add(adviceMunger);
                    } else {
                        theseShadowMungers.add(adviceMunger);
                    }
                } else {
                    theseShadowMungers.add(munger);
                }
            }
            Set<ShadowMunger> tempSet = new HashSet<>();
            tempSet.addAll(other.shadowMungers);
            Set<ShadowMunger> otherShadowMungers = new HashSet<>();
            Set<ShadowMunger> otherInlinedAroundMungers = new HashSet<>();
            for (ShadowMunger munger2 : tempSet) {
                if (munger2 instanceof Advice) {
                    Advice adviceMunger2 = (Advice) munger2;
                    if (!this.world.isXnoInline() && adviceMunger2.getKind().equals(AdviceKind.Around)) {
                        otherInlinedAroundMungers.add(rewritePointcutInMunger(adviceMunger2));
                    } else {
                        otherShadowMungers.add(rewritePointcutInMunger(adviceMunger2));
                    }
                } else {
                    otherShadowMungers.add(rewritePointcutInMunger(munger2));
                }
            }
            if (!theseShadowMungers.equals(otherShadowMungers)) {
                changed = true;
            }
            if (!equivalent(theseInlinedAroundMungers, otherInlinedAroundMungers)) {
                changed = true;
            }
            if (!changed) {
                for (ShadowMunger munger3 : this.shadowMungers) {
                    int i = other.shadowMungers.indexOf(munger3);
                    ShadowMunger otherMunger = other.shadowMungers.get(i);
                    if (munger3 instanceof Advice) {
                        ((Advice) otherMunger).setHasMatchedSomething(((Advice) munger3).hasMatchedSomething());
                    }
                }
            }
            this.shadowMungers = other.shadowMungers;
        }
        Set<Object> theseTypeMungers = new HashSet<>();
        Set<Object> otherTypeMungers = new HashSet<>();
        if (!careAboutShadowMungers) {
            for (Object o : this.typeMungers) {
                if (o instanceof ConcreteTypeMunger) {
                    ConcreteTypeMunger typeMunger = (ConcreteTypeMunger) o;
                    if (!typeMunger.existsToSupportShadowMunging()) {
                        theseTypeMungers.add(typeMunger);
                    }
                } else {
                    theseTypeMungers.add(o);
                }
            }
            for (Object o2 : other.typeMungers) {
                if (o2 instanceof ConcreteTypeMunger) {
                    ConcreteTypeMunger typeMunger2 = (ConcreteTypeMunger) o2;
                    if (!typeMunger2.existsToSupportShadowMunging()) {
                        otherTypeMungers.add(typeMunger2);
                    }
                } else {
                    otherTypeMungers.add(o2);
                }
            }
        } else {
            theseTypeMungers.addAll(this.typeMungers);
            otherTypeMungers.addAll(other.typeMungers);
        }
        if (theseTypeMungers.size() != otherTypeMungers.size()) {
            changed = true;
            this.typeMungers = other.typeMungers;
        } else {
            boolean shouldOverwriteThis = false;
            boolean foundInequality = false;
            Iterator<Object> iter = theseTypeMungers.iterator();
            while (iter.hasNext() && !foundInequality) {
                Object thisOne = iter.next();
                boolean foundInOtherSet = false;
                for (Object otherOne : otherTypeMungers) {
                    if ((thisOne instanceof ConcreteTypeMunger) && ((ConcreteTypeMunger) thisOne).shouldOverwrite()) {
                        shouldOverwriteThis = true;
                    }
                    if ((thisOne instanceof ConcreteTypeMunger) && (otherOne instanceof ConcreteTypeMunger)) {
                        if (((ConcreteTypeMunger) thisOne).equivalentTo(otherOne)) {
                            foundInOtherSet = true;
                        } else if (thisOne.equals(otherOne)) {
                            foundInOtherSet = true;
                        }
                    } else if (thisOne.equals(otherOne)) {
                        foundInOtherSet = true;
                    }
                }
                if (!foundInOtherSet) {
                    foundInequality = true;
                }
            }
            if (foundInequality) {
                changed = true;
            }
            if (shouldOverwriteThis) {
                this.typeMungers = other.typeMungers;
            }
        }
        if (!this.lateTypeMungers.equals(other.lateTypeMungers)) {
            changed = true;
            this.lateTypeMungers = other.lateTypeMungers;
        }
        if (!this.declareDominates.equals(other.declareDominates)) {
            changed = true;
            this.declareDominates = other.declareDominates;
        }
        if (!this.declareParents.equals(other.declareParents)) {
            if (!careAboutShadowMungers) {
                Set<DeclareParents> trimmedThis = new HashSet<>();
                for (DeclareParents decp : this.declareParents) {
                    if (!decp.isMixin()) {
                        trimmedThis.add(decp);
                    }
                }
                Set<DeclareParents> trimmedOther = new HashSet<>();
                for (DeclareParents decp2 : other.declareParents) {
                    if (!decp2.isMixin()) {
                        trimmedOther.add(decp2);
                    }
                }
                if (!trimmedThis.equals(trimmedOther)) {
                    changed = true;
                    this.declareParents = other.declareParents;
                }
            } else {
                changed = true;
                this.declareParents = other.declareParents;
            }
        }
        if (!this.declareSofts.equals(other.declareSofts)) {
            changed = true;
            this.declareSofts = other.declareSofts;
        }
        if (!this.declareAnnotationsOnType.equals(other.declareAnnotationsOnType)) {
            changed = true;
            this.declareAnnotationsOnType = other.declareAnnotationsOnType;
        }
        if (!this.declareAnnotationsOnField.equals(other.declareAnnotationsOnField)) {
            changed = true;
            this.declareAnnotationsOnField = other.declareAnnotationsOnField;
        }
        if (!this.declareAnnotationsOnMethods.equals(other.declareAnnotationsOnMethods)) {
            changed = true;
            this.declareAnnotationsOnMethods = other.declareAnnotationsOnMethods;
        }
        if (!this.declareTypeEow.equals(other.declareTypeEow)) {
            changed = true;
            this.declareTypeEow = other.declareTypeEow;
        }
        return changed;
    }

    private boolean equivalent(Set<ShadowMunger> theseInlinedAroundMungers, Set<ShadowMunger> otherInlinedAroundMungers) {
        if (theseInlinedAroundMungers.size() != otherInlinedAroundMungers.size()) {
            return false;
        }
        Iterator<ShadowMunger> iter = theseInlinedAroundMungers.iterator();
        while (iter.hasNext()) {
            Advice thisAdvice = (Advice) iter.next();
            boolean foundIt = false;
            Iterator<ShadowMunger> iterator = otherInlinedAroundMungers.iterator();
            while (iterator.hasNext()) {
                Advice otherAdvice = (Advice) iterator.next();
                if (thisAdvice.equals(otherAdvice)) {
                    if ((thisAdvice.getSignature() instanceof ResolvedMemberImpl) && ((ResolvedMemberImpl) thisAdvice.getSignature()).isEquivalentTo(otherAdvice.getSignature())) {
                        foundIt = true;
                    } else {
                        return false;
                    }
                }
            }
            if (!foundIt) {
                return false;
            }
        }
        return true;
    }

    private ShadowMunger rewritePointcutInMunger(ShadowMunger munger) {
        PointcutRewriter pr = new PointcutRewriter();
        Pointcut p = munger.getPointcut();
        Pointcut newP = pr.rewrite(p);
        if (p.m_ignoreUnboundBindingForNames.length != 0) {
            newP.m_ignoreUnboundBindingForNames = p.m_ignoreUnboundBindingForNames;
        }
        munger.setPointcut(newP);
        return munger;
    }

    public void setPerClause(PerClause perClause) {
        if (this.shouldConcretizeIfNeeded) {
            this.perClause = perClause.concretize(this.inAspect);
        } else {
            this.perClause = perClause;
        }
    }

    public List<Declare> getDeclareDominates() {
        return this.declareDominates;
    }

    public Collection<DeclareParents> getDeclareParents() {
        return this.declareParents;
    }

    public Collection<DeclareSoft> getDeclareSofts() {
        return this.declareSofts;
    }

    public List<ShadowMunger> getShadowMungers() {
        return this.shadowMungers;
    }

    public List<ConcreteTypeMunger> getTypeMungers() {
        return this.typeMungers;
    }

    public List<ConcreteTypeMunger> getLateTypeMungers() {
        return this.lateTypeMungers;
    }

    public Collection<DeclareAnnotation> getDeclareAnnotationOnTypes() {
        return this.declareAnnotationsOnType;
    }

    public Collection<DeclareAnnotation> getDeclareAnnotationOnFields() {
        return this.declareAnnotationsOnField;
    }

    public Collection<DeclareAnnotation> getDeclareAnnotationOnMethods() {
        return this.declareAnnotationsOnMethods;
    }

    public Collection<DeclareTypeErrorOrWarning> getDeclareTypeErrorOrWarning() {
        return this.declareTypeEow;
    }

    public Map<String, Object> getCflowBelowFields() {
        return this.cflowBelowFields;
    }

    public Map<String, Object> getCflowFields() {
        return this.cflowFields;
    }

    public void clearCaches() {
        this.cflowFields.clear();
        this.cflowBelowFields.clear();
    }
}
