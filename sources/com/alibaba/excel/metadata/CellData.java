package com.alibaba.excel.metadata;

import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.util.StringUtils;
import java.math.BigDecimal;

/* loaded from: easyexcel-2.1.6.jar:com/alibaba/excel/metadata/CellData.class */
public class CellData<T> {
    private CellDataTypeEnum type;
    private BigDecimal numberValue;
    private String stringValue;
    private Boolean booleanValue;
    private Boolean formula;
    private String formulaValue;
    private byte[] imageValue;
    private Integer dataFormat;
    private String dataFormatString;
    private T data;

    public CellData(CellData<T> other) {
        this.type = other.type;
        this.numberValue = other.numberValue;
        this.stringValue = other.stringValue;
        this.booleanValue = other.booleanValue;
        this.formula = other.formula;
        this.formulaValue = other.formulaValue;
        this.imageValue = other.imageValue;
        this.dataFormat = other.dataFormat;
        this.dataFormatString = other.dataFormatString;
        this.data = other.data;
    }

    public CellData() {
    }

    public CellData(T data) {
        this.data = data;
    }

    public CellData(T data, String formulaValue) {
        this.data = data;
        this.formula = Boolean.TRUE;
        this.formulaValue = formulaValue;
    }

    public CellData(String stringValue) {
        this(CellDataTypeEnum.STRING, stringValue);
    }

    public CellData(CellDataTypeEnum type, String stringValue) {
        if (type != CellDataTypeEnum.STRING && type != CellDataTypeEnum.ERROR) {
            throw new IllegalArgumentException("Only support CellDataTypeEnum.STRING and  CellDataTypeEnum.ERROR");
        }
        if (stringValue == null) {
            throw new IllegalArgumentException("StringValue can not be null");
        }
        this.type = type;
        this.stringValue = stringValue;
        this.formula = Boolean.FALSE;
    }

    public CellData(BigDecimal numberValue) {
        if (numberValue == null) {
            throw new IllegalArgumentException("DoubleValue can not be null");
        }
        this.type = CellDataTypeEnum.NUMBER;
        this.numberValue = numberValue;
        this.formula = Boolean.FALSE;
    }

    public CellData(byte[] imageValue) {
        if (imageValue == null) {
            throw new IllegalArgumentException("ImageValue can not be null");
        }
        this.type = CellDataTypeEnum.IMAGE;
        this.imageValue = imageValue;
        this.formula = Boolean.FALSE;
    }

    public CellData(Boolean booleanValue) {
        if (booleanValue == null) {
            throw new IllegalArgumentException("BooleanValue can not be null");
        }
        this.type = CellDataTypeEnum.BOOLEAN;
        this.booleanValue = booleanValue;
        this.formula = Boolean.FALSE;
    }

    public CellData(CellDataTypeEnum type) {
        if (type == null) {
            throw new IllegalArgumentException("Type can not be null");
        }
        this.type = type;
        this.formula = Boolean.FALSE;
    }

    public CellDataTypeEnum getType() {
        return this.type;
    }

    public void setType(CellDataTypeEnum type) {
        this.type = type;
    }

    public BigDecimal getNumberValue() {
        return this.numberValue;
    }

    public void setNumberValue(BigDecimal numberValue) {
        this.numberValue = numberValue;
    }

    public String getStringValue() {
        return this.stringValue;
    }

    public void setStringValue(String stringValue) {
        this.stringValue = stringValue;
    }

    public Boolean getBooleanValue() {
        return this.booleanValue;
    }

    public void setBooleanValue(Boolean booleanValue) {
        this.booleanValue = booleanValue;
    }

    public Boolean getFormula() {
        return this.formula;
    }

    public void setFormula(Boolean formula) {
        this.formula = formula;
    }

    public String getFormulaValue() {
        return this.formulaValue;
    }

    public void setFormulaValue(String formulaValue) {
        this.formulaValue = formulaValue;
    }

    public byte[] getImageValue() {
        return this.imageValue;
    }

    public void setImageValue(byte[] imageValue) {
        this.imageValue = imageValue;
    }

    public Integer getDataFormat() {
        return this.dataFormat;
    }

    public void setDataFormat(Integer dataFormat) {
        this.dataFormat = dataFormat;
    }

    public String getDataFormatString() {
        return this.dataFormatString;
    }

    public void setDataFormatString(String dataFormatString) {
        this.dataFormatString = dataFormatString;
    }

    public T getData() {
        return this.data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public void checkEmpty() {
        if (this.type == null) {
            this.type = CellDataTypeEnum.EMPTY;
        }
        switch (this.type) {
            case STRING:
            case ERROR:
                if (StringUtils.isEmpty(this.stringValue)) {
                    this.type = CellDataTypeEnum.EMPTY;
                    break;
                }
                break;
            case NUMBER:
                if (this.numberValue == null) {
                    this.type = CellDataTypeEnum.EMPTY;
                    break;
                }
                break;
            case BOOLEAN:
                if (this.booleanValue == null) {
                    this.type = CellDataTypeEnum.EMPTY;
                    break;
                }
                break;
        }
    }

    public String toString() {
        if (this.type == null) {
            return "";
        }
        switch (this.type) {
            case STRING:
            case ERROR:
            case DIRECT_STRING:
                return this.stringValue;
            case NUMBER:
                return this.numberValue.toString();
            case BOOLEAN:
                return this.booleanValue.toString();
            case IMAGE:
                return "image[" + this.imageValue.length + "]";
            default:
                return "";
        }
    }
}
