package org.apache.poi.hssf.extractor;

import java.io.BufferedInputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.hssf.OldExcelFormatException;
import org.apache.poi.hssf.model.InternalWorkbook;
import org.apache.poi.hssf.record.BOFRecord;
import org.apache.poi.hssf.record.CodepageRecord;
import org.apache.poi.hssf.record.FormulaRecord;
import org.apache.poi.hssf.record.NumberRecord;
import org.apache.poi.hssf.record.OldFormulaRecord;
import org.apache.poi.hssf.record.OldLabelRecord;
import org.apache.poi.hssf.record.OldSheetRecord;
import org.apache.poi.hssf.record.OldStringRecord;
import org.apache.poi.hssf.record.RKRecord;
import org.apache.poi.hssf.record.RecordInputStream;
import org.apache.poi.poifs.filesystem.DirectoryNode;
import org.apache.poi.poifs.filesystem.DocumentNode;
import org.apache.poi.poifs.filesystem.NPOIFSFileSystem;
import org.apache.poi.poifs.filesystem.NotOLE2FileException;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.util.IOUtils;
import org.apache.poi.util.RecordFormatException;

/* loaded from: poi-3.17.jar:org/apache/poi/hssf/extractor/OldExcelExtractor.class */
public class OldExcelExtractor implements Closeable {
    private static final int FILE_PASS_RECORD_SID = 47;
    private RecordInputStream ris;
    private Closeable toClose;
    private int biffVersion;
    private int fileType;

    public OldExcelExtractor(InputStream input) throws IOException, RecordFormatException {
        open(input);
    }

    public OldExcelExtractor(File f) throws IOException {
        FileInputStream biffStream;
        NPOIFSFileSystem poifs = null;
        try {
            try {
                try {
                    poifs = new NPOIFSFileSystem(f);
                    open(poifs);
                    this.toClose = poifs;
                    if (this.toClose == null) {
                        IOUtils.closeQuietly(poifs);
                    }
                } catch (IOException e) {
                    throw e;
                } catch (OldExcelFormatException e2) {
                    if (this.toClose == null) {
                        IOUtils.closeQuietly(poifs);
                    }
                    biffStream = new FileInputStream(f);
                    try {
                        open(biffStream);
                    } catch (IOException e3) {
                        biffStream.close();
                        throw e3;
                    } catch (RuntimeException e4) {
                        biffStream.close();
                        throw e4;
                    }
                }
            } catch (RuntimeException e5) {
                throw e5;
            } catch (NotOLE2FileException e6) {
                if (this.toClose == null) {
                    IOUtils.closeQuietly(poifs);
                }
                biffStream = new FileInputStream(f);
                open(biffStream);
            }
        } catch (Throwable th) {
            if (this.toClose == null) {
                IOUtils.closeQuietly(poifs);
            }
            throw th;
        }
    }

    public OldExcelExtractor(NPOIFSFileSystem fs) throws IOException, RecordFormatException {
        open(fs);
    }

    public OldExcelExtractor(DirectoryNode directory) throws IOException, RecordFormatException {
        open(directory);
    }

    private void open(InputStream biffStream) throws IOException, RecordFormatException {
        BufferedInputStream bis = biffStream instanceof BufferedInputStream ? (BufferedInputStream) biffStream : new BufferedInputStream(biffStream, 8);
        if (NPOIFSFileSystem.hasPOIFSHeader(bis)) {
            NPOIFSFileSystem poifs = new NPOIFSFileSystem(bis);
            try {
                open(poifs);
                poifs.close();
                return;
            } catch (Throwable th) {
                poifs.close();
                throw th;
            }
        }
        this.ris = new RecordInputStream(bis);
        this.toClose = bis;
        prepare();
    }

    private void open(NPOIFSFileSystem fs) throws IOException, RecordFormatException {
        open(fs.getRoot());
    }

    private void open(DirectoryNode directory) throws IOException, RecordFormatException {
        DocumentNode book;
        try {
            book = (DocumentNode) directory.getEntry(InternalWorkbook.OLD_WORKBOOK_DIR_ENTRY_NAME);
        } catch (FileNotFoundException e) {
            book = (DocumentNode) directory.getEntry(InternalWorkbook.WORKBOOK_DIR_ENTRY_NAMES[0]);
        }
        if (book == null) {
            throw new IOException("No Excel 5/95 Book stream found");
        }
        this.ris = new RecordInputStream(directory.createDocumentInputStream(book));
        prepare();
    }

    public static void main(String[] args) throws IOException {
        if (args.length < 1) {
            System.err.println("Use:");
            System.err.println("   OldExcelExtractor <filename>");
            System.exit(1);
        }
        OldExcelExtractor extractor = new OldExcelExtractor(new File(args[0]));
        System.out.println(extractor.getText());
        extractor.close();
    }

    private void prepare() throws RecordFormatException {
        if (!this.ris.hasNextRecord()) {
            throw new IllegalArgumentException("File contains no records!");
        }
        this.ris.nextRecord();
        int bofSid = this.ris.getSid();
        switch (bofSid) {
            case 9:
                this.biffVersion = 2;
                break;
            case 521:
                this.biffVersion = 3;
                break;
            case 1033:
                this.biffVersion = 4;
                break;
            case 2057:
                this.biffVersion = 5;
                break;
            default:
                throw new IllegalArgumentException("File does not begin with a BOF, found sid of " + bofSid);
        }
        BOFRecord bof = new BOFRecord(this.ris);
        this.fileType = bof.getType();
    }

    public int getBiffVersion() {
        return this.biffVersion;
    }

    public int getFileType() {
        return this.fileType;
    }

    public String getText() throws IOException, RecordFormatException {
        StringBuffer text = new StringBuffer();
        CodepageRecord codepage = null;
        while (this.ris.hasNextRecord()) {
            int sid = this.ris.getNextSid();
            this.ris.nextRecord();
            switch (sid) {
                case 4:
                case 516:
                    OldLabelRecord lr = new OldLabelRecord(this.ris);
                    lr.setCodePage(codepage);
                    text.append(lr.getValue());
                    text.append('\n');
                    break;
                case 6:
                case 518:
                case 1030:
                    if (this.biffVersion == 5) {
                        FormulaRecord fr = new FormulaRecord(this.ris);
                        if (fr.getCachedResultType() != CellType.NUMERIC.getCode()) {
                            break;
                        } else {
                            handleNumericCell(text, fr.getValue());
                            break;
                        }
                    } else {
                        OldFormulaRecord fr2 = new OldFormulaRecord(this.ris);
                        if (fr2.getCachedResultType() != CellType.NUMERIC.getCode()) {
                            break;
                        } else {
                            handleNumericCell(text, fr2.getValue());
                            break;
                        }
                    }
                case 7:
                case 519:
                    OldStringRecord sr = new OldStringRecord(this.ris);
                    sr.setCodePage(codepage);
                    text.append(sr.getString());
                    text.append('\n');
                    break;
                case 47:
                    throw new EncryptedDocumentException("Encryption not supported for Old Excel files");
                case 66:
                    codepage = new CodepageRecord(this.ris);
                    break;
                case 133:
                    OldSheetRecord shr = new OldSheetRecord(this.ris);
                    shr.setCodePage(codepage);
                    text.append("Sheet: ");
                    text.append(shr.getSheetname());
                    text.append('\n');
                    break;
                case 515:
                    NumberRecord nr = new NumberRecord(this.ris);
                    handleNumericCell(text, nr.getValue());
                    break;
                case RKRecord.sid /* 638 */:
                    RKRecord rr = new RKRecord(this.ris);
                    handleNumericCell(text, rr.getRKNumber());
                    break;
                default:
                    this.ris.readFully(new byte[this.ris.remaining()]);
                    break;
            }
        }
        close();
        this.ris = null;
        return text.toString();
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        if (this.toClose != null) {
            IOUtils.closeQuietly(this.toClose);
            this.toClose = null;
        }
    }

    protected void handleNumericCell(StringBuffer text, double value) {
        text.append(value);
        text.append('\n');
    }
}
