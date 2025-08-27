package org.hyperic.sigar.cmd;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.hyperic.sigar.SigarException;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/cmd/Who.class */
public class Who extends SigarCommandBase {
    public Who(Shell shell) {
        super(shell);
    }

    public Who() {
    }

    @Override // org.hyperic.sigar.shell.ShellCommandBase, org.hyperic.sigar.shell.ShellCommandHandler
    public String getUsageShort() {
        return "Show who is logged on";
    }

    private String getTime(long time) {
        if (time == 0) {
            return "unknown";
        }
        return new SimpleDateFormat("MMM dd HH:mm").format(new Date(time));
    }

    @Override // org.hyperic.sigar.cmd.SigarCommandBase
    public void output(String[] args) throws SigarException {
        org.hyperic.sigar.Who[] who = this.sigar.getWhoList();
        for (int i = 0; i < who.length; i++) {
            String host = who[i].getHost();
            if (host.length() != 0) {
                host = new StringBuffer().append("(").append(host).append(")").toString();
            }
            printf(new String[]{who[i].getUser(), who[i].getDevice(), getTime(who[i].getTime() * 1000), host});
        }
    }

    public static void main(String[] args) throws Exception {
        new Who().processCommand(args);
    }
}
