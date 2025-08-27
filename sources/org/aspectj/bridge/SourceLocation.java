package org.aspectj.bridge;

import java.io.File;
import org.aspectj.util.LangUtil;
import org.hyperic.sigar.NetFlags;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/bridge/SourceLocation.class */
public class SourceLocation implements ISourceLocation {
    private static final long serialVersionUID = -5434765814401009794L;
    private transient int cachedHashcode;
    public static final ISourceLocation UNKNOWN = new SourceLocation(ISourceLocation.NO_FILE, 0, 0, 0);
    private final File sourceFile;
    private final int startLine;
    private final int column;
    private final int endLine;
    private int offset;
    private final String context;
    private boolean noColumn;
    private String sourceFileName;

    public static final void validLine(int line) {
        if (line < 0) {
            throw new IllegalArgumentException("negative line: " + line);
        }
        if (line > 1073741823) {
            throw new IllegalArgumentException("line too large: " + line);
        }
    }

    public static final void validColumn(int column) {
        if (column < 0) {
            throw new IllegalArgumentException("negative column: " + column);
        }
        if (column > 1073741823) {
            throw new IllegalArgumentException("column too large: " + column);
        }
    }

    public SourceLocation(File file, int line) {
        this(file, line, line, ISourceLocation.NO_COLUMN);
    }

    public SourceLocation(File file, int line, int endLine) {
        this(file, line, endLine, ISourceLocation.NO_COLUMN);
    }

    public SourceLocation(File file, int line, int endLine, int column) {
        this(file, line, endLine, column, (String) null);
    }

    public SourceLocation(File file, int line, int endLine, int column, String context) {
        this.cachedHashcode = -1;
        if (column == -2147483647) {
            column = 0;
            this.noColumn = true;
        }
        file = null == file ? ISourceLocation.NO_FILE : file;
        validLine(line);
        validLine(endLine);
        LangUtil.throwIaxIfFalse(line <= endLine, line + " > " + endLine);
        LangUtil.throwIaxIfFalse(column >= 0, "negative column: " + column);
        this.sourceFile = file;
        this.startLine = line;
        this.column = column;
        this.endLine = endLine;
        this.context = context;
    }

    public SourceLocation(File file, int line, int endLine, int column, String context, String sourceFileName) {
        this(file, line, endLine, column, context);
        this.sourceFileName = sourceFileName;
    }

    @Override // org.aspectj.bridge.ISourceLocation
    public File getSourceFile() {
        return this.sourceFile;
    }

    @Override // org.aspectj.bridge.ISourceLocation
    public int getLine() {
        return this.startLine;
    }

    @Override // org.aspectj.bridge.ISourceLocation
    public int getColumn() {
        return this.column;
    }

    @Override // org.aspectj.bridge.ISourceLocation
    public int getEndLine() {
        return this.endLine;
    }

    @Override // org.aspectj.bridge.ISourceLocation
    public String getContext() {
        return this.context;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        if (null != this.context) {
            sb.append(this.context);
            sb.append(LangUtil.EOL);
        }
        if (this.sourceFile != ISourceLocation.NO_FILE) {
            sb.append(this.sourceFile.getPath());
        }
        if (this.startLine > 0) {
            sb.append(":");
            sb.append(this.startLine);
        }
        if (!this.noColumn) {
            sb.append(":" + this.column);
        }
        if (this.offset >= 0) {
            sb.append(NetFlags.ANY_ADDR_V6 + this.offset);
        }
        return sb.toString();
    }

    @Override // org.aspectj.bridge.ISourceLocation
    public int getOffset() {
        return this.offset;
    }

    public void setOffset(int i) {
        this.cachedHashcode = -1;
        this.offset = i;
    }

    @Override // org.aspectj.bridge.ISourceLocation
    public String getSourceFileName() {
        return this.sourceFileName;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof SourceLocation)) {
            return false;
        }
        SourceLocation o = (SourceLocation) obj;
        return this.startLine == o.startLine && this.column == o.column && this.endLine == o.endLine && this.offset == o.offset && (this.sourceFile != null ? this.sourceFile.equals(o.sourceFile) : o.sourceFile == null) && (this.context != null ? this.context.equals(o.context) : o.context == null) && this.noColumn == o.noColumn && (this.sourceFileName != null ? this.sourceFileName.equals(o.sourceFileName) : o.sourceFileName == null);
    }

    public int hashCode() {
        if (this.cachedHashcode == -1) {
            this.cachedHashcode = this.sourceFile == null ? 0 : this.sourceFile.hashCode();
            this.cachedHashcode = (this.cachedHashcode * 37) + this.startLine;
            this.cachedHashcode = (this.cachedHashcode * 37) + this.column;
            this.cachedHashcode = (this.cachedHashcode * 37) + this.endLine;
            this.cachedHashcode = (this.cachedHashcode * 37) + this.offset;
            this.cachedHashcode = (this.cachedHashcode * 37) + (this.context == null ? 0 : this.context.hashCode());
            this.cachedHashcode = (this.cachedHashcode * 37) + (this.noColumn ? 0 : 1);
            this.cachedHashcode = (this.cachedHashcode * 37) + (this.sourceFileName == null ? 0 : this.sourceFileName.hashCode());
        }
        return this.cachedHashcode;
    }
}
