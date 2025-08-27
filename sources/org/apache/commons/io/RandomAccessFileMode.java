package org.apache.commons.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Objects;
import org.apache.commons.io.function.IOConsumer;
import org.apache.commons.io.function.IOFunction;

/* loaded from: commons-io-2.18.0.jar:org/apache/commons/io/RandomAccessFileMode.class */
public enum RandomAccessFileMode {
    READ_ONLY("r", 1),
    READ_WRITE(RW, 2),
    READ_WRITE_SYNC_ALL(RWS, 4),
    READ_WRITE_SYNC_CONTENT(RWD, 3);

    private static final String R = "r";
    private static final String RW = "rw";
    private static final String RWD = "rwd";
    private static final String RWS = "rws";
    private final int level;
    private final String mode;

    public static RandomAccessFileMode valueOf(OpenOption... openOption) {
        RandomAccessFileMode bestFit = READ_ONLY;
        for (OpenOption option : openOption) {
            if (option instanceof StandardOpenOption) {
                switch (AnonymousClass1.$SwitchMap$java$nio$file$StandardOpenOption[((StandardOpenOption) option).ordinal()]) {
                    case 1:
                        if (bestFit.implies(READ_WRITE)) {
                            break;
                        } else {
                            bestFit = READ_WRITE;
                            break;
                        }
                    case 2:
                        if (bestFit.implies(READ_WRITE_SYNC_CONTENT)) {
                            break;
                        } else {
                            bestFit = READ_WRITE_SYNC_CONTENT;
                            break;
                        }
                    case 3:
                        if (bestFit.implies(READ_WRITE_SYNC_ALL)) {
                            break;
                        } else {
                            bestFit = READ_WRITE_SYNC_ALL;
                            break;
                        }
                }
            }
        }
        return bestFit;
    }

    /* renamed from: org.apache.commons.io.RandomAccessFileMode$1, reason: invalid class name */
    /* loaded from: commons-io-2.18.0.jar:org/apache/commons/io/RandomAccessFileMode$1.class */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$java$nio$file$StandardOpenOption = new int[StandardOpenOption.values().length];

        static {
            try {
                $SwitchMap$java$nio$file$StandardOpenOption[StandardOpenOption.WRITE.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$java$nio$file$StandardOpenOption[StandardOpenOption.DSYNC.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$java$nio$file$StandardOpenOption[StandardOpenOption.SYNC.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
        }
    }

    public static RandomAccessFileMode valueOfMode(String mode) {
        switch (mode) {
            case "r":
                return READ_ONLY;
            case "rw":
                return READ_WRITE;
            case "rwd":
                return READ_WRITE_SYNC_CONTENT;
            case "rws":
                return READ_WRITE_SYNC_ALL;
            default:
                throw new IllegalArgumentException(mode);
        }
    }

    RandomAccessFileMode(String mode, int level) {
        this.mode = mode;
        this.level = level;
    }

    public void accept(Path file, IOConsumer<RandomAccessFile> consumer) throws IOException {
        RandomAccessFile raf = create(file);
        try {
            consumer.accept(raf);
            if (raf != null) {
                raf.close();
            }
        } catch (Throwable th) {
            if (raf != null) {
                try {
                    raf.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
            }
            throw th;
        }
    }

    public <T> T apply(Path file, IOFunction<RandomAccessFile, T> function) throws IOException {
        RandomAccessFile raf = create(file);
        try {
            T tApply = function.apply(raf);
            if (raf != null) {
                raf.close();
            }
            return tApply;
        } catch (Throwable th) {
            if (raf != null) {
                try {
                    raf.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
            }
            throw th;
        }
    }

    public RandomAccessFile create(File file) throws FileNotFoundException {
        return new IORandomAccessFile(file, this.mode);
    }

    public RandomAccessFile create(Path file) throws FileNotFoundException {
        return create((File) Objects.requireNonNull(file.toFile(), "file"));
    }

    public RandomAccessFile create(String name) throws FileNotFoundException {
        return new IORandomAccessFile(name, this.mode);
    }

    private int getLevel() {
        return this.level;
    }

    public String getMode() {
        return this.mode;
    }

    public boolean implies(RandomAccessFileMode other) {
        return getLevel() >= other.getLevel();
    }

    public IORandomAccessFile io(String name) throws FileNotFoundException {
        return new IORandomAccessFile(name, this.mode);
    }
}
