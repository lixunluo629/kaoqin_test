package com.alibaba.excel.metadata.property;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.format.NumberFormat;
import com.alibaba.excel.converters.AutoConverter;
import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.HeadKindEnum;
import com.alibaba.excel.exception.ExcelCommonException;
import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.metadata.Holder;
import com.alibaba.excel.util.ClassUtils;
import com.alibaba.excel.util.StringUtils;
import com.alibaba.excel.write.metadata.holder.AbstractWriteHolder;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* loaded from: easyexcel-2.1.6.jar:com/alibaba/excel/metadata/property/ExcelHeadProperty.class */
public class ExcelHeadProperty {
    private static final Logger LOGGER = LoggerFactory.getLogger((Class<?>) ExcelHeadProperty.class);
    private Class headClazz;
    private HeadKindEnum headKind;
    private Map<Integer, Head> headMap = new TreeMap();
    private Map<Integer, ExcelContentProperty> contentPropertyMap = new TreeMap();
    private Map<String, ExcelContentProperty> fieldNameContentPropertyMap = new HashMap();
    private Map<String, Field> ignoreMap = new HashMap(16);
    private int headRowNumber = 0;

    public ExcelHeadProperty(Holder holder, Class headClazz, List<List<String>> head, Boolean convertAllFiled) throws IllegalAccessException, InstantiationException {
        this.headClazz = headClazz;
        this.headKind = HeadKindEnum.NONE;
        if (head != null && !head.isEmpty()) {
            int headIndex = 0;
            for (int i = 0; i < head.size(); i++) {
                if (!(holder instanceof AbstractWriteHolder) || !((AbstractWriteHolder) holder).ignore(null, Integer.valueOf(i))) {
                    this.headMap.put(Integer.valueOf(headIndex), new Head(Integer.valueOf(headIndex), null, head.get(i), Boolean.FALSE, Boolean.TRUE));
                    this.contentPropertyMap.put(Integer.valueOf(headIndex), null);
                    headIndex++;
                }
            }
            this.headKind = HeadKindEnum.STRING;
        } else {
            initColumnProperties(holder, convertAllFiled);
        }
        initHeadRowNumber();
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("The initialization sheet/table 'ExcelHeadProperty' is complete , head kind is {}", this.headKind);
        }
    }

    private void initHeadRowNumber() {
        this.headRowNumber = 0;
        for (Head head : this.headMap.values()) {
            List<String> list = head.getHeadNameList();
            if (list != null && list.size() > this.headRowNumber) {
                this.headRowNumber = list.size();
            }
        }
        for (Head head2 : this.headMap.values()) {
            List<String> headNameList = head2.getHeadNameList();
            if (headNameList != null && !headNameList.isEmpty() && headNameList.size() < this.headRowNumber) {
                int lack = this.headRowNumber - headNameList.size();
                int last = headNameList.size() - 1;
                for (int i = 0; i < lack; i++) {
                    headNameList.add(headNameList.get(last));
                }
            }
        }
    }

    private void initColumnProperties(Holder holder, Boolean convertAllFiled) throws IllegalAccessException, InstantiationException {
        if (this.headClazz == null) {
            return;
        }
        List<Field> defaultFieldList = new ArrayList<>();
        Map<Integer, Field> customFiledMap = new TreeMap<>();
        ClassUtils.declaredFields(this.headClazz, defaultFieldList, customFiledMap, this.ignoreMap, convertAllFiled);
        int index = 0;
        for (Field field : defaultFieldList) {
            while (customFiledMap.containsKey(Integer.valueOf(index))) {
                Field customFiled = customFiledMap.get(Integer.valueOf(index));
                customFiledMap.remove(Integer.valueOf(index));
                if (!initOneColumnProperty(holder, index, customFiled, Boolean.TRUE)) {
                    index++;
                }
            }
            if (!initOneColumnProperty(holder, index, field, Boolean.FALSE)) {
                index++;
            }
        }
        for (Map.Entry<Integer, Field> entry : customFiledMap.entrySet()) {
            initOneColumnProperty(holder, entry.getKey().intValue(), entry.getValue(), Boolean.TRUE);
        }
        this.headKind = HeadKindEnum.CLASS;
    }

    private boolean initOneColumnProperty(Holder holder, int index, Field field, Boolean forceIndex) throws IllegalAccessException, InstantiationException {
        Class<? extends Converter> convertClazz;
        if ((holder instanceof AbstractWriteHolder) && ((AbstractWriteHolder) holder).ignore(field.getName(), Integer.valueOf(index))) {
            return true;
        }
        ExcelProperty excelProperty = (ExcelProperty) field.getAnnotation(ExcelProperty.class);
        List<String> tmpHeadList = new ArrayList<>();
        boolean notForceName = excelProperty == null || excelProperty.value().length <= 0 || (excelProperty.value().length == 1 && StringUtils.isEmpty(excelProperty.value()[0]));
        if (notForceName) {
            tmpHeadList.add(field.getName());
        } else {
            Collections.addAll(tmpHeadList, excelProperty.value());
        }
        Head head = new Head(Integer.valueOf(index), field.getName(), tmpHeadList, forceIndex, Boolean.valueOf(!notForceName));
        ExcelContentProperty excelContentProperty = new ExcelContentProperty();
        if (excelProperty != null && (convertClazz = excelProperty.converter()) != AutoConverter.class) {
            try {
                Converter converter = convertClazz.newInstance();
                excelContentProperty.setConverter(converter);
            } catch (Exception e) {
                throw new ExcelCommonException("Can not instance custom converter:" + convertClazz.getName());
            }
        }
        excelContentProperty.setHead(head);
        excelContentProperty.setField(field);
        excelContentProperty.setDateTimeFormatProperty(DateTimeFormatProperty.build((DateTimeFormat) field.getAnnotation(DateTimeFormat.class)));
        excelContentProperty.setNumberFormatProperty(NumberFormatProperty.build((NumberFormat) field.getAnnotation(NumberFormat.class)));
        this.headMap.put(Integer.valueOf(index), head);
        this.contentPropertyMap.put(Integer.valueOf(index), excelContentProperty);
        this.fieldNameContentPropertyMap.put(field.getName(), excelContentProperty);
        return false;
    }

    public Class getHeadClazz() {
        return this.headClazz;
    }

    public void setHeadClazz(Class headClazz) {
        this.headClazz = headClazz;
    }

    public HeadKindEnum getHeadKind() {
        return this.headKind;
    }

    public void setHeadKind(HeadKindEnum headKind) {
        this.headKind = headKind;
    }

    public boolean hasHead() {
        return this.headKind != HeadKindEnum.NONE;
    }

    public int getHeadRowNumber() {
        return this.headRowNumber;
    }

    public void setHeadRowNumber(int headRowNumber) {
        this.headRowNumber = headRowNumber;
    }

    public Map<Integer, Head> getHeadMap() {
        return this.headMap;
    }

    public void setHeadMap(Map<Integer, Head> headMap) {
        this.headMap = headMap;
    }

    public Map<Integer, ExcelContentProperty> getContentPropertyMap() {
        return this.contentPropertyMap;
    }

    public void setContentPropertyMap(Map<Integer, ExcelContentProperty> contentPropertyMap) {
        this.contentPropertyMap = contentPropertyMap;
    }

    public Map<String, ExcelContentProperty> getFieldNameContentPropertyMap() {
        return this.fieldNameContentPropertyMap;
    }

    public void setFieldNameContentPropertyMap(Map<String, ExcelContentProperty> fieldNameContentPropertyMap) {
        this.fieldNameContentPropertyMap = fieldNameContentPropertyMap;
    }

    public Map<String, Field> getIgnoreMap() {
        return this.ignoreMap;
    }

    public void setIgnoreMap(Map<String, Field> ignoreMap) {
        this.ignoreMap = ignoreMap;
    }
}
