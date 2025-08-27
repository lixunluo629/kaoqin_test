package org.apache.xmlbeans;

import java.math.BigDecimal;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/GDurationSpecification.class */
public interface GDurationSpecification {
    boolean isImmutable();

    int getSign();

    int getYear();

    int getMonth();

    int getDay();

    int getHour();

    int getMinute();

    int getSecond();

    BigDecimal getFraction();

    boolean isValid();

    int compareToGDuration(GDurationSpecification gDurationSpecification);
}
