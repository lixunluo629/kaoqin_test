package org.apache.commons.io.file;

import java.util.stream.Stream;
import org.apache.commons.io.IOUtils;

/* loaded from: commons-io-2.18.0.jar:org/apache/commons/io/file/StandardDeleteOption.class */
public enum StandardDeleteOption implements DeleteOption {
    OVERRIDE_READ_ONLY;

    public static boolean overrideReadOnly(DeleteOption[] options) {
        if (IOUtils.length(options) == 0) {
            return false;
        }
        return Stream.of((Object[]) options).anyMatch(e -> {
            return OVERRIDE_READ_ONLY == e;
        });
    }
}
