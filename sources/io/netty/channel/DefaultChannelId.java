package io.netty.channel;

import io.netty.buffer.ByteBufUtil;
import io.netty.util.internal.EmptyArrays;
import io.netty.util.internal.MacAddressUtil;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.SystemPropertyUtil;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/channel/DefaultChannelId.class */
public final class DefaultChannelId implements ChannelId {
    private static final long serialVersionUID = 3884076183504074063L;
    private static final InternalLogger logger;
    private static final byte[] MACHINE_ID;
    private static final int PROCESS_ID_LEN = 4;
    private static final int PROCESS_ID;
    private static final int SEQUENCE_LEN = 4;
    private static final int TIMESTAMP_LEN = 8;
    private static final int RANDOM_LEN = 4;
    private static final AtomicInteger nextSequence;
    private final byte[] data = new byte[(((MACHINE_ID.length + 4) + 4) + 8) + 4];
    private final int hashCode;
    private transient String shortValue;
    private transient String longValue;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !DefaultChannelId.class.desiredAssertionStatus();
        logger = InternalLoggerFactory.getInstance((Class<?>) DefaultChannelId.class);
        nextSequence = new AtomicInteger();
        int processId = -1;
        String customProcessId = SystemPropertyUtil.get("io.netty.processId");
        if (customProcessId != null) {
            try {
                processId = Integer.parseInt(customProcessId);
            } catch (NumberFormatException e) {
            }
            if (processId < 0) {
                processId = -1;
                logger.warn("-Dio.netty.processId: {} (malformed)", customProcessId);
            } else if (logger.isDebugEnabled()) {
                logger.debug("-Dio.netty.processId: {} (user-set)", Integer.valueOf(processId));
            }
        }
        if (processId < 0) {
            processId = defaultProcessId();
            if (logger.isDebugEnabled()) {
                logger.debug("-Dio.netty.processId: {} (auto-detected)", Integer.valueOf(processId));
            }
        }
        PROCESS_ID = processId;
        byte[] machineId = null;
        String customMachineId = SystemPropertyUtil.get("io.netty.machineId");
        if (customMachineId != null) {
            try {
                machineId = MacAddressUtil.parseMAC(customMachineId);
            } catch (Exception e2) {
                logger.warn("-Dio.netty.machineId: {} (malformed)", customMachineId, e2);
            }
            if (machineId != null) {
                logger.debug("-Dio.netty.machineId: {} (user-set)", customMachineId);
            }
        }
        if (machineId == null) {
            machineId = MacAddressUtil.defaultMachineId();
            if (logger.isDebugEnabled()) {
                logger.debug("-Dio.netty.machineId: {} (auto-detected)", MacAddressUtil.formatAddress(machineId));
            }
        }
        MACHINE_ID = machineId;
    }

    public static DefaultChannelId newInstance() {
        return new DefaultChannelId();
    }

    private static int defaultProcessId() throws NumberFormatException {
        String value;
        int pid;
        ClassLoader loader = null;
        try {
            loader = PlatformDependent.getClassLoader(DefaultChannelId.class);
            Class<?> mgmtFactoryType = Class.forName("java.lang.management.ManagementFactory", true, loader);
            Class<?> runtimeMxBeanType = Class.forName("java.lang.management.RuntimeMXBean", true, loader);
            Method getRuntimeMXBean = mgmtFactoryType.getMethod("getRuntimeMXBean", EmptyArrays.EMPTY_CLASSES);
            Object bean = getRuntimeMXBean.invoke(null, EmptyArrays.EMPTY_OBJECTS);
            Method getName = runtimeMxBeanType.getMethod("getName", EmptyArrays.EMPTY_CLASSES);
            value = (String) getName.invoke(bean, EmptyArrays.EMPTY_OBJECTS);
        } catch (Throwable t) {
            logger.debug("Could not invoke ManagementFactory.getRuntimeMXBean().getName(); Android?", t);
            try {
                Class<?> processType = Class.forName("android.os.Process", true, loader);
                Method myPid = processType.getMethod("myPid", EmptyArrays.EMPTY_CLASSES);
                value = myPid.invoke(null, EmptyArrays.EMPTY_OBJECTS).toString();
            } catch (Throwable t2) {
                logger.debug("Could not invoke Process.myPid(); not Android?", t2);
                value = "";
            }
        }
        int atIndex = value.indexOf(64);
        if (atIndex >= 0) {
            value = value.substring(0, atIndex);
        }
        try {
            pid = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            pid = -1;
        }
        if (pid < 0) {
            pid = PlatformDependent.threadLocalRandom().nextInt();
            logger.warn("Failed to find the current process ID from '{}'; using a random value: {}", value, Integer.valueOf(pid));
        }
        return pid;
    }

    private DefaultChannelId() {
        System.arraycopy(MACHINE_ID, 0, this.data, 0, MACHINE_ID.length);
        int i = 0 + MACHINE_ID.length;
        int i2 = writeLong(writeInt(writeInt(i, PROCESS_ID), nextSequence.getAndIncrement()), Long.reverse(System.nanoTime()) ^ System.currentTimeMillis());
        int random = PlatformDependent.threadLocalRandom().nextInt();
        int i3 = writeInt(i2, random);
        if (!$assertionsDisabled && i3 != this.data.length) {
            throw new AssertionError();
        }
        this.hashCode = Arrays.hashCode(this.data);
    }

    private int writeInt(int i, int value) {
        int i2 = i + 1;
        this.data[i] = (byte) (value >>> 24);
        int i3 = i2 + 1;
        this.data[i2] = (byte) (value >>> 16);
        int i4 = i3 + 1;
        this.data[i3] = (byte) (value >>> 8);
        int i5 = i4 + 1;
        this.data[i4] = (byte) value;
        return i5;
    }

    private int writeLong(int i, long value) {
        int i2 = i + 1;
        this.data[i] = (byte) (value >>> 56);
        int i3 = i2 + 1;
        this.data[i2] = (byte) (value >>> 48);
        int i4 = i3 + 1;
        this.data[i3] = (byte) (value >>> 40);
        int i5 = i4 + 1;
        this.data[i4] = (byte) (value >>> 32);
        int i6 = i5 + 1;
        this.data[i5] = (byte) (value >>> 24);
        int i7 = i6 + 1;
        this.data[i6] = (byte) (value >>> 16);
        int i8 = i7 + 1;
        this.data[i7] = (byte) (value >>> 8);
        int i9 = i8 + 1;
        this.data[i8] = (byte) value;
        return i9;
    }

    @Override // io.netty.channel.ChannelId
    public String asShortText() {
        String shortValue = this.shortValue;
        if (shortValue == null) {
            String strHexDump = ByteBufUtil.hexDump(this.data, this.data.length - 4, 4);
            shortValue = strHexDump;
            this.shortValue = strHexDump;
        }
        return shortValue;
    }

    @Override // io.netty.channel.ChannelId
    public String asLongText() {
        String longValue = this.longValue;
        if (longValue == null) {
            String strNewLongValue = newLongValue();
            longValue = strNewLongValue;
            this.longValue = strNewLongValue;
        }
        return longValue;
    }

    private String newLongValue() {
        StringBuilder buf = new StringBuilder((2 * this.data.length) + 5);
        int i = appendHexDumpField(buf, 0, MACHINE_ID.length);
        int i2 = appendHexDumpField(buf, appendHexDumpField(buf, appendHexDumpField(buf, appendHexDumpField(buf, i, 4), 4), 8), 4);
        if ($assertionsDisabled || i2 == this.data.length) {
            return buf.substring(0, buf.length() - 1);
        }
        throw new AssertionError();
    }

    private int appendHexDumpField(StringBuilder buf, int i, int length) {
        buf.append(ByteBufUtil.hexDump(this.data, i, length));
        buf.append('-');
        return i + length;
    }

    public int hashCode() {
        return this.hashCode;
    }

    @Override // java.lang.Comparable
    public int compareTo(ChannelId o) {
        if (this == o) {
            return 0;
        }
        if (o instanceof DefaultChannelId) {
            byte[] otherData = ((DefaultChannelId) o).data;
            int len1 = this.data.length;
            int len2 = otherData.length;
            int len = Math.min(len1, len2);
            for (int k = 0; k < len; k++) {
                byte x = this.data[k];
                byte y = otherData[k];
                if (x != y) {
                    return (x & 255) - (y & 255);
                }
            }
            return len1 - len2;
        }
        return asLongText().compareTo(o.asLongText());
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof DefaultChannelId)) {
            return false;
        }
        DefaultChannelId other = (DefaultChannelId) obj;
        return this.hashCode == other.hashCode && Arrays.equals(this.data, other.data);
    }

    public String toString() {
        return asShortText();
    }
}
