package org.aspectj.weaver;

import java.io.IOException;
import java.util.List;
import org.aspectj.bridge.ISourceLocation;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/NewMemberClassTypeMunger.class */
public class NewMemberClassTypeMunger extends ResolvedTypeMunger {
    private UnresolvedType targetType;
    private String memberTypeName;
    private int version;

    public NewMemberClassTypeMunger(UnresolvedType targetType, String memberTypeName) {
        super(ResolvedTypeMunger.InnerClass, null);
        this.version = 1;
        this.targetType = targetType;
        this.memberTypeName = memberTypeName;
    }

    @Override // org.aspectj.weaver.ResolvedTypeMunger
    public void write(CompressingDataOutputStream stream) throws IOException {
        this.kind.write(stream);
        stream.writeInt(this.version);
        this.targetType.write(stream);
        stream.writeUTF(this.memberTypeName);
        writeSourceLocation(stream);
        writeOutTypeAliases(stream);
    }

    public static ResolvedTypeMunger readInnerClass(VersionedDataInputStream stream, ISourceContext context) throws IOException {
        stream.readInt();
        UnresolvedType targetType = UnresolvedType.read(stream);
        String memberTypeName = stream.readUTF();
        ISourceLocation sourceLocation = readSourceLocation(stream);
        List<String> typeVarAliases = readInTypeAliases(stream);
        NewMemberClassTypeMunger newInstance = new NewMemberClassTypeMunger(targetType, memberTypeName);
        newInstance.setTypeVariableAliases(typeVarAliases);
        newInstance.setSourceLocation(sourceLocation);
        return newInstance;
    }

    public UnresolvedType getTargetType() {
        return this.targetType;
    }

    @Override // org.aspectj.weaver.ResolvedTypeMunger
    public UnresolvedType getDeclaringType() {
        return this.targetType;
    }

    public String getMemberTypeName() {
        return this.memberTypeName;
    }

    public int hashCode() {
        int result = (37 * 17) + this.kind.hashCode();
        return (37 * ((37 * ((37 * result) + this.memberTypeName.hashCode())) + this.targetType.hashCode())) + (this.typeVariableAliases == null ? 0 : this.typeVariableAliases.hashCode());
    }

    public boolean equals(Object other) {
        if (!(other instanceof NewMemberClassTypeMunger)) {
            return false;
        }
        NewMemberClassTypeMunger o = (NewMemberClassTypeMunger) other;
        if (this.kind != null ? this.kind.equals(o.kind) : o.kind == null) {
            if (this.memberTypeName.equals(o.memberTypeName) && this.targetType.equals(o.targetType) && (this.typeVariableAliases != null ? this.typeVariableAliases.equals(o.typeVariableAliases) : o.typeVariableAliases == null)) {
                return true;
            }
        }
        return false;
    }
}
