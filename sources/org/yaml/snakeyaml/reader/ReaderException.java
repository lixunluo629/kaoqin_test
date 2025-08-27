package org.yaml.snakeyaml.reader;

import org.yaml.snakeyaml.error.YAMLException;

/* loaded from: snakeyaml-1.17.jar:org/yaml/snakeyaml/reader/ReaderException.class */
public class ReaderException extends YAMLException {
    private static final long serialVersionUID = 8710781187529689083L;
    private final String name;
    private final char character;
    private final int position;

    public ReaderException(String name, int position, char character, String message) {
        super(message);
        this.name = name;
        this.character = character;
        this.position = position;
    }

    public String getName() {
        return this.name;
    }

    public char getCharacter() {
        return this.character;
    }

    public int getPosition() {
        return this.position;
    }

    @Override // java.lang.Throwable
    public String toString() {
        return "unacceptable character '" + this.character + "' (0x" + Integer.toHexString(this.character).toUpperCase() + ") " + getMessage() + "\nin \"" + this.name + "\", position " + this.position;
    }
}
