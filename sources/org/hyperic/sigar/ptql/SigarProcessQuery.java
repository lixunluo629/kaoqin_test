package org.hyperic.sigar.ptql;

import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/ptql/SigarProcessQuery.class */
public class SigarProcessQuery implements ProcessQuery {
    int sigarWrapper = 0;
    long longSigarWrapper = 0;

    native void create(String str) throws MalformedQueryException;

    native void destroy();

    @Override // org.hyperic.sigar.ptql.ProcessQuery
    public native boolean match(Sigar sigar, long j) throws SigarException;

    @Override // org.hyperic.sigar.ptql.ProcessQuery
    public native long findProcess(Sigar sigar) throws SigarException;

    @Override // org.hyperic.sigar.ptql.ProcessQuery
    public native long[] find(Sigar sigar) throws SigarException;

    protected void finalize() {
        destroy();
    }

    static boolean re(String haystack, String needle) {
        if (haystack == null || needle == null) {
            return false;
        }
        return StringPattern.matches(haystack, needle);
    }
}
