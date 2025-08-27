package org.hyperic.sigar.cmd;

import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.SigarNotImplementedException;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/cmd/ShowArgs.class */
public class ShowArgs extends SigarCommandBase {
    public ShowArgs(Shell shell) {
        super(shell);
    }

    public ShowArgs() {
    }

    @Override // org.hyperic.sigar.cmd.SigarCommandBase
    protected boolean validateArgs(String[] args) {
        return true;
    }

    @Override // org.hyperic.sigar.shell.ShellCommandBase, org.hyperic.sigar.shell.ShellCommandHandler
    public String getUsageShort() {
        return "Show process command line arguments";
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
                println(new StringBuffer().append("pid=").append(pids[i]).toString());
                output(pids[i]);
            } catch (SigarException e) {
                println(e.getMessage());
            }
            println("\n------------------------\n");
        }
    }

    public void output(long pid) throws SigarException {
        String[] argv = this.proxy.getProcArgs(pid);
        try {
            String exe = this.proxy.getProcExe(pid).getName();
            println(new StringBuffer().append("exe=").append(exe).toString());
        } catch (SigarNotImplementedException e) {
        } catch (SigarException e2) {
            println("exe=???");
        }
        try {
            String cwd = this.proxy.getProcExe(pid).getCwd();
            println(new StringBuffer().append("cwd=").append(cwd).toString());
        } catch (SigarNotImplementedException e3) {
        } catch (SigarException e4) {
            println("cwd=???");
        }
        for (int i = 0; i < argv.length; i++) {
            println(new StringBuffer().append("   ").append(i).append("=>").append(argv[i]).append("<=").toString());
        }
    }

    public static void main(String[] args) throws Exception {
        new ShowArgs().processCommand(args);
    }
}
