package org.jboss.logging;

import java.util.Locale;

/* loaded from: jboss-logging-3.3.2.Final.jar:org/jboss/logging/ParameterConverter.class */
public interface ParameterConverter<I> {
    Object convert(Locale locale, I i);
}
