package org.apache.poi.ss.usermodel;

/* loaded from: poi-3.17.jar:org/apache/poi/ss/usermodel/ConditionFilterData.class */
public interface ConditionFilterData {
    boolean getAboveAverage();

    boolean getBottom();

    boolean getEqualAverage();

    boolean getPercent();

    long getRank();

    int getStdDev();
}
