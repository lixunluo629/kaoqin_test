package com.adobe.xmp;

import java.util.Iterator;

/* loaded from: xmpcore-5.1.3.jar:com/adobe/xmp/XMPIterator.class */
public interface XMPIterator extends Iterator {
    void skipSubtree();

    void skipSiblings();
}
