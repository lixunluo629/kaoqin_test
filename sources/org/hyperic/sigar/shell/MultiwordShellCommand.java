package org.hyperic.sigar.shell;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.hyperic.sigar.util.PrintfFormat;
import org.springframework.jdbc.datasource.init.ScriptUtils;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/shell/MultiwordShellCommand.class */
public class MultiwordShellCommand extends ShellCommandBase {
    private Map itsSubHandlerMap = new HashMap();

    public ShellCommandHandler getSubHandler(String subName) {
        return (ShellCommandHandler) this.itsSubHandlerMap.get(subName);
    }

    public Set getHandlerNames() {
        return this.itsSubHandlerMap.keySet();
    }

    public void registerSubHandler(String subName, ShellCommandHandler handler) throws ShellCommandInitException {
        if (!this.itsSubHandlerMap.containsValue(handler)) {
            handler.init(new StringBuffer().append(getCommandName()).append(SymbolConstants.SPACE_SYMBOL).append(subName).toString(), getShell());
        }
        this.itsSubHandlerMap.put(subName, handler);
    }

    @Override // org.hyperic.sigar.shell.ShellCommandBase, org.hyperic.sigar.shell.ShellCommandHandler
    public void processCommand(String[] args) throws ShellCommandExecException, ShellCommandUsageException {
        String cmdName = getCommandName();
        if (args.length < 1) {
            throw new ShellCommandUsageException(new StringBuffer().append(cmdName).append(" command ").append("requires an argument.").toString());
        }
        ShellCommandHandler handler = (ShellCommandHandler) this.itsSubHandlerMap.get(args[0].toLowerCase());
        if (handler == null) {
            throw new ShellCommandUsageException(new StringBuffer().append("don't know how to ").append(cmdName).append(SymbolConstants.SPACE_SYMBOL).append(args[0]).toString());
        }
        String[] subArgs = new String[args.length - 1];
        System.arraycopy(args, 1, subArgs, 0, subArgs.length);
        handler.processCommand(subArgs);
    }

    @Override // org.hyperic.sigar.shell.ShellCommandBase
    public String getSyntaxArgs() {
        StringBuffer res = new StringBuffer();
        res.append("<");
        Iterator i = getHandlerNames().iterator();
        while (i.hasNext()) {
            res.append((String) i.next());
            if (i.hasNext()) {
                res.append(" | ");
            }
        }
        res.append(">");
        return res.toString();
    }

    @Override // org.hyperic.sigar.shell.ShellCommandBase, org.hyperic.sigar.shell.ShellCommandHandler
    public String getUsageHelp(String[] args) {
        if (args.length == 0) {
            StringBuffer res = new StringBuffer();
            Object[] fArgs = new Object[2];
            res.append(new StringBuffer().append("    ").append(getUsageShort()).toString());
            res.append(".\n    For further help on each subcommand, ");
            res.append("type 'help ");
            res.append(new StringBuffer().append(getCommandName()).append(" <subcommand>'\n\n").toString());
            int maxLen = 0;
            for (String cmdName : getHandlerNames()) {
                if (cmdName.length() > maxLen) {
                    maxLen = cmdName.length();
                }
            }
            String fmtStr = new StringBuffer().append("      %-").append(maxLen + 1).append("s %s").toString();
            PrintfFormat fmt = new PrintfFormat(fmtStr);
            Iterator i = getHandlerNames().iterator();
            while (i.hasNext()) {
                String cmdName2 = (String) i.next();
                ShellCommandHandler sub = getSubHandler(cmdName2);
                fArgs[0] = new StringBuffer().append(cmdName2).append(":").toString();
                fArgs[1] = sub.getUsageShort();
                res.append(fmt.sprintf(fArgs));
                if (i.hasNext()) {
                    res.append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
                }
            }
            return res.toString();
        }
        ShellCommandHandler handler = getSubHandler(args[0].toLowerCase());
        if (handler == null) {
            return null;
        }
        String[] subArgs = new String[args.length - 1];
        System.arraycopy(args, 1, subArgs, 0, subArgs.length);
        return handler.getUsageHelp(subArgs);
    }
}
