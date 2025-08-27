package org.hyperic.sigar;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/SigarPermissionDeniedException.class */
public class SigarPermissionDeniedException extends SigarException {
    public static String getUserDeniedMessage(SigarProxy sigar, long pid) {
        String user = "unknown";
        String owner = "unknown";
        try {
            user = sigar.getProcCredName(sigar.getPid()).getUser();
        } catch (SigarException e) {
        }
        try {
            owner = sigar.getProcCredName(pid).getUser();
        } catch (SigarException e2) {
        }
        return new StringBuffer().append("User ").append(user).append(" denied access to process ").append(pid).append(" owned by ").append(owner).toString();
    }

    public SigarPermissionDeniedException(String s) {
        super(s);
    }

    public SigarPermissionDeniedException(SigarProxy sigar, long pid) {
        super(getUserDeniedMessage(sigar, pid));
    }
}
