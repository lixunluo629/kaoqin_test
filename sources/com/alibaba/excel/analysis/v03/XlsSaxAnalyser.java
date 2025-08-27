package com.alibaba.excel.analysis.v03;

import com.alibaba.excel.analysis.ExcelReadExecutor;
import com.alibaba.excel.analysis.v03.handlers.BlankOrErrorRecordHandler;
import com.alibaba.excel.analysis.v03.handlers.BofRecordHandler;
import com.alibaba.excel.analysis.v03.handlers.FormulaRecordHandler;
import com.alibaba.excel.analysis.v03.handlers.IndexRecordHandler;
import com.alibaba.excel.analysis.v03.handlers.LabelRecordHandler;
import com.alibaba.excel.analysis.v03.handlers.MissingCellDummyRecordHandler;
import com.alibaba.excel.analysis.v03.handlers.NoteRecordHandler;
import com.alibaba.excel.analysis.v03.handlers.NumberRecordHandler;
import com.alibaba.excel.analysis.v03.handlers.RkRecordHandler;
import com.alibaba.excel.analysis.v03.handlers.SstRecordHandler;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.exception.ExcelAnalysisException;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.read.listener.event.EachRowAnalysisFinishEvent;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.alibaba.excel.read.metadata.holder.ReadRowHolder;
import com.alibaba.excel.util.CollectionUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.poi.hssf.eventusermodel.EventWorkbookBuilder;
import org.apache.poi.hssf.eventusermodel.FormatTrackingHSSFListener;
import org.apache.poi.hssf.eventusermodel.HSSFEventFactory;
import org.apache.poi.hssf.eventusermodel.HSSFListener;
import org.apache.poi.hssf.eventusermodel.HSSFRequest;
import org.apache.poi.hssf.eventusermodel.MissingRecordAwareHSSFListener;
import org.apache.poi.hssf.eventusermodel.dummyrecord.LastCellOfRowDummyRecord;
import org.apache.poi.hssf.record.Record;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* loaded from: easyexcel-2.1.6.jar:com/alibaba/excel/analysis/v03/XlsSaxAnalyser.class */
public class XlsSaxAnalyser implements HSSFListener, ExcelReadExecutor {
    private static final Logger LOGGER = LoggerFactory.getLogger((Class<?>) XlsSaxAnalyser.class);
    private POIFSFileSystem poifsFileSystem;
    private Boolean readAll;
    private List<ReadSheet> readSheetList;
    private int lastRowNumber;
    private int lastColumnNumber;
    private EventWorkbookBuilder.SheetRecordCollectingListener workbookBuildingListener;
    private FormatTrackingHSSFListener formatListener;
    private List<ReadSheet> sheets;
    private HSSFWorkbook stubWorkbook;
    private AnalysisContext analysisContext;
    private List<XlsRecordHandler> recordHandlers = new ArrayList();
    private Map<Integer, CellData> records = new LinkedHashMap();

    public XlsSaxAnalyser(AnalysisContext context, POIFSFileSystem poifsFileSystem) {
        this.analysisContext = context;
        this.poifsFileSystem = poifsFileSystem;
        this.analysisContext.readWorkbookHolder().setPoifsFileSystem(poifsFileSystem);
    }

    @Override // com.alibaba.excel.analysis.ExcelReadExecutor
    public List<ReadSheet> sheetList() {
        if (this.sheets == null) {
            LOGGER.warn("Getting the 'sheetList' before reading will cause the file to be read twice.");
            XlsListSheetListener xlsListSheetListener = new XlsListSheetListener(this.analysisContext, this.poifsFileSystem);
            this.sheets = xlsListSheetListener.getSheetList();
        }
        return this.sheets;
    }

    @Override // com.alibaba.excel.analysis.ExcelReadExecutor
    public void execute(List<ReadSheet> readSheetList, Boolean readAll) {
        this.readAll = readAll;
        this.readSheetList = readSheetList;
        MissingRecordAwareHSSFListener listener = new MissingRecordAwareHSSFListener(this);
        this.formatListener = new FormatTrackingHSSFListener(listener);
        this.workbookBuildingListener = new EventWorkbookBuilder.SheetRecordCollectingListener(this.formatListener);
        if (this.workbookBuildingListener != null && this.stubWorkbook == null) {
            this.stubWorkbook = this.workbookBuildingListener.getStubHSSFWorkbook();
        }
        init();
        HSSFEventFactory factory = new HSSFEventFactory();
        HSSFRequest request = new HSSFRequest();
        request.addListenerForAllRecords(this.formatListener);
        try {
            factory.processWorkbookEvents(request, this.poifsFileSystem);
            if (!this.records.isEmpty()) {
                endRow();
            }
        } catch (IOException e) {
            throw new ExcelAnalysisException(e);
        }
    }

    private void init() {
        this.lastRowNumber = 0;
        this.lastColumnNumber = 0;
        this.records = new LinkedHashMap();
        buildXlsRecordHandlers();
    }

    @Override // org.apache.poi.hssf.eventusermodel.HSSFListener
    public void processRecord(Record record) {
        if (ignoreRecord(record)) {
            return;
        }
        int thisRow = -1;
        int thisColumn = -1;
        CellData cellData = null;
        Iterator<XlsRecordHandler> it = this.recordHandlers.iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            XlsRecordHandler handler = it.next();
            if (handler.support(record)) {
                handler.processRecord(record);
                thisRow = handler.getRow();
                thisColumn = handler.getColumn();
                cellData = handler.getCellData();
                if (cellData != null) {
                    cellData.checkEmpty();
                    if (CellDataTypeEnum.EMPTY != cellData.getType()) {
                        this.records.put(Integer.valueOf(thisColumn), cellData);
                    }
                }
            }
        }
        if (cellData != null && this.analysisContext.currentReadHolder().globalConfiguration().getAutoTrim().booleanValue() && CellDataTypeEnum.STRING == cellData.getType()) {
            cellData.setStringValue(cellData.getStringValue().trim());
        }
        if (thisRow != -1 && thisRow != this.lastRowNumber) {
            this.lastColumnNumber = -1;
        }
        if (thisRow > -1) {
            this.lastRowNumber = thisRow;
        }
        if (thisColumn > -1) {
            this.lastColumnNumber = thisColumn;
        }
        processLastCellOfRow(record);
    }

    private boolean ignoreRecord(Record record) {
        return (!this.analysisContext.readWorkbookHolder().getIgnoreRecord03().booleanValue() || record.getSid() == 133 || record.getSid() == 2057) ? false : true;
    }

    private void processLastCellOfRow(Record record) {
        if (record instanceof LastCellOfRowDummyRecord) {
            endRow();
        }
    }

    private void endRow() {
        if (this.lastColumnNumber == -1) {
            this.lastColumnNumber = 0;
        }
        this.analysisContext.readRowHolder(new ReadRowHolder(Integer.valueOf(this.lastRowNumber), this.analysisContext.readSheetHolder().getGlobalConfiguration()));
        this.analysisContext.readSheetHolder().notifyEndOneRow(new EachRowAnalysisFinishEvent(this.records), this.analysisContext);
        this.records.clear();
        this.lastColumnNumber = -1;
    }

    private void buildXlsRecordHandlers() {
        if (CollectionUtils.isEmpty(this.recordHandlers)) {
            this.recordHandlers.add(new BlankOrErrorRecordHandler());
            if (this.sheets == null) {
                this.sheets = new ArrayList();
                this.recordHandlers.add(new BofRecordHandler(this.analysisContext, this.sheets, false, true));
            } else {
                this.recordHandlers.add(new BofRecordHandler(this.analysisContext, this.sheets, true, true));
            }
            this.recordHandlers.add(new FormulaRecordHandler(this.stubWorkbook, this.formatListener));
            this.recordHandlers.add(new LabelRecordHandler());
            this.recordHandlers.add(new NoteRecordHandler());
            this.recordHandlers.add(new NumberRecordHandler(this.formatListener));
            this.recordHandlers.add(new RkRecordHandler());
            this.recordHandlers.add(new SstRecordHandler());
            this.recordHandlers.add(new MissingCellDummyRecordHandler());
            this.recordHandlers.add(new IndexRecordHandler(this.analysisContext));
            Collections.sort(this.recordHandlers);
        }
        for (XlsRecordHandler x : this.recordHandlers) {
            x.init();
            if (x instanceof BofRecordHandler) {
                BofRecordHandler bofRecordHandler = (BofRecordHandler) x;
                bofRecordHandler.init(this.readSheetList, this.readAll);
            }
        }
    }
}
