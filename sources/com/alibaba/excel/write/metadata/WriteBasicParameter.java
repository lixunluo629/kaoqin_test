package com.alibaba.excel.write.metadata;

import com.alibaba.excel.metadata.BasicParameter;
import com.alibaba.excel.write.handler.WriteHandler;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/* loaded from: easyexcel-2.1.6.jar:com/alibaba/excel/write/metadata/WriteBasicParameter.class */
public class WriteBasicParameter extends BasicParameter {
    private Integer relativeHeadRowIndex;
    private Boolean needHead;
    private List<WriteHandler> customWriteHandlerList = new ArrayList();
    private Boolean useDefaultStyle;
    private Boolean automaticMergeHead;
    private Collection<Integer> excludeColumnIndexes;
    private Collection<String> excludeColumnFiledNames;
    private Collection<Integer> includeColumnIndexes;
    private Collection<String> includeColumnFiledNames;

    public Integer getRelativeHeadRowIndex() {
        return this.relativeHeadRowIndex;
    }

    public void setRelativeHeadRowIndex(Integer relativeHeadRowIndex) {
        this.relativeHeadRowIndex = relativeHeadRowIndex;
    }

    public Boolean getNeedHead() {
        return this.needHead;
    }

    public void setNeedHead(Boolean needHead) {
        this.needHead = needHead;
    }

    public List<WriteHandler> getCustomWriteHandlerList() {
        return this.customWriteHandlerList;
    }

    public void setCustomWriteHandlerList(List<WriteHandler> customWriteHandlerList) {
        this.customWriteHandlerList = customWriteHandlerList;
    }

    public Boolean getUseDefaultStyle() {
        return this.useDefaultStyle;
    }

    public void setUseDefaultStyle(Boolean useDefaultStyle) {
        this.useDefaultStyle = useDefaultStyle;
    }

    public Boolean getAutomaticMergeHead() {
        return this.automaticMergeHead;
    }

    public void setAutomaticMergeHead(Boolean automaticMergeHead) {
        this.automaticMergeHead = automaticMergeHead;
    }

    public Collection<Integer> getExcludeColumnIndexes() {
        return this.excludeColumnIndexes;
    }

    public void setExcludeColumnIndexes(Collection<Integer> excludeColumnIndexes) {
        this.excludeColumnIndexes = excludeColumnIndexes;
    }

    public Collection<String> getExcludeColumnFiledNames() {
        return this.excludeColumnFiledNames;
    }

    public void setExcludeColumnFiledNames(Collection<String> excludeColumnFiledNames) {
        this.excludeColumnFiledNames = excludeColumnFiledNames;
    }

    public Collection<Integer> getIncludeColumnIndexes() {
        return this.includeColumnIndexes;
    }

    public void setIncludeColumnIndexes(Collection<Integer> includeColumnIndexes) {
        this.includeColumnIndexes = includeColumnIndexes;
    }

    public Collection<String> getIncludeColumnFiledNames() {
        return this.includeColumnFiledNames;
    }

    public void setIncludeColumnFiledNames(Collection<String> includeColumnFiledNames) {
        this.includeColumnFiledNames = includeColumnFiledNames;
    }
}
