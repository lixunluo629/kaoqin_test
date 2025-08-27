package org.aspectj.weaver.patterns;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import org.aspectj.bridge.ISourceLocation;
import org.aspectj.weaver.CompressingDataOutputStream;
import org.aspectj.weaver.IHasSourceLocation;
import org.aspectj.weaver.ISourceContext;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/patterns/PatternNode.class */
public abstract class PatternNode implements IHasSourceLocation {
    protected ISourceContext sourceContext;
    protected int end = -1;
    protected int start = -1;

    public abstract void write(CompressingDataOutputStream compressingDataOutputStream) throws IOException;

    public abstract Object accept(PatternNodeVisitor patternNodeVisitor, Object obj);

    @Override // org.aspectj.weaver.IHasPosition
    public int getStart() {
        return this.start + (this.sourceContext != null ? this.sourceContext.getOffset() : 0);
    }

    @Override // org.aspectj.weaver.IHasPosition
    public int getEnd() {
        return this.end + (this.sourceContext != null ? this.sourceContext.getOffset() : 0);
    }

    @Override // org.aspectj.weaver.IHasSourceLocation
    public ISourceContext getSourceContext() {
        return this.sourceContext;
    }

    public String getFileName() {
        return "unknown";
    }

    public void setLocation(ISourceContext sourceContext, int start, int end) {
        this.sourceContext = sourceContext;
        this.start = start;
        this.end = end;
    }

    public void copyLocationFrom(PatternNode other) {
        this.start = other.start;
        this.end = other.end;
        this.sourceContext = other.sourceContext;
    }

    @Override // org.aspectj.weaver.IHasSourceLocation
    public ISourceLocation getSourceLocation() {
        if (this.sourceContext == null) {
            return null;
        }
        return this.sourceContext.makeSourceLocation(this);
    }

    public void writeLocation(DataOutputStream s) throws IOException {
        s.writeInt(this.start);
        s.writeInt(this.end);
    }

    public void readLocation(ISourceContext context, DataInputStream s) throws IOException {
        this.start = s.readInt();
        this.end = s.readInt();
        this.sourceContext = context;
    }

    public Object traverse(PatternNodeVisitor visitor, Object data) {
        return accept(visitor, data);
    }
}
