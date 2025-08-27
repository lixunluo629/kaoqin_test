package org.hyperic.sigar.cmd;

import org.hyperic.sigar.Mem;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.Swap;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/cmd/Free.class */
public class Free extends SigarCommandBase {
    public Free(Shell shell) {
        super(shell);
    }

    public Free() {
    }

    @Override // org.hyperic.sigar.shell.ShellCommandBase, org.hyperic.sigar.shell.ShellCommandHandler
    public String getUsageShort() {
        return "Display information about free and used memory";
    }

    private static Long format(long value) {
        return new Long(value / 1024);
    }

    @Override // org.hyperic.sigar.cmd.SigarCommandBase
    public void output(String[] args) throws SigarException {
        Mem mem = this.sigar.getMem();
        Swap swap = this.sigar.getSwap();
        Object[] header = {"total", "used", "free"};
        Object[] memRow = {format(mem.getTotal()), format(mem.getUsed()), format(mem.getFree())};
        Object[] actualRow = {format(mem.getActualUsed()), format(mem.getActualFree())};
        Object[] swapRow = {format(swap.getTotal()), format(swap.getUsed()), format(swap.getFree())};
        printf("%18s %10s %10s", header);
        printf("Mem:    %10ld %10ld %10ld", memRow);
        if (mem.getUsed() != mem.getActualUsed() || mem.getFree() != mem.getActualFree()) {
            printf("-/+ buffers/cache: %10ld %10d", actualRow);
        }
        printf("Swap:   %10ld %10ld %10ld", swapRow);
        printf("RAM:    %10ls", new Object[]{new StringBuffer().append(mem.getRam()).append("MB").toString()});
    }

    public static void main(String[] args) throws Exception {
        new Free().processCommand(args);
    }
}
