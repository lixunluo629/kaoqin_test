package org.aspectj.weaver;

import java.io.IOException;
import org.springframework.jdbc.datasource.init.ScriptUtils;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/TypeVariable.class */
public class TypeVariable {
    public static final TypeVariable[] NONE = new TypeVariable[0];
    private String name;
    private int rank;
    private UnresolvedType firstbound;
    private UnresolvedType superclass;
    private UnresolvedType[] superInterfaces;
    public static final int UNKNOWN = -1;
    public static final int METHOD = 1;
    public static final int TYPE = 2;
    private int declaringElementKind;
    private TypeVariableDeclaringElement declaringElement;
    public boolean isResolved;
    private boolean beingResolved;

    public TypeVariable(String name) {
        this.superInterfaces = UnresolvedType.NONE;
        this.declaringElementKind = -1;
        this.isResolved = false;
        this.beingResolved = false;
        this.name = name;
    }

    public TypeVariable(String name, UnresolvedType anUpperBound) {
        this(name);
        this.superclass = anUpperBound;
    }

    public TypeVariable(String name, UnresolvedType anUpperBound, UnresolvedType[] superInterfaces) {
        this(name, anUpperBound);
        this.superInterfaces = superInterfaces;
    }

    public UnresolvedType getFirstBound() {
        if (this.firstbound != null) {
            return this.firstbound;
        }
        if (this.superclass == null || this.superclass.getSignature().equals("Ljava/lang/Object;")) {
            if (this.superInterfaces.length > 0) {
                this.firstbound = this.superInterfaces[0];
            } else {
                this.firstbound = UnresolvedType.OBJECT;
            }
        } else {
            this.firstbound = this.superclass;
        }
        return this.firstbound;
    }

    public UnresolvedType getUpperBound() {
        return this.superclass;
    }

    public UnresolvedType[] getSuperInterfaces() {
        return this.superInterfaces;
    }

    public String getName() {
        return this.name;
    }

    public TypeVariable resolve(World world) {
        if (this.isResolved) {
            return this;
        }
        if (this.beingResolved) {
            return this;
        }
        this.beingResolved = true;
        TypeVariable resolvedTVar = null;
        if (this.declaringElement != null) {
            if (this.declaringElementKind == 2) {
                UnresolvedType declaring = (UnresolvedType) this.declaringElement;
                ReferenceType rd = (ReferenceType) declaring.resolve(world);
                TypeVariable[] tVars = rd.getTypeVariables();
                int i = 0;
                while (true) {
                    if (i >= tVars.length) {
                        break;
                    }
                    if (!tVars[i].getName().equals(getName())) {
                        i++;
                    } else {
                        resolvedTVar = tVars[i];
                        break;
                    }
                }
            } else {
                ResolvedMember declaring2 = (ResolvedMember) this.declaringElement;
                TypeVariable[] tvrts = declaring2.getTypeVariables();
                for (int i2 = 0; i2 < tvrts.length; i2++) {
                    if (tvrts[i2].getName().equals(getName())) {
                        resolvedTVar = tvrts[i2];
                    }
                }
            }
            if (resolvedTVar == null) {
                throw new IllegalStateException();
            }
        } else {
            resolvedTVar = this;
        }
        this.superclass = resolvedTVar.superclass;
        this.superInterfaces = resolvedTVar.superInterfaces;
        if (this.superclass != null) {
            ResolvedType rt = this.superclass.resolve(world);
            this.superclass = rt;
        }
        this.firstbound = getFirstBound().resolve(world);
        for (int i3 = 0; i3 < this.superInterfaces.length; i3++) {
            this.superInterfaces[i3] = this.superInterfaces[i3].resolve(world);
        }
        this.isResolved = true;
        this.beingResolved = false;
        return this;
    }

    public boolean canBeBoundTo(ResolvedType candidate) {
        if (!this.isResolved) {
            throw new IllegalStateException("Can't answer binding questions prior to resolving");
        }
        if (candidate.isGenericWildcard()) {
            return true;
        }
        if (this.superclass != null && !isASubtypeOf(this.superclass, candidate)) {
            return false;
        }
        for (int i = 0; i < this.superInterfaces.length; i++) {
            if (!isASubtypeOf(this.superInterfaces[i], candidate)) {
                return false;
            }
        }
        return true;
    }

    private boolean isASubtypeOf(UnresolvedType candidateSuperType, UnresolvedType candidateSubType) {
        ResolvedType superType = (ResolvedType) candidateSuperType;
        ResolvedType subType = (ResolvedType) candidateSubType;
        return superType.isAssignableFrom(subType);
    }

    public void setUpperBound(UnresolvedType superclass) {
        this.firstbound = null;
        this.superclass = superclass;
    }

    public void setAdditionalInterfaceBounds(UnresolvedType[] superInterfaces) {
        this.firstbound = null;
        this.superInterfaces = superInterfaces;
    }

    public String toDebugString() {
        return getDisplayName();
    }

    public String getDisplayName() {
        StringBuffer ret = new StringBuffer();
        ret.append(this.name);
        if (!getFirstBound().getName().equals("java.lang.Object")) {
            ret.append(" extends ");
            ret.append(getFirstBound().getName());
            if (this.superInterfaces != null) {
                for (int i = 0; i < this.superInterfaces.length; i++) {
                    if (!getFirstBound().equals(this.superInterfaces[i])) {
                        ret.append(" & ");
                        ret.append(this.superInterfaces[i].getName());
                    }
                }
            }
        }
        return ret.toString();
    }

    public String toString() {
        return "TypeVar " + getDisplayName();
    }

    public String getSignature() {
        StringBuffer sb = new StringBuffer();
        sb.append(this.name);
        sb.append(":");
        if (this.superInterfaces.length == 0 || !this.superclass.getSignature().equals(UnresolvedType.OBJECT.getSignature())) {
            sb.append(this.superclass.getSignature());
        }
        if (this.superInterfaces.length != 0) {
            for (int i = 0; i < this.superInterfaces.length; i++) {
                sb.append(":");
                UnresolvedType iBound = this.superInterfaces[i];
                sb.append(iBound.getSignature());
            }
        }
        return sb.toString();
    }

    public String getSignatureForAttribute() {
        StringBuffer sb = new StringBuffer();
        sb.append(this.name);
        sb.append(":");
        if (this.superInterfaces.length == 0 || !this.superclass.getSignature().equals(UnresolvedType.OBJECT.getSignature())) {
            sb.append(((ReferenceType) this.superclass).getSignatureForAttribute());
        }
        if (this.superInterfaces.length != 0) {
            for (int i = 0; i < this.superInterfaces.length; i++) {
                sb.append(":");
                ResolvedType iBound = (ResolvedType) this.superInterfaces[i];
                sb.append(iBound.getSignatureForAttribute());
            }
        }
        return sb.toString();
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public int getRank() {
        return this.rank;
    }

    public void setDeclaringElement(TypeVariableDeclaringElement element) {
        this.declaringElement = element;
        if (element instanceof UnresolvedType) {
            this.declaringElementKind = 2;
        } else {
            this.declaringElementKind = 1;
        }
    }

    public TypeVariableDeclaringElement getDeclaringElement() {
        return this.declaringElement;
    }

    public void setDeclaringElementKind(int kind) {
        this.declaringElementKind = kind;
    }

    public int getDeclaringElementKind() {
        return this.declaringElementKind;
    }

    public void write(CompressingDataOutputStream s) throws IOException {
        s.writeUTF(this.name);
        this.superclass.write(s);
        if (this.superInterfaces.length == 0) {
            s.writeInt(0);
            return;
        }
        s.writeInt(this.superInterfaces.length);
        for (int i = 0; i < this.superInterfaces.length; i++) {
            UnresolvedType ibound = this.superInterfaces[i];
            ibound.write(s);
        }
    }

    public static TypeVariable read(VersionedDataInputStream s) throws IOException {
        String name = s.readUTF();
        UnresolvedType ubound = UnresolvedType.read(s);
        int iboundcount = s.readInt();
        UnresolvedType[] ibounds = UnresolvedType.NONE;
        if (iboundcount > 0) {
            ibounds = new UnresolvedType[iboundcount];
            for (int i = 0; i < iboundcount; i++) {
                ibounds[i] = UnresolvedType.read(s);
            }
        }
        TypeVariable newVariable = new TypeVariable(name, ubound, ibounds);
        return newVariable;
    }

    public String getGenericSignature() {
        return "T" + this.name + ScriptUtils.DEFAULT_STATEMENT_SEPARATOR;
    }

    public String getErasureSignature() {
        return getFirstBound().getErasureSignature();
    }

    public UnresolvedType getSuperclass() {
        return this.superclass;
    }

    public void setSuperclass(UnresolvedType superclass) {
        this.firstbound = null;
        this.superclass = superclass;
    }
}
