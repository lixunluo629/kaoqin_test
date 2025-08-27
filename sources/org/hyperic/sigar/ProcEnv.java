package org.hyperic.sigar;

import java.util.Map;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/ProcEnv.class */
class ProcEnv {
    public static native Map getAll(Sigar sigar, long j) throws SigarException;

    public static native String getValue(Sigar sigar, long j, String str) throws SigarException;

    private ProcEnv() {
    }
}
