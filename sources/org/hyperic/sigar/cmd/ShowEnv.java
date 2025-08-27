package org.hyperic.sigar.cmd;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.util.Map;
import org.hyperic.sigar.SigarException;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/cmd/ShowEnv.class */
public class ShowEnv extends SigarCommandBase {
    public ShowEnv(Shell shell) {
        super(shell);
    }

    public ShowEnv() {
    }

    @Override // org.hyperic.sigar.cmd.SigarCommandBase
    protected boolean validateArgs(String[] args) {
        return true;
    }

    @Override // org.hyperic.sigar.shell.ShellCommandBase, org.hyperic.sigar.shell.ShellCommandHandler
    public String getUsageShort() {
        return "Show process environment";
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
        Map env = this.proxy.getProcEnv(pid);
        for (Map.Entry ent : env.entrySet()) {
            println(new StringBuffer().append(ent.getKey()).append(SymbolConstants.EQUAL_SYMBOL).append(ent.getValue()).toString());
        }
    }

    public static void main(String[] args) throws Exception {
        new ShowEnv().processCommand(args);
    }
}
