package org.aspectj.weaver.patterns;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.aspectj.bridge.AbortException;
import org.aspectj.bridge.IMessage;
import org.aspectj.bridge.ISourceLocation;
import org.aspectj.bridge.Message;
import org.aspectj.weaver.CompressingDataOutputStream;
import org.aspectj.weaver.ISourceContext;
import org.aspectj.weaver.ResolvedType;
import org.aspectj.weaver.UnresolvedType;
import org.aspectj.weaver.VersionedDataInputStream;
import org.aspectj.weaver.WeaverMessages;
import org.aspectj.weaver.World;
import org.aspectj.weaver.model.AsmRelationshipUtils;
import org.springframework.jdbc.datasource.init.ScriptUtils;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/patterns/DeclareParents.class */
public class DeclareParents extends Declare {
    protected TypePattern child;
    protected TypePatternList parents;
    private boolean isWildChild;
    protected boolean isExtends;

    public DeclareParents(TypePattern child, List parents, boolean isExtends) {
        this(child, new TypePatternList((List<TypePattern>) parents), isExtends);
    }

    protected DeclareParents(TypePattern child, TypePatternList parents, boolean isExtends) {
        this.isWildChild = false;
        this.isExtends = true;
        this.child = child;
        this.parents = parents;
        this.isExtends = isExtends;
        if (child instanceof WildTypePattern) {
            this.isWildChild = true;
        }
    }

    public boolean match(ResolvedType typeX) {
        if (!this.child.matchesStatically(typeX)) {
            return false;
        }
        if (typeX.getWorld().getLint().typeNotExposedToWeaver.isEnabled() && !typeX.isExposedToWeaver()) {
            typeX.getWorld().getLint().typeNotExposedToWeaver.signal(typeX.getName(), getSourceLocation());
            return true;
        }
        return true;
    }

    @Override // org.aspectj.weaver.patterns.PatternNode
    public Object accept(PatternNodeVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }

    @Override // org.aspectj.weaver.patterns.Declare
    public Declare parameterizeWith(Map typeVariableBindingMap, World w) {
        DeclareParents ret = new DeclareParents(this.child.parameterizeWith(typeVariableBindingMap, w), this.parents.parameterizeWith(typeVariableBindingMap, w), this.isExtends);
        ret.copyLocationFrom(this);
        return ret;
    }

    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("declare parents: ");
        buf.append(this.child);
        buf.append(this.isExtends ? " extends " : " implements ");
        buf.append(this.parents);
        buf.append(ScriptUtils.DEFAULT_STATEMENT_SEPARATOR);
        return buf.toString();
    }

    public boolean equals(Object other) {
        if (!(other instanceof DeclareParents)) {
            return false;
        }
        DeclareParents o = (DeclareParents) other;
        return o.child.equals(this.child) && o.parents.equals(this.parents);
    }

    public int hashCode() {
        int result = (37 * 23) + this.child.hashCode();
        return (37 * result) + this.parents.hashCode();
    }

    @Override // org.aspectj.weaver.patterns.PatternNode
    public void write(CompressingDataOutputStream s) throws IOException {
        s.writeByte(2);
        this.child.write(s);
        this.parents.write(s);
        writeLocation(s);
    }

    public static Declare read(VersionedDataInputStream s, ISourceContext context) throws IOException {
        DeclareParents ret = new DeclareParents(TypePattern.read(s, context), TypePatternList.read(s, context), true);
        ret.readLocation(context, s);
        return ret;
    }

    public boolean parentsIncludeInterface(World w) {
        for (int i = 0; i < this.parents.size(); i++) {
            if (this.parents.get(i).getExactType().resolve(w).isInterface()) {
                return true;
            }
        }
        return false;
    }

    public boolean parentsIncludeClass(World w) {
        for (int i = 0; i < this.parents.size(); i++) {
            if (this.parents.get(i).getExactType().resolve(w).isClass()) {
                return true;
            }
        }
        return false;
    }

    @Override // org.aspectj.weaver.patterns.Declare
    public void resolve(IScope scope) {
        this.child = this.child.resolveBindings(scope, Bindings.NONE, false, false);
        this.isWildChild = this.child instanceof WildTypePattern;
        this.parents = this.parents.resolveBindings(scope, Bindings.NONE, false, true);
    }

    public TypePatternList getParents() {
        return this.parents;
    }

    public TypePattern getChild() {
        return this.child;
    }

    public boolean isExtends() {
        return this.isExtends;
    }

    @Override // org.aspectj.weaver.patterns.Declare
    public boolean isAdviceLike() {
        return false;
    }

    private ResolvedType maybeGetNewParent(ResolvedType targetType, TypePattern typePattern, World world, boolean reportErrors) throws AbortException {
        if (typePattern == TypePattern.NO) {
            return null;
        }
        UnresolvedType iType = typePattern.getExactType();
        ResolvedType parentType = iType.resolve(world);
        if (targetType.equals(world.getCoreType(UnresolvedType.OBJECT))) {
            world.showMessage(IMessage.ERROR, WeaverMessages.format(WeaverMessages.DECP_OBJECT), getSourceLocation(), null);
            return null;
        }
        if (parentType.isParameterizedType() || parentType.isRawType()) {
            boolean isOK = verifyNoInheritedAlternateParameterization(targetType, parentType, world);
            if (!isOK) {
                return null;
            }
        }
        if (parentType.isAssignableFrom(targetType)) {
            return null;
        }
        if (reportErrors && this.isWildChild && targetType.isEnum()) {
            world.getLint().enumAsTargetForDecpIgnored.signal(targetType.toString(), getSourceLocation());
        }
        if (reportErrors && this.isWildChild && targetType.isAnnotation()) {
            world.getLint().annotationAsTargetForDecpIgnored.signal(targetType.toString(), getSourceLocation());
        }
        if (targetType.isEnum() && parentType.isInterface()) {
            if (reportErrors && !this.isWildChild) {
                world.showMessage(IMessage.ERROR, WeaverMessages.format(WeaverMessages.CANT_DECP_ON_ENUM_TO_IMPL_INTERFACE, targetType), getSourceLocation(), null);
                return null;
            }
            return null;
        }
        if (targetType.isAnnotation() && parentType.isInterface()) {
            if (reportErrors && !this.isWildChild) {
                world.showMessage(IMessage.ERROR, WeaverMessages.format(WeaverMessages.CANT_DECP_ON_ANNOTATION_TO_IMPL_INTERFACE, targetType), getSourceLocation(), null);
                return null;
            }
            return null;
        }
        if (targetType.isEnum() && parentType.isClass()) {
            if (reportErrors && !this.isWildChild) {
                world.showMessage(IMessage.ERROR, WeaverMessages.format(WeaverMessages.CANT_DECP_ON_ENUM_TO_EXTEND_CLASS, targetType), getSourceLocation(), null);
                return null;
            }
            return null;
        }
        if (targetType.isAnnotation() && parentType.isClass()) {
            if (reportErrors && !this.isWildChild) {
                world.showMessage(IMessage.ERROR, WeaverMessages.format(WeaverMessages.CANT_DECP_ON_ANNOTATION_TO_EXTEND_CLASS, targetType), getSourceLocation(), null);
                return null;
            }
            return null;
        }
        if (parentType.getSignature().equals(UnresolvedType.ENUM.getSignature())) {
            if (reportErrors && !this.isWildChild) {
                world.showMessage(IMessage.ERROR, WeaverMessages.format(WeaverMessages.CANT_DECP_TO_MAKE_ENUM_SUPERTYPE, targetType), getSourceLocation(), null);
                return null;
            }
            return null;
        }
        if (parentType.getSignature().equals(UnresolvedType.ANNOTATION.getSignature())) {
            if (reportErrors && !this.isWildChild) {
                world.showMessage(IMessage.ERROR, WeaverMessages.format(WeaverMessages.CANT_DECP_TO_MAKE_ANNOTATION_SUPERTYPE, targetType), getSourceLocation(), null);
                return null;
            }
            return null;
        }
        if (parentType.isAssignableFrom(targetType)) {
            return null;
        }
        if (targetType.isAssignableFrom(parentType)) {
            world.showMessage(IMessage.ERROR, WeaverMessages.format(WeaverMessages.CANT_EXTEND_SELF, targetType.getName()), getSourceLocation(), null);
            return null;
        }
        if (parentType.isClass()) {
            if (targetType.isInterface()) {
                world.showMessage(IMessage.ERROR, WeaverMessages.format(WeaverMessages.INTERFACE_CANT_EXTEND_CLASS), getSourceLocation(), null);
                return null;
            }
            if (!targetType.getSuperclass().isAssignableFrom(parentType)) {
                world.showMessage(IMessage.ERROR, WeaverMessages.format(WeaverMessages.DECP_HIERARCHY_ERROR, iType.getName(), targetType.getSuperclass().getName()), getSourceLocation(), null);
                return null;
            }
            return parentType;
        }
        return parentType;
    }

    private boolean verifyNoInheritedAlternateParameterization(ResolvedType typeToVerify, ResolvedType newParent, World world) throws AbortException {
        if (typeToVerify.equals(ResolvedType.OBJECT)) {
            return true;
        }
        ResolvedType newParentGenericType = newParent.getGenericType();
        Iterator<ResolvedType> iter = typeToVerify.getDirectSupertypes();
        while (iter.hasNext()) {
            ResolvedType supertype = iter.next();
            if (((supertype.isRawType() && newParent.isParameterizedType()) || (supertype.isParameterizedType() && newParent.isRawType())) && newParentGenericType.equals(supertype.getGenericType())) {
                world.getMessageHandler().handleMessage(new Message(WeaverMessages.format(WeaverMessages.CANT_DECP_MULTIPLE_PARAMETERIZATIONS, newParent.getName(), typeToVerify.getName(), supertype.getName()), getSourceLocation(), true, new ISourceLocation[]{typeToVerify.getSourceLocation()}));
                return false;
            }
            if (supertype.isParameterizedType()) {
                ResolvedType generictype = supertype.getGenericType();
                if (generictype.isAssignableFrom(newParentGenericType) && !supertype.isAssignableFrom(newParent)) {
                    world.getMessageHandler().handleMessage(new Message(WeaverMessages.format(WeaverMessages.CANT_DECP_MULTIPLE_PARAMETERIZATIONS, newParent.getName(), typeToVerify.getName(), supertype.getName()), getSourceLocation(), true, new ISourceLocation[]{typeToVerify.getSourceLocation()}));
                    return false;
                }
            }
            if (!verifyNoInheritedAlternateParameterization(supertype, newParent, world)) {
                return false;
            }
        }
        return true;
    }

    public List<ResolvedType> findMatchingNewParents(ResolvedType onType, boolean reportErrors) {
        if (onType.isRawType()) {
            onType = onType.getGenericType();
        }
        if (!match(onType)) {
            return Collections.emptyList();
        }
        List<ResolvedType> ret = new ArrayList<>();
        for (int i = 0; i < this.parents.size(); i++) {
            ResolvedType t = maybeGetNewParent(onType, this.parents.get(i), onType.getWorld(), reportErrors);
            if (t != null) {
                ret.add(t);
            }
        }
        return ret;
    }

    @Override // org.aspectj.weaver.patterns.Declare
    public String getNameSuffix() {
        return AsmRelationshipUtils.DECLARE_PARENTS;
    }

    public boolean isMixin() {
        return false;
    }
}
