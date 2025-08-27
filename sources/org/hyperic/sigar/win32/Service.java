package org.hyperic.sigar.win32;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.aspectj.weaver.Dump;
import org.hyperic.sigar.Sigar;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/win32/Service.class */
public class Service extends Win32 {
    public static final int SERVICE_STOPPED = 1;
    public static final int SERVICE_START_PENDING = 2;
    public static final int SERVICE_STOP_PENDING = 3;
    public static final int SERVICE_RUNNING = 4;
    public static final int SERVICE_CONTINUE_PENDING = 5;
    public static final int SERVICE_PAUSE_PENDING = 6;
    public static final int SERVICE_PAUSED = 7;
    private static final String[] STATUS = {Dump.UNKNOWN_FILENAME, "Stopped", "Start Pending", "Stop Pending", "Running", "Continue Pending", "Pause Pending", "Paused"};
    private static final int CONTROL_START = 0;
    private static final int CONTROL_STOP = 1;
    private static final int CONTROL_PAUSE = 2;
    private static final int CONTROL_RESUME = 3;
    private static final int STANDARD_RIGHTS_REQUIRED = 983040;
    private static final int SC_MANAGER_CONNECT = 1;
    private static final int SC_MANAGER_CREATE_SERVICE = 2;
    private static final int SC_MANAGER_ENUMERATE_SERVICE = 4;
    private static final int SC_MANAGER_LOCK = 8;
    private static final int SC_MANAGER_QUERY_LOCK_STATUS = 16;
    private static final int SC_MANAGER_MODIFY_BOOT_CONFIG = 32;
    private static final int SC_MANAGER_ALL_ACCESS = 983103;
    private static final int SERVICE_QUERY_CONFIG = 1;
    private static final int SERVICE_CHANGE_CONFIG = 2;
    private static final int SERVICE_QUERY_STATUS = 4;
    private static final int SERVICE_ENUMERATE_DEPENDENTS = 8;
    private static final int SERVICE_START = 16;
    private static final int SERVICE_STOP = 32;
    private static final int SERVICE_PAUSE_CONTINUE = 64;
    private static final int SERVICE_INTERROGATE = 128;
    private static final int SERVICE_USER_DEFINED_CONTROL = 256;
    private static final int SERVICE_ALL_ACCESS = 983551;
    private long manager;
    private long service;
    private String name;

    public static native List getServiceNames(Sigar sigar, String str) throws Win32Exception;

    private static native boolean ChangeServiceDescription(long j, String str);

    private static native boolean CloseServiceHandle(long j);

    private static native long CreateService(long j, String str, String str2, int i, int i2, int i3, String str3, String[] strArr, String str4, String str5) throws Win32Exception;

    private static native void ControlService(long j, int i) throws Win32Exception;

    private static native void DeleteService(long j) throws Win32Exception;

    private static native long OpenSCManager(String str, int i) throws Win32Exception;

    private static native long OpenService(long j, String str, int i) throws Win32Exception;

    private static native int QueryServiceStatus(long j);

    private static native boolean QueryServiceConfig(long j, ServiceConfig serviceConfig) throws Win32Exception;

    private Service() throws Win32Exception {
        this.manager = OpenSCManager("", SC_MANAGER_ALL_ACCESS);
    }

    public static List getServiceNames() throws Win32Exception {
        return getServiceNames(null, null);
    }

    public static List getServiceConfigs(Sigar sigar, String exe) throws Win32Exception {
        List services = new ArrayList();
        List names = getServiceNames(sigar, new StringBuffer().append("Service.Exe.Ieq=").append(exe).toString());
        for (int i = 0; i < names.size(); i++) {
            Service service = null;
            try {
                service = new Service((String) names.get(i));
                ServiceConfig config = service.getConfig();
                services.add(config);
                if (service != null) {
                    service.close();
                }
            } catch (Win32Exception e) {
                if (service != null) {
                    service.close();
                }
            } catch (Throwable th) {
                if (service != null) {
                    service.close();
                }
                throw th;
            }
        }
        return services;
    }

    public static List getServiceConfigs(String exe) throws Win32Exception {
        Sigar sigar = new Sigar();
        try {
            List serviceConfigs = getServiceConfigs(sigar, exe);
            sigar.close();
            return serviceConfigs;
        } catch (Throwable th) {
            sigar.close();
            throw th;
        }
    }

    public Service(String serviceName) throws Win32Exception {
        this();
        this.service = OpenService(this.manager, serviceName, SERVICE_ALL_ACCESS);
        this.name = serviceName;
    }

    protected void finalize() {
        close();
    }

    public synchronized void close() {
        if (this.service != 0) {
            CloseServiceHandle(this.service);
            this.service = 0L;
        }
        if (this.manager != 0) {
            CloseServiceHandle(this.manager);
            this.manager = 0L;
        }
    }

    public static Service create(ServiceConfig config) throws Win32Exception {
        if (config.getName() == null) {
            throw new IllegalArgumentException("name=null");
        }
        if (config.getPath() == null) {
            throw new IllegalArgumentException("path=null");
        }
        Service service = new Service();
        service.service = CreateService(service.manager, config.getName(), config.getDisplayName(), config.getType(), config.getStartType(), config.getErrorControl(), config.getPath(), config.getDependencies(), config.getStartName(), config.getPassword());
        if (config.getDescription() != null) {
            service.setDescription(config.getDescription());
        }
        return service;
    }

    public void delete() throws Win32Exception {
        DeleteService(this.service);
    }

    public void setDescription(String description) {
        ChangeServiceDescription(this.service, description);
    }

    public int status() {
        return getStatus();
    }

    public int getStatus() {
        return QueryServiceStatus(this.service);
    }

    public String getStatusString() {
        return STATUS[getStatus()];
    }

    private void control(int ctl) throws Win32Exception {
        ControlService(this.service, ctl);
    }

    /* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/win32/Service$Waiter.class */
    private static class Waiter {
        long start = System.currentTimeMillis();
        Service service;
        long timeout;
        int wantedState;
        int pendingState;

        Waiter(Service service, long timeout, int wantedState, int pendingState) {
            this.service = service;
            this.timeout = timeout;
            this.wantedState = wantedState;
            this.pendingState = pendingState;
        }

        boolean sleep() throws InterruptedException {
            int status;
            while (true) {
                status = this.service.getStatus();
                if (status != this.wantedState) {
                    if (status == this.pendingState) {
                        if (this.timeout > 0 && System.currentTimeMillis() - this.start >= this.timeout) {
                            break;
                        }
                        try {
                            Thread.sleep(100L);
                        } catch (InterruptedException e) {
                        }
                    } else {
                        return false;
                    }
                } else {
                    break;
                }
            }
            return status == this.wantedState;
        }
    }

    public void stop() throws Win32Exception {
        control(1);
    }

    public void stopAndWait(long timeout) throws Win32Exception {
        stop(timeout);
    }

    public void stop(long timeout) throws Win32Exception {
        stop();
        Waiter waiter = new Waiter(this, timeout, 1, 3);
        if (!waiter.sleep()) {
            throw new Win32Exception("Failed to stop service");
        }
    }

    public void start() throws Win32Exception {
        control(0);
    }

    public void start(long timeout) throws Win32Exception {
        start();
        Waiter waiter = new Waiter(this, timeout, 4, 2);
        if (!waiter.sleep()) {
            throw new Win32Exception("Failed to start service");
        }
    }

    public void pause() throws Win32Exception {
        control(2);
    }

    public void pause(long timeout) throws Win32Exception {
        pause();
        Waiter waiter = new Waiter(this, timeout, 7, 6);
        if (!waiter.sleep()) {
            throw new Win32Exception("Failed to pause service");
        }
    }

    public void resume() throws Win32Exception {
        control(3);
    }

    public ServiceConfig getConfig() throws Win32Exception {
        ServiceConfig config = new ServiceConfig();
        QueryServiceConfig(this.service, config);
        config.setName(this.name);
        return config;
    }

    public void list(PrintStream out) throws Win32Exception {
        getConfig().list(out);
        out.println(new StringBuffer().append("status........[").append(getStatusString()).append("]").toString());
    }

    public static void main(String[] args) throws Exception {
        List services;
        Sigar sigar;
        if (args.length == 0) {
            services = getServiceNames();
        } else {
            if (args.length == 2 && args[0].equals("-toggle")) {
                Service service = new Service(args[1]);
                if (service.getStatus() == 4) {
                    System.out.println("Stopping service...");
                    service.stop(5000L);
                } else {
                    System.out.println("Starting service...");
                    service.start(5000L);
                }
                System.out.println(service.getStatusString());
                return;
            }
            if (args.length == 1 && args[0].startsWith("Service.")) {
                sigar = new Sigar();
                try {
                    services = getServiceNames(sigar, args[0]);
                    sigar.close();
                } finally {
                }
            } else {
                if (args.length == 1 && args[0].endsWith(Win32.EXE_EXT)) {
                    sigar = new Sigar();
                    try {
                        List services2 = getServiceConfigs(args[0]);
                        sigar.close();
                        for (int i = 0; i < services2.size(); i++) {
                            ServiceConfig config = (ServiceConfig) services2.get(i);
                            config.list(System.out);
                            System.out.println("");
                        }
                        return;
                    } finally {
                    }
                }
                services = Arrays.asList(args);
            }
        }
        for (int i2 = 0; i2 < services.size(); i2++) {
            String name = (String) services.get(i2);
            new Service(name).list(System.out);
            System.out.println("");
        }
    }
}
