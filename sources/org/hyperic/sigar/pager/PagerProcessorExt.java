package org.hyperic.sigar.pager;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/pager/PagerProcessorExt.class */
public interface PagerProcessorExt extends PagerProcessor {
    PagerEventHandler getEventHandler();

    boolean skipNulls();

    Object processElement(Object obj, Object obj2);
}
