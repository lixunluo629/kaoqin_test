package net.coobird.thumbnailator.tasks;

import java.io.IOException;

/* loaded from: thumbnailator-0.4.8.jar:net/coobird/thumbnailator/tasks/UnsupportedFormatException.class */
public class UnsupportedFormatException extends IOException {
    private static final long serialVersionUID = 1254432584303852552L;
    private final String formatName;
    public static final String UNKNOWN = "<unknown>";

    public UnsupportedFormatException(String str) {
        this.formatName = str;
    }

    public UnsupportedFormatException(String str, String str2) {
        super(str2);
        this.formatName = str;
    }

    public String getFormatName() {
        return this.formatName;
    }
}
