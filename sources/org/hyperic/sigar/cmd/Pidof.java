package org.hyperic.sigar.cmd;

import org.hyperic.sigar.SigarException;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/cmd/Pidof.class */
public class Pidof extends SigarCommandBase {
    public Pidof(Shell shell) {
        super(shell);
    }

    public Pidof() {
    }

    @Override // org.hyperic.sigar.cmd.SigarCommandBase
    protected boolean validateArgs(String[] args) {
        return args.length > 0;
    }

    @Override // org.hyperic.sigar.shell.ShellCommandBase
    public String getSyntaxArgs() {
        return "query";
    }

    @Override // org.hyperic.sigar.shell.ShellCommandBase, org.hyperic.sigar.shell.ShellCommandHandler
    public String getUsageShort() {
        return "Find the process ID of a running program";
    }

    @Override // org.hyperic.sigar.cmd.SigarCommandBase
    public void output(String[] args) throws SigarException {
        long[] pids = this.shell.findPids(args);
        for (long j : pids) {
            this.out.print(j);
            this.out.print(' ');
        }
        this.out.println();
    }
}
