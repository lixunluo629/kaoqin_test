package org.hyperic.sigar.ptql;

import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/ptql/ProcessQuery.class */
public interface ProcessQuery {
    boolean match(Sigar sigar, long j) throws SigarException;

    long findProcess(Sigar sigar) throws SigarException;

    long[] find(Sigar sigar) throws SigarException;
}
