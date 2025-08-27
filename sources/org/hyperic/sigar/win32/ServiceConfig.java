package org.hyperic.sigar.win32;

import com.itextpdf.io.font.constants.FontStretches;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/win32/ServiceConfig.class */
public class ServiceConfig {
    public static final int START_BOOT = 0;
    public static final int START_SYSTEM = 1;
    public static final int START_AUTO = 2;
    public static final int START_MANUAL = 3;
    public static final int START_DISABLED = 4;
    public static final int TYPE_KERNEL_DRIVER = 1;
    public static final int TYPE_FILE_SYSTEM_DRIVER = 2;
    public static final int TYPE_ADAPTER = 4;
    public static final int TYPE_RECOGNIZER_DRIVER = 8;
    public static final int TYPE_WIN32_OWN_PROCESS = 16;
    public static final int TYPE_WIN32_SHARE_PROCESS = 32;
    public static final int TYPE_INTERACTIVE_PROCESS = 256;
    public static final int ERROR_IGNORE = 0;
    public static final int ERROR_NORMAL = 1;
    public static final int ERROR_SEVERE = 2;
    public static final int ERROR_CRITICAL = 3;
    private static final String[] START_TYPES = {"Boot", EventLog.SYSTEM, "Auto", "Manual", "Disabled"};
    private static final String[] ERROR_TYPES = {"Ignore", FontStretches.NORMAL, "Severe", "Critical"};
    int type;
    int startType;
    int errorControl;
    String path;
    String exe;
    String[] argv;
    String loadOrderGroup;
    int tagId;
    String[] dependencies;
    String startName;
    String displayName;
    String description;
    String password;
    String name;

    ServiceConfig() {
    }

    public ServiceConfig(String name) {
        this.name = name;
        this.type = 16;
        this.startType = 2;
        this.errorControl = 1;
        this.password = "";
    }

    public String getPath() {
        return this.path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String[] getArgv() {
        return this.argv;
    }

    public String getExe() {
        String[] argv;
        if (this.exe == null && (argv = getArgv()) != null && argv.length != 0) {
            this.exe = argv[0];
        }
        return this.exe;
    }

    public String[] getDependencies() {
        if (this.dependencies == null) {
            return new String[0];
        }
        return this.dependencies;
    }

    public void setDependencies(String[] dependencies) {
        this.dependencies = dependencies;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public int getErrorControl() {
        return this.errorControl;
    }

    public void setErrorControl(int errorControl) {
        this.errorControl = errorControl;
    }

    public String getErrorControlString() {
        return ERROR_TYPES[getErrorControl()];
    }

    public String getLoadOrderGroup() {
        return this.loadOrderGroup;
    }

    public void setLoadOrderGroup(String loadOrderGroup) {
        this.loadOrderGroup = loadOrderGroup;
    }

    public String getStartName() {
        return this.startName;
    }

    public void setStartName(String startName) {
        this.startName = startName;
    }

    public int getStartType() {
        return this.startType;
    }

    public void setStartType(int startType) {
        this.startType = startType;
    }

    public String getStartTypeString() {
        return START_TYPES[getStartType()];
    }

    public int getTagId() {
        return this.tagId;
    }

    public void setTagId(int tagId) {
        this.tagId = tagId;
    }

    public int getType() {
        return this.type;
    }

    public List getTypeList() {
        ArrayList types = new ArrayList();
        if ((this.type & 1) != 0) {
            types.add("Kernel Driver");
        }
        if ((this.type & 2) != 0) {
            types.add("File System Driver");
        }
        if ((this.type & 4) != 0) {
            types.add("Adapter");
        }
        if ((this.type & 8) != 0) {
            types.add("Recognizer Driver");
        }
        if ((this.type & 16) != 0) {
            types.add("Own Process");
        }
        if ((this.type & 32) != 0) {
            types.add("Share Process");
        }
        if ((this.type & 256) != 0) {
            types.add("Interactive Process");
        }
        return types;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void list(PrintStream out) throws Win32Exception {
        out.println(new StringBuffer().append("name..........[").append(getName()).append("]").toString());
        out.println(new StringBuffer().append("display.......[").append(getDisplayName()).append("]").toString());
        out.println(new StringBuffer().append("description...[").append(getDescription()).append("]").toString());
        out.println(new StringBuffer().append("start type....[").append(getStartTypeString()).append("]").toString());
        out.println(new StringBuffer().append("start name....[").append(getStartName()).append("]").toString());
        out.println(new StringBuffer().append("type..........").append(getTypeList()).toString());
        out.println(new StringBuffer().append("path..........[").append(getPath()).append("]").toString());
        out.println(new StringBuffer().append("exe...........[").append(getExe()).append("]").toString());
        out.println(new StringBuffer().append("deps..........").append(Arrays.asList(getDependencies())).toString());
        out.println(new StringBuffer().append("error ctl.....[").append(getErrorControlString()).append("]").toString());
    }
}
