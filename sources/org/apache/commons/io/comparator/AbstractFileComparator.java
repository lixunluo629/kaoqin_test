package org.apache.commons.io.comparator;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/* loaded from: commons-io-2.18.0.jar:org/apache/commons/io/comparator/AbstractFileComparator.class */
abstract class AbstractFileComparator implements Comparator<File> {
    AbstractFileComparator() {
    }

    public File[] sort(File... files) {
        if (files != null) {
            Arrays.sort(files, this);
        }
        return files;
    }

    public List<File> sort(List<File> files) {
        if (files != null) {
            files.sort(this);
        }
        return files;
    }

    public String toString() {
        return getClass().getSimpleName();
    }
}
