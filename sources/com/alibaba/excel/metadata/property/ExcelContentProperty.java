package com.alibaba.excel.metadata.property;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.metadata.Head;
import java.lang.reflect.Field;

/* loaded from: easyexcel-2.1.6.jar:com/alibaba/excel/metadata/property/ExcelContentProperty.class */
public class ExcelContentProperty {
    private Field field;
    private Head head;
    private Converter converter;
    private DateTimeFormatProperty dateTimeFormatProperty;
    private NumberFormatProperty numberFormatProperty;

    public Field getField() {
        return this.field;
    }

    public void setField(Field field) {
        this.field = field;
    }

    public Head getHead() {
        return this.head;
    }

    public void setHead(Head head) {
        this.head = head;
    }

    public DateTimeFormatProperty getDateTimeFormatProperty() {
        return this.dateTimeFormatProperty;
    }

    public void setDateTimeFormatProperty(DateTimeFormatProperty dateTimeFormatProperty) {
        this.dateTimeFormatProperty = dateTimeFormatProperty;
    }

    public NumberFormatProperty getNumberFormatProperty() {
        return this.numberFormatProperty;
    }

    public void setNumberFormatProperty(NumberFormatProperty numberFormatProperty) {
        this.numberFormatProperty = numberFormatProperty;
    }

    public Converter getConverter() {
        return this.converter;
    }

    public void setConverter(Converter converter) {
        this.converter = converter;
    }
}
