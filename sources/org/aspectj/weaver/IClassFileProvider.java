package org.aspectj.weaver;

import java.util.Iterator;
import org.aspectj.weaver.bcel.UnwovenClassFile;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/IClassFileProvider.class */
public interface IClassFileProvider {
    Iterator<UnwovenClassFile> getClassFileIterator();

    IWeaveRequestor getRequestor();

    boolean isApplyAtAspectJMungersOnly();
}
