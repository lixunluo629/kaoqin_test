package net.coobird.thumbnailator.name;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

/* loaded from: thumbnailator-0.4.8.jar:net/coobird/thumbnailator/name/ConsecutivelyNumberedFilenames.class */
public class ConsecutivelyNumberedFilenames implements Iterable<File> {
    private final Iterator<File> iter;

    public ConsecutivelyNumberedFilenames() {
        this.iter = new ConsecutivelyNumberedFilenamesIterator(new File("").getParentFile(), "%d", 0);
    }

    public ConsecutivelyNumberedFilenames(int i) {
        this.iter = new ConsecutivelyNumberedFilenamesIterator(new File("").getParentFile(), "%d", i);
    }

    public ConsecutivelyNumberedFilenames(File file) throws IOException {
        checkDirectory(file);
        this.iter = new ConsecutivelyNumberedFilenamesIterator(file, "%d", 0);
    }

    public ConsecutivelyNumberedFilenames(String str) {
        this.iter = new ConsecutivelyNumberedFilenamesIterator(new File("").getParentFile(), str, 0);
    }

    public ConsecutivelyNumberedFilenames(File file, int i) throws IOException {
        checkDirectory(file);
        this.iter = new ConsecutivelyNumberedFilenamesIterator(file, "%d", i);
    }

    public ConsecutivelyNumberedFilenames(File file, String str) throws IOException {
        checkDirectory(file);
        this.iter = new ConsecutivelyNumberedFilenamesIterator(file, str, 0);
    }

    public ConsecutivelyNumberedFilenames(String str, int i) {
        this.iter = new ConsecutivelyNumberedFilenamesIterator(new File("").getParentFile(), str, i);
    }

    public ConsecutivelyNumberedFilenames(File file, String str, int i) throws IOException {
        checkDirectory(file);
        this.iter = new ConsecutivelyNumberedFilenamesIterator(file, str, i);
    }

    private static void checkDirectory(File file) throws IOException {
        if (!file.isDirectory()) {
            throw new IOException("Specified path is not a directory or does not exist.");
        }
    }

    /* loaded from: thumbnailator-0.4.8.jar:net/coobird/thumbnailator/name/ConsecutivelyNumberedFilenames$ConsecutivelyNumberedFilenamesIterator.class */
    private static class ConsecutivelyNumberedFilenamesIterator implements Iterator<File> {
        private final File dir;
        private final String format;
        private int count;

        public ConsecutivelyNumberedFilenamesIterator(File file, String str, int i) {
            this.dir = file;
            this.format = str;
            this.count = i;
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return true;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.Iterator
        public File next() {
            File file = new File(this.dir, String.format(this.format, Integer.valueOf(this.count)));
            this.count++;
            return file;
        }

        @Override // java.util.Iterator
        public void remove() {
            throw new UnsupportedOperationException("Cannot remove elements from this iterator.");
        }
    }

    @Override // java.lang.Iterable
    public Iterator<File> iterator() {
        return this.iter;
    }
}
