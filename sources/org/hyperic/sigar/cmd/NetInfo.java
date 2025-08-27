package org.hyperic.sigar.cmd;

import org.hyperic.sigar.NetInterfaceConfig;
import org.hyperic.sigar.SigarException;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/cmd/NetInfo.class */
public class NetInfo extends SigarCommandBase {
    public NetInfo(Shell shell) {
        super(shell);
    }

    public NetInfo() {
    }

    @Override // org.hyperic.sigar.shell.ShellCommandBase, org.hyperic.sigar.shell.ShellCommandHandler
    public String getUsageShort() {
        return "Display network info";
    }

    @Override // org.hyperic.sigar.cmd.SigarCommandBase
    public void output(String[] args) throws SigarException {
        NetInterfaceConfig config = this.sigar.getNetInterfaceConfig(null);
        println(new StringBuffer().append("primary interface.....").append(config.getName()).toString());
        println(new StringBuffer().append("primary ip address....").append(config.getAddress()).toString());
        println(new StringBuffer().append("primary mac address...").append(config.getHwaddr()).toString());
        println(new StringBuffer().append("primary netmask.......").append(config.getNetmask()).toString());
        org.hyperic.sigar.NetInfo info = this.sigar.getNetInfo();
        println(new StringBuffer().append("host name.............").append(info.getHostName()).toString());
        println(new StringBuffer().append("domain name...........").append(info.getDomainName()).toString());
        println(new StringBuffer().append("default gateway.......").append(info.getDefaultGateway()).toString());
        println(new StringBuffer().append("primary dns...........").append(info.getPrimaryDns()).toString());
        println(new StringBuffer().append("secondary dns.........").append(info.getSecondaryDns()).toString());
    }

    public static void main(String[] args) throws Exception {
        new NetInfo().processCommand(args);
    }
}
