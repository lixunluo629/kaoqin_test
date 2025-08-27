package org.apache.commons.lang;

/* loaded from: commons-lang-2.6.jar:org/apache/commons/lang/NullArgumentException.class */
public class NullArgumentException extends IllegalArgumentException {
    private static final long serialVersionUID = 1174360235354917591L;

    public NullArgumentException(String argName) {
        super(new StringBuffer().append(argName == null ? "Argument" : argName).append(" must not be null.").toString());
    }
}
