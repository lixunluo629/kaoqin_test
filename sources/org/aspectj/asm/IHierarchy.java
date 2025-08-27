package org.aspectj.asm;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.aspectj.asm.IProgramElement;
import org.aspectj.asm.internal.ProgramElement;
import org.aspectj.bridge.ISourceLocation;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/asm/IHierarchy.class */
public interface IHierarchy extends Serializable {
    public static final IProgramElement NO_STRUCTURE = new ProgramElement(null, "<build to view structure>", IProgramElement.Kind.ERROR, null);

    IProgramElement getElement(String str);

    IProgramElement getRoot();

    void setRoot(IProgramElement iProgramElement);

    void addToFileMap(String str, IProgramElement iProgramElement);

    boolean removeFromFileMap(String str);

    void setFileMap(HashMap<String, IProgramElement> map);

    Object findInFileMap(Object obj);

    Set<Map.Entry<String, IProgramElement>> getFileMapEntrySet();

    boolean isValid();

    IProgramElement findElementForHandle(String str);

    IProgramElement findElementForHandleOrCreate(String str, boolean z);

    IProgramElement findElementForSignature(IProgramElement iProgramElement, IProgramElement.Kind kind, String str);

    IProgramElement findElementForLabel(IProgramElement iProgramElement, IProgramElement.Kind kind, String str);

    IProgramElement findElementForType(String str, String str2);

    IProgramElement findElementForSourceFile(String str);

    IProgramElement findElementForSourceLine(ISourceLocation iSourceLocation);

    IProgramElement findElementForSourceLine(String str, int i);

    IProgramElement findElementForOffSet(String str, int i, int i2);

    String getConfigFile();

    void setConfigFile(String str);

    void flushTypeMap();

    void flushHandleMap();

    void updateHandleMap(Set<String> set);

    IProgramElement findCloserMatchForLineNumber(IProgramElement iProgramElement, int i);

    IProgramElement findNodeForSourceFile(IProgramElement iProgramElement, String str);
}
