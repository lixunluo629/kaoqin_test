package lombok.delombok.ant;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import lombok.Lombok;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Location;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.Reference;

/* compiled from: DelombokTask.java */
/* loaded from: lombok-1.16.22.jar:lombok/delombok/ant/Tasks.class */
class Tasks {
    Tasks() {
    }

    /* compiled from: DelombokTask.java */
    /* loaded from: lombok-1.16.22.jar:lombok/delombok/ant/Tasks$Format.class */
    public static class Format {
        private String value;

        public int hashCode() {
            int result = (31 * 1) + (this.value == null ? 0 : this.value.hashCode());
            return result;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            Format other = (Format) obj;
            return this.value == null ? other.value == null : this.value.equals(other.value);
        }

        public String toString() {
            return "FormatOption [value=" + this.value + "]";
        }

        public String getValue() {
            return this.value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    /* compiled from: DelombokTask.java */
    /* loaded from: lombok-1.16.22.jar:lombok/delombok/ant/Tasks$Delombok.class */
    public static class Delombok extends Task {
        private File fromDir;
        private File toDir;
        private Path classpath;
        private Path sourcepath;
        private boolean verbose;
        private String encoding;
        private Path path;
        private List<Format> formatOptions = new ArrayList();
        private static ClassLoader shadowLoader;

        public void setClasspath(Path classpath) {
            if (this.classpath == null) {
                this.classpath = classpath;
            } else {
                this.classpath.append(classpath);
            }
        }

        public Path createClasspath() {
            if (this.classpath == null) {
                this.classpath = new Path(getProject());
            }
            return this.classpath.createPath();
        }

        public void setClasspathRef(Reference r) {
            createClasspath().setRefid(r);
        }

        public void setSourcepath(Path sourcepath) {
            if (this.sourcepath == null) {
                this.sourcepath = sourcepath;
            } else {
                this.sourcepath.append(sourcepath);
            }
        }

        public Path createSourcepath() {
            if (this.sourcepath == null) {
                this.sourcepath = new Path(getProject());
            }
            return this.sourcepath.createPath();
        }

        public void setSourcepathRef(Reference r) {
            createSourcepath().setRefid(r);
        }

        public void setFrom(File dir) {
            this.fromDir = dir;
        }

        public void setTo(File dir) {
            this.toDir = dir;
        }

        public void setVerbose(boolean verbose) {
            this.verbose = verbose;
        }

        public void setEncoding(String encoding) {
            this.encoding = encoding;
        }

        public void addFileset(FileSet set) {
            if (this.path == null) {
                this.path = new Path(getProject());
            }
            this.path.add(set);
        }

        public Format createFormat() {
            return new Format();
        }

        public void addFormat(Format format) {
            this.formatOptions.add(format);
        }

        public static Class<?> shadowLoadClass(String name) throws NoSuchMethodException, ClassNotFoundException, SecurityException {
            try {
                if (shadowLoader == null) {
                    try {
                        Class.forName("lombok.core.LombokNode");
                        shadowLoader = Delombok.class.getClassLoader();
                    } catch (ClassNotFoundException e) {
                        Class<?> launcherMain = Class.forName("lombok.launch.Main");
                        Method m = launcherMain.getDeclaredMethod("createShadowClassLoader", new Class[0]);
                        m.setAccessible(true);
                        shadowLoader = (ClassLoader) m.invoke(null, new Object[0]);
                    }
                }
                return Class.forName(name, true, shadowLoader);
            } catch (Exception e2) {
                throw Lombok.sneakyThrow(e2);
            }
        }

        /* JADX INFO: Thrown type has an unknown type hierarchy: org.apache.tools.ant.BuildException */
        public void execute() throws IllegalAccessException, NoSuchFieldException, NoSuchMethodException, InstantiationException, BuildException, SecurityException, IllegalArgumentException, InvocationTargetException {
            Location loc = getLocation();
            try {
                Object instance = shadowLoadClass("lombok.delombok.ant.DelombokTaskImpl").newInstance();
                for (Field selfField : getClass().getDeclaredFields()) {
                    if (!selfField.isSynthetic() && !Modifier.isStatic(selfField.getModifiers())) {
                        Field otherField = instance.getClass().getDeclaredField(selfField.getName());
                        otherField.setAccessible(true);
                        selfField.setAccessible(true);
                        if (selfField.getName().equals("formatOptions")) {
                            List<String> rep = new ArrayList<>();
                            for (Format f : this.formatOptions) {
                                if (f.getValue() == null) {
                                    throw new BuildException("'value' property required for <format>");
                                }
                                rep.add(f.getValue());
                            }
                            otherField.set(instance, rep);
                        } else {
                            otherField.set(instance, selfField.get(this));
                        }
                    }
                }
                Method m = instance.getClass().getMethod("execute", Location.class);
                m.setAccessible(true);
                m.invoke(instance, loc);
            } catch (InvocationTargetException e) {
                throw Lombok.sneakyThrow(e.getCause());
            } catch (Exception e2) {
                throw Lombok.sneakyThrow(e2);
            }
        }
    }
}
