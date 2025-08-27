package org.apache.commons.io.input;

/* loaded from: commons-io-2.18.0.jar:org/apache/commons/io/input/TailerListener.class */
public interface TailerListener {
    void fileNotFound();

    void fileRotated();

    void handle(Exception exc);

    void handle(String str);

    void init(Tailer tailer);
}
