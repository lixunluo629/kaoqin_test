package com.itextpdf.kernel.xmp;

import java.util.Iterator;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/xmp/XMPIterator.class */
public interface XMPIterator extends Iterator {
    void skipSubtree();

    void skipSiblings();
}
