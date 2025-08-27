package org.hyperic.sigar.cmd;

import org.hyperic.sigar.ProcExe;
import org.hyperic.sigar.ProcFd;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.SigarNotImplementedException;
import org.hyperic.sigar.SigarPermissionDeniedException;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/cmd/ProcFileInfo.class */
public class ProcFileInfo extends SigarCommandBase {
    public ProcFileInfo(Shell shell) {
        super(shell);
    }

    public ProcFileInfo() {
    }

    @Override // org.hyperic.sigar.cmd.SigarCommandBase
    protected boolean validateArgs(String[] args) {
        return true;
    }

    @Override // org.hyperic.sigar.shell.ShellCommandBase, org.hyperic.sigar.shell.ShellCommandHandler
    public String getUsageShort() {
        return "Display process file info";
    }

    @Override // org.hyperic.sigar.cmd.SigarCommandBase
    public boolean isPidCompleter() {
        return true;
    }

    @Override // org.hyperic.sigar.cmd.SigarCommandBase
    public void output(String[] args) throws SigarException {
        long[] pids = this.shell.findPids(args);
        for (int i = 0; i < pids.length; i++) {
            try {
                output(pids[i]);
            } catch (SigarPermissionDeniedException e) {
                println(this.shell.getUserDeniedMessage(pids[i]));
            } catch (SigarException e2) {
                println(new StringBuffer().append("(").append(e2.getMessage()).append(")").toString());
            }
            println("\n------------------------\n");
        }
    }

    public void output(long pid) throws SigarException {
        println(new StringBuffer().append("pid=").append(pid).toString());
        try {
            ProcFd fd = this.sigar.getProcFd(pid);
            println(new StringBuffer().append("open file descriptors=").append(fd.getTotal()).toString());
        } catch (SigarNotImplementedException e) {
        }
        ProcExe exe = this.sigar.getProcExe(pid);
        String name = exe.getName();
        if (name.length() == 0) {
            name = "unknown";
        }
        println(new StringBuffer().append("name=").append(name).toString());
        println(new StringBuffer().append("cwd=").append(exe.getCwd()).toString());
    }

    public static void main(String[] args) throws Exception {
        new ProcFileInfo().processCommand(args);
    }
}
