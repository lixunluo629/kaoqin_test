package org.hyperic.jni;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.io.File;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.taskdefs.Copy;

/* loaded from: sigar-1.6.4.jar:org/hyperic/jni/CopyDependsTask.class */
public class CopyDependsTask extends Copy {
    private File depends;

    /* JADX INFO: Thrown type has an unknown type hierarchy: org.apache.tools.ant.BuildException */
    protected void validateAttributes() throws BuildException {
        super.validateAttributes();
        if (this.depends == null) {
            throw new BuildException("missing depends attribute");
        }
    }

    public void execute() throws BuildException {
        String state;
        if (this.destFile.exists()) {
            if (this.depends.lastModified() > this.destFile.lastModified()) {
                setOverwrite(true);
                state = "out of date";
            } else {
                state = "up to date";
            }
            log(new StringBuffer().append(this.destFile).append(SymbolConstants.SPACE_SYMBOL).append(state).append(" with ").append(this.depends).toString());
        }
        super.execute();
    }

    public File getDepends() {
        return this.depends;
    }

    public void setDepends(String depends) {
        this.depends = new File(depends);
    }
}
