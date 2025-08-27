package org.hyperic.sigar.cmd;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import org.apache.poi.ddf.EscherProperties;
import org.hyperic.sigar.NetConnection;
import org.hyperic.sigar.NetFlags;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.Tcp;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/cmd/Netstat.class */
public class Netstat extends SigarCommandBase {
    private static final int LADDR_LEN = 20;
    private static final int RADDR_LEN = 35;
    private static final String[] HEADER = {"Proto", "Local Address", "Foreign Address", "State", ""};
    private static boolean isNumeric;
    private static boolean wantPid;
    private static boolean isStat;

    public Netstat(Shell shell) {
        super(shell);
    }

    public Netstat() {
    }

    @Override // org.hyperic.sigar.cmd.SigarCommandBase
    protected boolean validateArgs(String[] args) {
        return true;
    }

    @Override // org.hyperic.sigar.shell.ShellCommandBase, org.hyperic.sigar.shell.ShellCommandHandler
    public String getUsageShort() {
        return "Display network connections";
    }

    public static int getFlags(String[] args, int flags) {
        int proto_flags = 0;
        isNumeric = false;
        wantPid = false;
        isStat = false;
        for (String arg : args) {
            int j = 0;
            while (j < arg.length()) {
                int i = j;
                j++;
                switch (arg.charAt(i)) {
                    case '-':
                        break;
                    case 'a':
                        flags |= 3;
                        break;
                    case 'l':
                        flags = (flags & (-2)) | 2;
                        break;
                    case 'n':
                        isNumeric = true;
                        break;
                    case 'p':
                        wantPid = true;
                        break;
                    case 's':
                        isStat = true;
                        break;
                    case 't':
                        proto_flags |= 16;
                        break;
                    case 'u':
                        proto_flags |= 32;
                        break;
                    case 'w':
                        proto_flags |= 64;
                        break;
                    case 'x':
                        proto_flags |= 128;
                        break;
                    default:
                        System.err.println("unknown option");
                        break;
                }
            }
        }
        if (proto_flags != 0) {
            flags = (flags & (-241)) | proto_flags;
        }
        return flags;
    }

    private String formatPort(int proto, long port) {
        String service;
        if (port == 0) {
            return "*";
        }
        if (!isNumeric && (service = this.sigar.getNetServicesName(proto, port)) != null) {
            return service;
        }
        return String.valueOf(port);
    }

    private String formatAddress(int proto, String ip, long portnum, int max) {
        String address;
        String port = formatPort(proto, portnum);
        if (NetFlags.isAnyAddress(ip)) {
            address = "*";
        } else if (isNumeric) {
            address = ip;
        } else {
            try {
                address = InetAddress.getByName(ip).getHostName();
            } catch (UnknownHostException e) {
                address = ip;
            }
        }
        int max2 = max - (port.length() + 1);
        if (address.length() > max2) {
            address = address.substring(0, max2);
        }
        return new StringBuffer().append(address).append(":").append(port).toString();
    }

    private void outputTcpStats() throws SigarException {
        Tcp stat = this.sigar.getTcp();
        println(new StringBuffer().append("    ").append(stat.getActiveOpens()).append(" active connections openings").toString());
        println(new StringBuffer().append("    ").append(stat.getPassiveOpens()).append(" passive connection openings").toString());
        println(new StringBuffer().append("    ").append(stat.getAttemptFails()).append(" failed connection attempts").toString());
        println(new StringBuffer().append("    ").append(stat.getEstabResets()).append(" connection resets received").toString());
        println(new StringBuffer().append("    ").append(stat.getCurrEstab()).append(" connections established").toString());
        println(new StringBuffer().append("    ").append(stat.getInSegs()).append(" segments received").toString());
        println(new StringBuffer().append("    ").append(stat.getOutSegs()).append(" segments send out").toString());
        println(new StringBuffer().append("    ").append(stat.getRetransSegs()).append(" segments retransmited").toString());
        println(new StringBuffer().append("    ").append(stat.getInErrs()).append(" bad segments received.").toString());
        println(new StringBuffer().append("    ").append(stat.getOutRsts()).append(" resets sent").toString());
    }

    private void outputStats(int flags) throws SigarException {
        if ((flags & 16) != 0) {
            println("Tcp:");
            try {
                outputTcpStats();
            } catch (SigarException e) {
                println(new StringBuffer().append("    ").append(e).toString());
            }
        }
    }

    @Override // org.hyperic.sigar.cmd.SigarCommandBase
    public void output(String[] args) throws SigarException {
        String state;
        int flags = 241;
        if (args.length > 0) {
            flags = getFlags(args, EscherProperties.GEOTEXT__HASTEXTEFFECT);
            if (isStat) {
                outputStats(flags);
                return;
            }
        }
        NetConnection[] connections = this.sigar.getNetConnectionList(flags);
        printf(HEADER);
        for (NetConnection conn : connections) {
            String proto = conn.getTypeString();
            if (conn.getType() == 32) {
                state = "";
            } else {
                state = conn.getStateString();
            }
            ArrayList items = new ArrayList();
            items.add(proto);
            items.add(formatAddress(conn.getType(), conn.getLocalAddress(), conn.getLocalPort(), 20));
            items.add(formatAddress(conn.getType(), conn.getRemoteAddress(), conn.getRemotePort(), 35));
            items.add(state);
            String process = null;
            if (wantPid && conn.getState() == 10) {
                try {
                    long pid = this.sigar.getProcPort(conn.getType(), conn.getLocalPort());
                    if (pid != 0) {
                        String name = this.sigar.getProcState(pid).getName();
                        process = new StringBuffer().append(pid).append("/").append(name).toString();
                    }
                } catch (SigarException e) {
                }
            }
            if (process == null) {
                process = "";
            }
            items.add(process);
            printf(items);
        }
    }

    public static void main(String[] args) throws Exception {
        new Netstat().processCommand(args);
    }
}
