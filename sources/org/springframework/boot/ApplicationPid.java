package org.springframework.boot;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

/* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/ApplicationPid.class */
public class ApplicationPid {
    private final String pid;

    public ApplicationPid() {
        this.pid = getPid();
    }

    protected ApplicationPid(String pid) {
        this.pid = pid;
    }

    private String getPid() {
        try {
            String jvmName = ManagementFactory.getRuntimeMXBean().getName();
            return jvmName.split("@")[0];
        } catch (Throwable th) {
            return null;
        }
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj != null && (obj instanceof ApplicationPid)) {
            return ObjectUtils.nullSafeEquals(this.pid, ((ApplicationPid) obj).pid);
        }
        return false;
    }

    public int hashCode() {
        return ObjectUtils.nullSafeHashCode(this.pid);
    }

    public String toString() {
        return this.pid != null ? this.pid : "???";
    }

    public void write(File file) throws IOException {
        Assert.state(this.pid != null, "No PID available");
        createParentFolder(file);
        FileWriter writer = new FileWriter(file);
        try {
            writer.append((CharSequence) this.pid);
        } finally {
            writer.close();
        }
    }

    private void createParentFolder(File file) {
        File parent = file.getParentFile();
        if (parent != null) {
            parent.mkdirs();
        }
    }
}
