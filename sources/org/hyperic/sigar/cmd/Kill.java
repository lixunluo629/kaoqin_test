package org.hyperic.sigar.cmd;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import org.hyperic.sigar.SigarException;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/cmd/Kill.class */
public class Kill extends SigarCommandBase {
    public Kill(Shell shell) {
        super(shell);
    }

    public Kill() {
    }

    @Override // org.hyperic.sigar.cmd.SigarCommandBase
    protected boolean validateArgs(String[] args) {
        return args.length == 1 || args.length == 2;
    }

    @Override // org.hyperic.sigar.shell.ShellCommandBase
    public String getSyntaxArgs() {
        return "[signal] <query|pid>";
    }

    @Override // org.hyperic.sigar.shell.ShellCommandBase, org.hyperic.sigar.shell.ShellCommandHandler
    public String getUsageShort() {
        return "Send signal to a process";
    }

    @Override // org.hyperic.sigar.cmd.SigarCommandBase
    public boolean isPidCompleter() {
        return true;
    }

    @Override // org.hyperic.sigar.cmd.SigarCommandBase
    public void output(String[] args) throws SigarException, NumberFormatException {
        String query;
        String signal = "SIGTERM";
        if (args.length == 2) {
            signal = args[0];
            query = args[1];
        } else {
            query = args[0];
        }
        long[] pids = this.shell.findPids(new String[]{query});
        for (int i = 0; i < pids.length; i++) {
            println(new StringBuffer().append("kill ").append(signal).append(SymbolConstants.SPACE_SYMBOL).append(pids[i]).toString());
            this.sigar.kill(pids[i], signal);
        }
    }

    public static void main(String[] args) throws Exception {
        new Kill().processCommand(args);
    }
}
