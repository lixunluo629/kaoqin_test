package org.springframework.util;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.Random;
import java.util.SortedSet;
import java.util.TreeSet;
import javax.net.ServerSocketFactory;

/* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/util/SocketUtils.class */
public class SocketUtils {
    public static final int PORT_RANGE_MIN = 1024;
    public static final int PORT_RANGE_MAX = 65535;
    private static final Random random = new Random(System.currentTimeMillis());

    public static int findAvailableTcpPort() {
        return findAvailableTcpPort(1024);
    }

    public static int findAvailableTcpPort(int minPort) {
        return findAvailableTcpPort(minPort, 65535);
    }

    public static int findAvailableTcpPort(int minPort, int maxPort) {
        return SocketType.TCP.findAvailablePort(minPort, maxPort);
    }

    public static SortedSet<Integer> findAvailableTcpPorts(int numRequested) {
        return findAvailableTcpPorts(numRequested, 1024, 65535);
    }

    public static SortedSet<Integer> findAvailableTcpPorts(int numRequested, int minPort, int maxPort) {
        return SocketType.TCP.findAvailablePorts(numRequested, minPort, maxPort);
    }

    public static int findAvailableUdpPort() {
        return findAvailableUdpPort(1024);
    }

    public static int findAvailableUdpPort(int minPort) {
        return findAvailableUdpPort(minPort, 65535);
    }

    public static int findAvailableUdpPort(int minPort, int maxPort) {
        return SocketType.UDP.findAvailablePort(minPort, maxPort);
    }

    public static SortedSet<Integer> findAvailableUdpPorts(int numRequested) {
        return findAvailableUdpPorts(numRequested, 1024, 65535);
    }

    public static SortedSet<Integer> findAvailableUdpPorts(int numRequested, int minPort, int maxPort) {
        return SocketType.UDP.findAvailablePorts(numRequested, minPort, maxPort);
    }

    /* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/util/SocketUtils$SocketType.class */
    private enum SocketType {
        TCP { // from class: org.springframework.util.SocketUtils.SocketType.1
            @Override // org.springframework.util.SocketUtils.SocketType
            protected boolean isPortAvailable(int port) throws IOException {
                try {
                    ServerSocket serverSocket = ServerSocketFactory.getDefault().createServerSocket(port, 1, InetAddress.getByName("localhost"));
                    serverSocket.close();
                    return true;
                } catch (Exception e) {
                    return false;
                }
            }
        },
        UDP { // from class: org.springframework.util.SocketUtils.SocketType.2
            @Override // org.springframework.util.SocketUtils.SocketType
            protected boolean isPortAvailable(int port) {
                try {
                    DatagramSocket socket = new DatagramSocket(port, InetAddress.getByName("localhost"));
                    socket.close();
                    return true;
                } catch (Exception e) {
                    return false;
                }
            }
        };

        protected abstract boolean isPortAvailable(int i);

        private int findRandomPort(int minPort, int maxPort) {
            int portRange = maxPort - minPort;
            return minPort + SocketUtils.random.nextInt(portRange + 1);
        }

        int findAvailablePort(int minPort, int maxPort) {
            int candidatePort;
            Assert.isTrue(minPort > 0, "'minPort' must be greater than 0");
            Assert.isTrue(maxPort >= minPort, "'maxPort' must be greater than or equals 'minPort'");
            Assert.isTrue(maxPort <= 65535, "'maxPort' must be less than or equal to 65535");
            int portRange = maxPort - minPort;
            int searchCounter = 0;
            do {
                searchCounter++;
                if (searchCounter > portRange) {
                    throw new IllegalStateException(String.format("Could not find an available %s port in the range [%d, %d] after %d attempts", name(), Integer.valueOf(minPort), Integer.valueOf(maxPort), Integer.valueOf(searchCounter)));
                }
                candidatePort = findRandomPort(minPort, maxPort);
            } while (!isPortAvailable(candidatePort));
            return candidatePort;
        }

        SortedSet<Integer> findAvailablePorts(int numRequested, int minPort, int maxPort) {
            Assert.isTrue(minPort > 0, "'minPort' must be greater than 0");
            Assert.isTrue(maxPort > minPort, "'maxPort' must be greater than 'minPort'");
            Assert.isTrue(maxPort <= 65535, "'maxPort' must be less than or equal to 65535");
            Assert.isTrue(numRequested > 0, "'numRequested' must be greater than 0");
            Assert.isTrue(maxPort - minPort >= numRequested, "'numRequested' must not be greater than 'maxPort' - 'minPort'");
            SortedSet<Integer> availablePorts = new TreeSet<>();
            int attemptCount = 0;
            while (true) {
                attemptCount++;
                if (attemptCount > numRequested + 100 || availablePorts.size() >= numRequested) {
                    break;
                }
                availablePorts.add(Integer.valueOf(findAvailablePort(minPort, maxPort)));
            }
            if (availablePorts.size() != numRequested) {
                throw new IllegalStateException(String.format("Could not find %d available %s ports in the range [%d, %d]", Integer.valueOf(numRequested), name(), Integer.valueOf(minPort), Integer.valueOf(maxPort)));
            }
            return availablePorts;
        }
    }
}
