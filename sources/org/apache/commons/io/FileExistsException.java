package org.apache.commons.io;

import java.io.File;
import java.io.IOException;

/* loaded from: commons-io-2.18.0.jar:org/apache/commons/io/FileExistsException.class */
public class FileExistsException extends IOException {
    private static final long serialVersionUID = 1;

    public FileExistsException() {
    }

    public FileExistsException(File file) {
        super("File " + file + " exists");
    }

    public FileExistsException(String message) {
        super(message);
    }
}
