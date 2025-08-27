package com.alibaba.excel.metadata;

import com.alibaba.excel.converters.Converter;
import java.util.List;
import java.util.Map;

/* loaded from: easyexcel-2.1.6.jar:com/alibaba/excel/metadata/AbstractHolder.class */
public abstract class AbstractHolder implements ConfigurationHolder {
    private Boolean newInitialization = Boolean.TRUE;
    private List<List<String>> head;
    private Class clazz;
    private GlobalConfiguration globalConfiguration;
    private Map<String, Converter> converterMap;

    public AbstractHolder(BasicParameter basicParameter, AbstractHolder prentAbstractHolder) {
        if (basicParameter.getHead() == null && basicParameter.getClazz() == null && prentAbstractHolder != null) {
            this.head = prentAbstractHolder.getHead();
        } else {
            this.head = basicParameter.getHead();
        }
        if (basicParameter.getHead() == null && basicParameter.getClazz() == null && prentAbstractHolder != null) {
            this.clazz = prentAbstractHolder.getClazz();
        } else {
            this.clazz = basicParameter.getClazz();
        }
        this.globalConfiguration = new GlobalConfiguration();
        if (basicParameter.getAutoTrim() == null) {
            if (prentAbstractHolder == null) {
                this.globalConfiguration.setAutoTrim(Boolean.TRUE);
                return;
            } else {
                this.globalConfiguration.setAutoTrim(prentAbstractHolder.getGlobalConfiguration().getAutoTrim());
                return;
            }
        }
        this.globalConfiguration.setAutoTrim(basicParameter.getAutoTrim());
    }

    public Boolean getNewInitialization() {
        return this.newInitialization;
    }

    public void setNewInitialization(Boolean newInitialization) {
        this.newInitialization = newInitialization;
    }

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

    public GlobalConfiguration getGlobalConfiguration() {
        return this.globalConfiguration;
    }

    public void setGlobalConfiguration(GlobalConfiguration globalConfiguration) {
        this.globalConfiguration = globalConfiguration;
    }

    public Map<String, Converter> getConverterMap() {
        return this.converterMap;
    }

    public void setConverterMap(Map<String, Converter> converterMap) {
        this.converterMap = converterMap;
    }

    @Override // com.alibaba.excel.metadata.ConfigurationHolder
    public Map<String, Converter> converterMap() {
        return getConverterMap();
    }

    @Override // com.alibaba.excel.metadata.ConfigurationHolder
    public GlobalConfiguration globalConfiguration() {
        return getGlobalConfiguration();
    }

    @Override // com.alibaba.excel.metadata.ConfigurationHolder
    public boolean isNew() {
        return getNewInitialization().booleanValue();
    }
}
