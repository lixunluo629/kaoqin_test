package lombok.delombok.ant;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.UnsupportedCharsetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import lombok.delombok.Delombok;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Location;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.resources.FileResource;

/* loaded from: lombok-1.16.22.jar:lombok/delombok/ant/DelombokTaskImpl.SCL.lombok */
public class DelombokTaskImpl {
    private File fromDir;
    private File toDir;
    private Path classpath;
    private Path sourcepath;
    private boolean verbose;
    private String encoding;
    private Path path;
    private List<String> formatOptions = new ArrayList();

    /* JADX INFO: Thrown type has an unknown type hierarchy: org.apache.tools.ant.BuildException */
    public void execute(Location location) throws IllegalAccessException, BuildException, ClassNotFoundException, IllegalArgumentException, InvocationTargetException {
        if (this.fromDir == null && this.path == null) {
            throw new BuildException("Either 'from' attribute, or nested <fileset> tags are required.");
        }
        if (this.fromDir != null && this.path != null) {
            throw new BuildException("You can't specify both 'from' attribute and nested filesets. You need one or the other.");
        }
        if (this.toDir == null) {
            throw new BuildException("The to attribute is required.");
        }
        Delombok delombok = new Delombok();
        if (this.verbose) {
            delombok.setVerbose(true);
        }
        try {
            if (this.encoding != null) {
                delombok.setCharset(this.encoding);
            }
            if (this.classpath != null) {
                delombok.setClasspath(this.classpath.toString());
            }
            if (this.sourcepath != null) {
                delombok.setSourcepath(this.sourcepath.toString());
            }
            try {
                delombok.setFormatPreferences(Delombok.formatOptionsToMap(this.formatOptions));
                delombok.setOutput(this.toDir);
                try {
                    if (this.fromDir != null) {
                        delombok.addDirectory(this.fromDir);
                    } else {
                        Iterator<?> it = this.path.iterator();
                        while (it.hasNext()) {
                            FileResource fileResource = (FileResource) it.next();
                            File baseDir = fileResource.getBaseDir();
                            if (baseDir == null) {
                                File file = fileResource.getFile();
                                delombok.addFile(file.getParentFile(), file.getName());
                            } else {
                                delombok.addFile(baseDir, fileResource.getName());
                            }
                        }
                    }
                    delombok.delombok();
                } catch (IOException e) {
                    throw new BuildException("I/O problem during delombok", e, location);
                }
            } catch (Delombok.InvalidFormatOptionException e2) {
                throw new BuildException(e2.getMessage() + " Run java -jar lombok.jar --format-help for detailed format help.");
            }
        } catch (UnsupportedCharsetException e3) {
            throw new BuildException("Unknown charset: " + this.encoding, location);
        }
    }
}
