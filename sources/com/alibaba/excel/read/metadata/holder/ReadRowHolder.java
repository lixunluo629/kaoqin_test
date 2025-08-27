package com.alibaba.excel.read.metadata.holder;

import com.alibaba.excel.enums.HolderEnum;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.Holder;

/* loaded from: easyexcel-2.1.6.jar:com/alibaba/excel/read/metadata/holder/ReadRowHolder.class */
public class ReadRowHolder implements Holder {
    private Integer rowIndex;
    private Object currentRowAnalysisResult;
    private GlobalConfiguration globalConfiguration;

    public ReadRowHolder(Integer rowIndex, GlobalConfiguration globalConfiguration) {
        this.rowIndex = rowIndex;
        this.globalConfiguration = globalConfiguration;
    }

    public GlobalConfiguration getGlobalConfiguration() {
        return this.globalConfiguration;
    }

    public void setGlobalConfiguration(GlobalConfiguration globalConfiguration) {
        this.globalConfiguration = globalConfiguration;
    }

    public Object getCurrentRowAnalysisResult() {
        return this.currentRowAnalysisResult;
    }

    public void setCurrentRowAnalysisResult(Object currentRowAnalysisResult) {
        this.currentRowAnalysisResult = currentRowAnalysisResult;
    }

    public Integer getRowIndex() {
        return this.rowIndex;
    }

    public void setRowIndex(Integer rowIndex) {
        this.rowIndex = rowIndex;
    }

    @Override // com.alibaba.excel.metadata.Holder
    public HolderEnum holderType() {
        return HolderEnum.ROW;
    }
}
