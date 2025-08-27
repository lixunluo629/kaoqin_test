package org.hyperic.sigar;

import org.hyperic.sigar.ptql.ProcessFinder;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/MultiProcMem.class */
public class MultiProcMem extends ProcMem {
    static ProcMem get(Sigar sigar, String query) throws SigarException {
        ProcMem mem = new ProcMem();
        mem.share = -1L;
        long[] pids = ProcessFinder.find(sigar, query);
        for (long j : pids) {
            try {
                ProcMem pmem = sigar.getProcMem(j);
                mem.size += pmem.size;
                mem.resident += pmem.resident;
                if (pmem.share != -1) {
                    mem.share += pmem.share;
                }
            } catch (SigarException e) {
            }
        }
        return mem;
    }
}
