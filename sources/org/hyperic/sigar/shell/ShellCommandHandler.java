package org.hyperic.sigar.shell;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/shell/ShellCommandHandler.class */
public interface ShellCommandHandler {
    void init(String str, ShellBase shellBase) throws ShellCommandInitException;

    void processCommand(String[] strArr) throws ShellCommandExecException, ShellCommandUsageException;

    String getUsageHelp(String[] strArr);

    String getUsageShort();

    String getSyntax();
}
