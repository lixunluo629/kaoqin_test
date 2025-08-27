package org.apache.tomcat.util.scan;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.jar.Manifest;
import org.apache.tomcat.Jar;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/tomcat/util/scan/ReferenceCountedJar.class */
public class ReferenceCountedJar implements Jar {
    private final URL url;
    private Jar wrappedJar;
    private int referenceCount = 0;

    public ReferenceCountedJar(URL url) throws IOException {
        this.url = url;
        open();
    }

    private synchronized ReferenceCountedJar open() throws IOException {
        if (this.wrappedJar == null) {
            this.wrappedJar = JarFactory.newInstance(this.url);
        }
        this.referenceCount++;
        return this;
    }

    @Override // org.apache.tomcat.Jar, java.lang.AutoCloseable
    public synchronized void close() {
        this.referenceCount--;
        if (this.referenceCount == 0) {
            this.wrappedJar.close();
            this.wrappedJar = null;
        }
    }

    @Override // org.apache.tomcat.Jar
    public URL getJarFileURL() {
        return this.url;
    }

    @Override // org.apache.tomcat.Jar
    public InputStream getInputStream(String name) throws IOException {
        ReferenceCountedJar jar = open();
        Throwable th = null;
        try {
            try {
                InputStream inputStream = jar.wrappedJar.getInputStream(name);
                if (jar != null) {
                    if (0 != 0) {
                        try {
                            jar.close();
                        } catch (Throwable x2) {
                            th.addSuppressed(x2);
                        }
                    } else {
                        jar.close();
                    }
                }
                return inputStream;
            } finally {
            }
        } catch (Throwable th2) {
            if (jar != null) {
                if (th != null) {
                    try {
                        jar.close();
                    } catch (Throwable x22) {
                        th.addSuppressed(x22);
                    }
                } else {
                    jar.close();
                }
            }
            throw th2;
        }
    }

    @Override // org.apache.tomcat.Jar
    public long getLastModified(String name) throws IOException {
        ReferenceCountedJar jar = open();
        Throwable th = null;
        try {
            try {
                long lastModified = jar.wrappedJar.getLastModified(name);
                if (jar != null) {
                    if (0 != 0) {
                        try {
                            jar.close();
                        } catch (Throwable x2) {
                            th.addSuppressed(x2);
                        }
                    } else {
                        jar.close();
                    }
                }
                return lastModified;
            } finally {
            }
        } catch (Throwable th2) {
            if (jar != null) {
                if (th != null) {
                    try {
                        jar.close();
                    } catch (Throwable x22) {
                        th.addSuppressed(x22);
                    }
                } else {
                    jar.close();
                }
            }
            throw th2;
        }
    }

    @Override // org.apache.tomcat.Jar
    public boolean exists(String name) throws IOException {
        ReferenceCountedJar jar = open();
        Throwable th = null;
        try {
            try {
                boolean zExists = jar.wrappedJar.exists(name);
                if (jar != null) {
                    if (0 != 0) {
                        try {
                            jar.close();
                        } catch (Throwable x2) {
                            th.addSuppressed(x2);
                        }
                    } else {
                        jar.close();
                    }
                }
                return zExists;
            } finally {
            }
        } catch (Throwable th2) {
            if (jar != null) {
                if (th != null) {
                    try {
                        jar.close();
                    } catch (Throwable x22) {
                        th.addSuppressed(x22);
                    }
                } else {
                    jar.close();
                }
            }
            throw th2;
        }
    }

    @Override // org.apache.tomcat.Jar
    public void nextEntry() {
        try {
            ReferenceCountedJar jar = open();
            Throwable th = null;
            try {
                jar.wrappedJar.nextEntry();
                if (jar != null) {
                    if (0 != 0) {
                        try {
                            jar.close();
                        } catch (Throwable x2) {
                            th.addSuppressed(x2);
                        }
                    } else {
                        jar.close();
                    }
                }
            } finally {
            }
        } catch (IOException ioe) {
            throw new IllegalStateException(ioe);
        }
    }

    @Override // org.apache.tomcat.Jar
    public String getEntryName() {
        try {
            ReferenceCountedJar jar = open();
            Throwable th = null;
            try {
                String entryName = jar.wrappedJar.getEntryName();
                if (jar != null) {
                    if (0 != 0) {
                        try {
                            jar.close();
                        } catch (Throwable x2) {
                            th.addSuppressed(x2);
                        }
                    } else {
                        jar.close();
                    }
                }
                return entryName;
            } finally {
            }
        } catch (IOException ioe) {
            throw new IllegalStateException(ioe);
        }
    }

    @Override // org.apache.tomcat.Jar
    public InputStream getEntryInputStream() throws IOException {
        ReferenceCountedJar jar = open();
        Throwable th = null;
        try {
            InputStream entryInputStream = jar.wrappedJar.getEntryInputStream();
            if (jar != null) {
                if (0 != 0) {
                    try {
                        jar.close();
                    } catch (Throwable x2) {
                        th.addSuppressed(x2);
                    }
                } else {
                    jar.close();
                }
            }
            return entryInputStream;
        } catch (Throwable th2) {
            if (jar != null) {
                if (0 != 0) {
                    try {
                        jar.close();
                    } catch (Throwable x22) {
                        th.addSuppressed(x22);
                    }
                } else {
                    jar.close();
                }
            }
            throw th2;
        }
    }

    @Override // org.apache.tomcat.Jar
    public String getURL(String entry) {
        try {
            ReferenceCountedJar jar = open();
            Throwable th = null;
            try {
                try {
                    String url = jar.wrappedJar.getURL(entry);
                    if (jar != null) {
                        if (0 != 0) {
                            try {
                                jar.close();
                            } catch (Throwable x2) {
                                th.addSuppressed(x2);
                            }
                        } else {
                            jar.close();
                        }
                    }
                    return url;
                } finally {
                }
            } finally {
            }
        } catch (IOException ioe) {
            throw new IllegalStateException(ioe);
        }
    }

    @Override // org.apache.tomcat.Jar
    public Manifest getManifest() throws IOException {
        ReferenceCountedJar jar = open();
        Throwable th = null;
        try {
            Manifest manifest = jar.wrappedJar.getManifest();
            if (jar != null) {
                if (0 != 0) {
                    try {
                        jar.close();
                    } catch (Throwable x2) {
                        th.addSuppressed(x2);
                    }
                } else {
                    jar.close();
                }
            }
            return manifest;
        } catch (Throwable th2) {
            if (jar != null) {
                if (0 != 0) {
                    try {
                        jar.close();
                    } catch (Throwable x22) {
                        th.addSuppressed(x22);
                    }
                } else {
                    jar.close();
                }
            }
            throw th2;
        }
    }

    @Override // org.apache.tomcat.Jar
    public void reset() throws IOException {
        ReferenceCountedJar jar = open();
        Throwable th = null;
        try {
            jar.wrappedJar.reset();
            if (jar != null) {
                if (0 != 0) {
                    try {
                        jar.close();
                        return;
                    } catch (Throwable x2) {
                        th.addSuppressed(x2);
                        return;
                    }
                }
                jar.close();
            }
        } catch (Throwable th2) {
            if (jar != null) {
                if (0 != 0) {
                    try {
                        jar.close();
                    } catch (Throwable x22) {
                        th.addSuppressed(x22);
                    }
                } else {
                    jar.close();
                }
            }
            throw th2;
        }
    }

    @Override // org.apache.tomcat.Jar
    @Deprecated
    public boolean entryExists(String name) throws IOException {
        ReferenceCountedJar jar = open();
        Throwable th = null;
        try {
            try {
                boolean zEntryExists = jar.wrappedJar.entryExists(name);
                if (jar != null) {
                    if (0 != 0) {
                        try {
                            jar.close();
                        } catch (Throwable x2) {
                            th.addSuppressed(x2);
                        }
                    } else {
                        jar.close();
                    }
                }
                return zEntryExists;
            } finally {
            }
        } catch (Throwable th2) {
            if (jar != null) {
                if (th != null) {
                    try {
                        jar.close();
                    } catch (Throwable x22) {
                        th.addSuppressed(x22);
                    }
                } else {
                    jar.close();
                }
            }
            throw th2;
        }
    }
}
