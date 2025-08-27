package com.google.common.net;

import com.google.common.annotations.Beta;
import com.google.common.base.Preconditions;
import java.net.InetAddress;
import java.text.ParseException;
import javax.annotation.Nullable;

@Beta
/* loaded from: guava-18.0.jar:com/google/common/net/HostSpecifier.class */
public final class HostSpecifier {
    private final String canonicalForm;

    private HostSpecifier(String canonicalForm) {
        this.canonicalForm = canonicalForm;
    }

    public static HostSpecifier fromValid(String specifier) throws NumberFormatException {
        String strConcat;
        HostAndPort parsedHost = HostAndPort.fromString(specifier);
        Preconditions.checkArgument(!parsedHost.hasPort());
        String host = parsedHost.getHostText();
        InetAddress addr = null;
        try {
            addr = InetAddresses.forString(host);
        } catch (IllegalArgumentException e) {
        }
        if (addr != null) {
            return new HostSpecifier(InetAddresses.toUriString(addr));
        }
        InternetDomainName domain = InternetDomainName.from(host);
        if (domain.hasPublicSuffix()) {
            return new HostSpecifier(domain.toString());
        }
        String strValueOf = String.valueOf(host);
        if (strValueOf.length() != 0) {
            strConcat = "Domain name does not have a recognized public suffix: ".concat(strValueOf);
        } else {
            strConcat = str;
            String str = new String("Domain name does not have a recognized public suffix: ");
        }
        throw new IllegalArgumentException(strConcat);
    }

    public static HostSpecifier from(String specifier) throws ParseException {
        String strConcat;
        try {
            return fromValid(specifier);
        } catch (IllegalArgumentException e) {
            String strValueOf = String.valueOf(specifier);
            if (strValueOf.length() != 0) {
                strConcat = "Invalid host specifier: ".concat(strValueOf);
            } else {
                strConcat = str;
                String str = new String("Invalid host specifier: ");
            }
            ParseException parseException = new ParseException(strConcat, 0);
            parseException.initCause(e);
            throw parseException;
        }
    }

    public static boolean isValid(String specifier) {
        try {
            fromValid(specifier);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public boolean equals(@Nullable Object other) {
        if (this == other) {
            return true;
        }
        if (other instanceof HostSpecifier) {
            HostSpecifier that = (HostSpecifier) other;
            return this.canonicalForm.equals(that.canonicalForm);
        }
        return false;
    }

    public int hashCode() {
        return this.canonicalForm.hashCode();
    }

    public String toString() {
        return this.canonicalForm;
    }
}
