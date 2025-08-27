package org.springframework.data.auditing;

import java.util.Calendar;
import java.util.GregorianCalendar;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/auditing/CurrentDateTimeProvider.class */
public enum CurrentDateTimeProvider implements DateTimeProvider {
    INSTANCE;

    @Override // org.springframework.data.auditing.DateTimeProvider
    public Calendar getNow() {
        return new GregorianCalendar();
    }
}
