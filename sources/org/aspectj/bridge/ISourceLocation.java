package org.aspectj.bridge;

import java.io.File;
import java.io.Serializable;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/bridge/ISourceLocation.class */
public interface ISourceLocation extends Serializable {
    public static final int MAX_LINE = 1073741823;
    public static final int MAX_COLUMN = 1073741823;
    public static final int NO_COLUMN = -2147483647;
    public static final File NO_FILE = new File("ISourceLocation.NO_FILE");
    public static final ISourceLocation EMPTY = new SourceLocation(NO_FILE, 0, 0, 0);

    File getSourceFile();

    int getLine();

    int getColumn();

    int getOffset();

    int getEndLine();

    String getContext();

    String getSourceFileName();
}
