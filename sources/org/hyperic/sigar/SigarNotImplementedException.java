package org.hyperic.sigar;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/SigarNotImplementedException.class */
public class SigarNotImplementedException extends SigarException {
    private static final String msg = "This method has not been implemented on this platform";
    public static final SigarNotImplementedException INSTANCE = new SigarNotImplementedException(msg);

    public SigarNotImplementedException() {
    }

    public SigarNotImplementedException(String s) {
        super(s);
    }
}
