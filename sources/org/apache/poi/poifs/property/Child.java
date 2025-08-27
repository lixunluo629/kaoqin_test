package org.apache.poi.poifs.property;

/* loaded from: poi-3.17.jar:org/apache/poi/poifs/property/Child.class */
public interface Child {
    Child getNextChild();

    Child getPreviousChild();

    void setNextChild(Child child);

    void setPreviousChild(Child child);
}
