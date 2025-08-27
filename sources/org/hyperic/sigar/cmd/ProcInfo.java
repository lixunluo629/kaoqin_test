package org.hyperic.sigar.cmd;

import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.SigarPermissionDeniedException;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/cmd/ProcInfo.class */
public class ProcInfo extends SigarCommandBase {
    private boolean isSingleProcess;

    public ProcInfo(Shell shell) {
        super(shell);
    }

    public ProcInfo() {
    }

    @Override // org.hyperic.sigar.cmd.SigarCommandBase
    protected boolean validateArgs(String[] args) {
        return true;
    }

    @Override // org.hyperic.sigar.shell.ShellCommandBase, org.hyperic.sigar.shell.ShellCommandHandler
    public String getUsageShort() {
        return "Display all process info";
    }

    @Override // org.hyperic.sigar.cmd.SigarCommandBase
    public boolean isPidCompleter() {
        return true;
    }

    @Override // org.hyperic.sigar.cmd.SigarCommandBase
    public void output(String[] args) throws SigarException {
        this.isSingleProcess = false;
        if (args.length != 0 && args[0].startsWith("-s")) {
            this.isSingleProcess = true;
        }
        if (this.isSingleProcess) {
            for (int i = 1; i < args.length; i++) {
                try {
                    output(args[i]);
                } catch (SigarException e) {
                    println(new StringBuffer().append("(").append(e.getMessage()).append(")").toString());
                }
                println("\n------------------------\n");
            }
            return;
        }
        long[] pids = this.shell.findPids(args);
        for (int i2 = 0; i2 < pids.length; i2++) {
            try {
                output(String.valueOf(pids[i2]));
            } catch (SigarPermissionDeniedException e2) {
                println(this.shell.getUserDeniedMessage(pids[i2]));
            } catch (SigarException e3) {
                println(new StringBuffer().append("(").append(e3.getMessage()).append(")").toString());
            }
            println("\n------------------------\n");
        }
    }

    public void output(String pid) throws SigarException {
        println(new StringBuffer().append("pid=").append(pid).toString());
        try {
            println(new StringBuffer().append("state=").append(this.sigar.getProcState(pid)).toString());
        } catch (SigarException e) {
            if (this.isSingleProcess) {
                println(e.getMessage());
            }
        }
        try {
            println(new StringBuffer().append("mem=").append(this.sigar.getProcMem(pid)).toString());
        } catch (SigarException e2) {
        }
        try {
            println(new StringBuffer().append("cpu=").append(this.sigar.getProcCpu(pid)).toString());
        } catch (SigarException e3) {
        }
        try {
            println(new StringBuffer().append("cred=").append(this.sigar.getProcCred(pid)).toString());
        } catch (SigarException e4) {
        }
        try {
            println(new StringBuffer().append("credname=").append(this.sigar.getProcCredName(pid)).toString());
        } catch (SigarException e5) {
        }
    }

    public static void main(String[] args) throws Exception {
        new ProcInfo().processCommand(args);
    }
}
