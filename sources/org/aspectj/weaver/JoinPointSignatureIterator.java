package org.aspectj.weaver;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/JoinPointSignatureIterator.class */
public class JoinPointSignatureIterator implements Iterator<JoinPointSignature> {
    ResolvedType firstDefiningType;
    private Member signaturesOfMember;
    private ResolvedMember firstDefiningMember;
    private World world;
    private List<JoinPointSignature> discoveredSignatures = new ArrayList();
    private List<JoinPointSignature> additionalSignatures = Collections.emptyList();
    private Iterator<JoinPointSignature> discoveredSignaturesIterator = null;
    private Iterator<ResolvedType> superTypeIterator = null;
    private boolean isProxy = false;
    private Set<ResolvedType> visitedSuperTypes = new HashSet();
    private List<SearchPair> yetToBeProcessedSuperMembers = null;
    private boolean iteratingOverDiscoveredSignatures = true;
    private boolean couldBeFurtherAsYetUndiscoveredSignatures;
    private static final UnresolvedType jlrProxy = UnresolvedType.forSignature("Ljava/lang/reflect/Proxy;");

    public JoinPointSignatureIterator(Member joinPointSignature, World world) {
        this.couldBeFurtherAsYetUndiscoveredSignatures = true;
        this.signaturesOfMember = joinPointSignature;
        this.world = world;
        addSignaturesUpToFirstDefiningMember();
        if (!shouldWalkUpHierarchy()) {
            this.couldBeFurtherAsYetUndiscoveredSignatures = false;
        }
    }

    public void reset() {
        this.discoveredSignaturesIterator = this.discoveredSignatures.iterator();
        this.additionalSignatures.clear();
        this.iteratingOverDiscoveredSignatures = true;
    }

    @Override // java.util.Iterator
    public boolean hasNext() {
        if (this.iteratingOverDiscoveredSignatures && this.discoveredSignaturesIterator.hasNext()) {
            return true;
        }
        if (this.couldBeFurtherAsYetUndiscoveredSignatures) {
            if (this.additionalSignatures.size() > 0) {
                return true;
            }
            return findSignaturesFromSupertypes();
        }
        return false;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // java.util.Iterator
    public JoinPointSignature next() {
        if (this.iteratingOverDiscoveredSignatures && this.discoveredSignaturesIterator.hasNext()) {
            return this.discoveredSignaturesIterator.next();
        }
        if (this.additionalSignatures.size() > 0) {
            return this.additionalSignatures.remove(0);
        }
        throw new NoSuchElementException();
    }

    @Override // java.util.Iterator
    public void remove() {
        throw new UnsupportedOperationException("can't remove from JoinPointSignatureIterator");
    }

    private void addSignaturesUpToFirstDefiningMember() {
        ResolvedType originalDeclaringType = this.signaturesOfMember.getDeclaringType().resolve(this.world);
        ResolvedType superType = originalDeclaringType.getSuperclass();
        if (superType != null && superType.equals(jlrProxy)) {
            this.isProxy = true;
        }
        if (this.world.isJoinpointArrayConstructionEnabled() && originalDeclaringType.isArray()) {
            Member m = this.signaturesOfMember;
            ResolvedMember rm = new ResolvedMemberImpl(m.getKind(), m.getDeclaringType(), m.getModifiers(), m.getReturnType(), m.getName(), m.getParameterTypes());
            this.discoveredSignatures.add(new JoinPointSignature(rm, originalDeclaringType));
            this.couldBeFurtherAsYetUndiscoveredSignatures = false;
            return;
        }
        this.firstDefiningMember = this.signaturesOfMember instanceof ResolvedMember ? (ResolvedMember) this.signaturesOfMember : this.signaturesOfMember.resolve(this.world);
        if (this.firstDefiningMember == null) {
            this.couldBeFurtherAsYetUndiscoveredSignatures = false;
            return;
        }
        this.firstDefiningType = this.firstDefiningMember.getDeclaringType().resolve(this.world);
        if (this.firstDefiningType != originalDeclaringType && this.signaturesOfMember.getKind() == Member.CONSTRUCTOR) {
            return;
        }
        if (originalDeclaringType == this.firstDefiningType) {
            this.discoveredSignatures.add(new JoinPointSignature(this.firstDefiningMember, originalDeclaringType));
            return;
        }
        List<ResolvedType> declaringTypes = new ArrayList<>();
        accumulateTypesInBetween(originalDeclaringType, this.firstDefiningType, declaringTypes);
        for (ResolvedType declaringType : declaringTypes) {
            this.discoveredSignatures.add(new JoinPointSignature(this.firstDefiningMember, declaringType));
        }
    }

    private void accumulateTypesInBetween(ResolvedType subType, ResolvedType superType, List<ResolvedType> types) {
        types.add(subType);
        if (subType == superType) {
            return;
        }
        Iterator<ResolvedType> iter = subType.getDirectSupertypes();
        while (iter.hasNext()) {
            ResolvedType parent = iter.next();
            if (superType.isAssignableFrom(parent, true)) {
                accumulateTypesInBetween(parent, superType, types);
            }
        }
    }

    private boolean shouldWalkUpHierarchy() {
        if (this.signaturesOfMember.getKind() == Member.CONSTRUCTOR || this.signaturesOfMember.getKind() == Member.FIELD || Modifier.isStatic(this.signaturesOfMember.getModifiers())) {
            return false;
        }
        return true;
    }

    private boolean findSignaturesFromSupertypes() {
        this.iteratingOverDiscoveredSignatures = false;
        if (this.superTypeIterator == null) {
            this.superTypeIterator = this.firstDefiningType.getDirectSupertypes();
        }
        if (this.superTypeIterator.hasNext()) {
            ResolvedType superType = this.superTypeIterator.next();
            if (this.isProxy && (superType.isGenericType() || superType.isParameterizedType())) {
                superType = superType.getRawType();
            }
            if (this.visitedSuperTypes.contains(superType)) {
                return findSignaturesFromSupertypes();
            }
            this.visitedSuperTypes.add(superType);
            if (superType.isMissing()) {
                warnOnMissingType(superType);
                return findSignaturesFromSupertypes();
            }
            ResolvedMemberImpl foundMember = (ResolvedMemberImpl) superType.lookupResolvedMember(this.firstDefiningMember, true, this.isProxy);
            if (foundMember != null && isVisibleTo(this.firstDefiningMember, foundMember)) {
                List<ResolvedType> declaringTypes = new ArrayList<>();
                ResolvedType resolvedDeclaringType = foundMember.getDeclaringType().resolve(this.world);
                accumulateTypesInBetween(superType, resolvedDeclaringType, declaringTypes);
                for (ResolvedType declaringType : declaringTypes) {
                    if (this.isProxy && (declaringType.isGenericType() || declaringType.isParameterizedType())) {
                        declaringType = declaringType.getRawType();
                    }
                    JoinPointSignature member = new JoinPointSignature(foundMember, declaringType);
                    this.discoveredSignatures.add(member);
                    if (this.additionalSignatures == Collections.EMPTY_LIST) {
                        this.additionalSignatures = new ArrayList();
                    }
                    this.additionalSignatures.add(member);
                }
                if (!this.isProxy && superType.isParameterizedType() && foundMember.backingGenericMember != null) {
                    JoinPointSignature member2 = new JoinPointSignature(foundMember.backingGenericMember, foundMember.declaringType.resolve(this.world));
                    this.discoveredSignatures.add(member2);
                    if (this.additionalSignatures == Collections.EMPTY_LIST) {
                        this.additionalSignatures = new ArrayList();
                    }
                    this.additionalSignatures.add(member2);
                }
                if (this.yetToBeProcessedSuperMembers == null) {
                    this.yetToBeProcessedSuperMembers = new ArrayList();
                }
                this.yetToBeProcessedSuperMembers.add(new SearchPair(foundMember, superType));
                return true;
            }
            return findSignaturesFromSupertypes();
        }
        if (this.yetToBeProcessedSuperMembers != null && !this.yetToBeProcessedSuperMembers.isEmpty()) {
            SearchPair nextUp = this.yetToBeProcessedSuperMembers.remove(0);
            this.firstDefiningType = nextUp.type;
            this.firstDefiningMember = nextUp.member;
            this.superTypeIterator = null;
            return findSignaturesFromSupertypes();
        }
        this.couldBeFurtherAsYetUndiscoveredSignatures = false;
        return false;
    }

    private boolean isVisibleTo(ResolvedMember childMember, ResolvedMember parentMember) {
        if (!childMember.getDeclaringType().equals(parentMember.getDeclaringType()) && Modifier.isPrivate(parentMember.getModifiers())) {
            return false;
        }
        return true;
    }

    private void warnOnMissingType(ResolvedType missing) {
        if (missing instanceof MissingResolvedTypeWithKnownSignature) {
            MissingResolvedTypeWithKnownSignature mrt = (MissingResolvedTypeWithKnownSignature) missing;
            mrt.raiseWarningOnJoinPointSignature(this.signaturesOfMember.toString());
        }
    }

    /* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/JoinPointSignatureIterator$SearchPair.class */
    private static class SearchPair {
        public ResolvedMember member;
        public ResolvedType type;

        public SearchPair(ResolvedMember member, ResolvedType type) {
            this.member = member;
            this.type = type;
        }
    }
}
