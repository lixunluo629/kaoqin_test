package com.mysql.jdbc.util;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import com.mysql.jdbc.StringUtils;
import java.io.File;
import java.io.IOException;
import java.util.Properties;
import org.springframework.jdbc.datasource.init.ScriptUtils;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/util/ServerController.class */
public class ServerController {
    public static final String BASEDIR_KEY = "basedir";
    public static final String DATADIR_KEY = "datadir";
    public static final String DEFAULTS_FILE_KEY = "defaults-file";
    public static final String EXECUTABLE_NAME_KEY = "executable";
    public static final String EXECUTABLE_PATH_KEY = "executablePath";
    private Process serverProcess = null;
    private Properties serverProps = null;
    private Properties systemProps = null;

    public ServerController(String baseDir) {
        setBaseDir(baseDir);
    }

    public ServerController(String basedir, String datadir) {
    }

    public void setBaseDir(String baseDir) {
        getServerProps().setProperty(BASEDIR_KEY, baseDir);
    }

    public void setDataDir(String dataDir) {
        getServerProps().setProperty(DATADIR_KEY, dataDir);
    }

    public Process start() throws IOException {
        if (this.serverProcess != null) {
            throw new IllegalArgumentException("Server already started");
        }
        this.serverProcess = Runtime.getRuntime().exec(getCommandLine());
        return this.serverProcess;
    }

    public void stop(boolean forceIfNecessary) throws InterruptedException, IOException {
        if (this.serverProcess != null) {
            String basedir = getServerProps().getProperty(BASEDIR_KEY);
            StringBuilder pathBuf = new StringBuilder(basedir);
            if (!basedir.endsWith(File.separator)) {
                pathBuf.append(File.separator);
            }
            pathBuf.append("bin");
            pathBuf.append(File.separator);
            pathBuf.append("mysqladmin shutdown");
            System.out.println(pathBuf.toString());
            Process mysqladmin = Runtime.getRuntime().exec(pathBuf.toString());
            int exitStatus = -1;
            try {
                exitStatus = mysqladmin.waitFor();
            } catch (InterruptedException e) {
            }
            if (exitStatus != 0 && forceIfNecessary) {
                forceStop();
            }
        }
    }

    public void forceStop() {
        if (this.serverProcess != null) {
            this.serverProcess.destroy();
            this.serverProcess = null;
        }
    }

    public synchronized Properties getServerProps() {
        if (this.serverProps == null) {
            this.serverProps = new Properties();
        }
        return this.serverProps;
    }

    private String getCommandLine() {
        return getFullExecutablePath() + buildOptionalCommandLine();
    }

    private String getFullExecutablePath() {
        StringBuilder pathBuf = new StringBuilder();
        String optionalExecutablePath = getServerProps().getProperty(EXECUTABLE_PATH_KEY);
        if (optionalExecutablePath == null) {
            String basedir = getServerProps().getProperty(BASEDIR_KEY);
            pathBuf.append(basedir);
            if (!basedir.endsWith(File.separator)) {
                pathBuf.append(File.separatorChar);
            }
            if (runningOnWindows()) {
                pathBuf.append("bin");
            } else {
                pathBuf.append("libexec");
            }
            pathBuf.append(File.separatorChar);
        } else {
            pathBuf.append(optionalExecutablePath);
            if (!optionalExecutablePath.endsWith(File.separator)) {
                pathBuf.append(File.separatorChar);
            }
        }
        String executableName = getServerProps().getProperty(EXECUTABLE_NAME_KEY, "mysqld");
        pathBuf.append(executableName);
        return pathBuf.toString();
    }

    private String buildOptionalCommandLine() {
        StringBuilder commandLineBuf = new StringBuilder();
        if (this.serverProps != null) {
            for (String key : this.serverProps.keySet()) {
                String value = this.serverProps.getProperty(key);
                if (!isNonCommandLineArgument(key)) {
                    if (value != null && value.length() > 0) {
                        commandLineBuf.append(" \"");
                        commandLineBuf.append(ScriptUtils.DEFAULT_COMMENT_PREFIX);
                        commandLineBuf.append(key);
                        commandLineBuf.append(SymbolConstants.EQUAL_SYMBOL);
                        commandLineBuf.append(value);
                        commandLineBuf.append(SymbolConstants.QUOTES_SYMBOL);
                    } else {
                        commandLineBuf.append(" --");
                        commandLineBuf.append(key);
                    }
                }
            }
        }
        return commandLineBuf.toString();
    }

    private boolean isNonCommandLineArgument(String propName) {
        return propName.equals(EXECUTABLE_NAME_KEY) || propName.equals(EXECUTABLE_PATH_KEY);
    }

    private synchronized Properties getSystemProperties() {
        if (this.systemProps == null) {
            this.systemProps = System.getProperties();
        }
        return this.systemProps;
    }

    private boolean runningOnWindows() {
        return StringUtils.indexOfIgnoreCase(getSystemProperties().getProperty("os.name"), "WINDOWS") != -1;
    }
}
