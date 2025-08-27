package org.apache.poi.hssf.record.cont;

import org.apache.poi.util.DelayableLittleEndianOutput;
import org.apache.poi.util.LittleEndianOutput;
import org.apache.poi.util.StringUtil;

/* loaded from: poi-3.17.jar:org/apache/poi/hssf/record/cont/ContinuableRecordOutput.class */
public final class ContinuableRecordOutput implements LittleEndianOutput {
    private final LittleEndianOutput _out;
    private UnknownLengthRecordOutput _ulrOutput;
    private int _totalPreviousRecordsSize = 0;
    private static final LittleEndianOutput NOPOutput = new DelayableLittleEndianOutput() { // from class: org.apache.poi.hssf.record.cont.ContinuableRecordOutput.1
        @Override // org.apache.poi.util.DelayableLittleEndianOutput
        public LittleEndianOutput createDelayedOutput(int size) {
            return this;
        }

        @Override // org.apache.poi.util.LittleEndianOutput
        public void write(byte[] b) {
        }

        @Override // org.apache.poi.util.LittleEndianOutput
        public void write(byte[] b, int offset, int len) {
        }

        @Override // org.apache.poi.util.LittleEndianOutput
        public void writeByte(int v) {
        }

        @Override // org.apache.poi.util.LittleEndianOutput
        public void writeDouble(double v) {
        }

        @Override // org.apache.poi.util.LittleEndianOutput
        public void writeInt(int v) {
        }

        @Override // org.apache.poi.util.LittleEndianOutput
        public void writeLong(long v) {
        }

        @Override // org.apache.poi.util.LittleEndianOutput
        public void writeShort(int v) {
        }
    };

    public ContinuableRecordOutput(LittleEndianOutput out, int sid) {
        this._ulrOutput = new UnknownLengthRecordOutput(out, sid);
        this._out = out;
    }

    public static ContinuableRecordOutput createForCountingOnly() {
        return new ContinuableRecordOutput(NOPOutput, -777);
    }

    public int getTotalSize() {
        return this._totalPreviousRecordsSize + this._ulrOutput.getTotalSize();
    }

    void terminate() {
        this._ulrOutput.terminate();
    }

    public int getAvailableSpace() {
        return this._ulrOutput.getAvailableSpace();
    }

    public void writeContinue() {
        this._ulrOutput.terminate();
        this._totalPreviousRecordsSize += this._ulrOutput.getTotalSize();
        this._ulrOutput = new UnknownLengthRecordOutput(this._out, 60);
    }

    public void writeContinueIfRequired(int requiredContinuousSize) {
        if (this._ulrOutput.getAvailableSpace() < requiredContinuousSize) {
            writeContinue();
        }
    }

    public void writeStringData(String text) {
        boolean is16bitEncoded = StringUtil.hasMultibyte(text);
        int keepTogetherSize = 2;
        int optionFlags = 0;
        if (is16bitEncoded) {
            optionFlags = 0 | 1;
            keepTogetherSize = 2 + 1;
        }
        writeContinueIfRequired(keepTogetherSize);
        writeByte(optionFlags);
        writeCharacterData(text, is16bitEncoded);
    }

    public void writeString(String text, int numberOfRichTextRuns, int extendedDataSize) {
        boolean is16bitEncoded = StringUtil.hasMultibyte(text);
        int keepTogetherSize = 4;
        int optionFlags = 0;
        if (is16bitEncoded) {
            optionFlags = 0 | 1;
            keepTogetherSize = 4 + 1;
        }
        if (numberOfRichTextRuns > 0) {
            optionFlags |= 8;
            keepTogetherSize += 2;
        }
        if (extendedDataSize > 0) {
            optionFlags |= 4;
            keepTogetherSize += 4;
        }
        writeContinueIfRequired(keepTogetherSize);
        writeShort(text.length());
        writeByte(optionFlags);
        if (numberOfRichTextRuns > 0) {
            writeShort(numberOfRichTextRuns);
        }
        if (extendedDataSize > 0) {
            writeInt(extendedDataSize);
        }
        writeCharacterData(text, is16bitEncoded);
    }

    private void writeCharacterData(String text, boolean is16bitEncoded) {
        int nChars = text.length();
        int i = 0;
        if (is16bitEncoded) {
            while (true) {
                for (int nWritableChars = Math.min(nChars - i, this._ulrOutput.getAvailableSpace() / 2); nWritableChars > 0; nWritableChars--) {
                    int i2 = i;
                    i++;
                    this._ulrOutput.writeShort(text.charAt(i2));
                }
                if (i < nChars) {
                    writeContinue();
                    writeByte(1);
                } else {
                    return;
                }
            }
        } else {
            while (true) {
                for (int nWritableChars2 = Math.min(nChars - i, this._ulrOutput.getAvailableSpace() / 1); nWritableChars2 > 0; nWritableChars2--) {
                    int i3 = i;
                    i++;
                    this._ulrOutput.writeByte(text.charAt(i3));
                }
                if (i < nChars) {
                    writeContinue();
                    writeByte(0);
                } else {
                    return;
                }
            }
        }
    }

    @Override // org.apache.poi.util.LittleEndianOutput
    public void write(byte[] b) {
        writeContinueIfRequired(b.length);
        this._ulrOutput.write(b);
    }

    @Override // org.apache.poi.util.LittleEndianOutput
    public void write(byte[] b, int offset, int len) {
        int i = 0;
        while (true) {
            for (int nWritableChars = Math.min(len - i, this._ulrOutput.getAvailableSpace() / 1); nWritableChars > 0; nWritableChars--) {
                int i2 = i;
                i++;
                this._ulrOutput.writeByte(b[offset + i2]);
            }
            if (i < len) {
                writeContinue();
            } else {
                return;
            }
        }
    }

    @Override // org.apache.poi.util.LittleEndianOutput
    public void writeByte(int v) {
        writeContinueIfRequired(1);
        this._ulrOutput.writeByte(v);
    }

    @Override // org.apache.poi.util.LittleEndianOutput
    public void writeDouble(double v) {
        writeContinueIfRequired(8);
        this._ulrOutput.writeDouble(v);
    }

    @Override // org.apache.poi.util.LittleEndianOutput
    public void writeInt(int v) {
        writeContinueIfRequired(4);
        this._ulrOutput.writeInt(v);
    }

    @Override // org.apache.poi.util.LittleEndianOutput
    public void writeLong(long v) {
        writeContinueIfRequired(8);
        this._ulrOutput.writeLong(v);
    }

    @Override // org.apache.poi.util.LittleEndianOutput
    public void writeShort(int v) {
        writeContinueIfRequired(2);
        this._ulrOutput.writeShort(v);
    }
}
