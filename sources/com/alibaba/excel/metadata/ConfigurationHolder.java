package com.alibaba.excel.metadata;

import com.alibaba.excel.converters.Converter;
import java.util.Map;

/* loaded from: easyexcel-2.1.6.jar:com/alibaba/excel/metadata/ConfigurationHolder.class */
public interface ConfigurationHolder extends Holder {
    boolean isNew();

    GlobalConfiguration globalConfiguration();

    Map<String, Converter> converterMap();
}
