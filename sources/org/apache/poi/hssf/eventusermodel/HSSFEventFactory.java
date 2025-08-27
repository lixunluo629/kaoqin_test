package org.apache.poi.hssf.eventusermodel;

import java.io.IOException;
import java.io.InputStream;
import java.util.Set;
import org.apache.poi.hssf.model.InternalWorkbook;
import org.apache.poi.hssf.record.Record;
import org.apache.poi.hssf.record.RecordFactoryInputStream;
import org.apache.poi.poifs.filesystem.DirectoryNode;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

/* loaded from: poi-3.17.jar:org/apache/poi/hssf/eventusermodel/HSSFEventFactory.class */
public class HSSFEventFactory {
    public void processWorkbookEvents(HSSFRequest req, POIFSFileSystem fs) throws IOException {
        processWorkbookEvents(req, fs.getRoot());
    }

    public void processWorkbookEvents(HSSFRequest req, DirectoryNode dir) throws IOException {
        String name = null;
        Set<String> entryNames = dir.getEntryNames();
        String[] arr$ = InternalWorkbook.WORKBOOK_DIR_ENTRY_NAMES;
        int len$ = arr$.length;
        int i$ = 0;
        while (true) {
            if (i$ >= len$) {
                break;
            }
            String potentialName = arr$[i$];
            if (!entryNames.contains(potentialName)) {
                i$++;
            } else {
                name = potentialName;
                break;
            }
        }
        if (name == null) {
            name = InternalWorkbook.WORKBOOK_DIR_ENTRY_NAMES[0];
        }
        InputStream in = dir.createDocumentInputStream(name);
        try {
            processEvents(req, in);
            in.close();
        } catch (Throwable th) {
            in.close();
            throw th;
        }
    }

    public short abortableProcessWorkbookEvents(HSSFRequest req, POIFSFileSystem fs) throws HSSFUserException, IOException {
        return abortableProcessWorkbookEvents(req, fs.getRoot());
    }

    public short abortableProcessWorkbookEvents(HSSFRequest req, DirectoryNode dir) throws HSSFUserException, IOException {
        InputStream in = dir.createDocumentInputStream("Workbook");
        try {
            short sAbortableProcessEvents = abortableProcessEvents(req, in);
            in.close();
            return sAbortableProcessEvents;
        } catch (Throwable th) {
            in.close();
            throw th;
        }
    }

    public void processEvents(HSSFRequest req, InputStream in) {
        try {
            genericProcessEvents(req, in);
        } catch (HSSFUserException e) {
        }
    }

    public short abortableProcessEvents(HSSFRequest req, InputStream in) throws HSSFUserException {
        return genericProcessEvents(req, in);
    }

    private short genericProcessEvents(HSSFRequest req, InputStream in) throws HSSFUserException {
        short userCode = 0;
        RecordFactoryInputStream recordStream = new RecordFactoryInputStream(in, false);
        do {
            Record r = recordStream.nextRecord();
            if (r == null) {
                break;
            }
            userCode = req.processRecord(r);
        } while (userCode == 0);
        return userCode;
    }
}
