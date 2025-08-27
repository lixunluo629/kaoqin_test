package io.netty.handler.codec.haproxy;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/haproxy/HAProxyCommand.class */
public enum HAProxyCommand {
    LOCAL((byte) 0),
    PROXY((byte) 1);

    private static final byte COMMAND_MASK = 15;
    private final byte byteValue;

    HAProxyCommand(byte byteValue) {
        this.byteValue = byteValue;
    }

    public static HAProxyCommand valueOf(byte verCmdByte) {
        int cmd = verCmdByte & 15;
        switch ((byte) cmd) {
            case 0:
                return LOCAL;
            case 1:
                return PROXY;
            default:
                throw new IllegalArgumentException("unknown command: " + cmd);
        }
    }

    public byte byteValue() {
        return this.byteValue;
    }
}
