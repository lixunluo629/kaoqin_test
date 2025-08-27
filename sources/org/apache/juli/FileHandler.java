package org.apache.juli;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.ErrorManager;
import java.util.logging.Filter;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.LogRecord;
import java.util.regex.Pattern;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/juli/FileHandler.class */
public class FileHandler extends Handler {
    public static final int DEFAULT_MAX_DAYS = -1;
    private static final ExecutorService DELETE_FILES_SERVICE = Executors.newSingleThreadExecutor(new ThreadFactory() { // from class: org.apache.juli.FileHandler.1
        private final boolean isSecurityEnabled;
        private final ThreadGroup group;
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private final String namePrefix = "FileHandlerLogFilesCleaner-";

        {
            SecurityManager s = System.getSecurityManager();
            if (s == null) {
                this.isSecurityEnabled = false;
                this.group = Thread.currentThread().getThreadGroup();
            } else {
                this.isSecurityEnabled = true;
                this.group = s.getThreadGroup();
            }
        }

        @Override // java.util.concurrent.ThreadFactory
        public Thread newThread(Runnable r) {
            final ClassLoader loader = Thread.currentThread().getContextClassLoader();
            try {
                if (this.isSecurityEnabled) {
                    AccessController.doPrivileged(new PrivilegedAction<Void>() { // from class: org.apache.juli.FileHandler.1.1
                        /* JADX WARN: Can't rename method to resolve collision */
                        @Override // java.security.PrivilegedAction
                        public Void run() {
                            Thread.currentThread().setContextClassLoader(getClass().getClassLoader());
                            return null;
                        }
                    });
                } else {
                    Thread.currentThread().setContextClassLoader(getClass().getClassLoader());
                }
                Thread t = new Thread(this.group, r, "FileHandlerLogFilesCleaner-" + this.threadNumber.getAndIncrement());
                t.setDaemon(true);
                if (this.isSecurityEnabled) {
                    AccessController.doPrivileged(new PrivilegedAction<Void>() { // from class: org.apache.juli.FileHandler.1.2
                        /* JADX WARN: Can't rename method to resolve collision */
                        @Override // java.security.PrivilegedAction
                        public Void run() {
                            Thread.currentThread().setContextClassLoader(loader);
                            return null;
                        }
                    });
                } else {
                    Thread.currentThread().setContextClassLoader(loader);
                }
                return t;
            } catch (Throwable th) {
                if (this.isSecurityEnabled) {
                    AccessController.doPrivileged(new PrivilegedAction<Void>() { // from class: org.apache.juli.FileHandler.1.2
                        /* JADX WARN: Can't rename method to resolve collision */
                        @Override // java.security.PrivilegedAction
                        public Void run() {
                            Thread.currentThread().setContextClassLoader(loader);
                            return null;
                        }
                    });
                } else {
                    Thread.currentThread().setContextClassLoader(loader);
                }
                throw th;
            }
        }
    });
    private volatile String date;
    private String directory;
    private String prefix;
    private String suffix;
    private boolean rotatable;
    private int maxDays;
    private volatile PrintWriter writer;
    protected final ReadWriteLock writerLock;
    private int bufferSize;
    private Pattern pattern;

    public FileHandler() {
        this(null, null, null, -1);
    }

    public FileHandler(String directory, String prefix, String suffix) {
        this(directory, prefix, suffix, -1);
    }

    public FileHandler(String directory, String prefix, String suffix, int maxDays) {
        this.date = "";
        this.directory = null;
        this.prefix = null;
        this.suffix = null;
        this.rotatable = true;
        this.maxDays = -1;
        this.writer = null;
        this.writerLock = new ReentrantReadWriteLock();
        this.bufferSize = -1;
        this.directory = directory;
        this.prefix = prefix;
        this.suffix = suffix;
        this.maxDays = maxDays;
        configure();
        openWriter();
        clean();
    }

    /* JADX WARN: Finally extract failed */
    @Override // java.util.logging.Handler
    public void publish(LogRecord record) {
        if (!isLoggable(record)) {
            return;
        }
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        String tsDate = ts.toString().substring(0, 10);
        this.writerLock.readLock().lock();
        try {
            if (this.rotatable && !this.date.equals(tsDate)) {
                this.writerLock.readLock().unlock();
                this.writerLock.writeLock().lock();
                try {
                    if (!this.date.equals(tsDate)) {
                        closeWriter();
                        this.date = tsDate;
                        openWriter();
                        clean();
                    }
                    this.writerLock.readLock().lock();
                    this.writerLock.writeLock().unlock();
                } catch (Throwable th) {
                    this.writerLock.readLock().lock();
                    this.writerLock.writeLock().unlock();
                    throw th;
                }
            }
            try {
                String result = getFormatter().format(record);
                try {
                    if (this.writer != null) {
                        this.writer.write(result);
                        if (this.bufferSize < 0) {
                            this.writer.flush();
                        }
                    } else {
                        reportError("FileHandler is closed or not yet initialized, unable to log [" + result + "]", null, 1);
                    }
                    this.writerLock.readLock().unlock();
                } catch (Exception e) {
                    reportError(null, e, 1);
                    this.writerLock.readLock().unlock();
                }
            } catch (Exception e2) {
                reportError(null, e2, 5);
                this.writerLock.readLock().unlock();
            }
        } catch (Throwable th2) {
            this.writerLock.readLock().unlock();
            throw th2;
        }
    }

    @Override // java.util.logging.Handler
    public void close() {
        closeWriter();
    }

    protected void closeWriter() {
        this.writerLock.writeLock().lock();
        try {
            try {
                if (this.writer == null) {
                    return;
                }
                this.writer.write(getFormatter().getTail(this));
                this.writer.flush();
                this.writer.close();
                this.writer = null;
                this.date = "";
                this.writerLock.writeLock().unlock();
            } catch (Exception e) {
                reportError(null, e, 3);
                this.writerLock.writeLock().unlock();
            }
        } finally {
            this.writerLock.writeLock().unlock();
        }
    }

    @Override // java.util.logging.Handler
    public void flush() {
        this.writerLock.readLock().lock();
        try {
            try {
                if (this.writer == null) {
                    return;
                }
                this.writer.flush();
                this.writerLock.readLock().unlock();
            } catch (Exception e) {
                reportError(null, e, 2);
                this.writerLock.readLock().unlock();
            }
        } finally {
            this.writerLock.readLock().unlock();
        }
    }

    private void configure() {
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        String tsString = ts.toString().substring(0, 19);
        this.date = tsString.substring(0, 10);
        String className = getClass().getName();
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        this.rotatable = Boolean.parseBoolean(getProperty(className + ".rotatable", "true"));
        if (this.directory == null) {
            this.directory = getProperty(className + ".directory", "logs");
        }
        if (this.prefix == null) {
            this.prefix = getProperty(className + ".prefix", "juli.");
        }
        if (this.suffix == null) {
            this.suffix = getProperty(className + ".suffix", ".log");
        }
        boolean shouldCheckForRedundantSeparator = (this.rotatable || this.prefix.isEmpty() || this.suffix.isEmpty()) ? false : true;
        if (shouldCheckForRedundantSeparator && this.prefix.charAt(this.prefix.length() - 1) == this.suffix.charAt(0)) {
            this.suffix = this.suffix.substring(1);
        }
        this.pattern = Pattern.compile("^(" + Pattern.quote(this.prefix) + ")\\d{4}-\\d{1,2}-\\d{1,2}(" + Pattern.quote(this.suffix) + ")$");
        String sMaxDays = getProperty(className + ".maxDays", String.valueOf(-1));
        if (this.maxDays <= 0) {
            try {
                this.maxDays = Integer.parseInt(sMaxDays);
            } catch (NumberFormatException e) {
            }
        }
        String sBufferSize = getProperty(className + ".bufferSize", String.valueOf(this.bufferSize));
        try {
            this.bufferSize = Integer.parseInt(sBufferSize);
        } catch (NumberFormatException e2) {
        }
        String encoding = getProperty(className + ".encoding", null);
        if (encoding != null && encoding.length() > 0) {
            try {
                setEncoding(encoding);
            } catch (UnsupportedEncodingException e3) {
            }
        }
        setLevel(Level.parse(getProperty(className + ".level", "" + Level.ALL)));
        String filterName = getProperty(className + ".filter", null);
        if (filterName != null) {
            try {
                setFilter((Filter) cl.loadClass(filterName).getConstructor(new Class[0]).newInstance(new Object[0]));
            } catch (Exception e4) {
            }
        }
        String formatterName = getProperty(className + ".formatter", null);
        if (formatterName != null) {
            try {
                setFormatter((Formatter) cl.loadClass(formatterName).getConstructor(new Class[0]).newInstance(new Object[0]));
            } catch (Exception e5) {
                setFormatter(new OneLineFormatter());
            }
        } else {
            setFormatter(new OneLineFormatter());
        }
        setErrorManager(new ErrorManager());
    }

    private String getProperty(String name, String defaultValue) {
        String value;
        String value2 = LogManager.getLogManager().getProperty(name);
        if (value2 == null) {
            value = defaultValue;
        } else {
            value = value2.trim();
        }
        return value;
    }

    protected void open() {
        openWriter();
    }

    protected void openWriter() {
        File dir = new File(this.directory);
        if (!dir.mkdirs() && !dir.isDirectory()) {
            reportError("Unable to create [" + dir + "]", null, 4);
            this.writer = null;
            return;
        }
        this.writerLock.writeLock().lock();
        FileOutputStream fos = null;
        OutputStream os = null;
        try {
            try {
                File pathname = new File(dir.getAbsoluteFile(), this.prefix + (this.rotatable ? this.date : "") + this.suffix);
                File parent = pathname.getParentFile();
                if (!parent.mkdirs() && !parent.isDirectory()) {
                    reportError("Unable to create [" + parent + "]", null, 4);
                    this.writer = null;
                    this.writerLock.writeLock().unlock();
                } else {
                    String encoding = getEncoding();
                    FileOutputStream fos2 = new FileOutputStream(pathname, true);
                    OutputStream os2 = this.bufferSize > 0 ? new BufferedOutputStream(fos2, this.bufferSize) : fos2;
                    this.writer = new PrintWriter((Writer) (encoding != null ? new OutputStreamWriter(os2, encoding) : new OutputStreamWriter(os2)), false);
                    this.writer.write(getFormatter().getHead(this));
                    this.writerLock.writeLock().unlock();
                }
            } catch (Exception e) {
                reportError(null, e, 4);
                this.writer = null;
                if (0 != 0) {
                    try {
                        fos.close();
                    } catch (IOException e2) {
                    }
                }
                if (0 != 0) {
                    try {
                        os.close();
                    } catch (IOException e3) {
                    }
                }
                this.writerLock.writeLock().unlock();
            }
        } catch (Throwable th) {
            this.writerLock.writeLock().unlock();
            throw th;
        }
    }

    private void clean() {
        if (this.maxDays <= 0) {
            return;
        }
        DELETE_FILES_SERVICE.submit(new Runnable() { // from class: org.apache.juli.FileHandler.2
            /* JADX WARN: Finally extract failed */
            @Override // java.lang.Runnable
            public void run() {
                try {
                    DirectoryStream<Path> files = FileHandler.this.streamFilesForDelete();
                    Throwable th = null;
                    try {
                        for (Path file : files) {
                            Files.delete(file);
                        }
                        if (files != null) {
                            if (0 != 0) {
                                try {
                                    files.close();
                                } catch (Throwable x2) {
                                    th.addSuppressed(x2);
                                }
                            } else {
                                files.close();
                            }
                        }
                    } catch (Throwable th2) {
                        if (files != null) {
                            if (0 != 0) {
                                try {
                                    files.close();
                                } catch (Throwable x22) {
                                    th.addSuppressed(x22);
                                }
                            } else {
                                files.close();
                            }
                        }
                        throw th2;
                    }
                } catch (IOException e) {
                    FileHandler.this.reportError("Unable to delete log files older than [" + FileHandler.this.maxDays + "] days", null, 0);
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public DirectoryStream<Path> streamFilesForDelete() throws IOException {
        final Date maxDaysOffset = getMaxDaysOffset();
        final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return Files.newDirectoryStream(new File(this.directory).toPath(), new DirectoryStream.Filter<Path>() { // from class: org.apache.juli.FileHandler.3
            @Override // java.nio.file.DirectoryStream.Filter
            public boolean accept(Path path) throws IOException {
                boolean result = false;
                String date = FileHandler.this.obtainDateFromPath(path);
                if (date != null) {
                    try {
                        Date dateFromFile = formatter.parse(date);
                        result = dateFromFile.before(maxDaysOffset);
                    } catch (ParseException e) {
                    }
                }
                return result;
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String obtainDateFromPath(Path path) {
        Path fileName = path.getFileName();
        if (fileName == null) {
            return null;
        }
        String date = fileName.toString();
        if (this.pattern.matcher(date).matches()) {
            String date2 = date.substring(this.prefix.length());
            return date2.substring(0, date2.length() - this.suffix.length());
        }
        return null;
    }

    private Date getMaxDaysOffset() {
        Calendar cal = Calendar.getInstance();
        cal.set(11, 0);
        cal.set(12, 0);
        cal.set(13, 0);
        cal.set(14, 0);
        cal.add(5, -this.maxDays);
        return cal.getTime();
    }
}
