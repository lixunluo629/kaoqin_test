package io.netty.util;

import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.SocketUtils;
import io.netty.util.internal.StringUtil;
import io.netty.util.internal.SystemPropertyUtil;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import org.hyperic.sigar.NetFlags;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/util/NetUtil.class */
public final class NetUtil {
    public static final Inet4Address LOCALHOST4;
    public static final Inet6Address LOCALHOST6;
    public static final InetAddress LOCALHOST;
    public static final NetworkInterface LOOPBACK_IF;
    public static final int SOMAXCONN;
    private static final int IPV6_WORD_COUNT = 8;
    private static final int IPV6_MAX_CHAR_COUNT = 39;
    private static final int IPV6_BYTE_COUNT = 16;
    private static final int IPV6_MAX_CHAR_BETWEEN_SEPARATOR = 4;
    private static final int IPV6_MIN_SEPARATORS = 2;
    private static final int IPV6_MAX_SEPARATORS = 8;
    private static final int IPV4_MAX_CHAR_BETWEEN_SEPARATOR = 3;
    private static final int IPV4_SEPARATORS = 3;
    private static final boolean IPV4_PREFERRED = SystemPropertyUtil.getBoolean("java.net.preferIPv4Stack", false);
    private static final boolean IPV6_ADDRESSES_PREFERRED = SystemPropertyUtil.getBoolean("java.net.preferIPv6Addresses", false);
    private static final InternalLogger logger = InternalLoggerFactory.getInstance((Class<?>) NetUtil.class);

    static {
        logger.debug("-Djava.net.preferIPv4Stack: {}", Boolean.valueOf(IPV4_PREFERRED));
        logger.debug("-Djava.net.preferIPv6Addresses: {}", Boolean.valueOf(IPV6_ADDRESSES_PREFERRED));
        byte[] LOCALHOST4_BYTES = {Byte.MAX_VALUE, 0, 0, 1};
        byte[] LOCALHOST6_BYTES = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1};
        Inet4Address localhost4 = null;
        try {
            localhost4 = (Inet4Address) InetAddress.getByAddress("localhost", LOCALHOST4_BYTES);
        } catch (Exception e) {
            PlatformDependent.throwException(e);
        }
        LOCALHOST4 = localhost4;
        Inet6Address localhost6 = null;
        try {
            localhost6 = (Inet6Address) InetAddress.getByAddress("localhost", LOCALHOST6_BYTES);
        } catch (Exception e2) {
            PlatformDependent.throwException(e2);
        }
        LOCALHOST6 = localhost6;
        List<NetworkInterface> ifaces = new ArrayList<>();
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            if (interfaces != null) {
                while (interfaces.hasMoreElements()) {
                    NetworkInterface iface = interfaces.nextElement();
                    if (SocketUtils.addressesFromNetworkInterface(iface).hasMoreElements()) {
                        ifaces.add(iface);
                    }
                }
            }
        } catch (SocketException e3) {
            logger.warn("Failed to retrieve the list of available network interfaces", (Throwable) e3);
        }
        NetworkInterface loopbackIface = null;
        InetAddress loopbackAddr = null;
        Iterator<NetworkInterface> it = ifaces.iterator();
        loop1: while (true) {
            if (!it.hasNext()) {
                break;
            }
            NetworkInterface iface2 = it.next();
            Enumeration<InetAddress> i = SocketUtils.addressesFromNetworkInterface(iface2);
            while (i.hasMoreElements()) {
                InetAddress addr = i.nextElement();
                if (addr.isLoopbackAddress()) {
                    loopbackIface = iface2;
                    loopbackAddr = addr;
                    break loop1;
                }
            }
        }
        if (loopbackIface == null) {
            try {
                Iterator<NetworkInterface> it2 = ifaces.iterator();
                while (true) {
                    if (!it2.hasNext()) {
                        break;
                    }
                    NetworkInterface iface3 = it2.next();
                    if (iface3.isLoopback()) {
                        Enumeration<InetAddress> i2 = SocketUtils.addressesFromNetworkInterface(iface3);
                        if (i2.hasMoreElements()) {
                            loopbackIface = iface3;
                            loopbackAddr = i2.nextElement();
                            break;
                        }
                    }
                }
                if (loopbackIface == null) {
                    logger.warn("Failed to find the loopback interface");
                }
            } catch (SocketException e4) {
                logger.warn("Failed to find the loopback interface", (Throwable) e4);
            }
        }
        if (loopbackIface != null) {
            logger.debug("Loopback interface: {} ({}, {})", loopbackIface.getName(), loopbackIface.getDisplayName(), loopbackAddr.getHostAddress());
        } else if (loopbackAddr == null) {
            try {
                if (NetworkInterface.getByInetAddress(LOCALHOST6) != null) {
                    logger.debug("Using hard-coded IPv6 localhost address: {}", localhost6);
                    loopbackAddr = localhost6;
                }
                if (loopbackAddr == null) {
                    logger.debug("Using hard-coded IPv4 localhost address: {}", localhost4);
                    loopbackAddr = localhost4;
                }
            } catch (Exception e5) {
                if (loopbackAddr == null) {
                    logger.debug("Using hard-coded IPv4 localhost address: {}", localhost4);
                    loopbackAddr = localhost4;
                }
            } catch (Throwable th) {
                if (loopbackAddr == null) {
                    logger.debug("Using hard-coded IPv4 localhost address: {}", localhost4);
                }
                throw th;
            }
        }
        LOOPBACK_IF = loopbackIface;
        LOCALHOST = loopbackAddr;
        SOMAXCONN = ((Integer) AccessController.doPrivileged(new PrivilegedAction<Integer>() { // from class: io.netty.util.NetUtil.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            public Integer run() throws IOException {
                int somaxconn = PlatformDependent.isWindows() ? 200 : 128;
                File file = new File("/proc/sys/net/core/somaxconn");
                BufferedReader in = null;
                try {
                    try {
                        if (file.exists()) {
                            in = new BufferedReader(new FileReader(file));
                            somaxconn = Integer.parseInt(in.readLine());
                            if (NetUtil.logger.isDebugEnabled()) {
                                NetUtil.logger.debug("{}: {}", file, Integer.valueOf(somaxconn));
                            }
                        } else {
                            Integer tmp = null;
                            if (SystemPropertyUtil.getBoolean("io.netty.net.somaxconn.trySysctl", false)) {
                                tmp = NetUtil.sysctlGetInt("kern.ipc.somaxconn");
                                if (tmp == null) {
                                    tmp = NetUtil.sysctlGetInt("kern.ipc.soacceptqueue");
                                    if (tmp != null) {
                                        somaxconn = tmp.intValue();
                                    }
                                } else {
                                    somaxconn = tmp.intValue();
                                }
                            }
                            if (tmp == null) {
                                NetUtil.logger.debug("Failed to get SOMAXCONN from sysctl and file {}. Default: {}", file, Integer.valueOf(somaxconn));
                            }
                        }
                        if (in != null) {
                            try {
                                in.close();
                            } catch (Exception e6) {
                            }
                        }
                    } catch (Exception e7) {
                        if (NetUtil.logger.isDebugEnabled()) {
                            NetUtil.logger.debug("Failed to get SOMAXCONN from sysctl and file {}. Default: {}", file, Integer.valueOf(somaxconn), e7);
                        }
                        if (0 != 0) {
                            try {
                                in.close();
                            } catch (Exception e8) {
                            }
                        }
                    }
                    return Integer.valueOf(somaxconn);
                } catch (Throwable th2) {
                    if (0 != 0) {
                        try {
                            in.close();
                        } catch (Exception e9) {
                        }
                    }
                    throw th2;
                }
            }
        })).intValue();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Integer sysctlGetInt(String sysctlKey) throws IOException {
        Process process = new ProcessBuilder("sysctl", sysctlKey).start();
        try {
            InputStream is = process.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            try {
                String line = br.readLine();
                if (line != null && line.startsWith(sysctlKey)) {
                    for (int i = line.length() - 1; i > sysctlKey.length(); i--) {
                        if (!Character.isDigit(line.charAt(i))) {
                            Integer numValueOf = Integer.valueOf(line.substring(i + 1));
                            br.close();
                            if (process != null) {
                                process.destroy();
                            }
                            return numValueOf;
                        }
                    }
                }
                if (process != null) {
                    process.destroy();
                }
                return null;
            } finally {
                br.close();
            }
        } catch (Throwable th) {
            if (process != null) {
                process.destroy();
            }
            throw th;
        }
    }

    public static boolean isIpV4StackPreferred() {
        return IPV4_PREFERRED;
    }

    public static boolean isIpV6AddressesPreferred() {
        return IPV6_ADDRESSES_PREFERRED;
    }

    public static byte[] createByteArrayFromIpAddressString(String ipAddressString) {
        if (isValidIpV4Address(ipAddressString)) {
            return validIpV4ToBytes(ipAddressString);
        }
        if (isValidIpV6Address(ipAddressString)) {
            if (ipAddressString.charAt(0) == '[') {
                ipAddressString = ipAddressString.substring(1, ipAddressString.length() - 1);
            }
            int percentPos = ipAddressString.indexOf(37);
            if (percentPos >= 0) {
                ipAddressString = ipAddressString.substring(0, percentPos);
            }
            return getIPv6ByName(ipAddressString, true);
        }
        return null;
    }

    private static int decimalDigit(String str, int pos) {
        return str.charAt(pos) - '0';
    }

    private static byte ipv4WordToByte(String ip, int from, int toExclusive) {
        int ret = decimalDigit(ip, from);
        int from2 = from + 1;
        if (from2 == toExclusive) {
            return (byte) ret;
        }
        int ret2 = (ret * 10) + decimalDigit(ip, from2);
        int from3 = from2 + 1;
        if (from3 == toExclusive) {
            return (byte) ret2;
        }
        return (byte) ((ret2 * 10) + decimalDigit(ip, from3));
    }

    static byte[] validIpV4ToBytes(String ip) {
        int i = ip.indexOf(46, 1);
        int i2 = i + 1;
        int i3 = ip.indexOf(46, i + 2);
        int i4 = i3 + 1;
        int i5 = ip.indexOf(46, i3 + 2);
        return new byte[]{ipv4WordToByte(ip, 0, i), ipv4WordToByte(ip, i2, i3), ipv4WordToByte(ip, i4, i5), ipv4WordToByte(ip, i5 + 1, ip.length())};
    }

    public static String intToIpAddress(int i) {
        StringBuilder buf = new StringBuilder(15);
        buf.append((i >> 24) & 255);
        buf.append('.');
        buf.append((i >> 16) & 255);
        buf.append('.');
        buf.append((i >> 8) & 255);
        buf.append('.');
        buf.append(i & 255);
        return buf.toString();
    }

    public static String bytesToIpAddress(byte[] bytes) {
        return bytesToIpAddress(bytes, 0, bytes.length);
    }

    public static String bytesToIpAddress(byte[] bytes, int offset, int length) {
        switch (length) {
            case 4:
                return new StringBuilder(15).append(bytes[offset] & 255).append('.').append(bytes[offset + 1] & 255).append('.').append(bytes[offset + 2] & 255).append('.').append(bytes[offset + 3] & 255).toString();
            case 16:
                return toAddressString(bytes, offset, false);
            default:
                throw new IllegalArgumentException("length: " + length + " (expected: 4 or 16)");
        }
    }

    public static boolean isValidIpV6Address(String ip) {
        return isValidIpV6Address((CharSequence) ip);
    }

    /* JADX WARN: Code restructure failed: missing block: B:100:0x01c6, code lost:
    
        if (r11 <= 0) goto L106;
     */
    /* JADX WARN: Code restructure failed: missing block: B:102:0x01cd, code lost:
    
        if (r9 < 8) goto L105;
     */
    /* JADX WARN: Code restructure failed: missing block: B:104:0x01d3, code lost:
    
        if (r10 > r7) goto L106;
     */
    /* JADX WARN: Code restructure failed: missing block: B:105:0x01d6, code lost:
    
        return true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:106:0x01da, code lost:
    
        return false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:89:0x01a7, code lost:
    
        if (r10 >= 0) goto L97;
     */
    /* JADX WARN: Code restructure failed: missing block: B:91:0x01ae, code lost:
    
        if (r9 != 7) goto L95;
     */
    /* JADX WARN: Code restructure failed: missing block: B:93:0x01b3, code lost:
    
        if (r11 <= 0) goto L95;
     */
    /* JADX WARN: Code restructure failed: missing block: B:94:0x01b6, code lost:
    
        return true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:95:0x01ba, code lost:
    
        return false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:98:0x01c1, code lost:
    
        if ((r10 + 2) == r6) goto L105;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static boolean isValidIpV6Address(java.lang.CharSequence r5) {
        /*
            Method dump skipped, instructions count: 476
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: io.netty.util.NetUtil.isValidIpV6Address(java.lang.CharSequence):boolean");
    }

    private static boolean isValidIpV4Word(CharSequence word, int from, int toExclusive) {
        char c0;
        char c2;
        int len = toExclusive - from;
        if (len < 1 || len > 3 || (c0 = word.charAt(from)) < '0') {
            return false;
        }
        if (len != 3) {
            return c0 <= '9' && (len == 1 || isValidNumericChar(word.charAt(from + 1)));
        }
        char c1 = word.charAt(from + 1);
        return c1 >= '0' && (c2 = word.charAt(from + 2)) >= '0' && ((c0 <= '1' && c1 <= '9' && c2 <= '9') || (c0 == '2' && c1 <= '5' && (c2 <= '5' || (c1 < '5' && c2 <= '9'))));
    }

    private static boolean isValidHexChar(char c) {
        return (c >= '0' && c <= '9') || (c >= 'A' && c <= 'F') || (c >= 'a' && c <= 'f');
    }

    private static boolean isValidNumericChar(char c) {
        return c >= '0' && c <= '9';
    }

    private static boolean isValidIPv4MappedChar(char c) {
        return c == 'f' || c == 'F';
    }

    private static boolean isValidIPv4MappedSeparators(byte b0, byte b1, boolean mustBeZero) {
        return b0 == b1 && (b0 == 0 || (!mustBeZero && b1 == -1));
    }

    private static boolean isValidIPv4Mapped(byte[] bytes, int currentIndex, int compressBegin, int compressLength) {
        boolean mustBeZero = compressBegin + compressLength >= 14;
        return currentIndex <= 12 && currentIndex >= 2 && (!mustBeZero || compressBegin < 12) && isValidIPv4MappedSeparators(bytes[currentIndex - 1], bytes[currentIndex - 2], mustBeZero) && PlatformDependent.isZero(bytes, 0, currentIndex - 3);
    }

    public static boolean isValidIpV4Address(CharSequence ip) {
        return isValidIpV4Address(ip, 0, ip.length());
    }

    public static boolean isValidIpV4Address(String ip) {
        return isValidIpV4Address(ip, 0, ip.length());
    }

    private static boolean isValidIpV4Address(CharSequence ip, int from, int toExcluded) {
        if (ip instanceof String) {
            return isValidIpV4Address((String) ip, from, toExcluded);
        }
        if (ip instanceof AsciiString) {
            return isValidIpV4Address((AsciiString) ip, from, toExcluded);
        }
        return isValidIpV4Address0(ip, from, toExcluded);
    }

    private static boolean isValidIpV4Address(String ip, int from, int toExcluded) {
        int i;
        int from2;
        int i2;
        int from3;
        int i3;
        int len = toExcluded - from;
        return len <= 15 && len >= 7 && (i = ip.indexOf(46, from + 1)) > 0 && isValidIpV4Word(ip, from, i) && (i2 = ip.indexOf(46, (from2 = i + 2))) > 0 && isValidIpV4Word(ip, from2 - 1, i2) && (i3 = ip.indexOf(46, (from3 = i2 + 2))) > 0 && isValidIpV4Word(ip, from3 - 1, i3) && isValidIpV4Word(ip, i3 + 1, toExcluded);
    }

    private static boolean isValidIpV4Address(AsciiString ip, int from, int toExcluded) {
        int i;
        int from2;
        int i2;
        int from3;
        int i3;
        int len = toExcluded - from;
        return len <= 15 && len >= 7 && (i = ip.indexOf('.', from + 1)) > 0 && isValidIpV4Word(ip, from, i) && (i2 = ip.indexOf('.', (from2 = i + 2))) > 0 && isValidIpV4Word(ip, from2 - 1, i2) && (i3 = ip.indexOf('.', (from3 = i2 + 2))) > 0 && isValidIpV4Word(ip, from3 - 1, i3) && isValidIpV4Word(ip, i3 + 1, toExcluded);
    }

    private static boolean isValidIpV4Address0(CharSequence ip, int from, int toExcluded) {
        int i;
        int from2;
        int i2;
        int from3;
        int i3;
        int len = toExcluded - from;
        return len <= 15 && len >= 7 && (i = AsciiString.indexOf(ip, '.', from + 1)) > 0 && isValidIpV4Word(ip, from, i) && (i2 = AsciiString.indexOf(ip, '.', (from2 = i + 2))) > 0 && isValidIpV4Word(ip, from2 - 1, i2) && (i3 = AsciiString.indexOf(ip, '.', (from3 = i2 + 2))) > 0 && isValidIpV4Word(ip, from3 - 1, i3) && isValidIpV4Word(ip, i3 + 1, toExcluded);
    }

    public static Inet6Address getByName(CharSequence ip) {
        return getByName(ip, true);
    }

    public static Inet6Address getByName(CharSequence ip, boolean ipv4Mapped) {
        byte[] bytes = getIPv6ByName(ip, ipv4Mapped);
        if (bytes == null) {
            return null;
        }
        try {
            return Inet6Address.getByAddress((String) null, bytes, -1);
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    private static byte[] getIPv6ByName(CharSequence ip, boolean ipv4Mapped) {
        int currentIndex;
        int begin;
        int currentIndex2;
        byte[] bytes = new byte[16];
        int ipLength = ip.length();
        int compressBegin = 0;
        int compressLength = 0;
        int currentIndex3 = 0;
        int value = 0;
        int begin2 = -1;
        int i = 0;
        int ipv6Separators = 0;
        int ipv4Separators = 0;
        boolean needsShift = false;
        while (i < ipLength) {
            char c = ip.charAt(i);
            switch (c) {
                case '.':
                    ipv4Separators++;
                    int tmp = i - begin2;
                    if (tmp > 3 || begin2 < 0 || ipv4Separators > 3) {
                        return null;
                    }
                    if ((ipv6Separators > 0 && currentIndex3 + compressLength < 12) || i + 1 >= ipLength || currentIndex3 >= bytes.length) {
                        return null;
                    }
                    if (ipv4Separators == 1) {
                        if (ipv4Mapped) {
                            if (currentIndex3 == 0 || isValidIPv4Mapped(bytes, currentIndex3, compressBegin, compressLength)) {
                                if (tmp != 3 || (isValidNumericChar(ip.charAt(i - 1)) && isValidNumericChar(ip.charAt(i - 2)) && isValidNumericChar(ip.charAt(i - 3)))) {
                                    if (tmp != 2 || (isValidNumericChar(ip.charAt(i - 1)) && isValidNumericChar(ip.charAt(i - 2)))) {
                                        if (tmp == 1 && !isValidNumericChar(ip.charAt(i - 1))) {
                                            return null;
                                        }
                                    } else {
                                        return null;
                                    }
                                } else {
                                    return null;
                                }
                            } else {
                                return null;
                            }
                        } else {
                            return null;
                        }
                    }
                    int value2 = value << ((3 - tmp) << 2);
                    int begin3 = ((value2 & 15) * 100) + (((value2 >> 4) & 15) * 10) + ((value2 >> 8) & 15);
                    if (begin3 < 0 || begin3 > 255) {
                        return null;
                    }
                    int i2 = currentIndex3;
                    currentIndex3++;
                    bytes[i2] = (byte) begin3;
                    value = 0;
                    begin2 = -1;
                    break;
                    break;
                case ':':
                    ipv6Separators++;
                    if (i - begin2 > 4 || ipv4Separators > 0 || ipv6Separators > 8 || currentIndex3 + 1 >= bytes.length) {
                        return null;
                    }
                    int value3 = value << ((4 - (i - begin2)) << 2);
                    if (compressLength > 0) {
                        compressLength -= 2;
                    }
                    int i3 = currentIndex3;
                    int currentIndex4 = currentIndex3 + 1;
                    bytes[i3] = (byte) (((value3 & 15) << 4) | ((value3 >> 4) & 15));
                    currentIndex3 = currentIndex4 + 1;
                    bytes[currentIndex4] = (byte) ((((value3 >> 8) & 15) << 4) | ((value3 >> 12) & 15));
                    int tmp2 = i + 1;
                    if (tmp2 < ipLength && ip.charAt(tmp2) == ':') {
                        int tmp3 = tmp2 + 1;
                        if (compressBegin != 0) {
                            return null;
                        }
                        if (tmp3 < ipLength && ip.charAt(tmp3) == ':') {
                            return null;
                        }
                        ipv6Separators++;
                        needsShift = ipv6Separators == 2 && value3 == 0;
                        compressBegin = currentIndex3;
                        compressLength = (bytes.length - compressBegin) - 2;
                        i++;
                    }
                    value = 0;
                    begin2 = -1;
                    break;
                    break;
                default:
                    if (!isValidHexChar(c)) {
                        return null;
                    }
                    if (ipv4Separators > 0 && !isValidNumericChar(c)) {
                        return null;
                    }
                    if (begin2 < 0) {
                        begin2 = i;
                    } else if (i - begin2 > 4) {
                        return null;
                    }
                    value += StringUtil.decodeHexNibble(c) << ((i - begin2) << 2);
                    break;
            }
            i++;
        }
        boolean isCompressed = compressBegin > 0;
        if (ipv4Separators > 0) {
            if ((begin2 > 0 && i - begin2 > 3) || ipv4Separators != 3 || currentIndex3 >= bytes.length) {
                return null;
            }
            if (ipv6Separators == 0) {
                compressLength = 12;
            } else if (ipv6Separators >= 2) {
                if (isCompressed || ipv6Separators != 6 || ip.charAt(0) == ':') {
                    if (isCompressed && ipv6Separators < 8) {
                        if (ip.charAt(0) == ':' && compressBegin > 2) {
                            return null;
                        }
                    } else {
                        return null;
                    }
                }
                compressLength -= 2;
            } else {
                return null;
            }
            int value4 = value << ((3 - (i - begin2)) << 2);
            int begin4 = ((value4 & 15) * 100) + (((value4 >> 4) & 15) * 10) + ((value4 >> 8) & 15);
            if (begin4 < 0 || begin4 > 255) {
                return null;
            }
            int i4 = currentIndex3;
            currentIndex = currentIndex3 + 1;
            bytes[i4] = (byte) begin4;
        } else {
            int tmp4 = ipLength - 1;
            if ((begin2 <= 0 || i - begin2 <= 4) && ipv6Separators >= 2) {
                if (!isCompressed && (ipv6Separators + 1 != 8 || ip.charAt(0) == ':' || ip.charAt(tmp4) == ':')) {
                    return null;
                }
                if (isCompressed) {
                    if (ipv6Separators > 8) {
                        return null;
                    }
                    if (ipv6Separators == 8) {
                        if (compressBegin > 2 || ip.charAt(0) == ':') {
                            if (compressBegin >= 14 && ip.charAt(tmp4) != ':') {
                                return null;
                            }
                        } else {
                            return null;
                        }
                    }
                }
                if (currentIndex3 + 1 < bytes.length) {
                    if (begin2 >= 0 || ip.charAt(tmp4 - 1) == ':') {
                        if (compressBegin > 2 && ip.charAt(0) == ':') {
                            return null;
                        }
                        if (begin2 >= 0 && i - begin2 <= 4) {
                            value <<= (4 - (i - begin2)) << 2;
                        }
                        int i5 = currentIndex3;
                        int currentIndex5 = currentIndex3 + 1;
                        bytes[i5] = (byte) (((value & 15) << 4) | ((value >> 4) & 15));
                        currentIndex = currentIndex5 + 1;
                        bytes[currentIndex5] = (byte) ((((value >> 8) & 15) << 4) | ((value >> 12) & 15));
                    } else {
                        return null;
                    }
                } else {
                    return null;
                }
            } else {
                return null;
            }
        }
        int i6 = currentIndex + compressLength;
        if (needsShift || i6 >= bytes.length) {
            if (i6 >= bytes.length) {
                compressBegin++;
            }
            for (int i7 = currentIndex; i7 < bytes.length; i7++) {
                int begin5 = bytes.length - 1;
                while (begin5 >= compressBegin) {
                    bytes[begin5] = bytes[begin5 - 1];
                    begin5--;
                }
                bytes[begin5] = 0;
                compressBegin++;
            }
        } else {
            for (int i8 = 0; i8 < compressLength && (currentIndex2 = (begin = i8 + compressBegin) + compressLength) < bytes.length; i8++) {
                bytes[currentIndex2] = bytes[begin];
                bytes[begin] = 0;
            }
        }
        if (ipv4Separators > 0) {
            bytes[11] = -1;
            bytes[10] = -1;
        }
        return bytes;
    }

    public static String toSocketAddressString(InetSocketAddress addr) {
        StringBuilder sb;
        String port = String.valueOf(addr.getPort());
        if (addr.isUnresolved()) {
            String hostname = getHostname(addr);
            sb = newSocketAddressStringBuilder(hostname, port, !isValidIpV6Address(hostname));
        } else {
            InetAddress address = addr.getAddress();
            String hostString = toAddressString(address);
            sb = newSocketAddressStringBuilder(hostString, port, address instanceof Inet4Address);
        }
        return sb.append(':').append(port).toString();
    }

    public static String toSocketAddressString(String host, int port) {
        String portStr = String.valueOf(port);
        return newSocketAddressStringBuilder(host, portStr, !isValidIpV6Address(host)).append(':').append(portStr).toString();
    }

    private static StringBuilder newSocketAddressStringBuilder(String host, String port, boolean ipv4) {
        int hostLen = host.length();
        if (ipv4) {
            return new StringBuilder(hostLen + 1 + port.length()).append(host);
        }
        StringBuilder stringBuilder = new StringBuilder(hostLen + 3 + port.length());
        if (hostLen > 1 && host.charAt(0) == '[' && host.charAt(hostLen - 1) == ']') {
            return stringBuilder.append(host);
        }
        return stringBuilder.append('[').append(host).append(']');
    }

    public static String toAddressString(InetAddress ip) {
        return toAddressString(ip, false);
    }

    public static String toAddressString(InetAddress ip, boolean ipv4Mapped) {
        if (ip instanceof Inet4Address) {
            return ip.getHostAddress();
        }
        if (!(ip instanceof Inet6Address)) {
            throw new IllegalArgumentException("Unhandled type: " + ip);
        }
        return toAddressString(ip.getAddress(), 0, ipv4Mapped);
    }

    private static String toAddressString(byte[] bytes, int offset, boolean ipv4Mapped) {
        boolean isIpv4Mapped;
        int currentLength;
        int[] words = new int[8];
        int end = offset + words.length;
        for (int i = offset; i < end; i++) {
            words[i] = ((bytes[i << 1] & 255) << 8) | (bytes[(i << 1) + 1] & 255);
        }
        int currentStart = -1;
        int shortestStart = -1;
        int shortestLength = 0;
        int i2 = 0;
        while (i2 < words.length) {
            if (words[i2] == 0) {
                if (currentStart < 0) {
                    currentStart = i2;
                }
            } else if (currentStart >= 0) {
                int currentLength2 = i2 - currentStart;
                if (currentLength2 > shortestLength) {
                    shortestStart = currentStart;
                    shortestLength = currentLength2;
                }
                currentStart = -1;
            }
            i2++;
        }
        if (currentStart >= 0 && (currentLength = i2 - currentStart) > shortestLength) {
            shortestStart = currentStart;
            shortestLength = currentLength;
        }
        if (shortestLength == 1) {
            shortestLength = 0;
            shortestStart = -1;
        }
        int shortestEnd = shortestStart + shortestLength;
        StringBuilder b = new StringBuilder(39);
        if (shortestEnd < 0) {
            b.append(Integer.toHexString(words[0]));
            for (int i3 = 1; i3 < words.length; i3++) {
                b.append(':');
                b.append(Integer.toHexString(words[i3]));
            }
        } else {
            if (inRangeEndExclusive(0, shortestStart, shortestEnd)) {
                b.append(NetFlags.ANY_ADDR_V6);
                isIpv4Mapped = ipv4Mapped && shortestEnd == 5 && words[5] == 65535;
            } else {
                b.append(Integer.toHexString(words[0]));
                isIpv4Mapped = false;
            }
            for (int i4 = 1; i4 < words.length; i4++) {
                if (!inRangeEndExclusive(i4, shortestStart, shortestEnd)) {
                    if (!inRangeEndExclusive(i4 - 1, shortestStart, shortestEnd)) {
                        if (!isIpv4Mapped || i4 == 6) {
                            b.append(':');
                        } else {
                            b.append('.');
                        }
                    }
                    if (isIpv4Mapped && i4 > 5) {
                        b.append(words[i4] >> 8);
                        b.append('.');
                        b.append(words[i4] & 255);
                    } else {
                        b.append(Integer.toHexString(words[i4]));
                    }
                } else if (!inRangeEndExclusive(i4 - 1, shortestStart, shortestEnd)) {
                    b.append(NetFlags.ANY_ADDR_V6);
                }
            }
        }
        return b.toString();
    }

    public static String getHostname(InetSocketAddress addr) {
        return PlatformDependent.javaVersion() >= 7 ? addr.getHostString() : addr.getHostName();
    }

    private static boolean inRangeEndExclusive(int value, int start, int end) {
        return value >= start && value < end;
    }

    private NetUtil() {
    }
}
