package com.alibaba.excel.analysis.v03;

import com.alibaba.excel.analysis.v03.handlers.BofRecordHandler;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.exception.ExcelAnalysisException;
import com.alibaba.excel.read.metadata.ReadSheet;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.hssf.eventusermodel.EventWorkbookBuilder;
import org.apache.poi.hssf.eventusermodel.FormatTrackingHSSFListener;
import org.apache.poi.hssf.eventusermodel.HSSFEventFactory;
import org.apache.poi.hssf.eventusermodel.HSSFListener;
import org.apache.poi.hssf.eventusermodel.HSSFRequest;
import org.apache.poi.hssf.eventusermodel.MissingRecordAwareHSSFListener;
import org.apache.poi.hssf.record.Record;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

/* loaded from: easyexcel-2.1.6.jar:com/alibaba/excel/analysis/v03/XlsListSheetListener.class */
public class XlsListSheetListener implements HSSFListener {
    private POIFSFileSystem poifsFileSystem;
    private List<ReadSheet> sheetList = new ArrayList();
    private BofRecordHandler bofRecordHandler;

    public XlsListSheetListener(AnalysisContext analysisContext, POIFSFileSystem poifsFileSystem) {
        this.poifsFileSystem = poifsFileSystem;
        this.bofRecordHandler = new BofRecordHandler(analysisContext, this.sheetList, false, false);
        this.bofRecordHandler.init();
        this.bofRecordHandler.init(null, true);
    }

    @Override // org.apache.poi.hssf.eventusermodel.HSSFListener
    public void processRecord(Record record) {
        this.bofRecordHandler.processRecord(record);
    }

    public List<ReadSheet> getSheetList() {
        MissingRecordAwareHSSFListener listener = new MissingRecordAwareHSSFListener(this);
        HSSFListener formatListener = new FormatTrackingHSSFListener(listener);
        HSSFEventFactory factory = new HSSFEventFactory();
        HSSFRequest request = new HSSFRequest();
        EventWorkbookBuilder.SheetRecordCollectingListener workbookBuildingListener = new EventWorkbookBuilder.SheetRecordCollectingListener(formatListener);
        request.addListenerForAllRecords(workbookBuildingListener);
        try {
            factory.processWorkbookEvents(request, this.poifsFileSystem);
            return this.sheetList;
        } catch (IOException e) {
            throw new ExcelAnalysisException(e);
        }
    }
}
