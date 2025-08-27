package com.alibaba.excel.analysis.v03.handlers;

import com.alibaba.excel.analysis.v03.AbstractXlsRecordHandler;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.alibaba.excel.util.SheetUtils;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.poi.hssf.record.BOFRecord;
import org.apache.poi.hssf.record.BoundSheetRecord;
import org.apache.poi.hssf.record.Record;

/* loaded from: easyexcel-2.1.6.jar:com/alibaba/excel/analysis/v03/handlers/BofRecordHandler.class */
public class BofRecordHandler extends AbstractXlsRecordHandler {
    private List<BoundSheetRecord> boundSheetRecords = new ArrayList();
    private BoundSheetRecord[] orderedBsrs;
    private int sheetIndex;
    private List<ReadSheet> sheets;
    private Boolean readAll;
    private List<ReadSheet> readSheetList;
    private AnalysisContext context;
    private boolean alreadyInit;
    private boolean needInitSheet;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !BofRecordHandler.class.desiredAssertionStatus();
    }

    public BofRecordHandler(AnalysisContext context, List<ReadSheet> sheets, boolean alreadyInit, boolean needInitSheet) {
        this.context = context;
        this.sheets = sheets;
        this.alreadyInit = alreadyInit;
        this.needInitSheet = needInitSheet;
    }

    @Override // com.alibaba.excel.analysis.v03.XlsRecordHandler
    public boolean support(Record record) {
        return 133 == record.getSid() || 2057 == record.getSid();
    }

    @Override // com.alibaba.excel.analysis.v03.XlsRecordHandler
    public void processRecord(Record record) {
        if (record.getSid() == 133) {
            this.boundSheetRecords.add((BoundSheetRecord) record);
            return;
        }
        if (record.getSid() == 2057) {
            BOFRecord br = (BOFRecord) record;
            if (br.getType() == 16) {
                if (this.orderedBsrs == null) {
                    this.orderedBsrs = BoundSheetRecord.orderByBofPosition(this.boundSheetRecords);
                }
                String sheetName = this.orderedBsrs[this.sheetIndex].getSheetname();
                ReadSheet readSheet = null;
                if (!this.alreadyInit) {
                    readSheet = new ReadSheet(Integer.valueOf(this.sheetIndex), sheetName);
                    this.sheets.add(readSheet);
                }
                if (this.needInitSheet) {
                    if (readSheet == null) {
                        Iterator<ReadSheet> it = this.sheets.iterator();
                        while (true) {
                            if (!it.hasNext()) {
                                break;
                            }
                            ReadSheet sheet = it.next();
                            if (sheet.getSheetNo().intValue() == this.sheetIndex) {
                                readSheet = sheet;
                                break;
                            }
                        }
                    }
                    if (!$assertionsDisabled && readSheet == null) {
                        throw new AssertionError("Can't find the sheet.");
                    }
                    this.context.readWorkbookHolder().setIgnoreRecord03(Boolean.TRUE);
                    ReadSheet readSheet2 = SheetUtils.match(readSheet, this.readSheetList, this.readAll, this.context.readWorkbookHolder().getGlobalConfiguration());
                    if (readSheet2 != null) {
                        if (readSheet2.getSheetNo().intValue() != 0 && this.context.readSheetHolder() != null) {
                            this.context.readSheetHolder().notifyAfterAllAnalysed(this.context);
                        }
                        this.context.currentSheet(readSheet2);
                        this.context.readWorkbookHolder().setIgnoreRecord03(Boolean.FALSE);
                    }
                }
                this.sheetIndex++;
            }
        }
    }

    @Override // com.alibaba.excel.analysis.v03.XlsRecordHandler
    public void init() {
        this.sheetIndex = 0;
        this.orderedBsrs = null;
        this.boundSheetRecords.clear();
        if (!this.alreadyInit) {
            this.sheets.clear();
        }
    }

    public void init(List<ReadSheet> readSheetList, Boolean readAll) {
        this.readSheetList = readSheetList;
        this.readAll = readAll;
    }

    @Override // com.alibaba.excel.analysis.v03.XlsRecordHandler
    public int getOrder() {
        return 0;
    }
}
