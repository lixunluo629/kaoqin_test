package org.hyperic.sigar.shell;

import java.io.File;
import java.io.IOException;
import org.hyperic.sigar.util.GetlineCompleter;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/shell/ShellCommand_source.class */
public class ShellCommand_source extends ShellCommandBase implements GetlineCompleter {
    @Override // org.hyperic.sigar.util.GetlineCompleter
    public String complete(String line) {
        return new FileCompleter(getShell()).complete(line);
    }

    @Override // org.hyperic.sigar.shell.ShellCommandBase, org.hyperic.sigar.shell.ShellCommandHandler
    public void processCommand(String[] args) throws ShellCommandExecException, ShellCommandUsageException {
        if (args.length != 1) {
            throw new ShellCommandUsageException(new StringBuffer().append("Syntax: ").append(getCommandName()).append(" <rcfile>").toString());
        }
        File rcFile = new File(FileCompleter.expand(args[0]));
        if (!rcFile.isFile()) {
            throw new ShellCommandExecException(new StringBuffer().append("File '").append(rcFile).append("' not found").toString());
        }
        try {
            getShell().readRCFile(rcFile, true);
        } catch (IOException exc) {
            throw new ShellCommandExecException(new StringBuffer().append("Error reading file '").append(rcFile).append(": ").append(exc.getMessage()).toString());
        }
    }

    @Override // org.hyperic.sigar.shell.ShellCommandBase
    public String getSyntaxArgs() {
        return "<rcfile>";
    }

    @Override // org.hyperic.sigar.shell.ShellCommandBase, org.hyperic.sigar.shell.ShellCommandHandler
    public String getUsageShort() {
        return "Read a file, executing the contents";
    }

    @Override // org.hyperic.sigar.shell.ShellCommandBase, org.hyperic.sigar.shell.ShellCommandHandler
    public String getUsageHelp(String[] args) {
        return new StringBuffer().append("    ").append(getUsageShort()).append(".  The file must contain ").append("commands\n    which are executable by the shell.").toString();
    }
}
