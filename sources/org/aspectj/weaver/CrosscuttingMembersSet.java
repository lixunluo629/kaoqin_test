package org.aspectj.weaver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.aspectj.bridge.AbortException;
import org.aspectj.bridge.IMessage;
import org.aspectj.bridge.MessageUtil;
import org.aspectj.weaver.ResolvedTypeMunger;
import org.aspectj.weaver.patterns.Declare;
import org.aspectj.weaver.patterns.DeclareAnnotation;
import org.aspectj.weaver.patterns.DeclareParents;
import org.aspectj.weaver.patterns.DeclareSoft;
import org.aspectj.weaver.patterns.DeclareTypeErrorOrWarning;
import org.aspectj.weaver.patterns.IVerificationRequired;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/CrosscuttingMembersSet.class */
public class CrosscuttingMembersSet {
    private transient World world;
    private final Map<ResolvedType, CrosscuttingMembers> members = new HashMap();
    private transient List<IVerificationRequired> verificationList = null;
    private List<ShadowMunger> shadowMungers = null;
    private List<ConcreteTypeMunger> typeMungers = null;
    private List<ConcreteTypeMunger> lateTypeMungers = null;
    private List<DeclareSoft> declareSofts = null;
    private List<DeclareParents> declareParents = null;
    private List<DeclareAnnotation> declareAnnotationOnTypes = null;
    private List<DeclareAnnotation> declareAnnotationOnFields = null;
    private List<DeclareAnnotation> declareAnnotationOnMethods = null;
    private List<DeclareTypeErrorOrWarning> declareTypeEows = null;
    private List<Declare> declareDominates = null;
    private boolean changedSinceLastReset = false;
    public int serializationVersion = 1;

    public CrosscuttingMembersSet(World world) {
        this.world = world;
    }

    public boolean addOrReplaceAspect(ResolvedType aspectType) {
        return addOrReplaceAspect(aspectType, true);
    }

    private boolean excludeDueToParentAspectHavingUnresolvedDependency(ResolvedType aspectType) throws AbortException {
        boolean excludeDueToParent = false;
        for (ResolvedType parent = aspectType.getSuperclass(); parent != null; parent = parent.getSuperclass()) {
            if (parent.isAspect() && parent.isAbstract() && this.world.hasUnsatisfiedDependency(parent)) {
                if (!this.world.getMessageHandler().isIgnoring(IMessage.INFO)) {
                    this.world.getMessageHandler().handleMessage(MessageUtil.info("deactivating aspect '" + aspectType.getName() + "' as the parent aspect '" + parent.getName() + "' has unsatisfied dependencies"));
                }
                excludeDueToParent = true;
            }
        }
        return excludeDueToParent;
    }

    public boolean addOrReplaceAspect(ResolvedType aspectType, boolean inWeavingPhase) {
        boolean change;
        if (!this.world.isAspectIncluded(aspectType) || this.world.hasUnsatisfiedDependency(aspectType) || excludeDueToParentAspectHavingUnresolvedDependency(aspectType)) {
            return false;
        }
        CrosscuttingMembers xcut = this.members.get(aspectType);
        if (xcut == null) {
            this.members.put(aspectType, aspectType.collectCrosscuttingMembers(inWeavingPhase));
            clearCaches();
            change = true;
        } else if (xcut.replaceWith(aspectType.collectCrosscuttingMembers(inWeavingPhase), inWeavingPhase)) {
            clearCaches();
            change = true;
        } else {
            if (inWeavingPhase) {
                this.shadowMungers = null;
            }
            change = false;
        }
        if (aspectType.isAbstract()) {
            boolean ancestorChange = addOrReplaceDescendantsOf(aspectType, inWeavingPhase);
            change = change || ancestorChange;
        }
        this.changedSinceLastReset = this.changedSinceLastReset || change;
        return change;
    }

    private boolean addOrReplaceDescendantsOf(ResolvedType aspectType, boolean inWeavePhase) {
        Set<ResolvedType> knownAspects = this.members.keySet();
        Set<ResolvedType> toBeReplaced = new HashSet<>();
        for (ResolvedType candidateDescendant : knownAspects) {
            if (candidateDescendant != aspectType && aspectType.isAssignableFrom(candidateDescendant, true)) {
                toBeReplaced.add(candidateDescendant);
            }
        }
        boolean change = false;
        for (ResolvedType next : toBeReplaced) {
            boolean thisChange = addOrReplaceAspect(next, inWeavePhase);
            change = change || thisChange;
        }
        return change;
    }

    public void addAdviceLikeDeclares(ResolvedType aspectType) {
        if (!this.members.containsKey(aspectType)) {
            return;
        }
        CrosscuttingMembers xcut = this.members.get(aspectType);
        xcut.addDeclares(aspectType.collectDeclares(true));
    }

    public boolean deleteAspect(UnresolvedType aspectType) {
        boolean isAspect = this.members.remove(aspectType) != null;
        clearCaches();
        return isAspect;
    }

    public boolean containsAspect(UnresolvedType aspectType) {
        return this.members.containsKey(aspectType);
    }

    public void addFixedCrosscuttingMembers(ResolvedType aspectType) {
        this.members.put(aspectType, aspectType.crosscuttingMembers);
        clearCaches();
    }

    private void clearCaches() {
        this.shadowMungers = null;
        this.typeMungers = null;
        this.lateTypeMungers = null;
        this.declareSofts = null;
        this.declareParents = null;
        this.declareAnnotationOnFields = null;
        this.declareAnnotationOnMethods = null;
        this.declareAnnotationOnTypes = null;
        this.declareDominates = null;
    }

    public List<ShadowMunger> getShadowMungers() {
        if (this.shadowMungers == null) {
            List<ShadowMunger> ret = new ArrayList<>();
            Iterator<CrosscuttingMembers> i = this.members.values().iterator();
            while (i.hasNext()) {
                ret.addAll(i.next().getShadowMungers());
            }
            this.shadowMungers = ret;
        }
        return this.shadowMungers;
    }

    public List<ConcreteTypeMunger> getTypeMungers() {
        if (this.typeMungers == null) {
            List<ConcreteTypeMunger> ret = new ArrayList<>();
            for (CrosscuttingMembers xmembers : this.members.values()) {
                for (ConcreteTypeMunger mungerToAdd : xmembers.getTypeMungers()) {
                    ResolvedTypeMunger resolvedMungerToAdd = mungerToAdd.getMunger();
                    if (isNewStylePrivilegedAccessMunger(resolvedMungerToAdd)) {
                        String newFieldName = resolvedMungerToAdd.getSignature().getName();
                        boolean alreadyExists = false;
                        Iterator<ConcreteTypeMunger> it = ret.iterator();
                        while (true) {
                            if (!it.hasNext()) {
                                break;
                            }
                            ConcreteTypeMunger existingMunger = it.next();
                            ResolvedTypeMunger existing = existingMunger.getMunger();
                            if (isNewStylePrivilegedAccessMunger(existing)) {
                                String existingFieldName = existing.getSignature().getName();
                                if (existingFieldName.equals(newFieldName) && existing.getSignature().getDeclaringType().equals(resolvedMungerToAdd.getSignature().getDeclaringType())) {
                                    alreadyExists = true;
                                    break;
                                }
                            }
                        }
                        if (!alreadyExists) {
                            ret.add(mungerToAdd);
                        }
                    } else {
                        ret.add(mungerToAdd);
                    }
                }
            }
            this.typeMungers = ret;
        }
        return this.typeMungers;
    }

    public List<ConcreteTypeMunger> getTypeMungersOfKind(ResolvedTypeMunger.Kind kind) {
        List<ConcreteTypeMunger> collected = null;
        for (ConcreteTypeMunger typeMunger : this.typeMungers) {
            if (typeMunger.getMunger() != null && typeMunger.getMunger().getKind() == kind) {
                if (collected == null) {
                    collected = new ArrayList<>();
                }
                collected.add(typeMunger);
            }
        }
        if (collected == null) {
            return Collections.emptyList();
        }
        return collected;
    }

    private boolean isNewStylePrivilegedAccessMunger(ResolvedTypeMunger typeMunger) {
        boolean b = typeMunger != null && typeMunger.getKind() == ResolvedTypeMunger.PrivilegedAccess && typeMunger.getSignature().getKind() == Member.FIELD;
        if (!b) {
            return b;
        }
        PrivilegedAccessMunger privAccessMunger = (PrivilegedAccessMunger) typeMunger;
        return privAccessMunger.shortSyntax;
    }

    public List<ConcreteTypeMunger> getLateTypeMungers() {
        if (this.lateTypeMungers == null) {
            List<ConcreteTypeMunger> ret = new ArrayList<>();
            Iterator<CrosscuttingMembers> i = this.members.values().iterator();
            while (i.hasNext()) {
                ret.addAll(i.next().getLateTypeMungers());
            }
            this.lateTypeMungers = ret;
        }
        return this.lateTypeMungers;
    }

    public List<DeclareSoft> getDeclareSofts() {
        if (this.declareSofts == null) {
            Set<DeclareSoft> ret = new HashSet<>();
            Iterator<CrosscuttingMembers> i = this.members.values().iterator();
            while (i.hasNext()) {
                ret.addAll(i.next().getDeclareSofts());
            }
            this.declareSofts = new ArrayList();
            this.declareSofts.addAll(ret);
        }
        return this.declareSofts;
    }

    public List<DeclareParents> getDeclareParents() {
        if (this.declareParents == null) {
            Set<DeclareParents> ret = new HashSet<>();
            Iterator<CrosscuttingMembers> i = this.members.values().iterator();
            while (i.hasNext()) {
                ret.addAll(i.next().getDeclareParents());
            }
            this.declareParents = new ArrayList();
            this.declareParents.addAll(ret);
        }
        return this.declareParents;
    }

    public List<DeclareAnnotation> getDeclareAnnotationOnTypes() {
        if (this.declareAnnotationOnTypes == null) {
            Set<DeclareAnnotation> ret = new LinkedHashSet<>();
            Iterator<CrosscuttingMembers> i = this.members.values().iterator();
            while (i.hasNext()) {
                ret.addAll(i.next().getDeclareAnnotationOnTypes());
            }
            this.declareAnnotationOnTypes = new ArrayList();
            this.declareAnnotationOnTypes.addAll(ret);
        }
        return this.declareAnnotationOnTypes;
    }

    public List<DeclareAnnotation> getDeclareAnnotationOnFields() {
        if (this.declareAnnotationOnFields == null) {
            Set<DeclareAnnotation> ret = new LinkedHashSet<>();
            Iterator<CrosscuttingMembers> i = this.members.values().iterator();
            while (i.hasNext()) {
                ret.addAll(i.next().getDeclareAnnotationOnFields());
            }
            this.declareAnnotationOnFields = new ArrayList();
            this.declareAnnotationOnFields.addAll(ret);
        }
        return this.declareAnnotationOnFields;
    }

    public List<DeclareAnnotation> getDeclareAnnotationOnMethods() {
        if (this.declareAnnotationOnMethods == null) {
            Set<DeclareAnnotation> ret = new LinkedHashSet<>();
            Iterator<CrosscuttingMembers> i = this.members.values().iterator();
            while (i.hasNext()) {
                ret.addAll(i.next().getDeclareAnnotationOnMethods());
            }
            this.declareAnnotationOnMethods = new ArrayList();
            this.declareAnnotationOnMethods.addAll(ret);
        }
        return this.declareAnnotationOnMethods;
    }

    public List<DeclareTypeErrorOrWarning> getDeclareTypeEows() {
        if (this.declareTypeEows == null) {
            Set<DeclareTypeErrorOrWarning> ret = new HashSet<>();
            Iterator<CrosscuttingMembers> i = this.members.values().iterator();
            while (i.hasNext()) {
                ret.addAll(i.next().getDeclareTypeErrorOrWarning());
            }
            this.declareTypeEows = new ArrayList();
            this.declareTypeEows.addAll(ret);
        }
        return this.declareTypeEows;
    }

    public List<Declare> getDeclareDominates() {
        if (this.declareDominates == null) {
            List<Declare> ret = new ArrayList<>();
            Iterator<CrosscuttingMembers> i = this.members.values().iterator();
            while (i.hasNext()) {
                ret.addAll(i.next().getDeclareDominates());
            }
            this.declareDominates = ret;
        }
        return this.declareDominates;
    }

    public ResolvedType findAspectDeclaringParents(DeclareParents p) {
        Set<ResolvedType> keys = this.members.keySet();
        for (ResolvedType element : keys) {
            for (DeclareParents dp : this.members.get(element).getDeclareParents()) {
                if (dp.equals(p)) {
                    return element;
                }
            }
        }
        return null;
    }

    public void reset() {
        this.verificationList = null;
        this.changedSinceLastReset = false;
    }

    public boolean hasChangedSinceLastReset() {
        return this.changedSinceLastReset;
    }

    public void recordNecessaryCheck(IVerificationRequired verification) {
        if (this.verificationList == null) {
            this.verificationList = new ArrayList();
        }
        this.verificationList.add(verification);
    }

    public void verify() {
        if (this.verificationList == null) {
            return;
        }
        for (IVerificationRequired element : this.verificationList) {
            element.verify();
        }
        this.verificationList = null;
    }

    public void write(CompressingDataOutputStream stream) throws IOException {
        stream.writeInt(this.shadowMungers.size());
        for (ShadowMunger shadowMunger : this.shadowMungers) {
            shadowMunger.write(stream);
        }
    }
}
