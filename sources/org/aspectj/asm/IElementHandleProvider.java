package org.aspectj.asm;

import java.io.File;
import org.aspectj.bridge.ISourceLocation;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/asm/IElementHandleProvider.class */
public interface IElementHandleProvider {
    String createHandleIdentifier(ISourceLocation iSourceLocation);

    String createHandleIdentifier(File file, int i, int i2, int i3);

    String createHandleIdentifier(IProgramElement iProgramElement);

    String getFileForHandle(String str);

    int getLineNumberForHandle(String str);

    int getOffSetForHandle(String str);

    void initialize();
}
