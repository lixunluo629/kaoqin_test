package org.springframework.data.auditing;

import java.util.Calendar;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/auditing/DateTimeProvider.class */
public interface DateTimeProvider {
    Calendar getNow();
}
