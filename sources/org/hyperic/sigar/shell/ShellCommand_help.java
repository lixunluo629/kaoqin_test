package org.hyperic.sigar.shell;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import org.hyperic.sigar.util.PrintfFormat;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/shell/ShellCommand_help.class */
public class ShellCommand_help extends ShellCommandBase {
    @Override // org.hyperic.sigar.shell.ShellCommandBase, org.hyperic.sigar.shell.ShellCommandHandler
    public void processCommand(String[] args) throws ShellCommandExecException, ShellCommandUsageException {
        PrintStream out = getOutStream();
        if (args.length == 0) {
            PrintfFormat fmt = new PrintfFormat("\t%-14s - %s");
            Object[] fArgs = new Object[2];
            ArrayList cmdNamesList = new ArrayList();
            Iterator i = this.itsShell.getCommandNameIterator();
            while (i.hasNext()) {
                cmdNamesList.add(i.next());
            }
            String[] cmdNames = (String[]) cmdNamesList.toArray(new String[0]);
            Arrays.sort(cmdNames);
            out.println("Available commands:");
            for (int j = 0; j < cmdNames.length; j++) {
                ShellCommandHandler handler = this.itsShell.getHandler(cmdNames[j]);
                fArgs[0] = cmdNames[j];
                fArgs[1] = handler.getUsageShort();
                out.println(fmt.sprintf(fArgs));
            }
            return;
        }
        ShellCommandHandler handler2 = this.itsShell.getHandler(args[0]);
        ShellCommandHandler handler3 = handler2;
        ShellCommandHandler lastHandler = handler2;
        int useArgs = 1;
        int i2 = 1;
        while (true) {
            if (i2 >= args.length || args[i2].startsWith("-")) {
                break;
            }
            if (null == handler3) {
                handler3 = lastHandler;
                useArgs = i2 + 1;
                break;
            }
            if (handler3 instanceof MultiwordShellCommand) {
                MultiwordShellCommand mwsc = (MultiwordShellCommand) handler3;
                lastHandler = handler3;
                handler3 = mwsc.getSubHandler(args[i2]);
                useArgs = i2 + 1;
            }
            i2++;
        }
        if (handler3 == null) {
            out.print("Command '");
            for (int i3 = 0; i3 < args.length && !args[i3].startsWith("-"); i3++) {
                out.print(args[i3]);
                if (i3 < args.length - 1) {
                    out.print(' ');
                }
            }
            out.println("' not found.");
            return;
        }
        String[] otherArgs = new String[args.length - useArgs];
        System.arraycopy(args, useArgs, otherArgs, 0, otherArgs.length);
        out.println(handler3.getSyntax());
        out.println(handler3.getUsageHelp(otherArgs));
    }

    @Override // org.hyperic.sigar.shell.ShellCommandBase
    public String getSyntaxArgs() {
        return "<command name> [command arguments]";
    }

    @Override // org.hyperic.sigar.shell.ShellCommandBase, org.hyperic.sigar.shell.ShellCommandHandler
    public String getUsageShort() {
        return "Gives help on shell commands";
    }

    @Override // org.hyperic.sigar.shell.ShellCommandBase, org.hyperic.sigar.shell.ShellCommandHandler
    public String getUsageHelp(String[] args) {
        return "    Displays help about the given command name.  If the \n    command has arguments they may be entered for more specific\n    help";
    }
}
