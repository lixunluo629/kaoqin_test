package org.apache.poi.util;

import java.io.File;
import java.io.IOException;

/* loaded from: poi-3.17.jar:org/apache/poi/util/TempFileCreationStrategy.class */
public interface TempFileCreationStrategy {
    File createTempFile(String str, String str2) throws IOException;

    File createTempDirectory(String str) throws IOException;
}
