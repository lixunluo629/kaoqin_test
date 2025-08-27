package org.apache.commons.io.input;

import java.io.IOException;

/* loaded from: commons-io-2.18.0.jar:org/apache/commons/io/input/Input.class */
class Input {
    Input() {
    }

    static void checkOpen(boolean isOpen) throws IOException {
        if (!isOpen) {
            throw new IOException("Closed");
        }
    }
}
