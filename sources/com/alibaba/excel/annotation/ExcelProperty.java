package com.alibaba.excel.annotation;

import com.alibaba.excel.converters.AutoConverter;
import com.alibaba.excel.converters.Converter;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Inherited
@Retention(RetentionPolicy.RUNTIME)
/* loaded from: easyexcel-2.1.6.jar:com/alibaba/excel/annotation/ExcelProperty.class */
public @interface ExcelProperty {
    String[] value() default {""};

    int index() default -1;

    Class<? extends Converter> converter() default AutoConverter.class;

    @Deprecated
    String format() default "";
}
