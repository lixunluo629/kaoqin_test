package org.hyperic.sigar.ptql;

import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.SigarProxy;
import org.hyperic.sigar.SigarProxyCache;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/ptql/ProcessFinder.class */
public class ProcessFinder {
    private Sigar sigar;
    private ProcessQueryFactory qf;

    public ProcessFinder(SigarProxy proxy) {
        this(SigarProxyCache.getSigar(proxy));
    }

    public ProcessFinder(Sigar sigar) {
        this.sigar = sigar;
        this.qf = ProcessQueryFactory.getInstance();
    }

    public long findSingleProcess(String query) throws SigarException {
        return findSingleProcess(this.qf.getQuery(query));
    }

    public long findSingleProcess(ProcessQuery query) throws SigarException {
        return query.findProcess(this.sigar);
    }

    public static long[] find(Sigar sigar, String query) throws SigarException {
        return new ProcessFinder(sigar).find(query);
    }

    public static long[] find(SigarProxy sigar, String query) throws SigarException {
        return new ProcessFinder(sigar).find(query);
    }

    public long[] find(String query) throws SigarException {
        return find(this.qf.getQuery(query));
    }

    public long[] find(ProcessQuery query) throws SigarException {
        return query.find(this.sigar);
    }
}
