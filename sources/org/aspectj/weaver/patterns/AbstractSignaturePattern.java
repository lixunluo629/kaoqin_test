package org.aspectj.weaver.patterns;

import java.io.IOException;
import org.aspectj.weaver.BCException;
import org.aspectj.weaver.CompressingDataOutputStream;
import org.aspectj.weaver.ISourceContext;
import org.aspectj.weaver.VersionedDataInputStream;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/patterns/AbstractSignaturePattern.class */
public abstract class AbstractSignaturePattern implements ISignaturePattern {
    protected void writePlaceholderLocation(CompressingDataOutputStream s) throws IOException {
        s.writeInt(0);
        s.writeInt(0);
    }

    public static ISignaturePattern readCompoundSignaturePattern(VersionedDataInputStream s, ISourceContext context) throws IOException {
        byte key = s.readByte();
        switch (key) {
            case 1:
                return SignaturePattern.read(s, context);
            case 2:
                return NotSignaturePattern.readNotSignaturePattern(s, context);
            case 3:
                return OrSignaturePattern.readOrSignaturePattern(s, context);
            case 4:
                return AndSignaturePattern.readAndSignaturePattern(s, context);
            default:
                throw new BCException("unknown SignatureTypePattern kind: " + ((int) key));
        }
    }

    public static void writeCompoundSignaturePattern(CompressingDataOutputStream s, ISignaturePattern sigPattern) throws IOException {
        if (sigPattern instanceof SignaturePattern) {
            s.writeByte(1);
            ((SignaturePattern) sigPattern).write(s);
            return;
        }
        if (sigPattern instanceof AndSignaturePattern) {
            AndSignaturePattern andSignaturePattern = (AndSignaturePattern) sigPattern;
            s.writeByte(4);
            writeCompoundSignaturePattern(s, andSignaturePattern.getLeft());
            writeCompoundSignaturePattern(s, andSignaturePattern.getRight());
            s.writeInt(0);
            s.writeInt(0);
            return;
        }
        if (sigPattern instanceof OrSignaturePattern) {
            OrSignaturePattern orSignaturePattern = (OrSignaturePattern) sigPattern;
            s.writeByte(3);
            writeCompoundSignaturePattern(s, orSignaturePattern.getLeft());
            writeCompoundSignaturePattern(s, orSignaturePattern.getRight());
            s.writeInt(0);
            s.writeInt(0);
            return;
        }
        NotSignaturePattern notSignaturePattern = (NotSignaturePattern) sigPattern;
        s.writeByte(2);
        writeCompoundSignaturePattern(s, notSignaturePattern.getNegated());
        s.writeInt(0);
        s.writeInt(0);
    }
}
