package org.aspectj.weaver.patterns;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.io.IOException;
import java.util.Map;
import org.aspectj.weaver.CompressingDataOutputStream;
import org.aspectj.weaver.ISourceContext;
import org.aspectj.weaver.VersionedDataInputStream;
import org.aspectj.weaver.World;
import org.aspectj.weaver.model.AsmRelationshipUtils;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/patterns/DeclareTypeErrorOrWarning.class */
public class DeclareTypeErrorOrWarning extends Declare {
    private boolean isError;
    private TypePattern typePattern;
    private String message;

    public DeclareTypeErrorOrWarning(boolean isError, TypePattern typePattern, String message) {
        this.isError = isError;
        this.typePattern = typePattern;
        this.message = message;
    }

    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("declare ");
        if (this.isError) {
            buf.append("error: ");
        } else {
            buf.append("warning: ");
        }
        buf.append(this.typePattern);
        buf.append(": ");
        buf.append(SymbolConstants.QUOTES_SYMBOL);
        buf.append(this.message);
        buf.append("\";");
        return buf.toString();
    }

    public boolean equals(Object other) {
        if (!(other instanceof DeclareTypeErrorOrWarning)) {
            return false;
        }
        DeclareTypeErrorOrWarning o = (DeclareTypeErrorOrWarning) other;
        return o.isError == this.isError && o.typePattern.equals(this.typePattern) && o.message.equals(this.message);
    }

    public int hashCode() {
        int result = this.isError ? 19 : 23;
        return (37 * ((37 * result) + this.typePattern.hashCode())) + this.message.hashCode();
    }

    @Override // org.aspectj.weaver.patterns.PatternNode
    public Object accept(PatternNodeVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }

    @Override // org.aspectj.weaver.patterns.PatternNode
    public void write(CompressingDataOutputStream s) throws IOException {
        s.writeByte(7);
        s.writeBoolean(this.isError);
        this.typePattern.write(s);
        s.writeUTF(this.message);
        writeLocation(s);
    }

    public static Declare read(VersionedDataInputStream s, ISourceContext context) throws IOException {
        Declare ret = new DeclareTypeErrorOrWarning(s.readBoolean(), TypePattern.read(s, context), s.readUTF());
        ret.readLocation(context, s);
        return ret;
    }

    public boolean isError() {
        return this.isError;
    }

    public String getMessage() {
        return this.message;
    }

    public TypePattern getTypePattern() {
        return this.typePattern;
    }

    @Override // org.aspectj.weaver.patterns.Declare
    public void resolve(IScope scope) {
        this.typePattern.resolve(scope.getWorld());
    }

    @Override // org.aspectj.weaver.patterns.Declare
    public Declare parameterizeWith(Map typeVariableBindingMap, World w) {
        Declare ret = new DeclareTypeErrorOrWarning(this.isError, this.typePattern.parameterizeWith(typeVariableBindingMap, w), this.message);
        ret.copyLocationFrom(this);
        return ret;
    }

    @Override // org.aspectj.weaver.patterns.Declare
    public boolean isAdviceLike() {
        return false;
    }

    @Override // org.aspectj.weaver.patterns.Declare
    public String getNameSuffix() {
        return "teow";
    }

    public String getName() {
        StringBuffer buf = new StringBuffer();
        buf.append("declare type ");
        if (this.isError) {
            buf.append(AsmRelationshipUtils.DECLARE_ERROR);
        } else {
            buf.append(AsmRelationshipUtils.DECLARE_WARNING);
        }
        return buf.toString();
    }
}
