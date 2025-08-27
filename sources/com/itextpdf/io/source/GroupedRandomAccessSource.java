package com.itextpdf.io.source;

import com.itextpdf.io.LogMessageConstant;
import java.io.IOException;
import java.io.Serializable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* loaded from: io-7.1.10.jar:com/itextpdf/io/source/GroupedRandomAccessSource.class */
class GroupedRandomAccessSource implements IRandomAccessSource, Serializable {
    private static final long serialVersionUID = 3417070797788862099L;
    private final SourceEntry[] sources;
    private SourceEntry currentSourceEntry;
    private final long size;

    public GroupedRandomAccessSource(IRandomAccessSource[] sources) throws IOException {
        this.sources = new SourceEntry[sources.length];
        long totalSize = 0;
        for (int i = 0; i < sources.length; i++) {
            this.sources[i] = new SourceEntry(i, sources[i], totalSize);
            totalSize += sources[i].length();
        }
        this.size = totalSize;
        this.currentSourceEntry = this.sources[sources.length - 1];
        sourceInUse(this.currentSourceEntry.source);
    }

    protected int getStartingSourceIndex(long offset) {
        if (offset >= this.currentSourceEntry.firstByte) {
            return this.currentSourceEntry.index;
        }
        return 0;
    }

    private SourceEntry getSourceEntryForOffset(long offset) throws IOException {
        if (offset >= this.size) {
            return null;
        }
        if (offset >= this.currentSourceEntry.firstByte && offset <= this.currentSourceEntry.lastByte) {
            return this.currentSourceEntry;
        }
        sourceReleased(this.currentSourceEntry.source);
        int startAt = getStartingSourceIndex(offset);
        for (int i = startAt; i < this.sources.length; i++) {
            if (offset >= this.sources[i].firstByte && offset <= this.sources[i].lastByte) {
                this.currentSourceEntry = this.sources[i];
                sourceInUse(this.currentSourceEntry.source);
                return this.currentSourceEntry;
            }
        }
        return null;
    }

    protected void sourceReleased(IRandomAccessSource source) throws IOException {
    }

    protected void sourceInUse(IRandomAccessSource source) throws IOException {
    }

    @Override // com.itextpdf.io.source.IRandomAccessSource
    public int get(long position) throws IOException {
        SourceEntry entry = getSourceEntryForOffset(position);
        if (entry == null) {
            return -1;
        }
        return entry.source.get(entry.offsetN(position));
    }

    @Override // com.itextpdf.io.source.IRandomAccessSource
    public int get(long position, byte[] bytes, int off, int len) throws IOException {
        int count;
        SourceEntry entry = getSourceEntryForOffset(position);
        if (entry == null) {
            return -1;
        }
        long offN = entry.offsetN(position);
        int remaining = len;
        while (remaining > 0 && entry != null && offN <= entry.source.length() && (count = entry.source.get(offN, bytes, off, remaining)) != -1) {
            off += count;
            position += count;
            remaining -= count;
            offN = 0;
            entry = getSourceEntryForOffset(position);
        }
        if (remaining == len) {
            return -1;
        }
        return len - remaining;
    }

    @Override // com.itextpdf.io.source.IRandomAccessSource
    public long length() {
        return this.size;
    }

    @Override // com.itextpdf.io.source.IRandomAccessSource
    public void close() throws IOException {
        IOException firstThrownIOExc = null;
        for (SourceEntry entry : this.sources) {
            try {
                entry.source.close();
            } catch (IOException ex) {
                if (firstThrownIOExc == null) {
                    firstThrownIOExc = ex;
                } else {
                    Logger logger = LoggerFactory.getLogger((Class<?>) GroupedRandomAccessSource.class);
                    logger.error(LogMessageConstant.ONE_OF_GROUPED_SOURCES_CLOSING_FAILED, (Throwable) ex);
                }
            } catch (Exception ex2) {
                Logger logger2 = LoggerFactory.getLogger((Class<?>) GroupedRandomAccessSource.class);
                logger2.error(LogMessageConstant.ONE_OF_GROUPED_SOURCES_CLOSING_FAILED, (Throwable) ex2);
            }
        }
        if (firstThrownIOExc != null) {
            throw firstThrownIOExc;
        }
    }

    /* loaded from: io-7.1.10.jar:com/itextpdf/io/source/GroupedRandomAccessSource$SourceEntry.class */
    private static class SourceEntry implements Serializable {
        private static final long serialVersionUID = 924305549309252826L;
        final IRandomAccessSource source;
        final long firstByte;
        final long lastByte;
        final int index;

        public SourceEntry(int index, IRandomAccessSource source, long offset) {
            this.index = index;
            this.source = source;
            this.firstByte = offset;
            this.lastByte = (offset + source.length()) - 1;
        }

        public long offsetN(long absoluteOffset) {
            return absoluteOffset - this.firstByte;
        }
    }
}
