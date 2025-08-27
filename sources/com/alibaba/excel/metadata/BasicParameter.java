package com.alibaba.excel.metadata;

import com.alibaba.excel.converters.Converter;
import java.util.List;

/* loaded from: easyexcel-2.1.6.jar:com/alibaba/excel/metadata/BasicParameter.class */
public class BasicParameter {
    private List<List<String>> head;
    private Class clazz;
    private List<Converter> customConverterList;
    private Boolean autoTrim;
    private Boolean use1904windowing;

    public List<List<String>> getHead() {
        return this.head;
    }

    public void setHead(List<List<String>> head) {
        this.head = head;
    }

    public Class getClazz() {
        return this.clazz;
    }

    public void setClazz(Class clazz) {
        this.clazz = clazz;
    }

    public List<Converter> getCustomConverterList() {
        return this.customConverterList;
    }

    public void setCustomConverterList(List<Converter> customConverterList) {
        this.customConverterList = customConverterList;
    }

    public Boolean getAutoTrim() {
        return this.autoTrim;
    }

    public void setAutoTrim(Boolean autoTrim) {
        this.autoTrim = autoTrim;
    }

    public Boolean getUse1904windowing() {
        return this.use1904windowing;
    }

    public void setUse1904windowing(Boolean use1904windowing) {
        this.use1904windowing = use1904windowing;
    }
}
