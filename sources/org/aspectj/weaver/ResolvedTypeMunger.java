package org.aspectj.weaver;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.aspectj.bridge.ISourceLocation;
import org.aspectj.bridge.SourceLocation;
import org.aspectj.util.TypeSafeEnum;
import org.aspectj.weaver.MethodDelegateTypeMunger;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/ResolvedTypeMunger.class */
public abstract class ResolvedTypeMunger {
    protected Kind kind;
    protected ResolvedMember signature;
    protected ResolvedMember declaredSignature;
    protected List<String> typeVariableAliases;
    private ISourceLocation location;
    public static final Kind Field = new Kind("Field", 1);
    public static final Kind Method = new Kind("Method", 2);
    public static final Kind Constructor = new Kind("Constructor", 5);
    public static final Kind PerObjectInterface = new Kind("PerObjectInterface", 3);
    public static final Kind PrivilegedAccess = new Kind("PrivilegedAccess", 4);
    public static final Kind Parent = new Kind("Parent", 6);
    public static final Kind PerTypeWithinInterface = new Kind("PerTypeWithinInterface", 7);
    public static final Kind AnnotationOnType = new Kind("AnnotationOnType", 8);
    public static final Kind MethodDelegate = new Kind("MethodDelegate", 9);
    public static final Kind FieldHost = new Kind("FieldHost", 10);
    public static final Kind MethodDelegate2 = new Kind("MethodDelegate2", 11);
    public static final Kind InnerClass = new Kind("InnerClass", 12);
    public static final String SUPER_DISPATCH_NAME = "superDispatch";
    private Set<ResolvedMember> superMethodsCalled = Collections.emptySet();
    private ResolvedType onType = null;

    public abstract void write(CompressingDataOutputStream compressingDataOutputStream) throws IOException;

    public ResolvedTypeMunger(Kind kind, ResolvedMember signature) {
        this.kind = kind;
        this.signature = signature;
        UnresolvedType declaringType = signature != null ? signature.getDeclaringType() : null;
        if (declaringType != null) {
            if (declaringType.isRawType()) {
                throw new IllegalStateException("Use generic type, not raw type");
            }
            if (declaringType.isParameterizedType()) {
                throw new IllegalStateException("Use generic type, not parameterized type");
            }
        }
    }

    public void setSourceLocation(ISourceLocation isl) {
        this.location = isl;
    }

    public ISourceLocation getSourceLocation() {
        return this.location;
    }

    public boolean matches(ResolvedType matchType, ResolvedType aspectType) {
        if (this.onType == null) {
            this.onType = matchType.getWorld().resolve(getDeclaringType());
            if (this.onType.isRawType()) {
                this.onType = this.onType.getGenericType();
            }
        }
        if (matchType.equals(this.onType)) {
            if (!this.onType.isExposedToWeaver()) {
                boolean ok = this.onType.isInterface() && this.onType.lookupMemberWithSupersAndITDs(getSignature()) != null;
                if (!ok && this.onType.getWeaverState() == null && matchType.getWorld().getLint().typeNotExposedToWeaver.isEnabled()) {
                    matchType.getWorld().getLint().typeNotExposedToWeaver.signal(matchType.getName(), this.signature.getSourceLocation());
                    return true;
                }
                return true;
            }
            return true;
        }
        if (this.onType.isInterface()) {
            return matchType.isTopmostImplementor(this.onType);
        }
        return false;
    }

    public String toString() {
        return "ResolvedTypeMunger(" + getKind() + ", " + getSignature() + ")";
    }

    public static ResolvedTypeMunger read(VersionedDataInputStream s, ISourceContext context) throws IOException {
        Kind kind = Kind.read(s);
        if (kind == Field) {
            return NewFieldTypeMunger.readField(s, context);
        }
        if (kind == Method) {
            return NewMethodTypeMunger.readMethod(s, context);
        }
        if (kind == Constructor) {
            return NewConstructorTypeMunger.readConstructor(s, context);
        }
        if (kind == MethodDelegate) {
            return MethodDelegateTypeMunger.readMethod(s, context, false);
        }
        if (kind == FieldHost) {
            return MethodDelegateTypeMunger.FieldHostTypeMunger.readFieldHost(s, context);
        }
        if (kind == MethodDelegate2) {
            return MethodDelegateTypeMunger.readMethod(s, context, true);
        }
        if (kind == InnerClass) {
            return NewMemberClassTypeMunger.readInnerClass(s, context);
        }
        throw new RuntimeException("unimplemented");
    }

    protected static Set<ResolvedMember> readSuperMethodsCalled(VersionedDataInputStream s) throws IOException {
        int n;
        Set<ResolvedMember> ret = new HashSet<>();
        if (s.isAtLeast169()) {
            n = s.readByte();
        } else {
            n = s.readInt();
        }
        if (n < 0) {
            throw new BCException("Problem deserializing type munger");
        }
        for (int i = 0; i < n; i++) {
            ret.add(ResolvedMemberImpl.readResolvedMember(s, null));
        }
        return ret;
    }

    protected final void writeSuperMethodsCalled(CompressingDataOutputStream s) throws IOException {
        if (this.superMethodsCalled == null || this.superMethodsCalled.size() == 0) {
            s.writeByte(0);
            return;
        }
        List<ResolvedMember> ret = new ArrayList<>(this.superMethodsCalled);
        Collections.sort(ret);
        int n = ret.size();
        s.writeByte(n);
        for (ResolvedMember m : ret) {
            m.write(s);
        }
    }

    protected static ISourceLocation readSourceLocation(VersionedDataInputStream s) throws IOException {
        byte b;
        if (s.getMajorVersion() < 2) {
            return null;
        }
        SourceLocation ret = null;
        ObjectInputStream ois = null;
        try {
            try {
                if (!s.isAtLeast169() || (b = s.readByte()) == 0) {
                    ois = new ObjectInputStream(s);
                    boolean validLocation = ((Boolean) ois.readObject()).booleanValue();
                    if (validLocation) {
                        File f = (File) ois.readObject();
                        Integer ii = (Integer) ois.readObject();
                        Integer offset = (Integer) ois.readObject();
                        ret = new SourceLocation(f, ii.intValue());
                        ret.setOffset(offset.intValue());
                    }
                } else {
                    boolean validLocation2 = b == 2;
                    if (validLocation2) {
                        String path = s.readUtf8(s.readShort());
                        File f2 = new File(path);
                        ret = new SourceLocation(f2, s.readInt());
                        int offset2 = s.readInt();
                        ret.setOffset(offset2);
                    }
                }
                if (ois != null) {
                    ois.close();
                }
            } catch (EOFException e) {
                if (0 != 0) {
                    ois.close();
                }
                return null;
            } catch (IOException ioe) {
                ioe.printStackTrace();
                if (0 != 0) {
                    ois.close();
                }
                return null;
            } catch (ClassNotFoundException e2) {
                if (0 != 0) {
                    ois.close();
                }
            }
            return ret;
        } catch (Throwable th) {
            if (0 != 0) {
                ois.close();
            }
            throw th;
        }
    }

    protected final void writeSourceLocation(CompressingDataOutputStream s) throws IOException {
        if (s.canCompress()) {
            s.writeByte(1 + (this.location == null ? 0 : 1));
            if (this.location != null) {
                s.writeCompressedPath(this.location.getSourceFile().getPath());
                s.writeInt(this.location.getLine());
                s.writeInt(this.location.getOffset());
                return;
            }
            return;
        }
        s.writeByte(0);
        ObjectOutputStream oos = new ObjectOutputStream(s);
        oos.writeObject(new Boolean(this.location != null));
        if (this.location != null) {
            oos.writeObject(this.location.getSourceFile());
            oos.writeObject(new Integer(this.location.getLine()));
            oos.writeObject(new Integer(this.location.getOffset()));
        }
        oos.flush();
        oos.close();
    }

    public Kind getKind() {
        return this.kind;
    }

    /* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/ResolvedTypeMunger$Kind.class */
    public static class Kind extends TypeSafeEnum {
        Kind(String name, int key) {
            super(name, key);
        }

        public static Kind read(DataInputStream s) throws IOException {
            int key = s.readByte();
            switch (key) {
                case 1:
                    return ResolvedTypeMunger.Field;
                case 2:
                    return ResolvedTypeMunger.Method;
                case 3:
                case 4:
                case 6:
                case 7:
                case 8:
                default:
                    throw new BCException("bad kind: " + key);
                case 5:
                    return ResolvedTypeMunger.Constructor;
                case 9:
                    return ResolvedTypeMunger.MethodDelegate;
                case 10:
                    return ResolvedTypeMunger.FieldHost;
                case 11:
                    return ResolvedTypeMunger.MethodDelegate2;
                case 12:
                    return ResolvedTypeMunger.InnerClass;
            }
        }

        @Override // org.aspectj.util.TypeSafeEnum
        public String toString() {
            if (getName().startsWith(ResolvedTypeMunger.MethodDelegate.getName())) {
                return ResolvedTypeMunger.Method.toString();
            }
            return super.toString();
        }
    }

    public void setSuperMethodsCalled(Set<ResolvedMember> c) {
        this.superMethodsCalled = c;
    }

    public Set<ResolvedMember> getSuperMethodsCalled() {
        return this.superMethodsCalled;
    }

    public ResolvedMember getSignature() {
        return this.signature;
    }

    public ResolvedMember getMatchingSyntheticMember(Member member, ResolvedType aspectType) {
        if (getSignature() != null && getSignature().isPublic() && member.equals(getSignature())) {
            return getSignature();
        }
        return null;
    }

    public boolean changesPublicSignature() {
        return this.kind == Field || this.kind == Method || this.kind == Constructor;
    }

    public boolean needsAccessToTopmostImplementor() {
        if (this.kind == Field) {
            return true;
        }
        return this.kind == Method && !this.signature.isAbstract();
    }

    protected static List<String> readInTypeAliases(VersionedDataInputStream s) throws IOException {
        int count;
        if (s.getMajorVersion() >= 2) {
            if (s.isAtLeast169()) {
                count = s.readByte();
            } else {
                count = s.readInt();
            }
            if (count != 0) {
                List<String> aliases = new ArrayList<>();
                for (int i = 0; i < count; i++) {
                    aliases.add(s.readUTF());
                }
                return aliases;
            }
            return null;
        }
        return null;
    }

    protected final void writeOutTypeAliases(DataOutputStream s) throws IOException {
        if (this.typeVariableAliases == null || this.typeVariableAliases.size() == 0) {
            s.writeByte(0);
            return;
        }
        s.writeByte(this.typeVariableAliases.size());
        for (String element : this.typeVariableAliases) {
            s.writeUTF(element);
        }
    }

    public List<String> getTypeVariableAliases() {
        return this.typeVariableAliases;
    }

    protected void setTypeVariableAliases(List<String> typeVariableAliases) {
        this.typeVariableAliases = typeVariableAliases;
    }

    public boolean hasTypeVariableAliases() {
        return this.typeVariableAliases != null && this.typeVariableAliases.size() > 0;
    }

    public boolean sharesTypeVariablesWithGenericType() {
        return this.typeVariableAliases != null && this.typeVariableAliases.size() > 0;
    }

    public ResolvedTypeMunger parameterizedFor(ResolvedType target) {
        throw new BCException("Dont call parameterizedFor on a type munger of this kind: " + getClass());
    }

    public void setDeclaredSignature(ResolvedMember rm) {
        this.declaredSignature = rm;
    }

    public ResolvedMember getDeclaredSignature() {
        return this.declaredSignature;
    }

    public boolean isLateMunger() {
        return false;
    }

    public boolean existsToSupportShadowMunging() {
        return false;
    }

    public ResolvedTypeMunger parameterizeWith(Map<String, UnresolvedType> m, World w) {
        throw new BCException("Dont call parameterizeWith() on a type munger of this kind: " + getClass());
    }

    public UnresolvedType getDeclaringType() {
        return getSignature().getDeclaringType();
    }
}
