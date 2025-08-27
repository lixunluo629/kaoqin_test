package org.hyperic.sigar.shell;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/shell/ShellCommand_sleep.class */
public class ShellCommand_sleep extends ShellCommandBase {
    @Override // org.hyperic.sigar.shell.ShellCommandBase, org.hyperic.sigar.shell.ShellCommandHandler
    public void processCommand(String[] args) throws InterruptedException, ShellCommandExecException, ShellCommandUsageException {
        if (args.length != 1) {
            throw new ShellCommandUsageException(getSyntax());
        }
        try {
            Thread.sleep(Integer.parseInt(args[0]) * 1000);
        } catch (InterruptedException e) {
            throw new ShellCommandExecException("Sleep interrupted");
        } catch (NumberFormatException e2) {
            throw new ShellCommandExecException(new StringBuffer().append("Invalid time '").append(args[0]).append("' -- must be an integer").toString());
        }
    }

    @Override // org.hyperic.sigar.shell.ShellCommandBase
    public String getSyntaxArgs() {
        return "<numSeconds>";
    }

    @Override // org.hyperic.sigar.shell.ShellCommandBase, org.hyperic.sigar.shell.ShellCommandHandler
    public String getUsageShort() {
        return "Delay execution for the a number of seconds ";
    }

    @Override // org.hyperic.sigar.shell.ShellCommandBase, org.hyperic.sigar.shell.ShellCommandHandler
    public String getUsageHelp(String[] args) {
        return new StringBuffer().append("    ").append(getUsageShort()).append(".").toString();
    }
}
