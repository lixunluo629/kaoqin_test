package org.hyperic.sigar;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/CurrentProcessSummary.class */
public class CurrentProcessSummary extends ProcStat {
    public static CurrentProcessSummary get(SigarProxy sigar) throws SigarException {
        CurrentProcessSummary stat = new CurrentProcessSummary();
        stat.gather(SigarProxyCache.getSigar(sigar));
        return stat;
    }
}
