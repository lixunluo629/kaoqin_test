package org.hyperic.sigar.cmd;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.util.List;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.SigarNotImplementedException;
import org.springframework.beans.PropertyAccessor;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/cmd/ProcModuleInfo.class */
public class ProcModuleInfo extends SigarCommandBase {
    public ProcModuleInfo(Shell shell) {
        super(shell);
    }

    public ProcModuleInfo() {
    }

    @Override // org.hyperic.sigar.cmd.SigarCommandBase
    protected boolean validateArgs(String[] args) {
        return true;
    }

    @Override // org.hyperic.sigar.shell.ShellCommandBase, org.hyperic.sigar.shell.ShellCommandHandler
    public String getUsageShort() {
        return "Display process module info";
    }

    @Override // org.hyperic.sigar.cmd.SigarCommandBase
    public boolean isPidCompleter() {
        return true;
    }

    @Override // org.hyperic.sigar.cmd.SigarCommandBase
    public void output(String[] args) throws SigarException {
        long[] pids = this.shell.findPids(args);
        for (long j : pids) {
            try {
                output(j);
            } catch (SigarException e) {
                println(new StringBuffer().append("(").append(e.getMessage()).append(")").toString());
            }
            println("\n------------------------\n");
        }
    }

    public void output(long pid) throws SigarException {
        println(new StringBuffer().append("pid=").append(pid).toString());
        try {
            List modules = this.sigar.getProcModules(pid);
            for (int i = 0; i < modules.size(); i++) {
                println(new StringBuffer().append(i).append(SymbolConstants.EQUAL_SYMBOL).append(modules.get(i)).toString());
            }
        } catch (SigarNotImplementedException e) {
            throw e;
        } catch (SigarException e2) {
            println(new StringBuffer().append(PropertyAccessor.PROPERTY_KEY_PREFIX).append(e2.getMessage()).append("]").toString());
        }
    }

    public static void main(String[] args) throws Exception {
        new ProcModuleInfo().processCommand(args);
    }
}
